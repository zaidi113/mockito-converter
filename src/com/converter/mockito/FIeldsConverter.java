package com.converter.mockito;

import com.intellij.psi.PsiField;

import java.util.List;

import static java.util.Arrays.asList;

/**
 * Created by muhammadraza on 01/02/2016.
 */
public class FieldsConverter {

    private String SPACE = " ";

    public List<String> convert(PsiField[] fields){


        for (PsiField field : fields) {
            field.getNameIdentifier();
        }
        String className = extractClassName("");
        String instanceVariableNameBit = extractInstanceVariableName("");

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
//
//
//    public static void main(String[] args) {
//        FieldsConverter declationConverter = new FieldsConverter();
//        String jmock = "private Mock someThing = mock(MyClass.class, \"xxxx\");";
//        System.out.println("indx of Mock" + jmock.indexOf("Mock"));
//        List<String> mockito = declationConverter.convert(jmock);
//        for (String m : mockito) {
//            System.out.println(m);
//        }
//    }
}
