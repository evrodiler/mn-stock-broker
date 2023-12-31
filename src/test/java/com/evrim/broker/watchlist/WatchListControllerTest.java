package com.evrim.broker.watchlist;

import com.evrim.broker.Symbol;
import com.evrim.broker.data.InMemoryAccountStore;
import com.evrim.broker.data.InMemoryStore;
import com.fasterxml.jackson.databind.JsonNode;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.MediaType;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;
import java.util.stream.Stream;

import static io.micronaut.http.HttpRequest.GET;
import static org.junit.jupiter.api.Assertions.*;

@MicronautTest
class WatchListControllerTest {
    private static final Logger LOG = LoggerFactory.getLogger(WatchListControllerTest.class);
    private static final UUID TEST_ACCOUNT_ID = WatchListController.ACCOUNT_ID;

    @Inject
    @Client("/account/watchlist")
    HttpClient client;

    @Inject
    InMemoryAccountStore inMemoryAccountStore;

    @BeforeEach
    void setup()
    {
        inMemoryAccountStore.deleteWatchList(TEST_ACCOUNT_ID);
    }

    @Test
    void symbolsEndpointReturnsListOfSymbols() {
        final WatchList result = client.toBlocking().retrieve(GET("/"), WatchList.class);
        assertNull(result.symbols());
        assertTrue(inMemoryAccountStore.getWatchList(TEST_ACCOUNT_ID).symbols().isEmpty());
    }

    @Test
    void returnsWatchListForTestAccount()
    {
        givenWatchListForAccountExists();
        var response = client.toBlocking().exchange("/", JsonNode.class);
        assertEquals(HttpStatus.OK, response.getStatus());
        assertEquals("""
           {
             "symbols" : [ {
               "value" : "APPL"
             }, {
               "value" : "GOOGL"
             }, {
               "value" : "MSFT"
             } ]
           }""", response.getBody().get().toPrettyString());
    }

    private void givenWatchListForAccountExists() {
        inMemoryAccountStore.updateWatchList(TEST_ACCOUNT_ID, new WatchList(
                Stream.of("APPL", "GOOGL", "MSFT")
                        .map(Symbol::new)
                        .toList()
        ));
    }

    @Test
    void canUpdateWatchListForTestAccount()
    {
        var symbols = Stream.of("AAPL","GOOGL").map(Symbol::new).toList();
        final var request =  HttpRequest.PUT("/", new WatchList(symbols))
                .accept(MediaType.APPLICATION_JSON);
        final HttpResponse<Object> added = client.toBlocking().exchange(request);
        assertEquals(HttpStatus.OK, added.getStatus());
        assertEquals(symbols, inMemoryAccountStore.getWatchList(TEST_ACCOUNT_ID).symbols());

    }

    @Test
    void canDeleteWatchListForTestAccount()
    {
        givenWatchListForAccountExists(); //extracted
        assertFalse(inMemoryAccountStore.getWatchList(TEST_ACCOUNT_ID).symbols().isEmpty());
        var deleted = client.toBlocking().exchange(HttpRequest.DELETE("/"));
        assertEquals(HttpStatus.NO_CONTENT, deleted.getStatus());
        assertTrue(inMemoryAccountStore.getWatchList(TEST_ACCOUNT_ID).symbols().isEmpty());

    }

}
