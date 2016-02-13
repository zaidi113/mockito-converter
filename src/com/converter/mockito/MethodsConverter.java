package com.converter.mockito;

import com.google.common.base.Optional;
import com.intellij.psi.*;
import com.intellij.psi.impl.source.tree.java.PsiCodeBlockImpl;
import com.intellij.psi.util.PsiMethodUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by muhammadraza on 01/02/2016 . .
 */
public class MethodsConverter {

    private ExpectationConverter expectationConverter = new ExpectationConverter();

    public void convert(PsiClass psiClass, JavaPsiFacade javaPsiFacade){

        PsiElementFactory elementFactory = javaPsiFacade.getElementFactory();

        for (PsiMethod method : psiClass.getMethods()) {
            if(method.getNameIdentifier().getText().toLowerCase().startsWith("test")){
                PsiModifierList modifierList = method.getModifierList();
                if(!hasTestAnnotation(modifierList.getAnnotations())){
                    modifierList.addAnnotation("Test");
                }

                PsiCodeBlock body = method.getBody();
                PsiStatement[] bodyStatements = body.getStatements();

                List<String> verifyStatements = new ArrayList<String>();
                for (PsiStatement bodyStatement : bodyStatements) {

                    Optional<ConversionResult> conversionOptionalResult = expectationConverter.convert(bodyStatement.getText());
                    if(conversionOptionalResult.isPresent()){

                        String expectation = conversionOptionalResult.get().getExpectation();
                        verifyStatements.add(conversionOptionalResult.get().getVerification());
                        //Replace the original expectation with Mockito expectation
                        bodyStatement.replace(elementFactory.createStatementFromText(expectation, method));
                    }
                }
                //verification should be added at the end of the method body
                for (String verifyStatement : verifyStatements) {
                    body.add(elementFactory.createStatementFromText(verifyStatement, method));
                }
            }
        }
    }


    private boolean hasTestAnnotation(PsiAnnotation[] psiAnnotations){
        for (PsiAnnotation annotation : psiAnnotations) {
            if(annotation.getText().equals("Test")){
                return true;
            }
        }
        return false;
    }
}
