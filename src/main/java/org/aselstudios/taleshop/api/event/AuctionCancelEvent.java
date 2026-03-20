package org.aselstudios.taleshop.api.event;

import com.hypixel.hytale.server.core.entity.entities.Player;
import org.aselstudios.taleshop.model.AuctionListing;

/** Fired before an auction listing is cancelled. Cancel to prevent it. */
public class AuctionCancelEvent extends TaleShopEvent {

    private final Player player;
    private final AuctionListing listing;

    public AuctionCancelEvent(Player player, AuctionListing listing) {
        this.player = player;
        this.listing = listing;
    }

    public Player getPlayer() { return player; }
    public AuctionListing getListing() { return listing; }
}
