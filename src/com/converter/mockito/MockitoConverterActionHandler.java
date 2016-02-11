package com.converter.mockito;

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

    private final MockConverter converter = new MockConverter();
    private FieldsConverter fieldsConverter = new FieldsConverter();
    private MethodTestAnnotation methodTestAnnotation = new MethodTestAnnotation();
    private ClassLevelAnnotation classAnnotation = new ClassLevelAnnotation();


    @Override
    public void executeWriteAction(Editor editor, @Nullable Caret caret, DataContext dataContext) {
        doAction(dataContext);
    }

    public void doAction(DataContext dataContext) {

        PsiJavaFile psiJavaFile = (PsiJavaFile) dataContext.getData(CommonDataKeys.PSI_FILE.getName());
        ImportOrganizer importOrganizer = new ImportOrganizer(JavaPsiFacade.getInstance(psiJavaFile.getProject()));
        ImportsInjector importsInjector = new ImportsInjector(psiJavaFile, importOrganizer);
        importsInjector.inject();

        PsiClass psiClass = getUnitTestClass(psiJavaFile);

        classAnnotation.annotate(importOrganizer, psiJavaFile);
        methodTestAnnotation.annotate(importOrganizer, psiJavaFile);

        PsiField[] allFields = psiClass.getAllFields();
        fieldsConverter.convert(allFields);

        //Get all the required data from data keys
//        final Editor editor = anActionEvent.getRequiredData(CommonDataKeys.EDITOR);
//        final Project project = dataContext.getData(CommonDataKeys.PROJECT.);
        //Access document, caret, and selection
//        final Document document = editor.getDocument();
//        final SelectionModel selectionModel = editor.getSelectionModel();
//
//
//
//        //Select line if no selection is detected
//        if(!selectionModel.hasSelection()){
//            selectionModel.selectLineAtCaret();
//        }
//
//        int start = selectionModel.getSelectionStart();
//        int end = selectionModel.getSelectionEnd();
//        document.replaceString(start, end, convertSelection(selectionModel));

//        Runnable runnable = createRunnable(start, end, convertSelection(selectionModel), document);
//
//        //Making the replacement
//        WriteCommandAction.runWriteCommandAction(project, runnable);
//        selectionModel.removeSelection();
    }

    public static PsiClass getUnitTestClass(PsiJavaFile psiJavaFile) {
        for (PsiClass psiClass : psiJavaFile.getClasses()) {
            if (!PsiUtil.isInnerClass(psiClass) && !PsiUtil.isAbstractClass(psiClass)) {
                return psiClass;
            }
        }
        throw new IllegalStateException("Could not find a unit test class in file: " + psiJavaFile.getName());
    }

    private String convertSelection(SelectionModel selectionModel){


        String[] selectedText = selectionModel.getSelectedText().split("\n");


        final List<String> mockitoLines = converter.convert(asList(selectedText));
        final StringBuilder builder = new StringBuilder();

        for (String mockitoLine : mockitoLines) {
            builder.append(mockitoLine);
            builder.append("\n");
        }

        return builder.toString();
    }

    private Runnable createRunnable(final int start, final int end, final String text, final Document document){

        //New instance of Runnable to make a replacement
        return new Runnable() {
            @Override
            public void run() {
                document.replaceString(start, end, text);
            }
        };

    }
}
