package com.converter.mockito;

import com.google.common.base.Optional;
import com.intellij.openapi.util.Pair;

import java.util.ArrayList;
import java.util.List;

import static java.util.Arrays.asList;

/**
 * Created by muhammadraza on 01/02/2016.
 */
public class ExpectationConverter implements MConverter{

    public Optional<ConversionResult> convert(String code){

        if(!code.contains(".method(")){
            return Optional.absent();
        }


        Pair<String, ConversionResult> objectNameBit = extractObjectName(code);
        Pair<String, ConversionResult> methodNameBit = extractMethodName(objectNameBit.first);
        Pair<String, ConversionResult> parametersBit = extractMethodParams(methodNameBit.first);
        Pair<String, ConversionResult> returnBit = extractReturn(parametersBit.first);

        String exp = (new StringBuilder("when").
                append("(").
                append(objectNameBit.getSecond().getExpectation()).
                append(".").
                append(methodNameBit.getSecond().getExpectation()).
                append("(").
                append(parametersBit.getSecond().getExpectation()).
                append(")").
                append(")").
                append(".").
                append(returnBit.getSecond().getExpectation()).
                append(")").
                append(";").toString());

        String ver = (new StringBuilder("verify").
                append("(").
                append(objectNameBit.getSecond().getExpectation()).
                append(")").
                append(".").
                append(methodNameBit.getSecond().getExpectation()).
                append("(").
                append(parametersBit.getSecond().getExpectation()).
                append(")").
                append(";").toString());

        return Optional.of(new ConversionResult(exp, ver));

    }

    public static void main(String[] args) {

        String line1 = "mockReferenceDataRepository.stubs().method(\"findStringTranslationForBbgDayCountAndDesc\").with(eq(ANYTHING), ANYTHING, isA(\"TEST\")).will(returnValue(new TranslationPair<String, Boolean>(\"AA\", true)));";
        String line2 = "mockReferenceDataRepository.stubs().method(\"findDesc\").will(returnValue(new TranslationPair<String, Boolean>(\"AA\", true)));";
//        String mockLine = "private Mock something = mock(Something.class, \"Blahhh\");";
        ExpectationConverter mockConverter = new ExpectationConverter();
//
        for (String jmock : asList(line1, line2)) {
            Optional<ConversionResult> resultOptional = mockConverter.convert(jmock);
            if(resultOptional.isPresent()){
                System.out.println(resultOptional.get().getExpectation());
                System.out.println(resultOptional.get().getVerification());
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

        if(!line.contains("with(")){
            return emptyResult(line);
        }

//        "with(eq(some.method()), isA(anything))"

        StringBuilder expectation = new StringBuilder();
        StringBuilder verification = new StringBuilder();

        //remove everything from start of method(
        String[] splitStrings = line.split("with\\(");
        if(splitStrings != null && splitStrings.length > 0){

            String stringWithParameters = splitStrings[1];
            if(stringWithParameters.charAt(0) == ')') //no parameters specified
                return emptyResult(line);


            int paramIndex = 0;

                boolean allParametersFound = false;
                int beginParen = 1; //this is one because we have a bracket for with(.
                StringBuilder paramBuilder = new StringBuilder();

                while (beginParen != 0) {

                    char c = stringWithParameters.charAt(paramIndex);

                    if (c == '(') {
                        beginParen++;
                    }

                    if (c == ')') {
                        beginParen--;
                    }

                    if(beginParen != 0){
                        paramBuilder.append(c);
                        paramIndex++;
                    }

                }

                expectation.append(paramBuilder.toString());
                verification.append(paramBuilder.toString());

                return buildResult(stringWithParameters.substring(paramIndex), expectation, verification);
        }
        return emptyResult(line);
    }

    /**
     *
     * @param line . incoming Mock Line
     * @return remaining part of the original line.
     */
    private Pair<String, ConversionResult> extractReturn(String line){

        StringBuilder expectation = new StringBuilder();

        if(line.contains("returnValue")){
            String stringWithReturnValue = line.split("returnValue\\(")[1];

//            line = line.substring(stringWithReturnValue);
//            line = line.substring(line.indexOf("("));

            expectation.append("thenReturn(");
            expectation.append(readBetweenParenthesis(stringWithReturnValue));

        }
        return buildResult(line, expectation, new StringBuilder());
    }

    private String readBetweenParenthesis(String line){

        StringBuilder buffer = new StringBuilder();

        int paranthesis = 1;
        int charCount=0;
        while(paranthesis != 0) {

            char c = line.charAt(charCount);

            if(c == '(') {
                paranthesis++;
            }

            if(c == ')'){
                paranthesis--;
            }

            if(paranthesis != 0){
                buffer.append(c);
            }

            charCount++;
        }

        return buffer.toString();

    }

    private Pair<String, ConversionResult> buildResult(String line, StringBuilder expectation, StringBuilder verification){
        return Pair.create(line, new ConversionResult(expectation.toString(), verification.toString()));
    }

    private Pair<String, ConversionResult> emptyResult(){
        return Pair.create("", new ConversionResult("", ""));
    }

    private Pair<String, ConversionResult> emptyResult(String remainingLine){
        return Pair.create(remainingLine, new ConversionResult("", ""));
    }
}
