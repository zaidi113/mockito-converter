package com.converter.mockito;

import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.SelectionModel;
import com.intellij.openapi.editor.actionSystem.EditorAction;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiField;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiJavaFile;
import com.intellij.psi.util.PsiUtil;

import java.util.List;

import static java.util.Arrays.asList;

/**
 * Created by muhammadraza on 30/01/2016
 */
public class MockitoConverterAction extends EditorAction {

    private final MockConverter converter;
    private FieldsConverter fieldsConverter = new FieldsConverter();

    public MockitoConverterAction() {
        super(new MockitoConverterActionHandler());
        converter = new MockConverter();
    }


    /**
     * Enables action for test Java files only.
     */
    @Override
    public void update(Editor editor, Presentation presentation, DataContext dataContext) {
//        PsiFile psiFile = (PsiFile) dataContext.getData(CommonDataKeys.PSI_FILE.getName());
//        boolean enabled = false;
//        if (psiFile != null) {
//            enabled = psiFile.getName().endsWith("Test.java");
//        }
        presentation.setEnabled(true);
    }
}
