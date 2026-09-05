// Bộ minh họa vector thiết bị công nghệ hiện đại (Cool-toned Tech Gadgets)

export function TechHubUsbC({ size = 120, className = '' }) {
  return (
    <svg width={size} height={size} viewBox="0 0 160 160" fill="none" xmlns="http://www.w3.org/2000/svg" className={className}>
      <defs>
        <linearGradient id="hubBody" x1="30" y1="50" x2="130" y2="110" gradientUnits="userSpaceOnUse">
          <stop stopColor="#1e293b" />
          <stop offset="0.5" stopColor="#334155" />
          <stop offset="1" stopColor="#0f172a" />
        </linearGradient>
        <linearGradient id="hubBevel" x1="30" y1="50" x2="130" y2="50" gradientUnits="userSpaceOnUse">
          <stop stopColor="#64748b" />
          <stop offset="1" stopColor="#38bdf8" />
        </linearGradient>
        <filter id="hubGlowFilter" x="-20%" y="-20%" width="140%" height="140%">
          <feGaussianBlur stdDeviation="3" result="blur" />
          <feComposite in="SourceGraphic" in2="blur" operator="over" />
        </filter>
      </defs>

      {/* Shadow */}
      <ellipse cx="80" cy="138" rx="55" ry="9" fill="#0369a1" fillOpacity="0.16" />

      {/* Cable */}
      <path d="M 80 50 C 80 25 125 25 125 40 L 125 44" stroke="#475569" strokeWidth="6" strokeLinecap="round" />
      <path d="M 80 50 C 80 25 125 25 125 40 L 125 44" stroke="#0ea5e9" strokeWidth="1.5" strokeDasharray="3 3" strokeLinecap="round" />

      {/* Type-C Connector Head */}
      <rect x="117" y="44" width="16" height="22" rx="3" fill="#1e293b" stroke="#64748b" strokeWidth="1.5" />
      <rect x="120" y="66" width="10" height="7" rx="2" fill="#e2e8f0" stroke="#94a3b8" strokeWidth="1" />
      <rect x="123" y="68" width="4" height="3" rx="0.8" fill="#38bdf8" />

      {/* Hub Main Body */}
      <rect x="35" y="50" width="90" height="72" rx="12" fill="url(#hubBody)" stroke="url(#hubBevel)" strokeWidth="1.8" />

      {/* Surface accent line */}
      <line x1="45" y1="56" x2="115" y2="56" stroke="#475569" strokeWidth="1" strokeOpacity="0.7" />

      {/* LED Power Indicator with Cyan Aura */}
      <circle cx="48" cy="68" r="4.5" fill="#38bdf8" filter="url(#hubGlowFilter)" />
      <circle cx="48" cy="68" r="2" fill="#f0fdf4" />
      <text x="56" y="71" fill="#94a3b8" fontSize="7" fontFamily="monospace" fontWeight="bold">PWR</text>

      {/* 3x USB 3.0 Ports with SuperSpeed Blue inner */}
      <g transform="translate(42, 80)">
        <rect x="0" y="0" width="18" height="8" rx="1.5" fill="#090d16" stroke="#475569" strokeWidth="1" />
        <rect x="2" y="2" width="14" height="2" fill="#0284c7" />
        <text x="3" y="15" fill="#64748b" fontSize="5.5" fontFamily="sans-serif">USB 3.0</text>
      </g>
      <g transform="translate(68, 80)">
        <rect x="0" y="0" width="18" height="8" rx="1.5" fill="#090d16" stroke="#475569" strokeWidth="1" />
        <rect x="2" y="2" width="14" height="2" fill="#0284c7" />
        <text x="3" y="15" fill="#64748b" fontSize="5.5" fontFamily="sans-serif">USB 3.0</text>
      </g>
      <g transform="translate(94, 80)">
        <rect x="0" y="0" width="18" height="8" rx="1.5" fill="#090d16" stroke="#475569" strokeWidth="1" />
        <rect x="2" y="2" width="14" height="2" fill="#0284c7" />
        <text x="5" y="15" fill="#38bdf8" fontSize="5.5" fontFamily="sans-serif">PD 100W</text>
      </g>

      {/* SD Card Slot */}
      <g transform="translate(42, 104)">
        <rect x="0" y="0" width="30" height="4" rx="1" fill="#090d16" stroke="#475569" strokeWidth="0.8" />
        <text x="34" y="4" fill="#64748b" fontSize="5.5" fontFamily="sans-serif">4K HDMI • SD</text>
      </g>

      {/* 7-in-1 Badge */}
      <rect x="88" y="102" width="28" height="11" rx="3" fill="#0284c7" fillOpacity="0.2" stroke="#38bdf8" strokeWidth="0.8" />
      <text x="92" y="110" fill="#38bdf8" fontSize="6.5" fontWeight="bold" fontFamily="sans-serif">7 IN 1</text>
    </svg>
  )
}

