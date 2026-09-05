import js from '@eslint/js'
import react from 'eslint-plugin-react'
import reactHooks from 'eslint-plugin-react-hooks'

export default [
  { ignores: ['node_modules/**', '../src/main/webapp/storefront/**', 'vite.config.js', 'eslint.config.js'] },
  {
    files: ['src/**/*.{js,jsx}'],
    ...js.configs.recommended,
    languageOptions: { ecmaVersion: 2022, sourceType: 'module', parserOptions: { ecmaFeatures: { jsx: true } }, globals: { window: 'readonly', document: 'readonly', fetch: 'readonly', FormData: 'readonly', URLSearchParams: 'readonly', IntersectionObserver: 'readonly' } },
    plugins: { react, 'react-hooks': reactHooks },
    settings: { react: { version: 'detect' } },
    rules: { ...reactHooks.configs.recommended.rules, 'react/jsx-uses-vars': 'error', 'react/react-in-jsx-scope': 'off', 'no-unused-vars': ['error', { argsIgnorePattern: '^_' }] }
  }
]
