package org.aselstudios.taleshop.api.event;

import com.hypixel.hytale.server.core.entity.entities.Player;
import org.aselstudios.taleshop.model.AuctionListing;

/** Fired before an auction purchase. Cancel to prevent it. */
public class AuctionBuyEvent extends TaleShopEvent {

    private final Player buyer;
    private final AuctionListing listing;

    public AuctionBuyEvent(Player buyer, AuctionListing listing) {
        this.buyer = buyer;
        this.listing = listing;
    }

    public Player getBuyer() { return buyer; }
    public AuctionListing getListing() { return listing; }
}
