package com.converter.mockito;


import com.google.common.base.Optional;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.SelectionModel;
import com.intellij.psi.JavaPsiFacade;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiJavaFile;

import java.util.ArrayList;
import java.util.List;

import static java.util.Arrays.asList;

/**
 * Created by muhammadraza on 26/01/2016
 */
public class SelectionBasedMockConverter {

    private final ExpectationConverter expectationConverter;
    private final DeclationConverter declationConverter;

    public SelectionBasedMockConverter() {
        declationConverter = new DeclationConverter();
        expectationConverter = new ExpectationConverter();
    }

    public void doConvert(Editor editor, DataContext dataContext) {

        SelectionModel selectionModel = editor.getSelectionModel();
        String[] jmockCodeLines = selectionModel.getSelectedText().split("\n");

        List<String> mocikitoLines = new ArrayList<String>();
        for (String jmockLine : jmockCodeLines) {

            if(jmockLine.contains(".method")){
                Optional<ConversionResult> resultOptional = expectationConverter.convert(jmockLine);
                mocikitoLines.addAll(extractResultLines(resultOptional));
            }else {
                Optional<ConversionResult> resultOptional = declationConverter.convert(jmockLine);
                mocikitoLines.addAll(extractResultLines(resultOptional));
            }
        }

        int start = selectionModel.getSelectionStart();
        int end = selectionModel.getSelectionEnd();
        Runnable runnable = createRunnable(start, end, transformLines(mocikitoLines), editor.getDocument());

        //Making the replacement
        WriteCommandAction.runWriteCommandAction(editor.getProject(), runnable);
        selectionModel.removeSelection();

    }

    private String transformLines(List<String> mockitoLines){

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

    private List<String> extractResultLines(Optional<ConversionResult> resultOptional){

        List<String> mocikitoLines = new ArrayList<String>();
        if(resultOptional.isPresent()){
            String expectation = resultOptional.get().getExpectation();
            String verification = resultOptional.get().getVerification();

            if(expectation != null && !expectation.trim().equals("")){
                mocikitoLines.add(expectation);
            }

            if(verification != null && !verification.trim().equals("")){
                mocikitoLines.add(verification);
            }
        }
        return mocikitoLines;
    }
}
