# AuctionHouse API

Auction house operations. All database-backed methods return `CompletableFuture` and **must not be blocked on the main thread**.

## Getting the Auction House API

```java
TaleShopProvider api = TaleShopAPI.get();

// Auction house may be null if disabled in config
AuctionHouse auction = api.getAuctionHouse();
if (auction == null) return; // Auction house is disabled
```

---

## Reading Listings

```java
// Get all active (non-expired) listings
auction.getActiveListings().thenAccept(listings -> {
    for (AuctionListing listing : listings) {
        UUID id = listing.getId();
        UUID seller = listing.getSellerUuid();
        String sellerName = listing.getSellerName();
        BigDecimal price = listing.getPrice();
        long expiresAt = listing.getExpiresAt();
        boolean hasMoneyPrice = listing.hasMoneyPrice();

        // Barter/item request
        if (listing.hasRequestedItem()) {
            List<ItemCost> requested = listing.getRequestedItems();
            for (ItemCost cost : requested) {
                String material = cost.getMaterial();
                int count = cost.getCount();
            }
        }

        // Get the actual ItemStack
        ItemStack stack = listing.getItemStack();
    }
});
```

## Reading Mailbox

```java
UUID playerUuid = player.getUuid();

// Get paginated mailbox (items from expired/cancelled/purchased listings)
auction.getMailbox(playerUuid, 0, 10).thenAccept(items -> {
    for (AuctionMailboxItem item : items) {
        long id = item.getId();
        String itemId = item.getItemId();
        ItemStack stack = item.getItemStack();
    }
});
```

---

## Creating Listings

```java
// Create a money-only listing
// inventorySlot = the slot index in the player's inventory
auction.createListing(player, inventorySlot, new BigDecimal("100.00"), null)
    .thenAccept(success -> {
        if (success) {
            // Listing created, item removed from inventory
        }
    });

// Create a barter listing (requesting items in exchange)
List<ItemCost> requestedItems = List.of(
    new ItemCost("Diamond", 5),
    new ItemCost("Gold_Ingot", 10)
);
auction.createListing(player, inventorySlot, new BigDecimal("50.00"), requestedItems)
    .thenAccept(success -> {
        // Hybrid listing: buyer must pay 50.00 + 5 diamonds + 10 gold
    });
```

## Buying Listings

```java
UUID listingId = ...; // from getActiveListings()
auction.buyListing(buyer, listingId).thenAccept(success -> {
    if (success) {
        // Purchase complete — money withdrawn, items exchanged
    }
});
```

## Cancelling Listings

```java
// Only the seller can cancel their own listing
auction.cancelListing(player, listingId).thenAccept(success -> {
    if (success) {
        // Listing cancelled — item moved to seller's mailbox
    }
});
```

---

## Claiming from Mailbox

```java
// Claim all pending money from sold listings
auction.claimMoney(player);

// Claim all items from mailbox
auction.claimAllItems(player);

// Claim a specific mailbox item by ID
auction.claimItem(player, mailboxItemId);
```

---

## Auction State

```java
// Check if auction house is enabled
boolean enabled = auction.isEnabled();

// Get a player's listing limit
int limit = auction.getPlayerListingLimit(player);
// Players can have custom limits via permission: taleshop.auction.<number>
```

---

## Important Notes

- All methods return `CompletableFuture` — do **not** call `.join()` or `.get()` on the main thread.
- Listings expire automatically based on the configured duration.
- Tax is applied either at listing creation (upfront) or at sale, depending on config.
- If item delivery fails after purchase, the item is safely moved to the buyer's mailbox.
- The `AuctionCreateEvent`, `AuctionBuyEvent`, and `AuctionCancelEvent` events are fired before each operation.
