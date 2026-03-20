package org.aselstudios.taleshop.api.shop;

import com.hypixel.hytale.server.core.entity.entities.Player;
import org.aselstudios.taleshop.model.Category;
import org.aselstudios.taleshop.model.ItemCost;
import org.aselstudios.taleshop.model.ShopItem;
import org.aselstudios.taleshop.model.StockMode;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;

/** Read and write access to the TaleShop item catalog. */
public interface ShopCatalog {

    @Nullable
    Category getCategory(String id);

    List<Category> getAllCategories();

    List<Category> getVisibleCategories(Player player);

    @Nullable
    ShopItem getItem(String material);

    /**
     * Resolves an inventory item ID to a ShopItem. Tries exact match first,
     * then longest-prefix fallback when {@code PartialIdMatching} is enabled in config.
     */
    @Nullable
    ShopItem resolveItem(String inventoryItemId);

    List<ShopItem> getItemsByCategory(String categoryId);

    List<ShopItem> getVisibleItemsByCategory(String categoryId, Player player);

    List<ShopItem> search(String query);

    List<ShopItem> searchVisible(String query, Player player);

    void upsertCategory(String id, String name, String icon);

    void upsertCategory(String id, String name, String icon, @Nullable String permission);

    boolean removeCategory(String id);

    boolean renameCategory(String oldId, String newId);

    void upsertItem(String categoryId, String material, String name,
                     double buyPrice, double sellPrice);

    void upsertItem(String categoryId, String material, String name,
                     double buyPrice, double sellPrice,
                     @Nullable String command,
                     @Nullable List<ItemCost> buyCosts,
                     @Nullable List<ItemCost> sellCosts,
                     @Nullable String description,
                     @Nullable String permission,
                     @Nullable Map<String, Integer> enchantments);

    void upsertItem(String categoryId, String material, String name,
                     double buyPrice, double sellPrice,
                     @Nullable List<String> commands, boolean giveItem,
                     @Nullable List<ItemCost> buyCosts,
                     @Nullable List<ItemCost> sellCosts,
                     @Nullable String description,
                     @Nullable String permission,
                     @Nullable Map<String, Integer> enchantments);

    void upsertItem(String categoryId, String material, String name,
                     double buyPrice, double sellPrice,
                     @Nullable List<String> commands, boolean giveItem,
                     @Nullable List<ItemCost> buyCosts,
                     @Nullable List<ItemCost> sellCosts,
                     @Nullable String description,
                     @Nullable String permission,
                     @Nullable Map<String, Integer> enchantments,
                     @Nullable StockMode buyStockMode, int buyStockLimit, int buyStockRenewalHours,
                     @Nullable StockMode sellStockMode, int sellStockLimit, int sellStockRenewalHours);

    boolean removeItem(String material);

    void save();
}
