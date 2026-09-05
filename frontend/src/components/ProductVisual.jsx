import { useEffect, useState } from 'react'
import { detectGadgetType, localImage } from '../utils'
import { getGadgetComponent } from './TechGadgetVisual'

export default function ProductVisual({ product, className = '' }) {
  const source = localImage(product.image)
  const [failed, setFailed] = useState(false)
  useEffect(() => setFailed(false), [source])

  if (!source || failed) {
    const gadgetType = detectGadgetType(product.name, product.category?.name)
    const GadgetComponent = getGadgetComponent(gadgetType)
    return (
      <div className={`kg-product-fallback ${className}`} aria-label={product.name}>
        <div className="kg-product-showcase-inner">
          <GadgetComponent size={132} className="kg-gadget-svg" />
          <span className="kg-product-tech-tag">TECH SPEC</span>
        </div>
      </div>
    )
  }

  return <img className={className} src={source} alt={product.name} loading="lazy" onError={() => setFailed(true)} />
}