export function TechGamepad({ size = 120, className = '' }) {
  return (
    <svg width={size} height={size} viewBox="0 0 160 160" fill="none" xmlns="http://www.w3.org/2000/svg" className={className}>
      <defs>
        <linearGradient id="padBody" x1="40" y1="35" x2="120" y2="125" gradientUnits="userSpaceOnUse">
          <stop stopColor="#1e293b" />
          <stop offset="0.6" stopColor="#0f172a" />
          <stop offset="1" stopColor="#020617" />
        </linearGradient>
        <linearGradient id="stickGrad" x1="0" y1="0" x2="0" y2="1">
          <stop stopColor="#334155" />
          <stop offset="1" stopColor="#0f172a" />
        </linearGradient>
        <filter id="padGlow" x="-20%" y="-20%" width="140%" height="140%">
          <feGaussianBlur stdDeviation="3" result="blur" />
          <feComposite in="SourceGraphic" in2="blur" operator="over" />
        </filter>
      </defs>

      {/* Shadow */}
      <ellipse cx="80" cy="138" rx="58" ry="9" fill="#0369a1" fillOpacity="0.18" />

      {/* Left/Right Bumpers */}
      <rect x="42" y="38" width="24" height="8" rx="3" fill="#334155" stroke="#475569" strokeWidth="1" />
      <rect x="94" y="38" width="24" height="8" rx="3" fill="#334155" stroke="#475569" strokeWidth="1" />

      {/* Controller Main Contour */}
      <path
        d="M 52 44 C 36 44 24 58 22 84 C 20 108 28 128 42 128 C 50 128 58 116 66 100 C 74 94 86 94 94 100 C 102 116 110 128 118 128 C 132 128 140 108 138 84 C 136 58 124 44 108 44 Z"
        fill="url(#padBody)"
        stroke="#38bdf8"
        strokeWidth="1.6"
      />

      {/* Grip Textures */}
      <path d="M 28 85 C 29 98 33 112 38 120" stroke="#334155" strokeWidth="2" strokeLinecap="round" />
      <path d="M 132 85 C 131 98 127 112 122 120" stroke="#334155" strokeWidth="2" strokeLinecap="round" />

      {/* D-Pad (Left) */}
      <g transform="translate(43, 67)">
        <rect x="6" y="0" width="6" height="18" rx="1.5" fill="#334155" stroke="#64748b" strokeWidth="0.8" />
        <rect x="0" y="6" width="18" height="6" rx="1.5" fill="#334155" stroke="#64748b" strokeWidth="0.8" />
        <circle cx="9" cy="9" r="2" fill="#1e293b" />
      </g>

      {/* Action Buttons ABXY (Right) */}
      <g transform="translate(98, 62)">
        <circle cx="10" cy="4" r="3.5" fill="#0f172a" stroke="#38bdf8" strokeWidth="1" />
        <text x="8.5" y="6" fill="#38bdf8" fontSize="4.5" fontWeight="bold">Y</text>

        <circle cx="4" cy="10" r="3.5" fill="#0f172a" stroke="#38bdf8" strokeWidth="1" />
        <text x="2.5" y="12" fill="#38bdf8" fontSize="4.5" fontWeight="bold">X</text>

        <circle cx="16" cy="10" r="3.5" fill="#0f172a" stroke="#ef4444" strokeWidth="1" />
        <text x="14.5" y="12" fill="#ef4444" fontSize="4.5" fontWeight="bold">B</text>

        <circle cx="10" cy="16" r="3.5" fill="#0f172a" stroke="#22c55e" strokeWidth="1" />
        <text x="8.5" y="18" fill="#22c55e" fontSize="4.5" fontWeight="bold">A</text>
      </g>

      {/* Dual Analog Thumbsticks with Texture Rings */}
      <g transform="translate(56, 92)">
        <circle cx="8" cy="8" r="9" fill="url(#stickGrad)" stroke="#64748b" strokeWidth="1" />
        <circle cx="8" cy="8" r="6.5" fill="#1e293b" stroke="#38bdf8" strokeWidth="0.6" strokeDasharray="2 1" />
        <circle cx="8" cy="8" r="3" fill="#0284c7" />
      </g>
      <g transform="translate(86, 82)">
        <circle cx="8" cy="8" r="9" fill="url(#stickGrad)" stroke="#64748b" strokeWidth="1" />
        <circle cx="8" cy="8" r="6.5" fill="#1e293b" stroke="#38bdf8" strokeWidth="0.6" strokeDasharray="2 1" />
        <circle cx="8" cy="8" r="3" fill="#0284c7" />
      </g>

      {/* Center Glowing Logo / Light Bar */}
      <circle cx="80" cy="62" r="6" fill="#0284c7" filter="url(#padGlow)" />
      <circle cx="80" cy="62" r="4.5" fill="#38bdf8" />
      <path d="M 77 60 L 83 64 M 83 60 L 77 64" stroke="#ffffff" strokeWidth="1" strokeLinecap="round" />

      {/* LED Aura Line */}
      <path d="M 68 53 C 74 51 86 51 92 53" stroke="#38bdf8" strokeWidth="1.5" strokeLinecap="round" filter="url(#padGlow)" />
    </svg>
  )
}

