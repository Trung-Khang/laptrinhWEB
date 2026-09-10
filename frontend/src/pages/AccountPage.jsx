import { Ban, ChevronRight, ClipboardList, Save, UserRound } from 'lucide-react'
import { useEffect, useState } from 'react'
import { Link, useParams } from 'react-router-dom'
import { apiBase, storefrontApi } from '../api'
import { money, statusLabel } from '../utils'
import { useCart } from '../App'

const text = {
  notLoggedIn: 'B\u1ea1n ch\u01b0a \u0111\u0103ng nh\u1eadp',
  login: '\u0110\u0103ng nh\u1eadp',
  cancelConfirm: 'H\u1ee7y \u0111\u01a1n h\u00e0ng n\u00e0y? S\u1ed1 l\u01b0\u1ee3ng s\u1ea3n ph\u1ea9m s\u1ebd \u0111\u01b0\u1ee3c ho\u00e0n v\u00e0o kho.',
  cancelSuccess: '\u0110\u01a1n h\u00e0ng \u0111\u00e3 \u0111\u01b0\u1ee3c h\u1ee7y.',
  profileTab: 'H\u1ed3 s\u01a1',
  orderTab: '\u0110\u01a1n h\u00e0ng c\u1ee7a t\u00f4i',
  account: 'T\u00e0i kho\u1ea3n KhangGear',
  accountInfo: 'Th\u00f4ng tin t\u00e0i kho\u1ea3n',
  yourProfile: 'H\u1ed3 s\u01a1 c\u1ee7a b\u1ea1n',
  username: 'T\u00ean \u0111\u0103ng nh\u1eadp',
  fullName: 'H\u1ecd v\u00e0 t\u00ean',
  phone: 'S\u1ed1 \u0111i\u1ec7n tho\u1ea1i',
  saving: '\u0110ang l\u01b0u...',
  save: 'L\u01b0u thay \u0111\u1ed5i',
  profileSaved: '\u0110\u00e3 c\u1eadp nh\u1eadt h\u1ed3 s\u01a1.',
  emptyOrders: 'B\u1ea1n ch\u01b0a c\u00f3 \u0111\u01a1n h\u00e0ng n\u00e0o',
  shopNow: 'Mua s\u1eafm ngay',
  history: 'L\u1ecbch s\u1eed mua s\u1eafm',
  order: '\u0110\u01a1n h\u00e0ng',
  backOrders: 'Quay l\u1ea1i \u0111\u01a1n h\u00e0ng',
  shipTo: 'Giao \u0111\u1ebfn',
  payment: 'Thanh to\u00e1n',
  bank: 'Chuy\u1ec3n kho\u1ea3n',
  cod: 'Thanh to\u00e1n khi nh\u1eadn h\u00e0ng',
  cancelOrder: 'H\u1ee7y \u0111\u01a1n h\u00e0ng'
}

export default function AccountPage({ tab }) {
  const { id } = useParams(); const { profile, setProfile, notify } = useCart(); const [orders, setOrders] = useState(null); const [detail, setDetail] = useState(null)
  useEffect(() => { if (tab === 'orders') storefrontApi.orders().then(setOrders).catch((error) => notify(error.message)); if (tab === 'detail' && id) storefrontApi.order(id).then(setDetail).catch((error) => notify(error.message)) }, [tab, id, notify])
  if (!profile) return <div className="kg-page-width kg-empty-state"><UserRound size={44} /><h1>{text.notLoggedIn}</h1><a className="kg-button kg-button-primary" href={`${apiBase}/login`}>{text.login}</a></div>
  const cancel = async () => { if (!window.confirm(text.cancelConfirm)) return; try { const next = await storefrontApi.cancelOrder(detail.id); setDetail(next); notify(text.cancelSuccess) } catch (error) { notify(error.message) } }
  return <div className="kg-page-width kg-account-page"><div className="kg-account-tabs"><Link className={tab === 'profile' ? 'kg-tab-active' : ''} to="/account/profile"><UserRound size={18} /> {text.profileTab}</Link><Link className={tab !== 'profile' ? 'kg-tab-active' : ''} to="/account/orders"><ClipboardList size={18} /> {text.orderTab}</Link></div>{tab === 'profile' ? <Profile profile={profile} setProfile={setProfile} notify={notify} /> : tab === 'orders' ? <Orders orders={orders} /> : <OrderDetail detail={detail} onCancel={cancel} />}</div>
}

