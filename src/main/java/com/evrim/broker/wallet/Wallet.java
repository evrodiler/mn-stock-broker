package com.evrim.broker.wallet;

import java.math.BigDecimal;
import java.util.UUID;
import com.evrim.broker.Symbol;
import com.evrim.broker.api.RestApiResponse;

public record Wallet(
    UUID accountId,
    UUID walletId,
    Symbol symbol,
    BigDecimal available,
    BigDecimal locked
) implements RestApiResponse
{
    public Wallet addAvailable(BigDecimal amountToAdd)
    {
        return new Wallet(
                this.accountId,
                this.walletId,
                this.symbol,
                this.available.add(amountToAdd),
                this.locked
        );
    }

}
