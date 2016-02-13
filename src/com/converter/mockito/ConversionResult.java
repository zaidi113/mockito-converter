package com.converter.mockito;

/**
 * Created by muhammadraza on 01/02/2016.
 */
public class ConversionResult {

    private String expectation;
    private String verification;

    public ConversionResult(String expectation, String verification) {
        this.expectation = expectation;
        this.verification = verification;
    }

    public String getExpectation() {
        return expectation;
    }

    public String getVerification() {
        return verification;
    }
}
