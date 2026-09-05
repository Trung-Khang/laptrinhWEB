import { ArrowLeft, Check, Minus, Package, Plus, ShieldCheck, ShoppingBag, Truck } from 'lucide-react'
import { useEffect, useState } from 'react'
import { Link, useNavigate, useParams } from 'react-router-dom'
import { storefrontApi } from '../api'
import ProductCard from '../components/ProductCard'
import ProductVisual from '../components/ProductVisual'
import { money } from '../utils'
import { useCart } from '../App'

const text = {
  backProducts: 'Quay l\u1ea1i s\u1ea3n ph\u1ea9m',
  stockOut: 'S\u1ea3n ph\u1ea9m hi\u1ec7n \u0111\u00e3 h\u1ebft h\u00e0ng',
  stockPrefix: 'C\u00f2n',
  stockSuffix: 's\u1ea3n ph\u1ea9m trong kho',
  descriptionFallback: 'Th\u00f4ng tin chi ti\u1ebft \u0111ang \u0111\u01b0\u1ee3c KhangGear c\u1eadp nh\u1eadt.',
  quantity: 'S\u1ed1 l\u01b0\u1ee3ng',
  addToCart: 'Th\u00eam v\u00e0o gi\u1ecf',
  buyNow: 'Mua ngay',
  fastDelivery: 'Giao nhanh to\u00e0n qu\u1ed1c',
  warranty: 'B\u1ea3o h\u00e0nh ch\u00ednh h\u00e3ng',
  returnPolicy: '\u0110\u1ed5i tr\u1ea3 minh b\u1ea1ch',
  maybeLike: 'C\u00f3 th\u1ec3 b\u1ea1n c\u0169ng th\u00edch',
  related: 'S\u1ea3n ph\u1ea9m c\u00f9ng danh m\u1ee5c'
}

export default function ProductDetailPage() {
  const { id } = useParams(); const navigate = useNavigate(); const { addToCart } = useCart(); const [data, setData] = useState(null); const [quantity, setQuantity] = useState(1); const [error, setError] = useState('')
  useEffect(() => { setData(null); storefrontApi.product(id).then(setData).catch((err) => setError(err.message)) }, [id])
  if (error) return <div className="kg-page-width kg-error-state">{error}<Link to="/products">{text.backProducts}</Link></div>
  if (!data) return <div className="kg-page-width kg-detail-skeleton" />
  const { product, related } = data; const out = product.stockQuantity < 1
  const buyNow = async () => { await addToCart(product, quantity); navigate('/checkout') }
  return <div className="kg-page-width kg-detail-page"><Link className="kg-back-link" to="/products"><ArrowLeft size={17} /> {text.backProducts}</Link><div className="kg-product-detail"><div className="kg-detail-image"><ProductVisual product={product} className="kg-detail-product-image" /></div><div className="kg-detail-info"><p className="kg-product-category">{product.category?.name}</p><h1>{product.name}</h1><strong className="kg-detail-price">{money(product.price)}</strong><p className={out ? 'kg-detail-stock kg-stock-out' : 'kg-detail-stock'}><Package size={18} /> {out ? text.stockOut : `${text.stockPrefix} ${product.stockQuantity} ${text.stockSuffix}`}</p><p className="kg-detail-description">{product.description || text.descriptionFallback}</p><div className="kg-quantity-large"><span>{text.quantity}</span><div className="kg-quantity-control"><button onClick={() => setQuantity(Math.max(1, quantity - 1))}><Minus size={16} /></button><b>{quantity}</b><button disabled={quantity >= product.stockQuantity} onClick={() => setQuantity(Math.min(product.stockQuantity, quantity + 1))}><Plus size={16} /></button></div></div><div className="kg-detail-buttons"><button disabled={out} className="kg-button kg-button-secondary" onClick={() => addToCart(product, quantity)}><ShoppingBag size={18} /> {text.addToCart}</button><button disabled={out} className="kg-button kg-button-primary" onClick={buyNow}>{text.buyNow}</button></div><div className="kg-detail-policies"><span><Truck size={19} /> {text.fastDelivery}</span><span><ShieldCheck size={19} /> {text.warranty}</span><span><Check size={19} /> {text.returnPolicy}</span></div></div></div>{related.length > 0 && <section className="kg-section"><div className="kg-section-heading"><div><p className="kg-eyebrow">{text.maybeLike}</p><h2>{text.related}</h2></div></div><div className="kg-product-grid">{related.map((item) => <ProductCard key={item.id} product={item} onAdd={addToCart} />)}</div></section>}</div>
}
