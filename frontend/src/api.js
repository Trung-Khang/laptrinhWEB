import { cleanVietnameseText } from './utils'

const API_BASE = import.meta.env.VITE_API_BASE_URL || (import.meta.env.DEV ? 'http://localhost:8080/dangnhap' : '/dangnhap')

function sanitizeData(item) {
  if (typeof item === 'string') return cleanVietnameseText(item)
  if (Array.isArray(item)) return item.map(sanitizeData)
  if (item && typeof item === 'object') {
    const cleaned = {}
    for (const [key, val] of Object.entries(item)) {
      cleaned[key] = sanitizeData(val)
    }
    return cleaned
  }
  return item
}

async function request(path, options = {}) {
  const response = await fetch(`${API_BASE}${path}`, {
    credentials: 'include',
    headers: { 'Content-Type': 'application/json', ...(options.headers || {}) },
    ...options
  })
  const payload = await response.json().catch(() => null)
  if (!response.ok || !payload?.success) throw new Error(payload?.message || 'Không thể kết nối máy chủ.')
  return sanitizeData(payload.data)
}

export const apiBase = API_BASE
export const storefrontApi = {
  categories: () => request('/api/storefront/categories'),
  products: (query) => request(`/api/storefront/products?${new URLSearchParams(query).toString()}`),
  product: (id) => request(`/api/storefront/products/${id}`),
  featured: () => request('/api/storefront/products/featured'),
  bestSelling: () => request('/api/storefront/products/best-selling'),
  cart: () => request('/api/storefront/cart'),
  addCart: (productId, quantity = 1) => request('/api/storefront/cart/items', { method: 'POST', body: JSON.stringify({ productId, quantity }) }),
  updateCart: (productId, quantity) => request(`/api/storefront/cart/items/${productId}`, { method: 'PUT', body: JSON.stringify({ quantity }) }),
  removeCart: (productId) => request(`/api/storefront/cart/items/${productId}`, { method: 'DELETE' }),
  checkout: (form) => request('/api/storefront/checkout', { method: 'POST', body: JSON.stringify(form) }),
  profile: () => request('/api/account/profile'),
  updateProfile: (form) => request('/api/account/profile', { method: 'PUT', body: JSON.stringify(form) }),
  orders: () => request('/api/account/orders'),
  order: (id) => request(`/api/account/orders/${id}`),
  cancelOrder: (id) => request(`/api/account/orders/${id}/cancel`, { method: 'PUT', body: '{}' })
}
