package com.evrim.broker.wallet;

import com.evrim.broker.api.RestApiResponse;
import com.evrim.broker.data.InMemoryAccountStore;
import com.evrim.broker.wallet.error.CustomError;
import com.evrim.broker.wallet.error.FiatCurrencyNotSupportedException;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.List;

import static com.evrim.broker.data.InMemoryAccountStore.ACCOUNT_ID;

@Controller("/account/wallet")
public record WalletController(InMemoryAccountStore store) {
    private static final Logger LOG = LoggerFactory.getLogger(WalletController.class);
    public static final List<String> SUPPORTED_FIAT_CURRENCIES = List.of("EUR","USD","CHF","GBP");

    @Get(produces = MediaType.APPLICATION_JSON)
    public Collection<Wallet>get(){

        return store.getWallets(ACCOUNT_ID);
    }

    @Post(value = "/deposit",
          consumes = MediaType.APPLICATION_JSON,
          produces =  MediaType.APPLICATION_JSON
    )
    public HttpResponse<RestApiResponse> depositFiatMoney(@Body DepositFiatMoney deposit)
    {
        //option 1 : custom HttpResponse
        if (!SUPPORTED_FIAT_CURRENCIES.contains(deposit.symbol().value()))
        {
            return HttpResponse.badRequest()
                    .body(new CustomError(
                            HttpStatus.BAD_REQUEST.getCode(),
                            "UNSUPPORTED CURRENCY",
                            String.format("Only %s are supported", SUPPORTED_FIAT_CURRENCIES)
                    ));

        }
        //return HttpResponse.ok();
        var wallet = store.depositToWallet(deposit) ;
        LOG.debug("Deposit to wallet: {}", wallet);
        //return HttpResponse.ok().body(wallet);
        return HttpResponse.ok();
    }

    @Post(value = "/withdraw",
            consumes = MediaType.APPLICATION_JSON,
            produces =  MediaType.APPLICATION_JSON
    )
    public Wallet  withdrawFiatMoney(@Body WithdrawFiatMoney withdraw) throws FiatCurrencyNotSupportedException {
        //option 2 : Custom Error response
        if (!SUPPORTED_FIAT_CURRENCIES.contains(withdraw.symbol().value()))
        {
            //logic passed to exception handler
            throw new FiatCurrencyNotSupportedException(String.format("Only %s are supported", SUPPORTED_FIAT_CURRENCIES));
        }
        var wallet = store.withdrawFromWallet(withdraw);
        LOG.debug("Wallet after withdrawal: {}", wallet);
        return wallet;

        //return HttpResponse.ok();
    }



}
