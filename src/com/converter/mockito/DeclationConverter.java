package com.converter.mockito;

import java.util.ArrayList;
import java.util.List;

import static java.util.Arrays.asList;

/**
 * Created by muhammadraza on 01/02/2016.
 */
public class DeclationConverter implements MConverter{

    private String SPACE = " ";
//    public List<String> generateMockito(List<String> jMockLines){
//        List<String> res = new ArrayList<String>();
//        for (String mockLine : jMockLines) {
//            res.add(convert(mockLine));
//        }
//        return res;
//
//    }

    public List<String> convert(String jmockLine){

        String className = extractClassName(jmockLine);
        String instanceVariableNameBit = extractInstanceVariableName(jmockLine);

        return asList(new StringBuilder("@Mock").append("\n").
                append("private").append(SPACE).
                append(className).append(SPACE).
                append(instanceVariableNameBit).
                append(";").toString());

    }

    private String extractClassName(String mockLine){
        String strContainingClassName = mockLine.substring(mockLine.indexOf("mock(")+5);
        return strContainingClassName.substring(0, strContainingClassName.indexOf(".class"));
    }

    private String extractInstanceVariableName(String mockLine){
        String strBeginingWithInstanceVarName = mockLine.substring(mockLine.indexOf("Mock")+4);
        return strBeginingWithInstanceVarName.substring(0, strBeginingWithInstanceVarName.indexOf("=")).trim();
    }


    public static void main(String[] args) {
        DeclationConverter declationConverter = new DeclationConverter();
        String jmock = "private Mock someThing = mock(MyClass.class, \"xxxx\");";
        System.out.println("indx of Mock" + jmock.indexOf("Mock"));
        List<String> mockito = declationConverter.convert(jmock);
        for (String m : mockito) {
            System.out.println(m);
        }
    }
}
