package com.motos.jass.sistemalucky.cita.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/comprobantes")
@RequiredArgsConstructor
public class ComprobanteController {

    @Value("${app.upload.dir:./comprobantes}")
    private String uploadDir;

    @PostMapping("/upload")
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPCIONISTA')")
    public ResponseEntity<Map<String, String>> uploadComprobante(@RequestParam("file") MultipartFile file) {
        Map<String, String> response = new HashMap<>();
        
        try {
            // Validar archivo
            if (file.isEmpty()) {
                response.put("error", "El archivo está vacío");
                return ResponseEntity.badRequest().body(response);
            }

            // Validar tipo de archivo
            String contentType = file.getContentType();
            if (contentType == null || (!contentType.startsWith("image/") && !contentType.equals("application/pdf"))) {
                response.put("error", "Solo se permiten imágenes y PDFs");
                return ResponseEntity.badRequest().body(response);
            }

            // Validar tamaño (5MB)
            if (file.getSize() > 5 * 1024 * 1024) {
                response.put("error", "El archivo no debe exceder 5MB");
                return ResponseEntity.badRequest().body(response);
            }

            // Crear directorio si no existe
            Path uploadPath = Paths.get(uploadDir);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            // Generar nombre único
            String originalFilename = file.getOriginalFilename();
            String extension = "";
            if (originalFilename != null && originalFilename.contains(".")) {
                extension = originalFilename.substring(originalFilename.lastIndexOf("."));
            }
            String uniqueFilename = UUID.randomUUID().toString() + extension;

            // Guardar archivo
            Path filePath = uploadPath.resolve(uniqueFilename);
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            // Construir URL
            String fileUrl = "/api/comprobantes/view/" + uniqueFilename;

            response.put("url", fileUrl);
            response.put("filename", uniqueFilename);
            response.put("originalName", originalFilename);
            response.put("message", "Archivo subido exitosamente");

            return ResponseEntity.ok(response);

        } catch (IOException e) {
            response.put("error", "Error al guardar el archivo: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @GetMapping("/view/{filename}")
    public ResponseEntity<byte[]> viewComprobante(@PathVariable String filename) {
        try {
            Path filePath = Paths.get(uploadDir).resolve(filename);
            
            if (!Files.exists(filePath)) {
                return ResponseEntity.notFound().build();
            }

            byte[] fileContent = Files.readAllBytes(filePath);
            String contentType = Files.probeContentType(filePath);
            
            if (contentType == null) {
                contentType = "application/octet-stream";
            }

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .body(fileContent);

        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/{filename}")
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPCIONISTA')")
    public ResponseEntity<Map<String, String>> deleteComprobante(@PathVariable String filename) {
        Map<String, String> response = new HashMap<>();
        
        try {
            Path filePath = Paths.get(uploadDir).resolve(filename);
            
            if (!Files.exists(filePath)) {
                response.put("error", "Archivo no encontrado");
                return ResponseEntity.notFound().build();
            }

            Files.delete(filePath);
            response.put("message", "Archivo eliminado exitosamente");
            return ResponseEntity.ok(response);

        } catch (IOException e) {
            response.put("error", "Error al eliminar el archivo: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}

