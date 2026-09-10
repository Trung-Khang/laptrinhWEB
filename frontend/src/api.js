import { cleanVietnameseText } from './utils'

const API_BASE = import.meta.env.VITE_API_BASE_URL || (import.meta.env.DEV ? 'http://localhost:8080/dangnhap' : '/dangnhap')

function sanitizeData(item) {
  if (typeof item === 'string') return cleanVietnameseText(item)
  if (Array.isArray(item)) return item.map(sanitizeData)
  if (item && typeof item === 'object') return Object.fromEntries(Object.entries(item).map(([key, value]) => [key, sanitizeData(value)]))
  return item
}

async function request(path, options = {}) {
  const isMultipart = options.body instanceof FormData
  const headers = { ...(isMultipart ? {} : { 'Content-Type': 'application/json' }), ...(options.headers || {}) }
  const response = await fetch(`${API_BASE}${path}`, { credentials: 'include', headers, ...options })
  const payload = await response.json().catch(() => null)
  if (!response.ok || !payload?.success) {
    const error = new Error(payload?.message || 'Không thể kết nối máy chủ.')
    error.fieldErrors = payload?.fieldErrors || {}
    throw error
  }
  return sanitizeData(payload.data)
}

function updateProfile(form, avatar) {
  if (!avatar) return request('/api/account/profile', { method: 'PUT', body: JSON.stringify(form) })
  const payload = new FormData()
  payload.append('fullName', form.fullName || '')
  payload.append('phone', form.phone || '')
  payload.append('avatar', avatar)
  return request('/api/account/profile', { method: 'POST', body: payload })
}

export const apiBase = API_BASE
export const storefrontApi = {
  categories: () => request('/api/storefront/categories'),
  products: (query) => request(`/api/storefront/products?${new URLSearchParams(query).toString()}`),
  latest: (limit = 10) => request(`/api/storefront/products/latest?limit=${limit}`),
  product: (id) => request(`/api/storefront/products/${id}`),
  featured: () => request('/api/storefront/products/featured'),
  bestSelling: () => request('/api/storefront/products/best-selling'),
  cart: () => request('/api/storefront/cart'),
  addCart: (productId, quantity = 1) => request('/api/storefront/cart/items', { method: 'POST', body: JSON.stringify({ productId, quantity }) }),
  updateCart: (productId, quantity) => request(`/api/storefront/cart/items/${productId}`, { method: 'PUT', body: JSON.stringify({ quantity }) }),
  removeCart: (productId) => request(`/api/storefront/cart/items/${productId}`, { method: 'DELETE' }),
  checkout: (form) => request('/api/storefront/checkout', { method: 'POST', body: JSON.stringify(form) }),
  profile: () => request('/api/account/profile'),
  updateProfile,
  orders: () => request('/api/account/orders'),
  order: (id) => request(`/api/account/orders/${id}`),
  cancelOrder: (id) => request(`/api/account/orders/${id}/cancel`, { method: 'PUT', body: '{}' })
}