function Profile({ profile, setProfile, notify }) {
  const [form, setForm] = useState(profile);
  const [avatar, setAvatar] = useState(null);
  const [avatarPreview, setAvatarPreview] = useState('');
  const [saving, setSaving] = useState(false);
  const [fieldErrors, setFieldErrors] = useState({});
  useEffect(() => setForm(profile), [profile]);
  useEffect(() => {
    if (!avatar) {
      setAvatarPreview('');
      return undefined;
    }
    const preview = URL.createObjectURL(avatar);
    setAvatarPreview(preview);
    return () => URL.revokeObjectURL(preview);
  }, [avatar]);
  const save = async (event) => {
    event.preventDefault();
    setSaving(true);
    setFieldErrors({});
    try {
      const next = await storefrontApi.updateProfile(form, avatar);
      setProfile(next);
      setAvatar(null);
      notify(text.profileSaved);
    } catch (error) {
      setFieldErrors(error.fieldErrors || {});
      notify(error.message);
    } finally {
      setSaving(false);
    }
  };
  return (
    <div className="kg-profile-layout">
      <section className="kg-profile-card">
        <div className="kg-avatar" style={{overflow: 'hidden', display: 'flex', alignItems: 'center', justifyContent: 'center'}}>
          {avatarPreview || profile.avatarUrl ? (
            <img src={avatarPreview || profile.avatarUrl} alt="Avatar" style={{width: '100%', height: '100%', objectFit: 'cover'}} onError={(event) => { event.currentTarget.style.display = 'none' }} />
          ) : (
            <UserRound size={34} />
          )}
        </div>
        <p>{text.account}</p>
        <h1>{profile.fullName || profile.username}</h1>
        <span>{profile.role}</span>
        <a href={`${apiBase}/profile`} className="kg-button kg-button-primary" style={{marginTop: '16px', width: '100%', textDecoration: 'none', display: 'flex', alignItems: 'center', justifyContent: 'center', gap: '6px', fontSize: '13px'}}>
          <UserRound size={16} /> Mở Hồ sơ SiteMesh & Upload Avatar
        </a>
      </section>
      <form className="kg-profile-form" onSubmit={save}>
        <div className="kg-section-heading">
          <div>
            <p className="kg-eyebrow">{text.accountInfo}</p>
            <h2>{text.yourProfile}</h2>
          </div>
          <a href={`${apiBase}/profile`} className="kg-button kg-button-secondary" style={{textDecoration: 'none', fontSize: '12px', padding: '6px 12px'}}>
            Cập nhật Avatar bằng SiteMesh &rarr;
          </a>
        </div>
        <label>{text.username}<input value={form.username} disabled /></label>
        <label>{text.fullName}<input required value={form.fullName || ''} onChange={(event) => setForm({ ...form, fullName: event.target.value })} />{fieldErrors.fullName && <small className="kg-field-error">{fieldErrors.fullName}</small>}</label>
        <label>Email<input required type="email" value={form.email || ''} disabled />{fieldErrors.email && <small className="kg-field-error">{fieldErrors.email}</small>}</label>
        <label>{text.phone}<input value={form.phone || ''} onChange={(event) => setForm({ ...form, phone: event.target.value })} />{fieldErrors.phone && <small className="kg-field-error">{fieldErrors.phone}</small>}</label>
        <label>Ảnh đại diện<input type="file" accept="image/jpeg,image/png,image/webp" onChange={(event) => setAvatar(event.target.files?.[0] || null)} />{fieldErrors.avatar && <small className="kg-field-error">{fieldErrors.avatar}</small>}</label>
        <button className="kg-button kg-button-primary" disabled={saving}><Save size={18} /> {saving ? text.saving : text.save}</button>
      </form>
    </div>
  );
}

function Orders({ orders }) { if (!orders) return <div className="kg-detail-skeleton" />; if (orders.length === 0) return <div className="kg-empty-state"><ClipboardList size={42} /><h1>{text.emptyOrders}</h1><Link className="kg-button kg-button-primary" to="/products">{text.shopNow}</Link></div>; return <section className="kg-orders"><div className="kg-section-heading"><div><p className="kg-eyebrow">{text.history}</p><h1>{text.orderTab}</h1></div></div>{orders.map((order) => <Link className="kg-order-row" to={`/account/orders/${order.id}`} key={order.id}><div><b>{text.order} #{order.id}</b><span>{new Date(order.orderDate).toLocaleDateString('vi-VN')}</span></div><span className={`kg-status kg-status-${order.status}`}>{statusLabel(order.status)}</span><strong>{money(order.totalAmount)}</strong><ChevronRight size={19} /></Link>)}</section> }

function OrderDetail({ detail, onCancel }) { if (!detail) return <div className="kg-detail-skeleton" />; return <section className="kg-order-detail"><Link className="kg-back-link" to="/account/orders">{text.backOrders}</Link><div className="kg-section-heading"><div><p className="kg-eyebrow">{text.order} #{detail.id}</p><h1>{statusLabel(detail.status)}</h1><p>{new Date(detail.orderDate).toLocaleString('vi-VN')}</p></div><span className={`kg-status kg-status-${detail.status}`}>{statusLabel(detail.status)}</span></div><div className="kg-order-detail-grid"><div className="kg-order-products">{detail.items.map((item) => <article key={item.id}><span>{item.product.name} <b>x{item.quantity}</b></span><strong>{money(item.subtotal)}</strong></article>)}</div><aside><h3>{text.shipTo}</h3><p>{detail.customerName}<br />{detail.phone}<br />{detail.shippingAddress}</p><h3>{text.payment}</h3><p>{detail.paymentMethod === 'BANK_TRANSFER' ? text.bank : text.cod}</p><strong className="kg-detail-price">{money(detail.totalAmount)}</strong>{detail.cancellable && <button className="kg-button kg-button-danger" onClick={onCancel}><Ban size={17} /> {text.cancelOrder}</button>}</aside></div></section> }
