import { Minus, Plus, ShoppingBag, Trash2, X } from 'lucide-react'
import { Link } from 'react-router-dom'
import { money } from '../utils'
import ProductVisual from './ProductVisual'

export default function CartDrawer({ open, cart, onClose, onUpdate, onRemove }) {
  return <><button className={`kg-drawer-overlay ${open ? 'kg-is-visible' : ''}`} onClick={onClose} aria-label="Đóng giỏ hàng" />
    <aside className={`kg-cart-drawer ${open ? 'kg-is-open' : ''}`} aria-label="Giỏ hàng">
      <header className="kg-drawer-header"><div><span>Giỏ hàng</span><small>{cart.itemCount} sản phẩm</small></div><button className="kg-icon-button" onClick={onClose} aria-label="Đóng"><X size={20} /></button></header>
      <div className="kg-drawer-items">{cart.items.length === 0 ? <div className="kg-empty-mini"><ShoppingBag size={38} /><p>Giỏ hàng của bạn đang trống.</p><Link to="/products" onClick={onClose}>Khám phá sản phẩm</Link></div> : cart.items.map(({ product, quantity, subtotal }) => <div className="kg-drawer-item" key={product.id}><ProductVisual product={product} className="kg-drawer-image" /><div><Link to={`/products/${product.id}`} onClick={onClose}>{product.name}</Link><strong>{money(subtotal)}</strong><div className="kg-quantity-control"><button onClick={() => onUpdate(product.id, quantity - 1)} aria-label="Giảm số lượng"><Minus size={14} /></button><span>{quantity}</span><button disabled={quantity >= product.stockQuantity} onClick={() => onUpdate(product.id, quantity + 1)} aria-label="Tăng số lượng"><Plus size={14} /></button><button className="kg-remove-item" onClick={() => onRemove(product.id)} aria-label="Xóa sản phẩm"><Trash2 size={15} /></button></div></div></div>)}</div>
      {cart.items.length > 0 && <footer className="kg-drawer-footer"><div><span>Tạm tính</span><strong>{money(cart.total)}</strong></div><Link className="kg-button kg-button-primary" to="/checkout" onClick={onClose}>Thanh toán</Link><Link className="kg-button kg-button-secondary" to="/cart" onClick={onClose}>Xem giỏ hàng</Link></footer>}
    </aside></>
}
