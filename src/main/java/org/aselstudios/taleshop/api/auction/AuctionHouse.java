package org.aselstudios.taleshop.api.auction;

import com.hypixel.hytale.server.core.entity.entities.Player;
import org.aselstudios.taleshop.model.AuctionListing;
import org.aselstudios.taleshop.model.AuctionMailboxItem;
import org.aselstudios.taleshop.model.ItemCost;

import javax.annotation.Nullable;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

/** Auction house operations. All methods return {@link CompletableFuture}. */
public interface AuctionHouse {

    CompletableFuture<List<AuctionListing>> getActiveListings();

    CompletableFuture<List<AuctionMailboxItem>> getMailbox(UUID playerUuid, int page, int pageSize);

    CompletableFuture<Boolean> createListing(Player player, int inventorySlot,
                                              BigDecimal price,
                                              @Nullable List<ItemCost> requestedItems);

    CompletableFuture<Boolean> buyListing(Player buyer, UUID listingId);

    CompletableFuture<Boolean> cancelListing(Player player, UUID listingId);

    CompletableFuture<Void> claimMoney(Player player);

    CompletableFuture<Void> claimAllItems(Player player);

    CompletableFuture<Void> claimItem(Player player, long mailboxItemId);

    boolean isEnabled();

    int getPlayerListingLimit(Player player);
}
