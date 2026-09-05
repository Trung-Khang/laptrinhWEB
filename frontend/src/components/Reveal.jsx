import { useEffect, useRef, useState } from 'react'

export default function Reveal({ children, className = '' }) {
  const ref = useRef(null); const [shown, setShown] = useState(false)
  useEffect(() => { const observer = new IntersectionObserver(([entry]) => { if (entry.isIntersecting) { setShown(true); observer.disconnect() } }, { threshold: 0.1 }); if (ref.current) observer.observe(ref.current); return () => observer.disconnect() }, [])
  return <section ref={ref} className={`kg-reveal ${shown ? 'kg-revealed' : ''} ${className}`}>{children}</section>
}
