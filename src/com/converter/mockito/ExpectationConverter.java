package com.converter.mockito;

import com.intellij.openapi.util.Pair;

import java.util.ArrayList;
import java.util.List;

import static java.util.Arrays.asList;

/**
 * Created by muhammadraza on 01/02/2016.
 */
public class ExpectationConverter implements MConverter{

    public List<String> convert(String jmockCode){

        List<String> result = new ArrayList<String>();

        Pair<String, ConversionResult> objectNameBit = extractObjectName(jmockCode);
        Pair<String, ConversionResult> methodNameBit = extractMethodName(objectNameBit.first);
        Pair<String, ConversionResult> parametersBit = extractMethodParams(methodNameBit.first);
        Pair<String, ConversionResult> returnBit = extractReturn(parametersBit.first);

        result.add(new StringBuilder("when").
                append("(").
                append(objectNameBit.getSecond().getExpectation().toString()).
                append(".").
                append(methodNameBit.getSecond().getExpectation().toString()).
                append("(").
                append(parametersBit.getSecond().getExpectation().toString()).
                append(")").
                append(")").
                append(returnBit.getSecond().getExpectation().toString())
                .append(";").toString());

        result.add(new StringBuilder("verify").
                append("(").
                append(objectNameBit.getSecond().getExpectation().toString()).
                append(")").
                append(".").
                append(methodNameBit.getSecond().getExpectation().toString()).
                append("(").
                append(parametersBit.getSecond().getExpectation().toString()).
                append(")").
                append(";").toString());

        return result;

    }

    public static void main(String[] args) {

        String line1 = "mockReferenceDataRepository.stubs().method(\"findStringTranslationForBbgDayCountAndDesc\").with(eq(ANYTHING), ANYTHING, isA(\"TEST\")).will(returnValue(new TranslationPair<String, Boolean>(\"AA\", true)));";
        String line2 = "mockReferenceDataRepository.stubs().method(\"findDesc\").will(returnValue(new TranslationPair<String, Boolean>(\"AA\", true)));";
//        String mockLine = "private Mock something = mock(Something.class, \"Blahhh\");";
        ExpectationConverter mockConverter = new ExpectationConverter();
//
        for (String jmock : asList(line1, line2)) {
            for (String mockito : mockConverter.convert(jmock)) {
                System.out.println(mockito);
            }
        }


        System.out.println(mockConverter.extractMethodParams("with(eq(some.method()), isA(anything))").getSecond().getExpectation());
    }

    private Pair<String, ConversionResult> extractObjectName(String line){

        StringBuilder expectation = new StringBuilder();
        StringBuilder verification = new StringBuilder();

        int objectEndIndex = line.indexOf(".");
        String objectName = line.substring(0, objectEndIndex);
        expectation.append(objectName);
        verification.append(objectName);

        return buildResult(line.substring(objectEndIndex), expectation, verification);

    }

    private Pair<String, ConversionResult> extractMethodName(String mockitoLine){

        StringBuilder expectation = new StringBuilder();
        StringBuilder verification = new StringBuilder();

        //remove everything from start of method(
        int methodNameStartIndex = mockitoLine.indexOf("method");
        if(methodNameStartIndex < 0 )
            throw new IllegalArgumentException("Invalid Mock Statement. No JMock Found Found");

        String line1 = mockitoLine;
        line1 = line1.substring(methodNameStartIndex);
        line1 = line1.substring(line1.indexOf("(")+1);

        int methodNameEndIndex = line1.indexOf(")");

        String methodName = line1.substring(0, methodNameEndIndex);
        expectation.append(methodName.replaceAll("\"", ""));
        verification.append(methodName.replaceAll("\"", ""));

        return buildResult(line1.substring(methodNameEndIndex), expectation, verification);
    }

    /**
     *
     * @param line Incoming Mock Line.
     * @return .
     */
    private Pair<String, ConversionResult> extractMethodParams(String line){


//        "with(eq(some.method()), isA(anything))"

        StringBuilder expectation = new StringBuilder();
        StringBuilder verification = new StringBuilder();

        //remove everything from start of method(
        int methodParamStartIndex = line.indexOf("with");

        int paramIndex = 0;
        if(methodParamStartIndex > -1 ) {

            line = line.substring(methodParamStartIndex);
            line = line.substring(line.indexOf("("));

            boolean allParametersFound = false;
            int beginParen = 0;

            while (!allParametersFound) {

                char c = line.charAt(paramIndex);

                if (c == '(') {
                    beginParen++;
                }

                if (c == ')') {
                    beginParen--;
                }
                if(beginParen == 0){
                    allParametersFound = true;
                }
                paramIndex++;
            }

            String params = line.substring(0, paramIndex);
            expectation.append(params);
            verification.append(params);
        }

        return buildResult(line.substring(paramIndex), expectation, verification);
    }

    /**
     *
     * @param line . incoming Mock Line
     * @return remaining part of the original line.
     */
    private Pair<String, ConversionResult> extractReturn(String line){

        StringBuilder expectation = new StringBuilder();

        if(line.contains("returnValue")){
            int returnStartIndex = line.indexOf("returnValue");

            line = line.substring(returnStartIndex);
            line = line.substring(line.indexOf("("));

            expectation.append(".");
            expectation.append("thenReturn(");
            expectation.append(readBetweenParenthesis(line));
            expectation.append(")");
        }
        return buildResult(line, expectation, new StringBuilder());
    }

    private String readBetweenParenthesis(String line){

        boolean canContinue = true;
        boolean anySubParenthesis = false;


        StringBuilder buffer = new StringBuilder();

        int endParenthesis = 0;
        while(canContinue) {

            char c = line.charAt(endParenthesis);
            buffer.append(c);
            if(c == '(') {
                anySubParenthesis = true;
            }

            if(c == ')'){

                if(!anySubParenthesis){
                    canContinue = false;
                }else {
                    anySubParenthesis = false;
                }
            }

            endParenthesis++;
        }

        return buffer.toString();

    }

    private Pair<String, ConversionResult> buildResult(String line, StringBuilder expectation, StringBuilder verification){
        return Pair.create(line, new ConversionResult(expectation, verification));
    }

    private Pair<String, ConversionResult> emptyResult(){
        return Pair.create("", new ConversionResult(new StringBuilder(), new StringBuilder()));
    }
}
