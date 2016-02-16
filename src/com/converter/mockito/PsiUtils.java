package com.converter.mockito;

import com.google.common.base.Optional;
import com.intellij.openapi.project.Project;
import com.intellij.psi.*;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.search.ProjectScope;

/**
 * Created by muhammadraza on 12/02/2016.
 */
public class PsiUtils {

    public static PsiClass createPsiClass(String className, JavaPsiFacade javaPsiFacade){
        GlobalSearchScope projectSearchScope = ProjectScope.getAllScope(javaPsiFacade.getProject());

        PsiClass psiClass = javaPsiFacade.findClass(className, projectSearchScope);
        if (psiClass == null) {
            throw new IllegalArgumentException("Unable to find class " + className + " in the classpath");
        }
        return psiClass;
    }

    public static Optional<PsiField> createPsiField(String fieldName, String annotation, PsiClass psiClass){
        PsiElementFactory psiElementFactory = JavaPsiFacade.getInstance(psiClass.getProject()).getElementFactory();
        try {
            PsiClassType myType = psiElementFactory.createType(psiClass);
            PsiField field = psiElementFactory.createField(fieldName, myType);
            field.getModifierList().addAnnotation(annotation);

            return Optional.of(field);
        }catch (Exception e){
            System.out.println("Exception Occured " + e);
            return Optional.absent();
        }

    }

    public static Optional<PsiField> createPsiFieldFromText(String fieldName, String annotation, String type, Project project){
        PsiElementFactory psiElementFactory = JavaPsiFacade.getInstance(project).getElementFactory();

        try {
            PsiType myType = psiElementFactory.createTypeFromText(type, null);
            PsiField field = psiElementFactory.createField(fieldName, myType);
            field.getModifierList().addAnnotation(annotation);

            return Optional.of(field);
        }catch (Exception e){
            System.out.println("Exception Occured " + e);
            return Optional.absent();
        }

    }
}
