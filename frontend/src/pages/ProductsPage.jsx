import { Filter, Search, SlidersHorizontal, X } from 'lucide-react'
import { useEffect, useState } from 'react'
import { useSearchParams } from 'react-router-dom'
import { storefrontApi } from '../api'
import ProductCard from '../components/ProductCard'
import { useCart } from '../App'

const initialFilters = { q: '', categoryId: '', minPrice: '', maxPrice: '', inStock: false, sort: 'newest', page: 1, pageSize: 12 }
const text = {
  title: 'S\u1ea3n ph\u1ea9m c\u00f4ng ngh\u1ec7',
  subtitle: 'Ch\u1ecdn theo nhu c\u1ea7u, ng\u00e2n s\u00e1ch v\u00e0 t\u00ecnh tr\u1ea1ng t\u1ed3n kho th\u1ef1c t\u1ebf.',
  filters: 'B\u1ed9 l\u1ecdc',
  filterProducts: 'L\u1ecdc s\u1ea3n ph\u1ea9m',
  search: 'T\u00ecm ki\u1ebfm',
  productName: 'T\u00ean s\u1ea3n ph\u1ea9m...',
  category: 'Danh m\u1ee5c',
  allCategories: 'T\u1ea5t c\u1ea3 danh m\u1ee5c',
  priceFrom: 'Gi\u00e1 t\u1eeb',
  priceTo: '\u0110\u1ebfn',
  unlimited: 'Kh\u00f4ng gi\u1edbi h\u1ea1n',
  inStockOnly: 'Ch\u1ec9 hi\u1ec3n th\u1ecb c\u00f2n h\u00e0ng',
  clearFilters: 'X\u00f3a b\u1ed9 l\u1ecdc',
  products: 's\u1ea3n ph\u1ea9m',
  loading: '\u0110ang t\u1ea3i...',
  sort: 'S\u1eafp x\u1ebfp',
  newest: 'M\u1edbi nh\u1ea5t',
  priceAsc: 'Gi\u00e1 th\u1ea5p \u0111\u1ebfn cao',
  priceDesc: 'Gi\u00e1 cao \u0111\u1ebfn th\u1ea5p',
  bestSelling: 'B\u00e1n ch\u1ea1y',
  retry: 'Th\u1eed l\u1ea1i',
  emptyTitle: 'Kh\u00f4ng t\u00ecm th\u1ea5y s\u1ea3n ph\u1ea9m ph\u00f9 h\u1ee3p',
  emptyCopy: 'Th\u1eed \u0111\u1ed5i t\u1eeb kh\u00f3a ho\u1eb7c n\u1edbi kho\u1ea3ng gi\u00e1 \u0111\u1ec3 xem th\u00eam l\u1ef1a ch\u1ecdn.',
  allProducts: 'Xem to\u00e0n b\u1ed9 s\u1ea3n ph\u1ea9m',
  pagination: 'Ph\u00e2n trang'
}

export default function ProductsPage() {
  const [params, setParams] = useSearchParams(); const { addToCart } = useCart(); const [categories, setCategories] = useState([]); const [data, setData] = useState(null); const [loading, setLoading] = useState(true); const [error, setError] = useState(''); const [showFilters, setShowFilters] = useState(false)
  const filters = { ...initialFilters, ...Object.fromEntries(params.entries()), inStock: params.get('inStock') === 'true', page: Number(params.get('page') || 1) }
  useEffect(() => { storefrontApi.categories().then(setCategories).catch(() => {}) }, [])
  const filterKey = params.toString()
  useEffect(() => {
    const query = new URLSearchParams(filterKey)
    const requestFilters = { ...initialFilters, ...Object.fromEntries(query.entries()), inStock: query.get('inStock') === 'true', page: Number(query.get('page') || 1) }
    setLoading(true); setError(''); storefrontApi.products(requestFilters).then(setData).catch((err) => setError(err.message)).finally(() => setLoading(false))
  }, [filterKey])
  const update = (values) => { const next = { ...filters, ...values, page: values.page || 1 }; const entries = Object.entries(next).filter(([, value]) => value !== '' && value !== false && value !== 1 && value != null); setParams(Object.fromEntries(entries)) }
  return <div className="kg-page-width kg-products-page"><div className="kg-page-heading"><div><p className="kg-eyebrow">KhangGear store</p><h1>{text.title}</h1><p>{text.subtitle}</p></div><button className="kg-filter-toggle" onClick={() => setShowFilters(!showFilters)}><SlidersHorizontal size={18} /> {text.filters}</button></div><div className="kg-products-layout"><aside className={`kg-filters ${showFilters ? 'kg-filters-open' : ''}`}><div className="kg-filter-title"><b><Filter size={17} /> {text.filterProducts}</b><button onClick={() => setShowFilters(false)}><X size={18} /></button></div><label>{text.search}<input value={filters.q} onChange={(event) => update({ q: event.target.value })} placeholder={text.productName} /></label><label>{text.category}<select value={filters.categoryId} onChange={(event) => update({ categoryId: event.target.value })}><option value="">{text.allCategories}</option>{categories.map((category) => <option key={category.id} value={category.id}>{category.name}</option>)}</select></label><div className="kg-price-fields"><label>{text.priceFrom}<input type="number" min="0" value={filters.minPrice} onChange={(event) => update({ minPrice: event.target.value })} placeholder="0" /></label><label>{text.priceTo}<input type="number" min="0" value={filters.maxPrice} onChange={(event) => update({ maxPrice: event.target.value })} placeholder={text.unlimited} /></label></div><label className="kg-checkbox"><input type="checkbox" checked={filters.inStock} onChange={(event) => update({ inStock: event.target.checked })} /> {text.inStockOnly}</label><button className="kg-clear-filter" onClick={() => setParams({})}>{text.clearFilters}</button></aside><section className="kg-products-results"><div className="kg-results-bar"><span>{data ? `${data.totalItems} ${text.products}` : text.loading}</span><label><span>{text.sort}</span><select value={filters.sort} onChange={(event) => update({ sort: event.target.value })}><option value="newest">{text.newest}</option><option value="price-asc">{text.priceAsc}</option><option value="price-desc">{text.priceDesc}</option><option value="best-selling">{text.bestSelling}</option></select></label></div>{loading ? <div className="kg-product-grid">{Array.from({ length: 8 }).map((_, index) => <div className="kg-card-skeleton" key={index} />)}</div> : error ? <div className="kg-error-state">{error}<button onClick={() => setParams({ ...Object.fromEntries(params), retry: Date.now() })}>{text.retry}</button></div> : data.items.length === 0 ? <div className="kg-empty-state"><Search size={42} /><h2>{text.emptyTitle}</h2><p>{text.emptyCopy}</p><button className="kg-button kg-button-primary" onClick={() => setParams({})}>{text.allProducts}</button></div> : <><div className="kg-product-grid">{data.items.map((product) => <ProductCard key={product.id} product={product} onAdd={addToCart} />)}</div><Pagination page={data.page} totalPages={data.totalPages} onChange={(page) => update({ page })} /></>}</section></div></div>
}

function Pagination({ page, totalPages, onChange }) { if (totalPages <= 1) return null; return <nav className="kg-pagination" aria-label={text.pagination}>{Array.from({ length: totalPages }, (_, index) => index + 1).map((number) => <button className={number === page ? 'kg-current-page' : ''} key={number} onClick={() => onChange(number)}>{number}</button>)}</nav> }
