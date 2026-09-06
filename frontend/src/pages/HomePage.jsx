import { ArrowRight, ChevronRight, Headphones, PackageCheck, ShieldCheck, Truck } from 'lucide-react'
import { useEffect, useState } from 'react'
import { Link } from 'react-router-dom'
import { storefrontApi } from '../api'
import ProductCard from '../components/ProductCard'
import Reveal from '../components/Reveal'
import { getCategoryGadgetKey } from '../utils'
import { getGadgetComponent } from '../components/TechGadgetVisual'
import { useCart } from '../App'

const text = {
  selection: 'KhangGear selection',
  viewAll: 'Xem tất cả',
  loadError: 'Chưa tải được dữ liệu. Kiểm tra Tomcat và thử lại sau.',
  heroTitleA: 'Thiết bị tốt cho ',
  heroTitleB: 'mọi setup.',
  heroCopy: 'Chọn đúng công nghệ để làm việc mượt hơn, chơi đã hơn và tận\u00A0hưởng từng khoảnh khắc.',
  discoverProducts: 'Khám phá sản phẩm',
  viewCategories: 'Xem danh mục',
  setupAlt: 'Góc máy KhangGear',
  fastDeliveryTitle: 'Giao nhanh toàn quốc',
  fastDeliveryCopy: 'Đóng gói kỹ, theo dõi dễ dàng',
  warrantyTitle: 'Bảo hành chính hãng',
  warrantyCopy: 'Đồng hành suốt quá trình sử dụng',
  returnTitle: 'Đổi trả minh bạch',
  returnCopy: 'Hỗ trợ nhanh khi cần thiết',
  consultTitle: 'Tư vấn thật lòng',
  consultCopy: 'Chọn đúng nhu cầu, không chọn dư',
  featuredCategories: 'Danh mục nổi bật',
  categoryTitle: 'Tìm theo cách bạn dùng công nghệ',
  discover: 'Khám phá',
  products: 'sản phẩm',
  retry: 'Thử lại',
  bestTitle: 'Sản phẩm bán chạy',
  bestSubtitle: 'Những lựa chọn được khách hàng KhangGear yêu thích.',
  latestTitle: 'Mới về KhangGear',
  latestSubtitle: 'Thiết bị đáng chú ý vừa có mặt trong cửa hàng.',
  setupTitle: 'Một góc máy gọn gàng, một ngày làm việc hiệu quả.',
  setupCopy: 'Từ màn hình, bàn phím đến gaming gear, KhangGear giúp bạn ghép từng mảnh thật vừa vặn.',
  startBuild: 'Bắt đầu build setup',
  genuine: 'Sản phẩm chính hãng',
  stock: 'Tồn kho cập nhật liên tục',
  payment: 'Thanh toán COD hoặc chuyển khoản',
  dealTitle: 'Deal công nghệ đáng xem',
  dealSubtitle: 'Số lượng có hạn, ưu tiên những món đang sắp hết hàng.'
}

function ProductRow({ title, subtitle, products, loading }) {
  const { addToCart } = useCart()
  return (
    <Reveal className="kg-section">
      <div className="kg-section-heading">
        <div>
          <p className="kg-eyebrow">{text.selection}</p>
          <h2>{title}</h2>
          <p>{subtitle}</p>
        </div>
        <Link to="/product" className="kg-text-link">
          {text.viewAll} <ArrowRight size={17} />
        </Link>
      </div>
      <div className="kg-product-grid">
        {loading
          ? Array.from({ length: 4 }).map((_, index) => <div className="kg-card-skeleton" key={index} />)
          : products.map((product) => <ProductCard key={product.id} product={product} onAdd={addToCart} />)}
      </div>
    </Reveal>
  )
}

function CategoryVisual({ category }) {
  const gadgetKey = getCategoryGadgetKey(category.name)
  const GadgetComp = getGadgetComponent(gadgetKey)
  return <GadgetComp size={42} className="kg-category-tech-icon" />
}

