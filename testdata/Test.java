package com.home.converter;

/**
 * Created by muhammadraza on 30/01/2016.
 */
public class Test {

    private Mock a = mock(Something.class, "Blahhh");
    private Mock b = mock(Something.class, "Blahhh");
    private Mock c = mock(Something.class, "Blahhh");



    public void testBlahh(){

        mockReferenceDataRepository.stubs().method("findStringTranslationForBbgDayCountAndDesc").with(eq(ANYTHING), ANYTHING, isA("TEST")).will(returnValue(new TranslationPair<String, Boolean>("AA", true)));;
        mockRepository.method("foo").with(eq(my.proxy()), ANYTHING, isA("TEST")).will(returnValue(new Pair<String, Boolean>("AA", true)));;
    }

    public void testFoo(){

        mockReferenceDataRepository.stubs().method("findStringTranslationForBbgDayCountAndDesc").with(eq(ANYTHING), ANYTHING, isA("TEST")).will(returnValue(new TranslationPair<String, Boolean>("AA", true)));;
        mockRepository.method("foo").with(eq(my.proxy()), ANYTHING, isA("TEST")).will(returnValue(new Pair<String, Boolean>("AA", true)));;
    }
}
