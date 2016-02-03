package com.converter.mockito;

/**
 * Created by muhammadraza on 01/02/2016.
 */
public class ConversionResult {

    private StringBuilder expectation;
    private StringBuilder verification;

    public ConversionResult(StringBuilder expectation, StringBuilder verification) {
        this.expectation = expectation;
        this.verification = verification;
    }

    public StringBuilder getExpectation() {
        return expectation;
    }

    public StringBuilder getVerification() {
        return verification;
    }
}
