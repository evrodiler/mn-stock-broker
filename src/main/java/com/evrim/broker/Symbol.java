package com.evrim.broker;

public record Symbol(String value) {

}
//instead of this, use record
/*
public class Symbol {
    String value;

    public Symbol(String value) {
        this.value = value;
    }
}
*/
