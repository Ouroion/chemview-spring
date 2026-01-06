package com.chemview.model;

public class Element {
    private final String name;
    private final String symbol;

    public Element(String name, String symbol) {
        this.name = name;
        this.symbol = symbol;
    }

    public String getName() {
        return name;
    }

    public String getSymbol() {
        return symbol;
    }
}