package com.motos.jass.sistemalucky.Arranque.controller;

import com.motos.jass.sistemalucky.Arranque.mapper.ArranqueMapper;
import com.motos.jass.sistemalucky.Arranque.service.ArranqueService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/arranque")
@RequiredArgsConstructor
public class ArranqueController {

    private final ArranqueService arranqueService;
    private final ArranqueMapper mapper;
}