export function TechKeyboard({ size = 120, className = '' }) {
  return (
    <svg width={size} height={size} viewBox="0 0 160 160" fill="none" xmlns="http://www.w3.org/2000/svg" className={className}>
      <defs>
        <linearGradient id="kbBody" x1="20" y1="40" x2="140" y2="120" gradientUnits="userSpaceOnUse">
          <stop stopColor="#1e293b" />
          <stop offset="0.6" stopColor="#0f172a" />
          <stop offset="1" stopColor="#020617" />
        </linearGradient>
        <linearGradient id="accentKey" x1="0" y1="0" x2="0" y2="1">
          <stop stopColor="#0ea5e9" />
          <stop offset="1" stopColor="#0284c7" />
        </linearGradient>
        <linearGradient id="orangeKey" x1="0" y1="0" x2="0" y2="1">
          <stop stopColor="#fb923c" />
          <stop offset="1" stopColor="#ea580c" />
        </linearGradient>
      </defs>

      {/* Shadow */}
      <ellipse cx="80" cy="138" rx="64" ry="8" fill="#0369a1" fillOpacity="0.18" />

      {/* Keyboard Case */}
      <rect x="18" y="52" width="124" height="66" rx="10" fill="url(#kbBody)" stroke="#38bdf8" strokeWidth="1.6" />

      {/* RGB Underglow */}
      <rect x="24" y="57" width="112" height="56" rx="6" fill="#0b1329" stroke="#1e293b" strokeWidth="1" />

      {/* Top Row / Function Keys */}
      <rect x="28" y="61" width="10" height="7" rx="1.5" fill="url(#orangeKey)" />
      <rect x="41" y="61" width="8" height="7" rx="1.5" fill="#334155" />
      <rect x="51" y="61" width="8" height="7" rx="1.5" fill="#334155" />
      <rect x="61" y="61" width="8" height="7" rx="1.5" fill="#334155" />
      <rect x="71" y="61" width="8" height="7" rx="1.5" fill="#334155" />
      <rect x="83" y="61" width="8" height="7" rx="1.5" fill="#334155" />
      <rect x="93" y="61" width="8" height="7" rx="1.5" fill="#334155" />
      <rect x="103" y="61" width="8" height="7" rx="1.5" fill="#334155" />
      <rect x="113" y="61" width="15" height="7" rx="1.5" fill="url(#accentKey)" />

      {/* Row 2 / Number Row */}
      <rect x="28" y="71" width="12" height="7" rx="1.5" fill="#1e293b" stroke="#475569" strokeWidth="0.5" />
      <rect x="42" y="71" width="8" height="7" rx="1.5" fill="#1e293b" stroke="#475569" strokeWidth="0.5" />
      <rect x="52" y="71" width="8" height="7" rx="1.5" fill="#1e293b" stroke="#475569" strokeWidth="0.5" />
      <rect x="62" y="71" width="8" height="7" rx="1.5" fill="#1e293b" stroke="#475569" strokeWidth="0.5" />
      <rect x="72" y="71" width="8" height="7" rx="1.5" fill="#1e293b" stroke="#475569" strokeWidth="0.5" />
      <rect x="82" y="71" width="8" height="7" rx="1.5" fill="#1e293b" stroke="#475569" strokeWidth="0.5" />
      <rect x="92" y="71" width="8" height="7" rx="1.5" fill="#1e293b" stroke="#475569" strokeWidth="0.5" />
      <rect x="102" y="71" width="8" height="7" rx="1.5" fill="#1e293b" stroke="#475569" strokeWidth="0.5" />
      <rect x="112" y="71" width="16" height="7" rx="1.5" fill="#334155" />

      {/* Row 3 / QWERTY with WASD highlight */}
      <rect x="28" y="81" width="14" height="7" rx="1.5" fill="#334155" />
      <rect x="44" y="81" width="8" height="7" rx="1.5" fill="url(#accentKey)" />
      <rect x="54" y="81" width="8" height="7" rx="1.5" fill="#1e293b" stroke="#475569" strokeWidth="0.5" />
      <rect x="64" y="81" width="8" height="7" rx="1.5" fill="#1e293b" stroke="#475569" strokeWidth="0.5" />
      <rect x="74" y="81" width="8" height="7" rx="1.5" fill="#1e293b" stroke="#475569" strokeWidth="0.5" />
      <rect x="84" y="81" width="8" height="7" rx="1.5" fill="#1e293b" stroke="#475569" strokeWidth="0.5" />
      <rect x="94" y="81" width="8" height="7" rx="1.5" fill="#1e293b" stroke="#475569" strokeWidth="0.5" />
      <rect x="104" y="81" width="8" height="7" rx="1.5" fill="#1e293b" stroke="#475569" strokeWidth="0.5" />
      <rect x="114" y="81" width="14" height="7" rx="1.5" fill="url(#orangeKey)" />

      {/* Row 4 / ASDF */}
      <rect x="28" y="91" width="16" height="7" rx="1.5" fill="#334155" />
      <rect x="46" y="91" width="8" height="7" rx="1.5" fill="url(#accentKey)" />
      <rect x="56" y="91" width="8" height="7" rx="1.5" fill="url(#accentKey)" />
      <rect x="66" y="91" width="8" height="7" rx="1.5" fill="url(#accentKey)" />
      <rect x="76" y="91" width="8" height="7" rx="1.5" fill="#1e293b" stroke="#475569" strokeWidth="0.5" />
      <rect x="86" y="91" width="8" height="7" rx="1.5" fill="#1e293b" stroke="#475569" strokeWidth="0.5" />
      <rect x="96" y="91" width="8" height="7" rx="1.5" fill="#1e293b" stroke="#475569" strokeWidth="0.5" />
      <rect x="106" y="91" width="10" height="7" rx="1.5" fill="#334155" />
      <rect x="118" y="91" width="10" height="7" rx="1.5" fill="url(#accentKey)" />

      {/* Bottom Row / Spacebar */}
      <rect x="28" y="101" width="14" height="7" rx="1.5" fill="#334155" />
      <rect x="44" y="101" width="10" height="7" rx="1.5" fill="#334155" />
      <rect x="56" y="101" width="48" height="7" rx="2" fill="url(#accentKey)" />
      <rect x="106" y="101" width="10" height="7" rx="1.5" fill="#334155" />
      <rect x="118" y="101" width="10" height="7" rx="1.5" fill="#334155" />

      {/* Top Cable Connector */}
      <rect x="74" y="48" width="12" height="5" rx="1" fill="#475569" />
      <path d="M 80 48 L 80 38" stroke="#38bdf8" strokeWidth="2.5" strokeLinecap="round" />
    </svg>
  )
}

