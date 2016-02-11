package com.converter.mockito;

import com.intellij.psi.PsiImportStatement;
import com.intellij.psi.PsiImportStaticStatement;
import com.intellij.psi.PsiJavaFile;

/**
 * Inserts code for static imports for Mockito.
 * <p/>
 * Created by przemek on 8/10/15.
 */
public class ImportsInjector {

    public static final String MOCKITO_MOCK_CLASS_NAME = "org.mockito.Mock";
    public static final String MOCKITO_FULLY_QUALIFIED_CLASS_NAME = "org.mockito.Mockito";
    public static final String GROUPED_MOCKITO_STATIC_IMPORT = MOCKITO_FULLY_QUALIFIED_CLASS_NAME + ".*";

    private final PsiJavaFile psiJavaFile;
    private final ImportOrganizer importOrganizer;

    public ImportsInjector(PsiJavaFile psiJavaFile, ImportOrganizer importOrganizer) {
        this.psiJavaFile = psiJavaFile;
        this.importOrganizer = importOrganizer;
    }

    public void inject() {
        injectImports();
        injectStaticImports();
    }

    private void injectImports() {

        for (PsiImportStatement staticImport : psiJavaFile.getImportList().getImportStatements()) {
            if (staticImport.getText().contains(MOCKITO_MOCK_CLASS_NAME)) {
                return;
            }
        }
        importOrganizer.addClassImport(psiJavaFile, MOCKITO_MOCK_CLASS_NAME);
    }

    private void injectStaticImports() {

        for (PsiImportStaticStatement staticImport : psiJavaFile.getImportList().getImportStaticStatements()) {
            if (staticImport.getText().contains(GROUPED_MOCKITO_STATIC_IMPORT)) {
                return;
            }
        }
        importOrganizer.addStaticImportForAllMethods(psiJavaFile, MOCKITO_FULLY_QUALIFIED_CLASS_NAME);
    }
}
