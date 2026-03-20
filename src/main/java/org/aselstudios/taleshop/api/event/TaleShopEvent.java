package org.aselstudios.taleshop.api.event;

import com.hypixel.hytale.event.IEvent;

/** Base class for cancellable TaleShop events. */
public abstract class TaleShopEvent implements IEvent<Void> {

    private boolean cancelled = false;

    public boolean isCancelled() {
        return cancelled;
    }

    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }
}
