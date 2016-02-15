package com.converter.mockito;

import com.google.common.base.Optional;
import com.intellij.psi.*;

import java.util.ArrayList;
import java.util.List;

import static java.util.Arrays.asList;

/**
 * Created by muhammadraza on 01/02/2016 . .
 */
public class MethodsConverter {

    private static String TEST_ANNOTATION = "Test";
    private static String BEFORE_ANNOTATION = "Before";
    private static String AFTER_ANNOTATION = "After";
    private final List<String> supportedMethods = asList("test", "setup", "teardown");
    private final ExpectationConverter expectationConverter = new ExpectationConverter();

    public void convert(PsiClass psiClass, JavaPsiFacade javaPsiFacade){

        PsiElementFactory elementFactory = javaPsiFacade.getElementFactory();

        for (PsiMethod method : psiClass.getMethods()) {

            String methodName = method.getNameIdentifier().getText();
            if(isMethodSupported(methodName)){
                addAnnotations(method);
                convertMethodBody(elementFactory, method);
            }
        }
    }

    private boolean isMethodSupported(String methodName) {
        for (String supportedMethod : supportedMethods) {
            if(methodName.toLowerCase().startsWith(supportedMethod)){
                return true;
            }
        }
        return false;
    }

    private void convertMethodBody(PsiElementFactory elementFactory, PsiMethod method) {
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


    private void addAnnotations(PsiMethod method){

        String methodName = method.getNameIdentifier().getText();
        PsiModifierList modifierList = method.getModifierList();
        if(methodName.toLowerCase().startsWith("test")){
            annotate(modifierList, TEST_ANNOTATION);
        }else if(methodName.toLowerCase().startsWith("setup")){
            annotate(modifierList, BEFORE_ANNOTATION);
        }else if(methodName.toLowerCase().startsWith("teardown")){
            annotate(modifierList, AFTER_ANNOTATION);
        }
    }

    private void annotate(PsiModifierList modifierList, String annotation){
        if(!hasAnnotation(modifierList.getAnnotations(), annotation)){
            modifierList.addAnnotation(annotation);
        }

    }

    private boolean hasAnnotation(PsiAnnotation[] psiAnnotations, String annotationText){
        for (PsiAnnotation annotation : psiAnnotations) {
            if(annotation.getText().equals(annotationText)){
                return true;
            }
        }
        return false;
    }
}
