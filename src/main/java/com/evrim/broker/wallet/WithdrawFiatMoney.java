package com.evrim.broker.wallet;

import com.evrim.broker.Symbol;

import java.math.BigDecimal;
import java.util.UUID;

public record WithdrawFiatMoney(
    UUID accountId,
    UUID walletId,
    Symbol symbol,
    BigDecimal amount
    )
{

}
