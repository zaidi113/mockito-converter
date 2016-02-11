package com.converter.mockito;

import com.intellij.psi.*;
import com.intellij.psi.impl.PsiImplUtil;

import static com.converter.mockito.MockitoConverterActionHandler.getUnitTestClass;

/**
 * Created by muhammadraza on 11/02/2016.
 */
public class MethodTestAnnotation {


    public void annotate(ImportOrganizer importOrganizer, PsiJavaFile psiJavaFile){

        PsiClass psiClass = getUnitTestClass(psiJavaFile);
        PsiMethod[] methods = psiClass.getMethods();

        for (PsiMethod method : methods) {
            if(method.getNameIdentifier().getText().toLowerCase().startsWith("test")){
                PsiModifierList modifierList = method.getModifierList();
                if(!hasTestAnnotation(modifierList.getAnnotations())){
                    modifierList.addAnnotation("Test");
                    importOrganizer.addClassImport(psiJavaFile, "org.junit.Test");
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