export function TechMouse({ size = 120, className = '' }) {
  return (
    <svg width={size} height={size} viewBox="0 0 160 160" fill="none" xmlns="http://www.w3.org/2000/svg" className={className}>
      <defs>
        <linearGradient id="mouseBody" x1="45" y1="35" x2="115" y2="125" gradientUnits="userSpaceOnUse">
          <stop stopColor="#1e293b" />
          <stop offset="0.5" stopColor="#0f172a" />
          <stop offset="1" stopColor="#020617" />
        </linearGradient>
        <filter id="mouseGlow" x="-20%" y="-20%" width="140%" height="140%">
          <feGaussianBlur stdDeviation="3" result="blur" />
          <feComposite in="SourceGraphic" in2="blur" operator="over" />
        </filter>
      </defs>

      {/* Shadow */}
      <ellipse cx="80" cy="138" rx="42" ry="8" fill="#0369a1" fillOpacity="0.18" />

      {/* Ergonomic Mouse Contour */}
      <path
        d="M 80 34 C 62 34 50 48 48 76 C 46 102 54 126 80 126 C 106 126 114 102 112 76 C 110 48 98 34 80 34 Z"
        fill="url(#mouseBody)"
        stroke="#38bdf8"
        strokeWidth="1.6"
      />

      {/* Center Separation Line */}
      <line x1="80" y1="34" x2="80" y2="72" stroke="#475569" strokeWidth="1.2" />

      {/* Illuminated Scroll Wheel */}
      <rect x="76" y="44" width="8" height="20" rx="3.5" fill="#090d16" stroke="#38bdf8" strokeWidth="1.2" />
      <line x1="77" y1="49" x2="83" y2="49" stroke="#38bdf8" strokeWidth="1.2" />
      <line x1="77" y1="54" x2="83" y2="54" stroke="#38bdf8" strokeWidth="1.2" />
      <line x1="77" y1="59" x2="83" y2="59" stroke="#38bdf8" strokeWidth="1.2" />

      {/* DPI Button */}
      <rect x="78" y="70" width="4" height="6" rx="1" fill="#38bdf8" filter="url(#mouseGlow)" />

      {/* Side Thumb Rest with Cyan Strip */}
      <path d="M 50 68 C 45 80 46 98 52 108" stroke="#38bdf8" strokeWidth="2" strokeLinecap="round" filter="url(#mouseGlow)" />
      <path d="M 110 68 C 115 80 114 98 108 108" stroke="#38bdf8" strokeWidth="2" strokeLinecap="round" filter="url(#mouseGlow)" />

      {/* Palm Logo / RGB Arc */}
      <path d="M 72 104 C 77 99 83 99 88 104" stroke="#38bdf8" strokeWidth="2" strokeLinecap="round" />
      <circle cx="80" cy="98" r="2.5" fill="#38bdf8" filter="url(#mouseGlow)" />
    </svg>
  )
}

