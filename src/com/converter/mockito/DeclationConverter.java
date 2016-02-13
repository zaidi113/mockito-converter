package com.converter.mockito;

import com.google.common.base.Optional;

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
//            res.add(annotate(mockLine));
//        }
//        return res;
//
//    }

    public Optional<ConversionResult> convert(String jmockLine){

        throw new IllegalStateException("Not Implemented Yet");

    }

    private String extractClassName(String mockLine){
        String strContainingClassName = mockLine.substring(mockLine.indexOf("mock(")+5);
        return strContainingClassName.substring(0, strContainingClassName.indexOf(".class"));
    }

    private String extractInstanceVariableName(String mockLine){
        String strBeginingWithInstanceVarName = mockLine.substring(mockLine.indexOf("Mock")+4);
        return strBeginingWithInstanceVarName.substring(0, strBeginingWithInstanceVarName.indexOf("=")).trim();
    }

}
