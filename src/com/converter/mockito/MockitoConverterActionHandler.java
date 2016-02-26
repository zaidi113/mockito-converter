package com.converter.mockito;

import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.editor.Caret;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.actionSystem.EditorWriteActionHandler;
import com.intellij.openapi.ui.Messages;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiJavaFile;
import com.intellij.psi.util.PsiUtil;
import org.jetbrains.annotations.Nullable;

/**
 * Created by muhammadraza on 10/02/2016.
 */
public class MockitoConverterActionHandler extends EditorWriteActionHandler {

    private FullClassMockConverter fullClassMockConverter = new FullClassMockConverter();
    private SelectionBasedMockConverter selectionBasedMockConverter= new SelectionBasedMockConverter();

    @Override
    public void executeWriteAction(Editor editor, @Nullable Caret caret, DataContext dataContext) {
        try{
            if(!editor.getSelectionModel().hasSelection()){
                fullClassMockConverter.doConvert(editor, dataContext);
                return;
            }
            selectionBasedMockConverter.doConvert(editor, dataContext);
        }catch(Exception e){
            //so tht we don't crash
            Messages.showErrorDialog(editor.getProject(), e.getMessage(), "Error");
        }
    }

    public static PsiClass getUnitTestClass(PsiJavaFile psiJavaFile) {
        for (PsiClass psiClass : psiJavaFile.getClasses()) {
            if (!PsiUtil.isInnerClass(psiClass) ) {
                return psiClass;
            }
        }
        throw new IllegalStateException("Could not find a unit test class in file: " + psiJavaFile.getName() );
    }
}
