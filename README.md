# TaleShop Developer API

A comprehensive API for integrating with TaleShop — the server-side shop and auction house plugin for Hytale.

## Quick Start

```java
// Get the API instance
TaleShopProvider api = TaleShopAPI.get();
if (api == null) return; // TaleShop is not loaded or disabled

// Check if the plugin is functional
if (!api.isEnabled()) return;
```

## Sub-APIs

The TaleShop API is divided into four sub-APIs, each accessible from the main `TaleShopProvider`:

| Sub-API | Accessor | Description |
|---------|----------|-------------|
| [ShopCatalog](docs/SHOP_CATALOG.md) | `api.getShopCatalog()` | Read/write shop categories and items |
| [ShopTransaction](docs/SHOP_TRANSACTION.md) | `api.getShopTransaction()` | Execute buy/sell operations |
| [AuctionHouse](docs/AUCTION_HOUSE.md) | `api.getAuctionHouse()` | Auction listing operations (async) |
| [EconomyAccess](docs/ECONOMY.md) | `api.getEconomy()` | Read-only economy information |

Additionally, TaleShop fires [Events](docs/EVENTS.md) that your plugin can listen to.

## Setup (Dependency)

Add TaleShop as a dependency in your `manifest.json`:

```json
{
  "Dependencies": ["AselStudios:TaleShop"]
}
```

Then access the API from your plugin code:

```java
TaleShopProvider api = TaleShopAPI.get();
```

## Important Notes

- **Null safety:** `TaleShopAPI.get()` returns `null` when TaleShop is not loaded.
- **Auction house:** `api.getAuctionHouse()` returns `null` when the auction house is disabled in config.
- **Thread safety:** All catalog reads return unmodifiable copies. Auction operations return `CompletableFuture` and must not be blocked on the main thread.
- **Economy:** The `EconomyAccess` API is intentionally read-only. Economy manipulation should go through `ShopTransaction` or the economy plugin's own API.
- **Events:** All cancellable events are fired *before* the action executes. Cancel them to prevent the action.

## Package Structure

```
org.aselstudios.taleshop.api/
├── TaleShopAPI            — Static entry point
├── TaleShopProvider       — Main API interface
├── shop/
│   ├── ShopCatalog        — Category & item CRUD
│   └── ShopTransaction    — Buy/sell operations
├── auction/
│   └── AuctionHouse       — Auction operations (async)
├── economy/
│   └── EconomyAccess      — Read-only economy access
└── event/
    ├── TaleShopEvent       — Base cancellable event
    ├── TaleShopEvents      — Event dispatch utility
    ├── ShopBuyEvent        — Fired before purchase
    ├── ShopSellEvent       — Fired before sale
    ├── AuctionCreateEvent  — Fired before listing creation
    ├── AuctionBuyEvent     — Fired before auction purchase
    ├── AuctionCancelEvent  — Fired before listing cancellation
    └── AuctionExpireEvent  — Fired when listing expires (non-cancellable)
```

## Model Classes

These classes are returned by the API and can be used directly:

| Class | Description |
|-------|-------------|
| `Category` | Shop category (id, name, icon, permission) |
| `ShopItem` | Shop item with full metadata (material, prices, costs, description, enchantments) |
| `ItemCost` | Material + count pair used for item-based costs |
| `AuctionListing` | Active auction listing with price, seller, item data |
| `AuctionMailboxItem` | Mailbox entry for claimed/expired items |
