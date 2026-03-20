# ShopCatalog API

Read and write access to the TaleShop item catalog — categories and items.

## Getting the Catalog

```java
TaleShopProvider api = TaleShopAPI.get();
ShopCatalog catalog = api.getShopCatalog();
```

---

## Reading Categories

```java
// Get a specific category by ID
Category weapons = catalog.getCategory("weapons");
if (weapons != null) {
    String id   = weapons.getId();              // "weapons"
    String name = weapons.getName();            // "Weapons"
    String icon = weapons.getIconMaterial();     // "Diamond_Sword"
    String perm = weapons.getPermission();       // null or "shop.weapons"
    boolean hasPerm = weapons.hasPermission();   // true if permission is set
}

// Get all categories
List<Category> all = catalog.getAllCategories();

// Get categories visible to a specific player (permission-filtered)
List<Category> visible = catalog.getVisibleCategories(player);
```

## Reading Items

```java
// Get a specific item by exact material ID
ShopItem diamond = catalog.getItem("Diamond_Ore");
if (diamond != null) {
    String name = diamond.getDisplayName();       // "Diamond"
    String mat  = diamond.getMaterial();           // "Diamond_Ore"
    String catId = diamond.getCategoryId();        // "ores"
    BigDecimal buy  = diamond.getBuyPrice();       // 100.00
    BigDecimal sell = diamond.getSellPrice();      // 50.00
    String desc = diamond.getDescription();        // "A shiny gem" or null
    boolean canBuy  = diamond.canBuy();            // true if buy price > 0
    boolean canSell = diamond.canSell();           // true if sell price > 0

    // Helper checks
    boolean isCmd       = diamond.isCommandItem();   // has commands and doesn't give item
    boolean hasCommands = diamond.hasCommands();      // has any commands
    boolean givesItem   = diamond.isGiveItem();       // gives physical item on purchase
    boolean hasDesc     = diamond.hasDescription();   // has a description
    boolean hasPerm     = diamond.hasPermission();    // has permission requirement
    String perm         = diamond.getPermission();    // null or "shop.diamond"

    // Item costs (barter trading)
    List<ItemCost> buyCosts = diamond.getBuyCosts();   // items required to buy
    List<ItemCost> sellCosts = diamond.getSellCosts();  // bonus items given on sell
    boolean hasItemCost     = diamond.hasItemCost();     // buyCosts not empty
    boolean hasSellItemCost = diamond.hasSellItemCost(); // sellCosts not empty
    boolean hasMoneyCost    = diamond.hasMoneyCost();    // buy price > 0

    for (ItemCost cost : buyCosts) {
        String material = cost.getMaterial();   // "Gold_Ingot"
        int count = cost.getCount();            // 5
    }

    // Enchantments (requires SimpleEnchantments plugin)
    if (diamond.hasEnchantments()) {
        Map<String, Integer> enchants = diamond.getEnchantments();
        // e.g. {"sharpness": 3, "unbreaking": 2}
    }

    // Stock limits
    if (diamond.hasBuyStock()) {
        StockMode mode = diamond.getBuyStockMode();          // PER_PLAYER or SERVER
        int limit      = diamond.getBuyStockLimit();          // e.g. 10
        int renewal    = diamond.getBuyStockRenewalHours();   // 0 = permanent, 24 = daily
    }
    if (diamond.hasSellStock()) {
        StockMode mode = diamond.getSellStockMode();
        int limit      = diamond.getSellStockLimit();
        int renewal    = diamond.getSellStockRenewalHours();
    }
}

// Get all items in a category
List<ShopItem> weaponItems = catalog.getItemsByCategory("weapons");

// Get permission-filtered items
List<ShopItem> visibleItems = catalog.getVisibleItemsByCategory("weapons", player);

// Search items by name or material
List<ShopItem> results = catalog.search("diamond");

// Search with permission filtering
List<ShopItem> visibleResults = catalog.searchVisible("diamond", player);
```

### Partial ID Matching

Some third-party plugins append runtime metadata to item IDs (e.g. `HyFishing_Salmon_Common_Item__dtt_cb3f66c9`). Use `resolveItem()` to match these against shop entries:

```java
// Exact match first, then longest-prefix fallback
ShopItem item = catalog.resolveItem("HyFishing_Salmon_Common_Item__dtt_cb3f66c9");
// Returns the shop entry for "HyFishing_Salmon_Common_Item" if it exists

// Partial matching can be disabled via config: PartialIdMatching: false
// When disabled, resolveItem() behaves identically to getItem()
```

---

## Creating & Updating Categories

`upsertCategory` creates a new category or updates an existing one with the same ID.