export function TechLaptop({ size = 120, className = '' }) {
  return (
    <svg width={size} height={size} viewBox="0 0 160 160" fill="none" xmlns="http://www.w3.org/2000/svg" className={className}>
      <defs>
        <linearGradient id="screenWallpaper" x1="40" y1="36" x2="120" y2="92" gradientUnits="userSpaceOnUse">
          <stop stopColor="#0369a1" />
          <stop offset="0.5" stopColor="#0284c7" />
          <stop offset="1" stopColor="#38bdf8" />
        </linearGradient>
      </defs>

      {/* Shadow */}
      <ellipse cx="80" cy="136" rx="60" ry="8" fill="#0369a1" fillOpacity="0.18" />

      {/* Screen Lid Backing */}
      <rect x="30" y="34" width="100" height="66" rx="6" fill="#0f172a" stroke="#475569" strokeWidth="1.5" />

      {/* Screen Bezel & Display */}
      <rect x="34" y="38" width="92" height="58" rx="3" fill="#020617" />
      <rect x="36" y="40" width="88" height="54" rx="2" fill="url(#screenWallpaper)" />

      {/* Screen Abstract Tech Graphics */}
      <circle cx="80" cy="65" r="16" stroke="#ffffff" strokeWidth="1.2" strokeOpacity="0.5" />
      <circle cx="80" cy="65" r="8" fill="#ffffff" fillOpacity="0.2" />
      <line x1="50" y1="65" x2="110" y2="65" stroke="#ffffff" strokeWidth="1" strokeOpacity="0.4" />

      {/* Webcam Notch */}
      <circle cx="80" cy="36.5" r="1.2" fill="#38bdf8" />

      {/* Base / Bottom Deck */}
      <path d="M 18 106 L 142 106 L 132 124 L 28 124 Z" fill="#1e293b" stroke="#38bdf8" strokeWidth="1.5" />

      {/* Keyboard Area */}
      <polygon points="34,108 126,108 122,118 38,118" fill="#0f172a" />
      <line x1="42" y1="113" x2="118" y2="113" stroke="#334155" strokeWidth="1.5" strokeDasharray="3 2" />

      {/* Glass Trackpad */}
      <rect x="68" y="120" width="24" height="3" rx="0.8" fill="#334155" stroke="#475569" strokeWidth="0.5" />

      {/* Front Notch */}
      <rect x="74" y="123.5" width="12" height="1.5" rx="0.5" fill="#38bdf8" />
    </svg>
  )
}

