package org.aselstudios.taleshop.api.shop;

import com.hypixel.hytale.server.core.entity.entities.Player;
import org.aselstudios.taleshop.model.ShopItem;

import java.math.BigDecimal;

/** Buy and sell operations through TaleShop. */
public interface ShopTransaction {

    void buyItem(Player player, ShopItem item, int amount);

    void sellItem(Player player, ShopItem item, int amount);

    void fillInventory(Player player, ShopItem item);

    void sellAllOfItem(Player player, ShopItem item);

    void sellAll(Player player);

    SellAllStats previewSellAll(Player player);

    record SellAllStats(long itemCount, BigDecimal totalValue) {}
}
