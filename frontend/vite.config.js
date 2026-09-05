import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'

export default defineConfig({
  plugins: [react()],
  base: process.env.NODE_ENV === 'production' ? '/dangnhap/storefront/' : '/',
  build: {
    outDir: '../src/main/webapp/storefront',
    emptyOutDir: true
  },
  server: {
    port: 5173,
    strictPort: true
  }
})