export function TechPhone({ size = 120, className = '' }) {
  return (
    <svg width={size} height={size} viewBox="0 0 160 160" fill="none" xmlns="http://www.w3.org/2000/svg" className={className}>
      <defs>
        <linearGradient id="phoneWall" x1="50" y1="30" x2="110" y2="130" gradientUnits="userSpaceOnUse">
          <stop stopColor="#0f172a" />
          <stop offset="0.4" stopColor="#0369a1" />
          <stop offset="1" stopColor="#38bdf8" />
        </linearGradient>
      </defs>

      {/* Shadow */}
      <ellipse cx="80" cy="138" rx="35" ry="7" fill="#0369a1" fillOpacity="0.18" />

      {/* Phone Body Frame */}
      <rect x="52" y="26" width="56" height="108" rx="14" fill="#0f172a" stroke="#38bdf8" strokeWidth="1.6" />

      {/* Screen */}
      <rect x="55" y="29" width="50" height="102" rx="11" fill="url(#phoneWall)" />

      {/* Dynamic Island Notch */}
      <rect x="72" y="33" width="16" height="5" rx="2.5" fill="#020617" />
      <circle cx="83" cy="35.5" r="1.2" fill="#38bdf8" />

      {/* Screen UI Widgets */}
      <g transform="translate(62, 48)">
        <rect x="0" y="0" width="36" height="14" rx="4" fill="#ffffff" fillOpacity="0.15" />
        <text x="5" y="10" fill="#ffffff" fontSize="8" fontWeight="bold" fontFamily="sans-serif">09:41</text>
      </g>

      {/* Circular Tech Dial on Screen */}
      <circle cx="80" cy="85" r="14" stroke="#ffffff" strokeWidth="1.2" strokeOpacity="0.4" strokeDasharray="3 2" />
      <circle cx="80" cy="85" r="6" fill="#38bdf8" fillOpacity="0.5" />

      {/* Home Indicator Bar */}
      <rect x="70" y="125" width="20" height="2" rx="1" fill="#ffffff" fillOpacity="0.8" />
    </svg>
  )
}

export function TechMonitor({ size = 120, className = '' }) {
  return (
    <svg width={size} height={size} viewBox="0 0 160 160" fill="none" xmlns="http://www.w3.org/2000/svg" className={className}>
      <defs>
        <linearGradient id="monWall" x1="25" y1="36" x2="135" y2="96" gradientUnits="userSpaceOnUse">
          <stop stopColor="#020617" />
          <stop offset="0.5" stopColor="#0369a1" />
          <stop offset="1" stopColor="#0284c7" />
        </linearGradient>
      </defs>

      {/* Shadow */}
      <ellipse cx="80" cy="138" rx="48" ry="7" fill="#0369a1" fillOpacity="0.18" />

      {/* Stand Base */}
      <polygon points="60,136 100,136 90,126 70,126" fill="#1e293b" stroke="#38bdf8" strokeWidth="1" />
      <rect x="76" y="98" width="8" height="30" fill="#334155" stroke="#475569" strokeWidth="1" />

      {/* Display Frame */}
      <rect x="20" y="32" width="120" height="70" rx="6" fill="#0f172a" stroke="#38bdf8" strokeWidth="1.6" />

      {/* Screen */}
      <rect x="24" y="36" width="112" height="62" rx="3" fill="url(#monWall)" />

      {/* Display High-tech Graphics */}
      <path d="M 30 78 L 55 58 L 80 68 L 105 48 L 130 62" stroke="#38bdf8" strokeWidth="1.8" fill="none" />
      <circle cx="105" cy="48" r="3.5" fill="#38bdf8" />
      <line x1="24" y1="84" x2="136" y2="84" stroke="#ffffff" strokeWidth="0.8" strokeOpacity="0.2" />

      {/* Bottom Bezel Logo */}
      <circle cx="80" cy="100" r="1.5" fill="#38bdf8" />
    </svg>
  )
}

