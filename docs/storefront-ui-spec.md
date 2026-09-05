# KhangGear Storefront UI

React storefront is independent from the JSP admin UI. All storefront styles live in `frontend/src/styles/storefront.css` and use the `kg-` prefix.

| Route | Screen | Data source |
| --- | --- | --- |
| `/home` | Hero, policies, categories, best sellers, new arrivals, setup banner | storefront category/product APIs |
| `/products` | Search, filters, sorting, pagination, empty/loading/error states | `GET /api/storefront/products` |
| `/products/:id` | Product detail, stock, quantity picker, related products | `GET /api/storefront/products/:id` |
| `/cart` | Session cart and quantity validation | cart APIs |
| `/checkout` | Authenticated checkout for CUSTOMER users | `POST /api/storefront/checkout` |
| `/account/profile` | Safe profile editing without password data | account profile API |
| `/account/orders` | Customer order history | account order API |
| `/account/orders/:id` | Order details and pending-order cancellation | account order API |

The header includes KhangGear branding, search, cart quantity badge, account link, and logout when an authenticated session exists. The mobile navigation collapses at 767px. Motion uses transform/opacity only and respects `prefers-reduced-motion`.

## Design Notes

- Colors: navy `#0B2239`, blue `#1677FF`, cyan `#37C5FF`, page `#F5F7FB`.
- Product cards preserve image geometry using fixed visual areas and `object-fit: contain`.
- Existing database image URLs that point to remote placeholder services are deliberately not hotlinked. The UI renders a local Lucide category fallback until a local product image is uploaded.
- Hero and KhangGear logo are local assets emitted into the production bundle.
