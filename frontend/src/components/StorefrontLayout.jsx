import { ChevronDown, LogIn, LogOut, Menu, Search, ShoppingBag, UserRound, X } from 'lucide-react'
import { Link, NavLink, useLocation, useNavigate } from 'react-router-dom'
import { useEffect } from 'react'
import { apiBase } from '../api'
import CartDrawer from './CartDrawer'

const text = {
  searchAria: 'T\u00ecm ki\u1ebfm s\u1ea3n ph\u1ea9m',
  searchPlaceholder: 'T\u00ecm thi\u1ebft b\u1ecb, ph\u1ee5 ki\u1ec7n, gaming gear...',
  search: 'T\u00ecm',
  login: '\u0110\u0103ng nh\u1eadp',
  logout: '\u0110\u0103ng xu\u1ea5t',
  openCart: 'M\u1edf gi\u1ecf h\u00e0ng',
  openMenu: 'M\u1edf menu',
  home: 'Trang ch\u1ee7',
  products: 'S\u1ea3n ph\u1ea9m',
  categories: 'Danh m\u1ee5c',
  orders: '\u0110\u01a1n h\u00e0ng c\u1ee7a t\u00f4i',
  footerIntro: 'Thi\u1ebft b\u1ecb c\u00f4ng ngh\u1ec7 ch\u1ecdn l\u1ecdc cho c\u00f4ng vi\u1ec7c, gi\u1ea3i tr\u00ed v\u00e0 g\u00f3c m\u00e1y c\u1ee7a b\u1ea1n.',
  explore: 'Kh\u00e1m ph\u00e1',
  allProducts: 'T\u1ea5t c\u1ea3 s\u1ea3n ph\u1ea9m',
  support: 'H\u1ed7 tr\u1ee3',
  nationwideFastDelivery: 'Giao nhanh to\u00e0n qu\u1ed1c',
  genuineWarranty: 'B\u1ea3o h\u00e0nh ch\u00ednh h\u00e3ng',
  securePayment: 'Thanh to\u00e1n an to\u00e0n',
  transparentReturn: '\u0110\u1ed5i tr\u1ea3 minh b\u1ea1ch',
  copyright: '\u00a9 2026 KhangGear. C\u00f4ng ngh\u1ec7 cho setup c\u1ee7a b\u1ea1n.'
}

export default function StorefrontLayout({ children, cart, profile, menuOpen, setMenuOpen, cartOpen, setCartOpen, onUpdateCart, onRemoveCart }) {
  const navigate = useNavigate()
  const location = useLocation()
  const assetBase = import.meta.env.BASE_URL
  useEffect(() => {
    if (location.hash !== '#categories') return
    const timer = window.setTimeout(() => document.getElementById('categories')?.scrollIntoView({ behavior: 'smooth' }), 80)
    return () => window.clearTimeout(timer)
  }, [location.pathname, location.hash])
  const search = (event) => { event.preventDefault(); const query = new FormData(event.currentTarget).get('q')?.trim(); navigate(query ? `/products?q=${encodeURIComponent(query)}` : '/products'); setMenuOpen(false) }
  return <div className="kg-storefront">
    <header className="kg-header"><div className="kg-header-top"><Link to="/home" className="kg-brand" aria-label="KhangGear home"><img src={`${assetBase}assets/khanggear-logo.png`} alt="KhangGear" /></Link><form className="kg-search" onSubmit={search}><Search size={19} /><input name="q" aria-label={text.searchAria} placeholder={text.searchPlaceholder} /><button type="submit">{text.search}</button></form><div className="kg-header-actions">{profile ? <Link className="kg-account-link" to="/account/profile"><UserRound size={19} /><span>{profile.fullName || profile.username}</span><ChevronDown size={15} /></Link> : <a className="kg-account-link" href={`${apiBase}/login`}><LogIn size={19} /><span>{text.login}</span></a>} {profile && <a className="kg-logout-button" href={`${apiBase}/logout`} title={text.logout}><LogOut size={18} /><span>{text.logout}</span></a>}<button className="kg-cart-button" onClick={() => setCartOpen(true)} aria-label={text.openCart}><ShoppingBag size={21} />{cart.itemCount > 0 && <b>{cart.itemCount}</b>}</button></div><button className="kg-mobile-menu" onClick={() => setMenuOpen(!menuOpen)} aria-label={text.openMenu}>{menuOpen ? <X /> : <Menu />}</button></div>
      <nav className={`kg-nav ${menuOpen ? 'kg-nav-open' : ''}`}><NavLink to="/home" onClick={() => setMenuOpen(false)}>{text.home}</NavLink><NavLink to="/products" onClick={() => setMenuOpen(false)}>{text.products}</NavLink><Link to="/home#categories" onClick={() => setMenuOpen(false)}>{text.categories}</Link>{profile && <NavLink to="/account/orders" onClick={() => setMenuOpen(false)}>{text.orders}</NavLink>}</nav></header>
    <main>{children}</main><Footer assetBase={assetBase} /><CartDrawer open={cartOpen} cart={cart} onClose={() => setCartOpen(false)} onUpdate={onUpdateCart} onRemove={onRemoveCart} />
  </div>
}

function Footer({ assetBase }) { return <footer className="kg-footer"><div className="kg-page-width kg-footer-grid"><div><img className="kg-footer-logo" src={`${assetBase}assets/khanggear-logo.png`} alt="KhangGear" /><p>{text.footerIntro}</p></div><div><h3>{text.explore}</h3><Link to="/products">{text.allProducts}</Link><Link to="/home#categories">{text.categories}</Link></div><div><h3>{text.support}</h3><span>{text.nationwideFastDelivery}</span><span>{text.genuineWarranty}</span></div><div><h3>KhangGear</h3><span>{text.securePayment}</span><span>{text.transparentReturn}</span></div></div><div className="kg-footer-bottom">{text.copyright}</div></footer> }