export function TechHeadphones({ size = 120, className = '' }) {
  return (
    <svg width={size} height={size} viewBox="0 0 160 160" fill="none" xmlns="http://www.w3.org/2000/svg" className={className}>
      <defs>
        <linearGradient id="hpGrad" x1="40" y1="30" x2="120" y2="120" gradientUnits="userSpaceOnUse">
          <stop stopColor="#1e293b" />
          <stop offset="0.6" stopColor="#0f172a" />
          <stop offset="1" stopColor="#020617" />
        </linearGradient>
        <filter id="headGlow" x="-20%" y="-20%" width="140%" height="140%">
          <feGaussianBlur stdDeviation="3" result="blur" />
          <feComposite in="SourceGraphic" in2="blur" operator="over" />
        </filter>
      </defs>

      {/* Shadow */}
      <ellipse cx="80" cy="138" rx="46" ry="8" fill="#0369a1" fillOpacity="0.18" />

      {/* Headband Outer Arch */}
      <path d="M 40 85 C 40 40 120 40 120 85" stroke="#1e293b" strokeWidth="10" strokeLinecap="round" />
      <path d="M 44 80 C 44 45 116 45 116 80" stroke="#38bdf8" strokeWidth="2.5" strokeLinecap="round" />

      {/* Padded Cushion Under Headband */}
      <path d="M 52 64 C 62 54 98 54 108 64" stroke="#334155" strokeWidth="5" strokeLinecap="round" />

      {/* Left Earcup */}
      <g transform="translate(30, 75)">
        <rect x="0" y="0" width="18" height="38" rx="9" fill="url(#hpGrad)" stroke="#38bdf8" strokeWidth="1.5" />
        <rect x="14" y="4" width="8" height="30" rx="4" fill="#0f172a" stroke="#475569" strokeWidth="1" />
        <circle cx="9" cy="19" r="5" stroke="#38bdf8" strokeWidth="1.5" filter="url(#headGlow)" />
      </g>

      {/* Right Earcup */}
      <g transform="translate(112, 75)">
        <rect x="0" y="0" width="8" height="30" rx="4" fill="#0f172a" stroke="#475569" strokeWidth="1" />
        <rect x="4" y="0" width="18" height="38" rx="9" fill="url(#hpGrad)" stroke="#38bdf8" strokeWidth="1.5" />
        <circle cx="13" cy="19" r="5" stroke="#38bdf8" strokeWidth="1.5" filter="url(#headGlow)" />
      </g>
    </svg>
  )
}

export function TechPowerBank({ size = 120, className = '' }) {
  return (
    <svg width={size} height={size} viewBox="0 0 160 160" fill="none" xmlns="http://www.w3.org/2000/svg" className={className}>
      <defs>
        <linearGradient id="pbBody" x1="45" y1="35" x2="115" y2="125" gradientUnits="userSpaceOnUse">
          <stop stopColor="#1e293b" />
          <stop offset="0.5" stopColor="#334155" />
          <stop offset="1" stopColor="#0f172a" />
        </linearGradient>
      </defs>

      {/* Shadow */}
      <ellipse cx="80" cy="138" rx="45" ry="8" fill="#0369a1" fillOpacity="0.18" />

      {/* Power Bank Chassis */}
      <rect x="48" y="32" width="64" height="100" rx="14" fill="url(#pbBody)" stroke="#38bdf8" strokeWidth="1.6" />

      {/* Top Ports */}
      <rect x="58" y="36" width="12" height="4" rx="1.5" fill="#020617" stroke="#475569" strokeWidth="0.8" />
      <rect x="74" y="36" width="12" height="4" rx="1.5" fill="#020617" stroke="#475569" strokeWidth="0.8" />
      <rect x="90" y="36" width="12" height="4" rx="1.5" fill="#0284c7" />

      {/* Glossy LED Display Panel */}
      <rect x="56" y="52" width="48" height="26" rx="6" fill="#020617" stroke="#1e293b" strokeWidth="1" />
      <text x="63" y="69" fill="#38bdf8" fontSize="13" fontWeight="bold" fontFamily="monospace">100%</text>

      {/* Lightning Quick Charge Icon */}
      <polygon points="94,57 88,67 92,67 90,75 97,64 93,64" fill="#22c55e" />

      {/* Battery Status Bar */}
      <rect x="58" y="90" width="44" height="5" rx="2.5" fill="#0f172a" stroke="#475569" strokeWidth="0.8" />
      <rect x="59.5" y="91.5" width="41" height="2" rx="1" fill="#38bdf8" />

      <text x="64" y="112" fill="#64748b" fontSize="7" fontWeight="bold" fontFamily="sans-serif">20000 mAh</text>
      <text x="66" y="122" fill="#38bdf8" fontSize="6" fontFamily="sans-serif">65W FAST</text>
    </svg>
  )
}

