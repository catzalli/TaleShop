# ShopTransaction API

Execute buy and sell operations through TaleShop. All transactions fire the appropriate events (`ShopBuyEvent`, `ShopSellEvent`) before executing, allowing cancellation by listeners.

## Getting the Transaction API

```java
TaleShopProvider api = TaleShopAPI.get();
ShopTransaction tx = api.getShopTransaction();
```

---

## Buying Items

```java
// Get the item from catalog first
ShopItem item = api.getShopCatalog().getItem("Diamond_Ore");
if (item == null || !item.canBuy()) return;

// Buy a specific amount
tx.buyItem(player, item, 5);

// Buy a command item — use buyItem with amount 1
ShopItem vipRank = api.getShopCatalog().getItem("Emerald"); // command item
if (vipRank != null && vipRank.isCommandItem()) {
    tx.buyItem(player, vipRank, 1);
}

// Fill inventory — buys as many as the player can afford and carry
tx.fillInventory(player, item);
```

## Selling Items

```java
ShopItem item = api.getShopCatalog().getItem("Diamond_Ore");
if (item == null || !item.canSell()) return;

// Sell a specific amount
tx.sellItem(player, item, 10);

// Sell all of a specific item from inventory
tx.sellAllOfItem(player, item);

// Sell ALL sellable items from inventory
tx.sellAll(player);
```

## Preview Sell All

Preview how much a player would earn from selling everything, without actually executing the sale:

```java
ShopTransaction.SellAllStats stats = tx.previewSellAll(player);
long itemCount = stats.itemCount();        // 42
BigDecimal totalValue = stats.totalValue(); // 1250.00
```

---

## Transaction Flow

Every transaction follows this flow:

1. **Validation** — checks item exists, player has funds/items, inventory space
2. **Event** — fires `ShopBuyEvent` or `ShopSellEvent` (cancellable)
3. **Execution** — withdraws money, removes items, gives items
4. **Rollback** — if any step fails, all previous steps are reversed
5. **Message** — success/failure message sent to player

Transactions are protected by:
- Per-player locking (no concurrent transactions)
- 250ms cooldown between transactions
- Amount sanitization (max buy: configurable, max sell: 1,000,000)
- Full rollback on any failure
