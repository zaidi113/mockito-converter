package com.converter.mockito;

import com.intellij.openapi.editor.Editor;
import com.intellij.psi.JavaPsiFacade;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiImportStaticStatement;
import com.intellij.psi.PsiJavaFile;
import com.intellij.psi.impl.java.stubs.JavaClassInitializerElementType;
import com.intellij.psi.impl.java.stubs.impl.PsiClassStubImpl;
import com.intellij.psi.impl.source.PsiClassImpl;
import com.intellij.psi.impl.source.PsiClassReferenceType;
import com.intellij.psi.impl.source.tree.java.ClassElement;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.search.ProjectScope;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.util.PsiClassUtil;
import com.intellij.refactoring.util.CommonRefactoringUtil;

/**
 * Created by przemek on 8/9/15.
 */
public class ImportOrganizer {

    private final JavaPsiFacade javaPsiFacade;
    private final GlobalSearchScope projectSearchScope;
//    private final Editor editor;

    public ImportOrganizer(JavaPsiFacade javaPsiFacade) {
        this.javaPsiFacade = javaPsiFacade;
        this.projectSearchScope = ProjectScope.getAllScope(javaPsiFacade.getProject());
    }

    protected void addClassImport(PsiJavaFile psiJavaFile, String className) {
        PsiClass psiClass = javaPsiFacade.findClass(className, projectSearchScope);
        if (psiClass == null) {
            //showImportError(className);
            return;
        }
        psiJavaFile.importClass(psiClass);
    }

    protected void addStaticImportForAllMethods(PsiJavaFile psiJavaFile, String className) {
        PsiClass psiClass = javaPsiFacade.findClass(className, projectSearchScope);
        if (psiClass == null) {
//            showImportError(className);
            return;
        }

        PsiImportStaticStatement importStaticStatement = javaPsiFacade.getElementFactory().createImportStaticStatement(
                psiClass, "*");
        psiJavaFile.getImportList().add(importStaticStatement);
    }

//    private void showImportError(String importClassName) {
//        String msg = String.format(
//                "Class: %s was not found on the classpath, make sure Mockito and JUnit are added to dependencies",
//                importClassName);
//        CommonRefactoringUtil.showErrorHint(editor.getProject(), editor, msg, null, null);
//    }
}
