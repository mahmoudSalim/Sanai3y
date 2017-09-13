package com.example.mahmo.sanai3y.Enum;

/**
 * Created by Ahmed on 12-Sep-17.
 */

public enum ReviewRate {
    One(1),
    Two(2),
    Three(3),
    Four(4),
    Five(5);

    private int numVal;

    ReviewRate(int numVal) {
        this.numVal = numVal;
    }

    public int getNumVal() {
        return numVal;
    }
}
