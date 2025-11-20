import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'

// https://vitejs.dev/config/
export default defineConfig({
  plugins: [react()],
  server: {
    port: 3002,
    hmr: {
      overlay: true
    },
    watch: {
      usePolling: true,
      interval: 1000 
    },
    proxy: {
      '/api': {
        target: 'http://localhost:8081',
        changeOrigin: true,
        secure: false
      },
      '/auth': {
        target: 'http://localhost:8081',
        changeOrigin: true,
        secure: false
      },
      '/motos': {
        target: 'http://localhost:8081',
        changeOrigin: true,
        secure: false
      }
    }
  },
  build: {
    outDir: process.env.VITE_BUILD_OUT_DIR || '../src/main/resources/static',
    emptyOutDir: true,
    copyPublicDir: true,
    rollupOptions: {
      input: {
        main: './index.html'
      }
    }
  },
  define: {
    'process.env.VITE_API_URL': JSON.stringify(process.env.VITE_API_URL || 'http://localhost:8081')
  },
  publicDir: 'public'
})

