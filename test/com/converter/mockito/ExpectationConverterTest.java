package com.converter.mockito;

import com.google.common.base.Optional;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by muhammadraza on 26/02/2016.
 */
public class ExpectationConverterTest {

    private ExpectationConverter expectationConverter = new ExpectationConverter();

    @Test
    public void canConvertCodeWitThrowClause(){

        String jmock = "mockData.stubs().method(\"findDesc\").will(throwException(new RunTimeException(\"test\"));";
        Optional<ConversionResult> mockitoResult = expectationConverter.convert(jmock);

        assertTrue(mockitoResult.isPresent());

        ConversionResult result = mockitoResult.get();
        String expectation = result.getExpectation();
        String verification = result.getVerification();

        assertNotNull(expectation);
        assertNotNull(verification);

        assertEquals("when(mockData.findDesc()).thenThrow(new RunTimeException(\"test\"));", expectation);
        assertEquals("verify(mockData).findDesc();", verification);
    }

    @Test
    public void canConvertCodeWitReturnClause(){

        String jmock = "mockData.stubs().method(\"findDesc\").will(treturnValue(new TranslationPair<String, Boolean>(\"AA\", true)));";
        Optional<ConversionResult> mockitoResult = expectationConverter.convert(jmock);

        assertTrue(mockitoResult.isPresent());

        ConversionResult result = mockitoResult.get();
        String expectation = result.getExpectation();
        String verification = result.getVerification();

        assertNotNull(expectation);
        assertNotNull(verification);

        assertEquals("when(mockData.findDesc()).thenReturn(new TranslationPair<String, Boolean>(\"AA\", true));", expectation);
        assertEquals("verify(mockData).findDesc();", verification);
    }

    @Test
    public void canConvertCodeWitMethodsParams(){

        String jmock = "mockData.stubs().method(\"findDesc\").with(eq(\"dd\")).will(treturnValue(new TranslationPair<String, Boolean>(\"AA\", true)));";
        Optional<ConversionResult> mockitoResult = expectationConverter.convert(jmock);

        assertTrue(mockitoResult.isPresent());

        ConversionResult result = mockitoResult.get();
        String expectation = result.getExpectation();
        String verification = result.getVerification();

        assertNotNull(expectation);
        assertNotNull(verification);

        assertEquals("when(mockData.findDesc(eq(\"dd\"))).thenReturn(new TranslationPair<String, Boolean>(\"AA\", true));", expectation);
        assertEquals("verify(mockData).findDesc(eq(\"dd\"));", verification);
    }


}