package com.evrim.broker.watchlist;

import com.evrim.broker.data.InMemoryAccountStore;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.*;

import java.util.UUID;

@Controller("/account/watchlist")
public record WatchListController(InMemoryAccountStore store) {
    static final UUID ACCOUNT_ID = UUID.randomUUID();
    @Get(produces = MediaType.APPLICATION_JSON)
    public WatchList get() {
        return store.getWatchList(ACCOUNT_ID);
    }

    @Put(consumes = MediaType.APPLICATION_JSON,
         produces= MediaType.APPLICATION_JSON)
    public WatchList update(@Body WatchList watchlist)
    {
        return store.updateWatchList(ACCOUNT_ID, watchlist);
    }

    //DELETE alternate 1
    /*    @Status(HttpStatus.NO_CONTENT)
    @Delete(
            produces = MediaType.APPLICATION_JSON
    )
    public void delete(){
        store.deleteWatchList(ACCOUNT_ID);

    }*/

    //alternate delete
    @Delete(
            produces = MediaType.APPLICATION_JSON
    )
    public HttpResponse<Void> delete()
    {
        store.deleteWatchList(ACCOUNT_ID);
        return HttpResponse.noContent();
    }


}
