# TaleShop Events

TaleShop fires events through the Hytale event bus before key operations. Cancellable events allow you to prevent the action from executing.

## Listening to Events

Register listeners in your plugin's `setup()` method:

```java
@Override
protected void setup() {
    super.setup();

    // Listen for shop purchases
    getEventRegistry().registerGlobal(ShopBuyEvent.class, this::onShopBuy);

    // Listen for shop sales
    getEventRegistry().registerGlobal(ShopSellEvent.class, this::onShopSell);

    // Listen for auction listings
    getEventRegistry().registerGlobal(AuctionCreateEvent.class, this::onAuctionCreate);
}
```

---

## Shop Events

### ShopBuyEvent

Fired **before** a shop buy transaction executes. Cancel to prevent the purchase.

```java
private void onShopBuy(ShopBuyEvent event) {
    Player player = event.getPlayer();
    ShopItem item = event.getItem();
    int amount = event.getAmount();
    BigDecimal totalCost = event.getTotalCost();

    // Example: Block diamond purchases during an event
    if (item.getMaterial().equals("Diamond_Ore")) {
        event.setCancelled(true);
        player.sendMessage(Message.raw("Diamond purchases are disabled!"));
    }

    // Example: Log all purchases over $1000
    if (totalCost.compareTo(new BigDecimal("1000")) > 0) {
        logger.info(player.getDisplayName() + " buying " + amount + "x "
            + item.getDisplayName() + " for " + totalCost);
    }
}
```

### ShopSellEvent

Fired **before** a shop sell transaction executes. Cancel to prevent the sale.

```java
private void onShopSell(ShopSellEvent event) {
    Player player = event.getPlayer();
    ShopItem item = event.getItem();
    int amount = event.getAmount();
    BigDecimal totalEarnings = event.getTotalEarnings();

    // Example: Limit sell amounts to 100 per transaction
    if (amount > 100) {
        event.setCancelled(true);
    }
}
```

---

## Auction Events

### AuctionCreateEvent

Fired **before** an auction listing is created. Cancel to prevent the listing.

```java
private void onAuctionCreate(AuctionCreateEvent event) {
    Player seller = event.getSeller();
    ItemStack itemStack = event.getItemStack();
    BigDecimal price = event.getPrice();
    List<ItemCost> requestedItems = event.getRequestedItems(); // nullable

    // Example: Minimum listing price
    if (price.compareTo(new BigDecimal("10")) < 0) {
        event.setCancelled(true);
        seller.sendMessage(Message.raw("Minimum listing price is $10!"));
    }
}
```

### AuctionBuyEvent

Fired **before** an auction purchase is executed. Cancel to prevent the purchase.

```java
private void onAuctionBuy(AuctionBuyEvent event) {
    Player buyer = event.getBuyer();
    AuctionListing listing = event.getListing();

    // Example: Prevent buying from specific sellers
    if (isBannedSeller(listing.getSellerUuid())) {
        event.setCancelled(true);
    }
}
```

### AuctionCancelEvent

Fired **before** an auction listing is cancelled. Cancel to prevent the cancellation.

```java
private void onAuctionCancel(AuctionCancelEvent event) {
    Player player = event.getPlayer();
    AuctionListing listing = event.getListing();

    // Example: Prevent cancellation in the last hour
    long timeLeft = listing.getExpiresAt() - System.currentTimeMillis();
    if (timeLeft < 3_600_000) { // 1 hour
        event.setCancelled(true);
        player.sendMessage(Message.raw("Cannot cancel listings in the last hour!"));
    }
}
```

### AuctionExpireEvent

Fired when an auction listing expires. This event is **NOT cancellable** — it is informational only.

```java
getEventRegistry().registerGlobal(AuctionExpireEvent.class, event -> {
    AuctionListing listing = event.getListing();
    // Log, notify, or update external systems
});
```

---

## Event Summary

| Event | Cancellable | When Fired |
|-------|-------------|------------|
| `ShopBuyEvent` | Yes | Before shop purchase (item or command) |
| `ShopSellEvent` | Yes | Before shop sale |
| `AuctionCreateEvent` | Yes | Before auction listing creation |
| `AuctionBuyEvent` | Yes | Before auction purchase |
| `AuctionCancelEvent` | Yes | Before listing cancellation |
| `AuctionExpireEvent` | No | When listing expires (informational) |

## Event Fields

| Event | Fields |
|-------|--------|
| `ShopBuyEvent` | `player`, `item` (ShopItem), `amount`, `totalCost` |
| `ShopSellEvent` | `player`, `item` (ShopItem), `amount`, `totalEarnings` |
| `AuctionCreateEvent` | `seller`, `itemStack`, `price`, `requestedItems` (nullable) |
| `AuctionBuyEvent` | `buyer`, `listing` (AuctionListing) |
| `AuctionCancelEvent` | `player`, `listing` (AuctionListing) |
| `AuctionExpireEvent` | `listing` (AuctionListing) |

---

## Event Hierarchy

All cancellable events extend `TaleShopEvent`, which implements `IEvent<Void>` and provides `setCancelled(boolean)` / `isCancelled()`.

`AuctionExpireEvent` is the exception — it implements `IEvent<Void>` directly and is not cancellable.
