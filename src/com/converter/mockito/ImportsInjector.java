package com.converter.mockito;

import com.intellij.psi.PsiImportStatement;
import com.intellij.psi.PsiImportStatementBase;
import com.intellij.psi.PsiImportStaticStatement;
import com.intellij.psi.PsiJavaFile;

import java.util.List;

import static java.util.Arrays.asList;

/**
 * Inserts code for static imports for Mockito.
 * <p/>
 * Created by przemek on 8/10/15.
 */
public class ImportsInjector {

    public static final List<String> imports = asList("org.mockito.Mock", "org.junit.Test", "org.junit.Before", "org.junit.After");
    public static final String MOCKITO_FULLY_QUALIFIED_CLASS_NAME = "org.mockito.Mockito";
    public static final String GROUPED_MOCKITO_STATIC_IMPORT = MOCKITO_FULLY_QUALIFIED_CLASS_NAME + ".*";
    public static final List<String> importsToBeDeleted = asList("org.jmock.Mock");

    public static void inject(PsiJavaFile psiJavaFile, ImportOrganizer importOrganizer) {
        deleteImports(psiJavaFile);
        injectImports(psiJavaFile, importOrganizer);
        injectStaticImports(psiJavaFile, importOrganizer);
    }

    private static void injectImports(PsiJavaFile psiJavaFile, ImportOrganizer importOrganizer) {

        PsiImportStatement[] existingImportStatements = psiJavaFile.getImportList().getImportStatements();
        for (String newImportStatement : imports) {
            for (PsiImportStatement importStatement : existingImportStatements) {

                String importStatementText = importStatement.getQualifiedName();
                if (newImportStatement.equals(importStatementText)) {
                    break;
                }
            }
            importOrganizer.addClassImport(psiJavaFile, newImportStatement);
        }

    }

    private static void deleteImports(PsiJavaFile psiJavaFile){

        PsiImportStatement[] existingImportStatements = psiJavaFile.getImportList().getImportStatements();
        for (PsiImportStatement importStatement : existingImportStatements) {

            String importStatementText = importStatement.getQualifiedName();

            if(importsToBeDeleted.contains(importStatementText)){
                importStatement.delete();
                break;
            }
        }
    }

    private static void injectStaticImports(PsiJavaFile psiJavaFile, ImportOrganizer importOrganizer) {

        for (PsiImportStaticStatement staticImport : psiJavaFile.getImportList().getImportStaticStatements()) {
            if (staticImport.getText().contains(GROUPED_MOCKITO_STATIC_IMPORT)) {
                return;
            }
        }
        importOrganizer.addStaticImportForAllMethods(psiJavaFile, MOCKITO_FULLY_QUALIFIED_CLASS_NAME);
    }
}
