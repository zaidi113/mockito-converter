package com.converter.mockito;


import com.intellij.openapi.util.Pair;

import java.util.ArrayList;
import java.util.List;

import static java.util.Arrays.asList;

/**
 * Created by muhammadraza on 26/01/2016
 */
public class MockConverter {

    private final ExpectationConverter expectationConverter;
    private final DeclationConverter declationConverter;

    public MockConverter() {
        declationConverter = new DeclationConverter();
        expectationConverter = new ExpectationConverter();
    }

    public List<String> convert(List<String> jmockCodeLines){


        List<String> mocikitoLines = new ArrayList<String>();
        for (String jmockLine : jmockCodeLines) {

            if(jmockLine.contains(".method")){
                mocikitoLines.addAll(expectationConverter.convert(jmockLine));
            }else {
                mocikitoLines.addAll(declationConverter.convert(jmockLine));
            }
        }
        return mocikitoLines;

    }

    public static void main(String[] args) {

        String line1 = "mockReference.stubs().method(\"findDesc\").with(eq(data.test(x)), ANYTHING, isA(\"TEST\")).will(returnValue(new TranslationPair<String, Boolean>(\"AA\", true)));";
        String line2 = "mockReferenceDataRepository.stubs().method(\"findStringTranslationForBbgDayCountAndDesc\").will(returnValue(new TranslationPair<String, Boolean>(\"AA\", true)));";
        String line3 = "private Mock someThing = mock(MyClass.class, \"xxxx\");";

        MockConverter mockConverter = new MockConverter();
        List<String> mockitoLines = mockConverter.convert(asList(line1));

        for (String mockitoLine : mockitoLines) {
            System.out.println(mockitoLine);
        }
    }
}