export function TechChair({ size = 120, className = '' }) {
  return (
    <svg width={size} height={size} viewBox="0 0 160 160" fill="none" xmlns="http://www.w3.org/2000/svg" className={className}>
      {/* Shadow */}
      <ellipse cx="80" cy="138" rx="42" ry="7" fill="#0369a1" fillOpacity="0.18" />

      {/* 5-star Caster Base */}
      <path d="M 80 125 L 55 136 M 80 125 L 105 136 M 80 125 L 80 138 M 80 125 L 62 120 M 80 125 L 98 120" stroke="#475569" strokeWidth="3" strokeLinecap="round" />
      <rect x="78" y="105" width="4" height="20" fill="#334155" />

      {/* Seat Cushion */}
      <path d="M 48 98 C 48 94 60 92 80 92 C 100 92 112 94 112 98 L 108 106 C 100 108 60 108 52 106 Z" fill="#1e293b" stroke="#38bdf8" strokeWidth="1.5" />

      {/* Armrests */}
      <path d="M 46 80 L 46 95 M 42 80 L 50 80" stroke="#475569" strokeWidth="2.5" strokeLinecap="round" />
      <path d="M 114 80 L 114 95 M 110 80 L 118 80" stroke="#475569" strokeWidth="2.5" strokeLinecap="round" />

      {/* High-back Ergonomic Backrest with Cyber Bolsters */}
      <path
        d="M 60 92 C 55 70 54 48 62 36 C 68 28 92 28 98 36 C 106 48 105 70 100 92 Z"
        fill="#0f172a"
        stroke="#38bdf8"
        strokeWidth="1.6"
      />

      {/* Lumbar & Neck Pillows */}
      <rect x="68" y="38" width="24" height="8" rx="3" fill="#0284c7" />
      <rect x="65" y="72" width="30" height="12" rx="4" fill="#0284c7" />

      {/* Racing Harness Cutouts */}
      <ellipse cx="73" cy="50" rx="3" ry="5" fill="#1e293b" stroke="#38bdf8" strokeWidth="1" />
      <ellipse cx="87" cy="50" rx="3" ry="5" fill="#1e293b" stroke="#38bdf8" strokeWidth="1" />
    </svg>
  )
}

export function TechCable({ size = 120, className = '' }) {
  return (
    <svg width={size} height={size} viewBox="0 0 160 160" fill="none" xmlns="http://www.w3.org/2000/svg" className={className}>
      {/* Shadow */}
      <ellipse cx="80" cy="138" rx="46" ry="7" fill="#0369a1" fillOpacity="0.18" />

      {/* Braided Cable Arc */}
      <path d="M 45 105 C 45 45 115 45 115 105" stroke="#334155" strokeWidth="7" strokeLinecap="round" />
      <path d="M 45 105 C 45 45 115 45 115 105" stroke="#0ea5e9" strokeWidth="1.8" strokeDasharray="3 3" strokeLinecap="round" />

      {/* Left Connector */}
      <rect x="37" y="105" width="16" height="22" rx="3" fill="#0f172a" stroke="#38bdf8" strokeWidth="1.5" />
      <rect x="40" y="127" width="10" height="7" rx="1.5" fill="#e2e8f0" />
      <circle cx="45" cy="116" r="2" fill="#38bdf8" />

      {/* Right Connector */}
      <rect x="107" y="105" width="16" height="22" rx="3" fill="#0f172a" stroke="#38bdf8" strokeWidth="1.5" />
      <rect x="110" y="127" width="10" height="7" rx="1.5" fill="#e2e8f0" />
      <circle cx="115" cy="116" r="2" fill="#38bdf8" />
    </svg>
  )
}

// Ánh xạ linh hoạt theo từ khóa
export function getGadgetComponent(typeKey = 'cable') {
  switch (typeKey) {
    case 'hub': return TechHubUsbC
    case 'gamepad': return TechGamepad
    case 'keyboard': return TechKeyboard
    case 'mouse': return TechMouse
    case 'laptop': return TechLaptop
    case 'phone': return TechPhone
    case 'monitor': return TechMonitor
    case 'headphones': return TechHeadphones
    case 'powerbank': return TechPowerBank
    case 'chair': return TechChair
    default: return TechCable
  }
}
