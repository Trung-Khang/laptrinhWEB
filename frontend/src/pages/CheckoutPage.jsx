import { ArrowLeft, CheckCircle2, CreditCard, Landmark, LockKeyhole } from 'lucide-react'
import { useEffect, useState } from 'react'
import { Link } from 'react-router-dom'
import { apiBase, storefrontApi } from '../api'
import { money } from '../utils'
import { useCart } from '../App'

const text = {
  successKicker: '\u0110\u1eb7t h\u00e0ng th\u00e0nh c\u00f4ng',
  successTitle: 'C\u1ea3m \u01a1n b\u1ea1n \u0111\u00e3 ch\u1ecdn KhangGear.',
  order: '\u0110\u01a1n h\u00e0ng',
  created: '\u0111\u00e3 \u0111\u01b0\u1ee3c t\u1ea1o v\u00e0 \u0111ang ch\u1edd x\u00e1c nh\u1eadn.',
  viewOrder: 'Xem \u0111\u01a1n h\u00e0ng',
  loginToCheckout: '\u0110\u0103ng nh\u1eadp \u0111\u1ec3 thanh to\u00e1n',
  loginCopy: 'KhangGear c\u1ea7n th\u00f4ng tin t\u00e0i kho\u1ea3n \u0111\u1ec3 l\u01b0u \u0111\u01a1n h\u00e0ng c\u1ee7a b\u1ea1n.',
  login: '\u0110\u0103ng nh\u1eadp',
  emptyCart: 'Gi\u1ecf h\u00e0ng \u0111ang tr\u1ed1ng',
  continueShopping: 'Ti\u1ebfp t\u1ee5c mua s\u1eafm',
  backCart: 'Quay l\u1ea1i gi\u1ecf h\u00e0ng',
  secureCheckout: 'Thanh to\u00e1n b\u1ea3o m\u1eadt',
  finishOrder: 'Ho\u00e0n t\u1ea5t \u0111\u01a1n h\u00e0ng',
  shippingInfo: 'Th\u00f4ng tin nh\u1eadn h\u00e0ng',
  fullName: 'H\u1ecd v\u00e0 t\u00ean',
  phone: 'S\u1ed1 \u0111i\u1ec7n tho\u1ea1i',
  address: '\u0110\u1ecba ch\u1ec9 giao h\u00e0ng',
  addressPlaceholder: 'S\u1ed1 nh\u00e0, \u0111\u01b0\u1eddng, ph\u01b0\u1eddng/x\u00e3, qu\u1eadn/huy\u1ec7n, t\u1ec9nh/th\u00e0nh',
  note: 'Ghi ch\u00fa',
  optional: '(kh\u00f4ng b\u1eaft bu\u1ed9c)',
  paymentMethod: 'Ph\u01b0\u01a1ng th\u1ee9c thanh to\u00e1n',
  cod: 'Thanh to\u00e1n khi nh\u1eadn h\u00e0ng',
  codCopy: 'Thanh to\u00e1n ti\u1ec1n m\u1eb7t cho \u0111\u01a1n v\u1ecb giao h\u00e0ng.',
  bank: 'Chuy\u1ec3n kho\u1ea3n ng\u00e2n h\u00e0ng',
  bankCopy: 'Th\u00f4ng tin chuy\u1ec3n kho\u1ea3n \u0111\u01b0\u1ee3c x\u00e1c nh\u1eadn sau khi t\u1ea1o \u0111\u01a1n.',
  creating: '\u0110ang t\u1ea1o \u0111\u01a1n...',
  placeOrder: '\u0110\u1eb7t h\u00e0ng',
  yourOrder: '\u0110\u01a1n h\u00e0ng c\u1ee7a b\u1ea1n',
  total: 'T\u1ed5ng thanh to\u00e1n',
  serverCheck: 'T\u1ed5ng ti\u1ec1n \u0111\u01b0\u1ee3c t\u00ednh l\u1ea1i tr\u00ean m\u00e1y ch\u1ee7 khi \u0111\u1eb7t h\u00e0ng.'
}

