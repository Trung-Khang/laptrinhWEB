# KhangGear Storefront API

All responses are UTF-8 JSON with the envelope `success`, `message`, `data`, and `fieldErrors`. Entities are converted to DTOs; passwords and persistence internals are never serialized.

| Method | Endpoint | Access |
| --- | --- | --- |
| GET | `/api/storefront/categories` | Public |
| GET | `/api/storefront/products?q=&categoryId=&minPrice=&maxPrice=&inStock=&sort=&page=&pageSize=` | Public |
| GET | `/api/storefront/products/{id}` | Public |
| GET | `/api/storefront/products/featured` | Public |
| GET | `/api/storefront/products/best-selling` | Public |
| GET | `/api/storefront/cart` | Session cart |
| POST | `/api/storefront/cart/items` | Session cart |
| PUT | `/api/storefront/cart/items/{productId}` | Session cart |
| DELETE | `/api/storefront/cart/items/{productId}` | Session cart |
| POST | `/api/storefront/checkout` | Active CUSTOMER session |
| GET/PUT | `/api/account/profile` | Active user session |
| GET | `/api/account/orders` | Active user session |
| GET | `/api/account/orders/{id}` | Owner only |
| PUT | `/api/account/orders/{id}/cancel` | Owner only, PENDING status |

## Cart and Checkout

Cart data is stored in the HTTP session under `storefrontCart`; credentials are sent with every React request. Cart updates cannot exceed the stock returned from the database.

Checkout receives `fullName`, `phone`, `email`, `address`, `note`, and `paymentMethod` (`COD` or `BANK_TRANSFER`). The server locks each requested product, reloads the price and stock, calculates totals, inserts `orders` and `order_items`, decrements stock, and clears the session cart only after one successful transaction. Any exception rolls back the order and stock change.

The Vite development origin `http://localhost:5173` is allowed explicitly by `StorefrontCorsFilter` with credentials. Production React bundle and API are served by the Tomcat application origin, so CORS is not needed.
