package org.aselstudios.taleshop.api;

import org.aselstudios.taleshop.api.auction.AuctionHouse;
import org.aselstudios.taleshop.api.economy.EconomyAccess;
import org.aselstudios.taleshop.api.shop.ShopCatalog;
import org.aselstudios.taleshop.api.shop.ShopTransaction;

import javax.annotation.Nullable;

/** Main API interface for TaleShop. Obtain via {@link TaleShopAPI#get()}. */
public interface TaleShopProvider {

    ShopCatalog getShopCatalog();

    ShopTransaction getShopTransaction();

    /** Returns the auction house API, or {@code null} if disabled in config. */
    @Nullable
    AuctionHouse getAuctionHouse();

    EconomyAccess getEconomy();

    boolean isEnabled();

    String getVersion();
}
