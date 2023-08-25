package com.evrim.broker.wallet.error;

public class FiatCurrencyNotSupportedException extends Throwable {
    public FiatCurrencyNotSupportedException(String message) {
        super(message);
    }
}
