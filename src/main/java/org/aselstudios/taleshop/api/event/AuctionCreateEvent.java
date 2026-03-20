package org.aselstudios.taleshop.api.event;

import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.inventory.ItemStack;
import org.aselstudios.taleshop.model.ItemCost;

import javax.annotation.Nullable;
import java.math.BigDecimal;
import java.util.List;

/** Fired before an auction listing is created. Cancel to prevent it. */
public class AuctionCreateEvent extends TaleShopEvent {

    private final Player seller;
    private final ItemStack itemStack;
    private final BigDecimal price;
    @Nullable
    private final List<ItemCost> requestedItems;

    public AuctionCreateEvent(Player seller, ItemStack itemStack, BigDecimal price,
                               @Nullable List<ItemCost> requestedItems) {
        this.seller = seller;
        this.itemStack = itemStack;
        this.price = price;
        this.requestedItems = requestedItems;
    }

    public Player getSeller() { return seller; }
    public ItemStack getItemStack() { return itemStack; }
    public BigDecimal getPrice() { return price; }
    @Nullable
    public List<ItemCost> getRequestedItems() { return requestedItems; }
}
