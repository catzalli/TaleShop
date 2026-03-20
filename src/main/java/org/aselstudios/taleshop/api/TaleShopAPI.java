package org.aselstudios.taleshop.api;

import javax.annotation.Nullable;

/**
 * Static entry point for the TaleShop developer API.
 */
public final class TaleShopAPI {

    private static volatile TaleShopProvider instance;

    private TaleShopAPI() {}

    /** Returns the API provider, or {@code null} if TaleShop is not loaded. */
    @Nullable
    public static TaleShopProvider get() {
        return instance;
    }

    public static void register(TaleShopProvider provider) {
        instance = provider;
    }

    public static void unregister() {
        instance = null;
    }
}
