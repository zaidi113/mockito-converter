package com.home.converter;

/**
 * Created by muhammadraza on 30/01/2016.
 */
public class Test {


    public void test(){

        private Mock something = mock(Something.class, "Blahhh");
        mockReferenceDataRepository.stubs().method("findStringTranslationForBbgDayCountAndDesc").with(eq(ANYTHING), ANYTHING, isA("TEST")).will(returnValue(new TranslationPair<String, Boolean>("AA", true)));;
        mockReferenceDataRepository.method("foo").with(eq(ANYTHING), ANYTHING, isA("TEST")).will(returnValue(new Pair<String, Boolean>("AA", true)));;
    }

    mockReferenceDataRepository.stubs().method("findStringTranslationForBbgDayCountAndDesc").with(eq(ANYTHING), ANYTHING, isA("TEST")).will(returnValue(new TranslationPair<String, Boolean>("AA", true)));;

}
