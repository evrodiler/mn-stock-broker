package com.evrim.broker.wallet.error;

import com.evrim.broker.api.RestApiResponse;

public record CustomError(
        int status,
        String error,
        String message
) implements RestApiResponse
{

}
