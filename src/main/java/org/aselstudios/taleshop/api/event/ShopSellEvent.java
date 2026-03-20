package org.aselstudios.taleshop.api.event;

import com.hypixel.hytale.server.core.entity.entities.Player;
import org.aselstudios.taleshop.model.ShopItem;

import java.math.BigDecimal;

/** Fired before a shop sale. Cancel to prevent it. */
public class ShopSellEvent extends TaleShopEvent {

    private final Player player;
    private final ShopItem item;
    private final int amount;
    private final BigDecimal totalEarnings;

    public ShopSellEvent(Player player, ShopItem item, int amount, BigDecimal totalEarnings) {
        this.player = player;
        this.item = item;
        this.amount = amount;
        this.totalEarnings = totalEarnings;
    }

    public Player getPlayer() { return player; }
    public ShopItem getItem() { return item; }
    public int getAmount() { return amount; }
    public BigDecimal getTotalEarnings() { return totalEarnings; }
}
