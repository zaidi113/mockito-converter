package com.converter.mockito;

import com.google.common.base.Optional;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.editor.Caret;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.SelectionModel;
import com.intellij.openapi.editor.actionSystem.EditorWriteActionHandler;
import com.intellij.psi.*;
import com.intellij.psi.util.PsiUtil;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import static java.util.Arrays.asList;

/**
 * Created by muhammadraza on 10/02/2016.
 */
public class MockitoConverterActionHandler extends EditorWriteActionHandler {

    private FullClassMockConverter fullClassMockConverter = new FullClassMockConverter();
    private SelectionBasedMockConverter selectionBasedMockConverter= new SelectionBasedMockConverter();

    @Override
    public void executeWriteAction(Editor editor, @Nullable Caret caret, DataContext dataContext) {
        if(!editor.getSelectionModel().hasSelection()){
            fullClassMockConverter.doConvert(editor, dataContext);
            return;
        }
        selectionBasedMockConverter.doConvert(editor, dataContext);
    }

    public static PsiClass getUnitTestClass(PsiJavaFile psiJavaFile) {
        for (PsiClass psiClass : psiJavaFile.getClasses()) {
            if (!PsiUtil.isInnerClass(psiClass) && !PsiUtil.isAbstractClass(psiClass)) {
                return psiClass;
            }
        }
        throw new IllegalStateException("Could not find a unit test class in file: " + psiJavaFile.getName());
    }
}
