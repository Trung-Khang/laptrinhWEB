import { createContext, useCallback, useContext, useEffect, useMemo, useState } from 'react'
import { Navigate, Route, Routes } from 'react-router-dom'
import { storefrontApi } from './api'
import StorefrontLayout from './components/StorefrontLayout'
import HomePage from './pages/HomePage'
import ProductsPage from './pages/ProductsPage'
import ProductDetailPage from './pages/ProductDetailPage'
import CartPage from './pages/CartPage'
import CheckoutPage from './pages/CheckoutPage'
import AccountPage from './pages/AccountPage'

export const CartContext = createContext(null)
export const useCart = () => useContext(CartContext)

export default function App() {
  const [cart, setCart] = useState({ items: [], itemCount: 0, total: 0 })
  const [profile, setProfile] = useState(null)
  const [notice, setNotice] = useState('')
  const [cartOpen, setCartOpen] = useState(false)
  const [menuOpen, setMenuOpen] = useState(false)

  const refreshCart = useCallback(async () => setCart(await storefrontApi.cart()), [])
  const refreshProfile = useCallback(async () => {
    try { setProfile(await storefrontApi.profile()) } catch { setProfile(null) }
  }, [])
  useEffect(() => { refreshCart().catch(() => setNotice('Ch\u01b0a th\u1ec3 t\u1ea3i gi\u1ecf h\u00e0ng.')); refreshProfile() }, [refreshCart, refreshProfile])
  useEffect(() => { if (!notice) return undefined; const timer = window.setTimeout(() => setNotice(''), 3200); return () => window.clearTimeout(timer) }, [notice])

  const addToCart = useCallback(async (product, quantity = 1) => {
    try { const next = await storefrontApi.addCart(product.id, quantity); setCart(next); setNotice(`\u0110\u00e3 th\u00eam ${product.name} v\u00e0o gi\u1ecf h\u00e0ng.`); setCartOpen(true) }
    catch (error) { setNotice(error.message) }
  }, [])
  const updateCart = useCallback(async (id, quantity) => {
    if (quantity < 1) return storefrontApi.removeCart(id).then(setCart).catch((error) => setNotice(error.message))
    try { setCart(await storefrontApi.updateCart(id, quantity)) } catch (error) { setNotice(error.message) }
  }, [])
  const removeCart = useCallback(async (id) => { try { setCart(await storefrontApi.removeCart(id)) } catch (error) { setNotice(error.message) } }, [])
  const value = useMemo(() => ({ cart, profile, setProfile, refreshProfile, refreshCart, addToCart, updateCart, removeCart, notify: setNotice }), [cart, profile, refreshProfile, refreshCart, addToCart, updateCart, removeCart])

  return <CartContext.Provider value={value}><StorefrontLayout cart={cart} profile={profile} menuOpen={menuOpen} setMenuOpen={setMenuOpen} cartOpen={cartOpen} setCartOpen={setCartOpen} onUpdateCart={updateCart} onRemoveCart={removeCart}>
    <Routes><Route path="/" element={<Navigate to="/home" replace />} /><Route path="/home" element={<HomePage />} /><Route path="/product" element={<ProductsPage />} /><Route path="/products" element={<ProductsPage />} /><Route path="/product/:id" element={<ProductDetailPage />} /><Route path="/products/:id" element={<ProductDetailPage />} /><Route path="/cart" element={<CartPage />} /><Route path="/checkout" element={<CheckoutPage />} /><Route path="/account/profile" element={<AccountPage tab="profile" />} /><Route path="/account/orders" element={<AccountPage tab="orders" />} /><Route path="/account/orders/:id" element={<AccountPage tab="detail" />} /><Route path="*" element={<Navigate to="/home" replace />} /></Routes>
  </StorefrontLayout>{notice && <div className="kg-toast" role="status">{notice}</div>}</CartContext.Provider>
}
