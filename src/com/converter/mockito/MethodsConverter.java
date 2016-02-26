package com.converter.mockito;

import com.google.common.base.Optional;
import com.intellij.psi.*;

import java.util.ArrayList;
import java.util.List;

import static com.converter.mockito.ExpectationConverter.THEN_RETURN;
import static com.converter.mockito.ExpectationConverter.THEN_THROW;
import static java.util.Arrays.asList;

/**
 * Created by muhammadraza on 01/02/2016 . .
 */
public class MethodsConverter {

    private static String TEST_ANNOTATION = "Test";
    private static String BEFORE_ANNOTATION = "Before";
    private static String OVERRIDE_ANNOTATION = "Override";
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

                String stubbedStatement = conversionOptionalResult.get().getExpectation();
                verifyStatements.add(conversionOptionalResult.get().getVerification());

                if(isNotRedundantStubbing(stubbedStatement)){
                    //Replace the original expectation with Mockito expectation
                    bodyStatement.replace(elementFactory.createStatementFromText(stubbedStatement, method));
                }else {
                    bodyStatement.delete();
                }
            }
        }
        //verification should be added at the end of the method body
        for (String verifyStatement : verifyStatements) {
            body.add(elementFactory.createStatementFromText(verifyStatement, method));
        }
    }


    private boolean isNotRedundantStubbing(String stubbedStatement){
        return stubbedStatement.contains(THEN_RETURN) || stubbedStatement.contains(THEN_THROW);
    }

    private void addAnnotations(PsiMethod method){

        String methodName = method.getNameIdentifier().getText();
        PsiModifierList modifierList = method.getModifierList();
        if(methodName.toLowerCase().startsWith("test")){
            annotate(modifierList, TEST_ANNOTATION);
        }else if(methodName.toLowerCase().startsWith("setup")){
            method.getModifierList().setModifierProperty("public", true);
            removeAnnotation(modifierList, OVERRIDE_ANNOTATION);
            annotate(modifierList, BEFORE_ANNOTATION);
        }else if(methodName.toLowerCase().startsWith("teardown")){
            method.getModifierList().setModifierProperty("public", true);
            annotate(modifierList, AFTER_ANNOTATION);
        }
    }

    private void annotate(PsiModifierList modifierList, String annotation){
        if(!hasAnnotation(modifierList.getAnnotations(), annotation)){
            modifierList.addAnnotation(annotation);
        }

    }

    private void removeAnnotation(PsiModifierList modifierList, String annotation){
        for (PsiAnnotation psiAnnotation : modifierList.getAnnotations()) {
            if(psiAnnotation.getText().equals("@"+annotation)){
                psiAnnotation.delete();
            }
        }
    }

    private boolean hasAnnotation(PsiAnnotation[] psiAnnotations, String annotationText){
        for (PsiAnnotation annotation : psiAnnotations) {
            if(annotation.getText().equals("@"+annotationText)){
                return true;
            }
        }
        return false;
    }
}
