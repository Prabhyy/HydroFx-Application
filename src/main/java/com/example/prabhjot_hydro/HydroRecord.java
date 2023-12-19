package com.example.prabhjot_hydro;

/**
 * Student Number : 301310019
 * Name: Prabhjot Malhi Singh
 * Course: COMP 228
 * Section: 015
 * Username: prabhjotmalhisingh
 * Date: 12/11/23
 */


public class HydroRecord {
    private String accountNumber;
    private int hydroUnits;
    private String season;
    private double estimate;

    public HydroRecord(String accountNumber, int hydroUnits, String season, double estimate) {
        this.accountNumber = accountNumber;
        this.hydroUnits = hydroUnits;
        this.season = season;
        this.estimate = estimate;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public int getHydroUnits() {
        return hydroUnits;
    }

    public String getSeason() {
        return season;
    }

    public double getEstimate() {
        return estimate;
    }
}