```java
// Simple category (no permission required)
catalog.upsertCategory("tools", "Tools", "Iron_Pickaxe");

// Category with permission requirement
catalog.upsertCategory("vip-shop", "VIP Shop", "Gold_Block", "shop.vip");

// Don't forget to save to disk!
catalog.save();
```

## Removing & Renaming Categories

```java
// Remove a category and all its items — returns false if category not found
boolean removed = catalog.removeCategory("tools");

// Rename a category (moves all items to the new ID)
boolean success = catalog.renameCategory("old-id", "new-id");

catalog.save();
```

---

## Creating & Updating Items

`upsertItem` creates a new item or updates an existing one with the same material. Three overloads are available:

### Simple Item

```java
catalog.upsertItem("tools", "Iron_Pickaxe", "Iron Pickaxe", 50.0, 25.0);
```

### Full Item (single command string)

```java
catalog.upsertItem(
    "tools",                          // categoryId
    "Diamond_Pickaxe",                // material (unique item ID)
    "Enchanted Diamond Pickaxe",      // display name
    500.0,                            // buy price
    250.0,                            // sell price
    null,                             // command (null = normal item)
    List.of(new ItemCost("Diamond", 3)),  // buy costs (items required)
    List.of(),                        // sell costs (bonus items on sell)
    "A powerful mining tool",         // description
    "shop.diamond_tools",             // permission (null = no restriction)
    Map.of("efficiency", 4, "unbreaking", 3)  // enchantments
);
```

### Full Item (multiple commands + giveItem control)

```java
catalog.upsertItem(
    "ranks",                          // categoryId
    "Emerald",                        // material (used as display icon)
    "VIP Rank + Kit",                 // display name
    1000.0,                           // buy price
    0.0,                              // sell price
    List.of("rank set {player} vip",
            "kit give {player} vip"), // commands (multiple, executed in order)
    false,                            // giveItem (false = don't give physical item)
    null, null,                       // no item costs
    "Grants VIP rank and starter kit",// description
    null,                             // no permission
    null                              // no enchantments
);
```

### Full Item with Stock Limits

```java
catalog.upsertItem(
    "limited",                        // categoryId
    "Diamond_Ore",                    // material
    "Limited Diamond",                // display name
    500.0,                            // buy price
    250.0,                            // sell price
    null, true,                       // no commands, give item
    null, null,                       // no item costs
    "Only 10 per player per day!",    // description
    null,                             // no permission
    null,                             // no enchantments
    StockMode.PER_PLAYER, 10, 24,    // buy: 10 per player, resets every 24 hours
    StockMode.NONE, 0, 0             // sell: no limit
);

// Stock modes:
// StockMode.NONE       — no limit
// StockMode.PER_PLAYER — each player has independent limit
// StockMode.SERVER     — shared limit across all players
// Renewal hours: 0 = permanent (never resets), >0 = resets every N hours
```

### Command Items

Command items execute server command(s) when purchased instead of giving a physical item:

```java
// Single command — use the String command overload
catalog.upsertItem(
    "ranks", "Emerald", "VIP Rank",
    1000.0, 0.0,
    "rank set {player} vip",             // {player} and {uuid} are replaced
    null, null,                           // no item costs
    "Grants VIP rank permanently", null, null
);

// Multiple commands — use the List<String> commands overload
catalog.upsertItem(
    "ranks", "Emerald", "VIP Bundle",
    2000.0, 0.0,
    List.of("rank set {player} vip", "eco give {player} 1000"),
    false,                                // giveItem = false
    null, null, "VIP rank + $1000 bonus", null, null
);
```

**Always save after modifications:**

```java
catalog.save();
```

## Removing Items

```java
// Returns false if item not found
boolean removed = catalog.removeItem("Diamond_Pickaxe");
catalog.save();
```

---

## Persisting Changes

**Important:** All `upsertCategory`, `removeCategory`, `upsertItem`, and `removeItem` calls modify the in-memory catalog only. You must call `save()` to write changes to disk:

```java
catalog.save(); // Writes each category to its own JSON file in categories/ folder
```

---

## Web Integration Example

Build a REST API that exposes shop data to your website:

```java
// In your web plugin's endpoint handler:
TaleShopProvider api = TaleShopAPI.get();
if (api == null) return errorResponse("TaleShop not available");

ShopCatalog catalog = api.getShopCatalog();

// Serialize all categories and items to JSON for your website
List<Category> categories = catalog.getAllCategories();
for (Category cat : categories) {
    List<ShopItem> items = catalog.getItemsByCategory(cat.getId());
    // Build your JSON response with category name, icon, items, prices...
}

// Remote item management from admin panel
catalog.upsertItem("weapons", "Diamond_Sword", "Diamond Sword", 200.0, 100.0);
catalog.save();
```
