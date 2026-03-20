package org.aselstudios.taleshop.api.event;

import com.hypixel.hytale.event.IEvent;
import com.hypixel.hytale.logger.HytaleLogger;
import com.hypixel.hytale.server.core.HytaleServer;

import java.util.logging.Level;

/** Dispatches TaleShop events through the Hytale event bus. */
public final class TaleShopEvents {

    private TaleShopEvents() {}

    /** Fires a cancellable event and returns it for checking cancelled state. */
    @SuppressWarnings({"unchecked", "rawtypes"})
    public static <T extends TaleShopEvent> T fire(T event) {
        try {
            HytaleServer.get().getEventBus()
                    .dispatchFor((Class) event.getClass())
                    .dispatch(event);
        } catch (Exception e) {
            HytaleLogger.getLogger().at(Level.WARNING).withCause(e).log("TaleShop event dispatch failed");
        }
        return event;
    }

    /** Dispatches a non-cancellable event. */
    @SuppressWarnings({"unchecked", "rawtypes"})
    public static <T extends IEvent<Void>> void dispatch(T event) {
        try {
            HytaleServer.get().getEventBus()
                    .dispatchFor((Class) event.getClass())
                    .dispatch(event);
        } catch (Exception e) {
            HytaleLogger.getLogger().at(Level.WARNING).withCause(e).log("TaleShop event dispatch failed");
        }
    }
}
