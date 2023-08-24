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

}
