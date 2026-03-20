package org.aselstudios.taleshop.api.economy;

import java.math.BigDecimal;
import java.util.UUID;

/** Read-only access to the economy provider used by TaleShop. */
public interface EconomyAccess {

    boolean isAvailable();

    String getProviderName();

    BigDecimal getBalance(UUID playerUuid);

    boolean hasBalance(UUID playerUuid, BigDecimal amount);

    String format(BigDecimal amount);

    String formatPrice(BigDecimal amount);
}
