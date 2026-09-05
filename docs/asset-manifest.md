# Storefront Asset Manifest

| File | Source | Purpose | License / status | Used by |
| --- | --- | --- | --- | --- |
| `frontend/public/assets/khanggear-logo.png` | Existing local KhangGear system logo | Header and footer branding | Project-provided | `StorefrontLayout` |
| `frontend/public/assets/hero-setup.png` | Existing local `src/main/webapp/assets/images/auth-background.png` | Home hero setup photo | Project-provided | `HomePage` |
| `frontend/public/assets/images/Screenshot 2026-09-05 *.png` | User-provided local reference screenshots | Layout and visual reference only; not rendered by a storefront component | User-provided | Design reference |

Vite copies these assets into `src/main/webapp/storefront/assets/` for the Tomcat WAR. Product records currently containing `placehold.co` URLs are not requested by the storefront; a local UI fallback is shown instead. Upload local product images through the existing admin product form to display actual product photos.
