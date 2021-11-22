package com.example.practicalparentapp.Model;

/**
 * Keeps track of the Coin Flip History.
 * Data includes coin flip information
 * and the child who flipped the coin.
 */
public class History {
    private final String histInfo;
    private final Child childWhoFlipped;

    public History(String info, Child child) {
        histInfo = info;
        childWhoFlipped = child;
    }

    public Child getChildWhoFlipped() {
        return childWhoFlipped;
    }

    public String getHistInfo() {
        return histInfo;
    }
}
