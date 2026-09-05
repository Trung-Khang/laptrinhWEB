import React from 'react'
import ReactDOM from 'react-dom/client'
import { BrowserRouter } from 'react-router-dom'
import App from './App'
import './styles/storefront.css'

document.body.style.margin = '0'
const basename = window.location.pathname.startsWith('/dangnhap/') ? '/dangnhap' : ''

ReactDOM.createRoot(document.getElementById('root')).render(
  <React.StrictMode><BrowserRouter basename={basename}><App /></BrowserRouter></React.StrictMode>
)