export default function HomePage() {
  const [categories, setCategories] = useState([])
  const [featured, setFeatured] = useState([])
  const [bestSelling, setBestSelling] = useState([])
  const [latest, setLatest] = useState([])
  const [error, setError] = useState('')

  useEffect(() => {
    Promise.all([
      storefrontApi.categories(),
      storefrontApi.featured(),
      storefrontApi.bestSelling(),
      storefrontApi.latest(10)
    ])
      .then(([cats, feature, best, newest]) => {
        setCategories(cats)
        setFeatured(feature)
        setBestSelling(best)
        setLatest(newest)
      })
      .catch(() => setError(text.loadError))
  }, [])

  return (
    <>
      <section className="kg-hero">
        <div
          className="kg-page-width kg-hero-inner"
          style={{
            backgroundImage: `linear-gradient(90deg, rgba(6,29,54,.96) 0%, rgba(6,29,54,.78) 48%, rgba(6,29,54,.15) 100%), url(${import.meta.env.BASE_URL}assets/hero-setup.png)`
          }}
        >
          <div className="kg-hero-copy">
            <span className="kg-hero-kicker">KhangGear choice 2026</span>
            <h1>
              {text.heroTitleA}
              <em>{text.heroTitleB}</em>
            </h1>
            <p>{text.heroCopy}</p>
            <div className="kg-hero-actions">
              <Link to="/product" className="kg-button kg-button-primary">
                {text.discoverProducts} <ArrowRight size={18} />
              </Link>
              <Link to="/home#categories" className="kg-button kg-button-ghost">
                {text.viewCategories}
              </Link>
            </div>
          </div>
        </div>
      </section>

      <section className="kg-policy-strip">
        <div className="kg-page-width kg-policy-grid">
          <div>
            <Truck />
            <span>
              <b>{text.fastDeliveryTitle}</b>
              {text.fastDeliveryCopy}
            </span>
          </div>
          <div>
            <ShieldCheck />
            <span>
              <b>{text.warrantyTitle}</b>
              {text.warrantyCopy}
            </span>
          </div>
          <div>
            <PackageCheck />
            <span>
              <b>{text.returnTitle}</b>
              {text.returnCopy}
            </span>
          </div>
          <div>
            <Headphones />
            <span>
              <b>{text.consultTitle}</b>
              {text.consultCopy}
            </span>
          </div>
        </div>
      </section>

      <Reveal className="kg-section kg-page-width">
        <div id="categories" className="kg-section-heading">
          <div>
            <p className="kg-eyebrow">{text.featuredCategories}</p>
            <h2>{text.categoryTitle}</h2>
          </div>
          <Link to="/product" className="kg-text-link">
            {text.discover} <ArrowRight size={17} />
          </Link>
        </div>
        <div className="kg-category-grid">
          {categories.map((category) => (
            <Link className="kg-category-card" key={category.id} to={`/product?categoryId=${category.id}`}>
              <span>
                <CategoryVisual category={category} />
              </span>
              <b>{category.name}</b>
              <small>
                {category.productCount} {text.products}
              </small>
              <ChevronRight size={17} />
            </Link>
          ))}
        </div>
      </Reveal>

      {error ? (
        <div className="kg-page-width kg-error-state">
          {error}
          <button onClick={() => window.location.reload()}>{text.retry}</button>
        </div>
      ) : (
        <div className="kg-page-width">
          <ProductRow title="Sản phẩm mới nhất" subtitle="10 sản phẩm vừa được bổ sung, sắp xếp từ mới đến cũ." products={latest} loading={!latest.length} />
          <ProductRow title={text.bestTitle} subtitle={text.bestSubtitle} products={bestSelling} loading={!bestSelling.length} />
        </div>
      )}

      <div className="kg-page-width">
        <ProductRow title={text.dealTitle} subtitle={text.dealSubtitle} products={featured} loading={!featured.length} />
      </div>
    </>
  )
}