export default function CheckoutPage() {
  const { cart, profile, refreshCart, notify } = useCart(); const [form, setForm] = useState({ fullName: '', phone: '', email: '', address: '', note: '', paymentMethod: 'COD' }); const [submitting, setSubmitting] = useState(false); const [result, setResult] = useState(null)
  useEffect(() => { if (profile) setForm((current) => ({ ...current, fullName: current.fullName || profile.fullName || '', phone: current.phone || profile.phone || '', email: current.email || profile.email || '' })) }, [profile])
  if (result) return <div className="kg-page-width kg-checkout-success"><CheckCircle2 size={64} /><p className="kg-eyebrow">{text.successKicker}</p><h1>{text.successTitle}</h1><p>{text.order} #{result.orderId} {text.created}</p><strong>{money(result.totalAmount)}</strong><Link className="kg-button kg-button-primary" to={`/account/orders/${result.orderId}`}>{text.viewOrder}</Link></div>
  if (!profile) return <div className="kg-page-width kg-empty-state"><LockKeyhole size={45} /><h1>{text.loginToCheckout}</h1><p>{text.loginCopy}</p><a className="kg-button kg-button-primary" href={`${apiBase}/login`}>{text.login}</a></div>
  if (cart.items.length === 0) return <div className="kg-page-width kg-empty-state"><h1>{text.emptyCart}</h1><Link className="kg-button kg-button-primary" to="/products">{text.continueShopping}</Link></div>
  const update = (event) => setForm({ ...form, [event.target.name]: event.target.value })
  const submit = async (event) => { event.preventDefault(); setSubmitting(true); try { const order = await storefrontApi.checkout(form); await refreshCart(); setResult(order) } catch (error) { notify(error.message) } finally { setSubmitting(false) } }
  return <div className="kg-page-width kg-checkout-page"><Link className="kg-back-link" to="/cart"><ArrowLeft size={17} /> {text.backCart}</Link><div className="kg-page-heading"><div><p className="kg-eyebrow">{text.secureCheckout}</p><h1>{text.finishOrder}</h1></div></div><div className="kg-checkout-layout"><form className="kg-checkout-form" onSubmit={submit}><section><h2>{text.shippingInfo}</h2><div className="kg-form-grid"><label>{text.fullName}<input required name="fullName" value={form.fullName} onChange={update} /></label><label>{text.phone}<input required name="phone" value={form.phone} onChange={update} /></label><label className="kg-form-wide">Email<input required type="email" name="email" value={form.email} onChange={update} /></label><label className="kg-form-wide">{text.address}<textarea required name="address" value={form.address} onChange={update} rows="3" placeholder={text.addressPlaceholder} /></label><label className="kg-form-wide">{text.note} <small>{text.optional}</small><textarea name="note" value={form.note} onChange={update} rows="2" /></label></div></section><section><h2>{text.paymentMethod}</h2><label className={`kg-payment-option ${form.paymentMethod === 'COD' ? 'kg-payment-selected' : ''}`}><input type="radio" name="paymentMethod" value="COD" checked={form.paymentMethod === 'COD'} onChange={update} /><CreditCard size={21} /><span><b>{text.cod}</b>{text.codCopy}</span></label><label className={`kg-payment-option ${form.paymentMethod === 'BANK_TRANSFER' ? 'kg-payment-selected' : ''}`}><input type="radio" name="paymentMethod" value="BANK_TRANSFER" checked={form.paymentMethod === 'BANK_TRANSFER'} onChange={update} /><Landmark size={21} /><span><b>{text.bank}</b>{text.bankCopy}</span></label></section><button className="kg-button kg-button-primary kg-submit-order" disabled={submitting}>{submitting ? text.creating : text.placeOrder}</button></form><aside className="kg-order-summary"><h2>{text.yourOrder}</h2>{cart.items.map(({ product, quantity, subtotal }) => <div className="kg-checkout-item" key={product.id}><span>{product.name} <b>x{quantity}</b></span><strong>{money(subtotal)}</strong></div>)}<hr /><div className="kg-summary-total"><span>{text.total}</span><strong>{money(cart.total)}</strong></div><p><LockKeyhole size={14} /> {text.serverCheck}</p></aside></div></div>
}
