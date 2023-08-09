package com.evrim.broker.data;

import com.evrim.broker.Symbol;
//import com.github.javafaker.Faker;
import jakarta.annotation.PostConstruct;
import jakarta.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

import java.util.stream.IntStream;


@Singleton
public class InMemoryStore {

    private static final Logger LOG = LoggerFactory.getLogger(InMemoryStore.class);
    private final Map<String, Symbol> symbols  = new HashMap<>();

    //private final Faker faker = new Faker();

    @PostConstruct
    public void initialize(){

        //IntStream.range(0, 10).forEach(this::addNewSymbol);
        initializeWith(10);
    }
    public void initializeWith(int numberOfEntries)
    {
        symbols.clear();
        IntStream.range(0, numberOfEntries).forEach(this::addNewSymbol);
    }

    private void addNewSymbol(int i) {
        //var symbol = new Symbol(faker.stock().nsdqSymbol());
        var symbol = new Symbol( "SYMBOL_" + Integer.toString(i));
        symbols.put(symbol.value(), symbol);
        LOG.debug("Added symbol{}", symbol);
    }



    public Map<String, Symbol> getSymbols()
    {
        return symbols;
    }
}
