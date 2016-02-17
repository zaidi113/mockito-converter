package com.converter.mockito;

import com.intellij.psi.*;

/**
 * Created by muhammadraza on 01/02/2016.
 */
public class FieldsConverter {

    public void convert(PsiClass psiClass){

        for (PsiField field : psiClass.getFields()) {
            if(field.getType().getPresentableText().equals("Mock")) {
                String className = findClassName(field.getText());
                field.getParent().add(PsiUtils.createPsiFieldFromText(field.getName(), "Mock", className, psiClass.getProject()).get());
                field.delete();
            }
        }
    }

    private String findClassName(String mockLine){
        String strContainingClassName = mockLine.substring(mockLine.indexOf("mock(")+5);
        return strContainingClassName.substring(0, strContainingClassName.indexOf(".class"));
    }
}
