package org.aselstudios.taleshop.api.event;

import com.hypixel.hytale.server.core.entity.entities.Player;
import org.aselstudios.taleshop.model.ShopItem;

import java.math.BigDecimal;

/** Fired before a shop purchase. Cancel to prevent it. */
public class ShopBuyEvent extends TaleShopEvent {

    private final Player player;
    private final ShopItem item;
    private final int amount;
    private final BigDecimal totalCost;

    public ShopBuyEvent(Player player, ShopItem item, int amount, BigDecimal totalCost) {
        this.player = player;
        this.item = item;
        this.amount = amount;
        this.totalCost = totalCost;
    }

    public Player getPlayer() { return player; }
    public ShopItem getItem() { return item; }
    public int getAmount() { return amount; }
    public BigDecimal getTotalCost() { return totalCost; }
}
