package com.converter.mockito;

import com.intellij.psi.*;
import com.intellij.psi.impl.source.PsiFieldImpl;
import com.intellij.psi.util.PsiUtil;

import java.util.List;

import static java.util.Arrays.asList;

/**
 * Created by muhammadraza on 01/02/2016.
 */
public class FieldsConverter {

    public void convert(PsiClass psiClass, JavaPsiFacade javaPsiFacade){

        for (PsiField field : psiClass.getFields()) {
            if(field.getType().getCanonicalText().contains("Mock")) {
                PsiExpression psiExpression = field.getInitializer();
                PsiClass classType = extractClassName(psiExpression.getText(), javaPsiFacade);
                field.getParent().add(PsiUtils.createPsiField(field.getName(), "Mock", classType).get());
                field.delete();
            }
        }
    }

    private PsiClass extractClassName(String mockLine, JavaPsiFacade javaPsiFacade){
        String strContainingClassName = mockLine.substring(mockLine.indexOf("mock(")+5);
        String className = strContainingClassName.substring(0, strContainingClassName.indexOf(".class"));

        return PsiUtils.createPsiClass(className, javaPsiFacade);
    }
}
