package com.converter.mockito;

import com.intellij.psi.PsiAnnotation;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiJavaFile;
import com.intellij.psi.PsiModifierList;

import static com.converter.mockito.MockitoConverterActionHandler.getUnitTestClass;

/**
 * Created by muhammadraza on 11/02/2016.
 */
public class ClassLevelAnnotation {


    public void annotate(ImportOrganizer importOrganizer, PsiJavaFile psiJavaFile){

        PsiClass psiClass = getUnitTestClass(psiJavaFile);
        PsiModifierList modifierList = psiClass.getModifierList();

        if(!hasRunWithAnnotation(modifierList.getAnnotations())){
            modifierList.addAnnotation("RunWith(MockitoJUnitRunner.class)");
            importOrganizer.addClassImport(psiJavaFile, "org.mockito.runners.MockitoJUnitRunner");
            importOrganizer.addClassImport(psiJavaFile, "org.junit.runner.RunWith");
        }
    }

    private boolean hasRunWithAnnotation(PsiAnnotation[] psiAnnotations){
        for (PsiAnnotation annotation : psiAnnotations) {
            if(annotation.getText().startsWith("RunWith")){
                return true;
            }
        }
        return false;
    }
}
