import { Cable, Gamepad2, Headphones, Keyboard, Laptop, Monitor, Mouse, Smartphone } from 'lucide-react'
import { apiBase } from './api'

export const money = (value) => new Intl.NumberFormat('vi-VN', { style: 'currency', currency: 'VND', maximumFractionDigits: 0 }).format(value || 0)

export function cleanVietnameseText(str) {
  if (!str || typeof str !== 'string') return str || ''
  return str
    .replace(/Tay c\?m/gi, 'Tay cầm')
    .replace(/t\?o th\? test/gi, 'tạo thử test')
    .replace(/t\?o th\?/gi, 'tạo thử')
    .replace(/\bc\?m\b/gi, 'cầm')
    .replace(/th\? test/gi, 'thử test')
    .replace(/b\?n ph\?m/gi, 'bàn phím')
    .replace(/chu\?t/gi, 'chuột')
    .replace(/m\?n h\?nh/gi, 'màn hình')
    .replace(/tai ngh\?/gi, 'tai nghe')
    .replace(/ph\? ki\?n/gi, 'phụ kiện')
    .replace(/s\?c d\? ph\?ng/gi, 'sạc dự phòng')
    .replace(/gh\? gaming/gi, 'ghế gaming')
}

export function detectGadgetType(name = '', categoryName = '') {
  const text = `${cleanVietnameseText(name)} ${cleanVietnameseText(categoryName)}`.toLowerCase()
  if (text.includes('hub') || text.includes('usb-c') || text.includes('7 in 1') || text.includes('type-c')) return 'hub'
  if (text.includes('tay cầm') || text.includes('c?m') || text.includes('gamepad') || text.includes('xbox') || text.includes('controller') || text.includes('gear')) return 'gamepad'
  if (text.includes('bàn phím') || text.includes('keyboard') || text.includes('aula') || text.includes('akko')) return 'keyboard'
  if (text.includes('chuột') || text.includes('mouse') || text.includes('superlight') || text.includes('deathadder') || text.includes('logitech g')) return 'mouse'
  if (text.includes('laptop') || text.includes('macbook') || text.includes('vivobook') || text.includes('zenbook') || text.includes('thinkpad')) return 'laptop'
  if (text.includes('điện') || text.includes('phone') || text.includes('iphone') || text.includes('galaxy') || text.includes('smartphone')) return 'phone'
  if (text.includes('tai nghe') || text.includes('headphone') || text.includes('headset') || text.includes('wh-1000') || text.includes('earphone')) return 'headphones'
  if (text.includes('màn hình') || text.includes('monitor') || text.includes('ultragear') || text.includes('oled')) return 'monitor'
  if (text.includes('sạc dự phòng') || text.includes('dự phòng') || text.includes('powerbank') || text.includes('anker')) return 'powerbank'
  if (text.includes('ghế') || text.includes('chair')) return 'chair'
  return 'cable'
}

export function getCategoryGadgetKey(name = '') {
  const value = cleanVietnameseText(name).toLowerCase()
  if (value.includes('hub') || value.includes('usb')) return 'hub'
  if (value.includes('điện') || value.includes('iphone') || value.includes('phone')) return 'phone'
  if (value.includes('laptop') || value.includes('macbook')) return 'laptop'
  if (value.includes('bàn phím') || value.includes('keyboard')) return 'keyboard'
  if (value.includes('chuột') || value.includes('mouse')) return 'mouse'
  if (value.includes('tai nghe') || value.includes('headphone')) return 'headphones'
  if (value.includes('màn hình') || value.includes('monitor')) return 'monitor'
  if (value.includes('gear') || value.includes('tay cầm')) return 'gamepad'
  if (value.includes('ghế') || value.includes('chair')) return 'chair'
  if (value.includes('sạc') || value.includes('dự phòng')) return 'powerbank'
  return 'cable'
}

export function categoryIcon(name = '') {
  const value = cleanVietnameseText(name).toLowerCase()
  if (value.includes('điện') || value.includes('iphone')) return Smartphone
  if (value.includes('laptop')) return Laptop
  if (value.includes('bàn phím') || value.includes('keyboard')) return Keyboard
  if (value.includes('chuột') || value.includes('mouse')) return Mouse
  if (value.includes('tai nghe')) return Headphones
  if (value.includes('màn hình')) return Monitor
  if (value.includes('gear') || value.includes('tay cầm')) return Gamepad2
  return Cable
}

export function localImage(source) {
  if (!source || source.startsWith('http://') || source.startsWith('https://')) return null
  return `${apiBase}/image?fname=${encodeURIComponent(source)}`
}

export function statusLabel(status) {
  return ({
    PENDING: 'Chờ xác nhận',
    CONFIRMED: 'Đã xác nhận',
    SHIPPING: 'Đang giao',
    COMPLETED: 'Hoàn thành',
    CANCELLED: 'Đã hủy'
  })[status] || status
}
