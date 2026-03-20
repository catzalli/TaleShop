package org.aselstudios.taleshop.api.event;

import com.hypixel.hytale.event.IEvent;
import org.aselstudios.taleshop.model.AuctionListing;

/** Fired when an auction listing expires. Not cancellable. */
public class AuctionExpireEvent implements IEvent<Void> {

    private final AuctionListing listing;

    public AuctionExpireEvent(AuctionListing listing) {
        this.listing = listing;
    }

    public AuctionListing getListing() { return listing; }
}
