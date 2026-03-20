# EconomyAccess API

Read-only access to the economy provider used by TaleShop.

> **Note:** Withdraw and deposit are intentionally omitted. Economy manipulation should go through `ShopTransaction` or the economy plugin's own API.

## Getting the Economy API

```java
TaleShopProvider api = TaleShopAPI.get();
EconomyAccess economy = api.getEconomy();
```

---

## Checking Availability

```java
if (!economy.isAvailable()) {
    // No economy provider is loaded
    return;
}

// Get the provider name (e.g., "EcoTale", "IEconomy")
String provider = economy.getProviderName();
```

## Reading Balances

```java
UUID playerUuid = player.getUuid();

// Get player balance
BigDecimal balance = economy.getBalance(playerUuid);

// Check if player can afford an amount
boolean canAfford = economy.hasBalance(playerUuid, new BigDecimal("100.00"));
```

## Formatting

```java
BigDecimal amount = new BigDecimal("1500.50");

// Format as display string (e.g., "$1,500.50" or "1500.50 coins")
String display = economy.format(amount);

// Format as price string (may include currency symbol)
String price = economy.formatPrice(amount);
```

---

## Example: Price Display on Website

```java
TaleShopProvider api = TaleShopAPI.get();
EconomyAccess economy = api.getEconomy();
ShopCatalog catalog = api.getShopCatalog();

ShopItem item = catalog.getItem("Diamond_Sword");
if (item != null && item.canBuy()) {
    String formattedPrice = economy.format(item.getBuyPrice());
    // Send to website: "Diamond Sword — $500.00"
}
```
