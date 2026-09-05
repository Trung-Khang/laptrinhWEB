import { Eye, ShoppingBag } from 'lucide-react'
import { Link } from 'react-router-dom'
import ProductVisual from './ProductVisual'
import { cleanVietnameseText, money } from '../utils'

export default function ProductCard({ product, onAdd }) {
  const soldOut = product.stockQuantity < 1
  const productName = cleanVietnameseText(product.name)
  const categoryName = cleanVietnameseText(product.category?.name)

  return (
    <article className="kg-product-card">
      <Link className="kg-product-visual-link" to={`/products/${product.id}`} aria-label={`Xem ${productName}`}>
        <ProductVisual product={product} className="kg-product-visual" />
      </Link>
      {product.stockQuantity > 0 && product.stockQuantity <= 5 && <span className="kg-product-badge">{'Sắp hết'}</span>}
      <div className="kg-product-card-body">
        <p className="kg-product-category">{categoryName}</p>
        <Link className="kg-product-name" to={`/products/${product.id}`}>
          {productName}
        </Link>
        <div className="kg-product-bottom">
          <div>
            <strong className="kg-price">{money(product.price)}</strong>
            <span className={soldOut ? 'kg-stock kg-stock-out' : 'kg-stock'}>
              {soldOut ? 'Hết hàng' : `Còn ${product.stockQuantity}`}
            </span>
          </div>
          <div className="kg-product-actions">
            <Link to={`/products/${product.id}`} className="kg-icon-button" aria-label="Xem chi tiết">
              <Eye size={18} />
            </Link>
            <button
              className="kg-icon-button kg-icon-button-primary"
              disabled={soldOut}
              onClick={() => onAdd(product)}
              aria-label="Thêm vào giỏ"
            >
              <ShoppingBag size={18} />
            </button>
          </div>
        </div>
      </div>
    </article>
  )
}
