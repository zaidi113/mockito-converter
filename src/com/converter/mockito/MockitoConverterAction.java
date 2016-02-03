package com.converter.mockito;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.SelectionModel;
import com.intellij.openapi.project.Project;

import java.util.List;

import static java.util.Arrays.asList;

/**
 * Created by muhammadraza on 30/01/2016
 */
public class MockitoConverterAction extends AnAction {

    private final MockConverter converter;
    public MockitoConverterAction() {
        converter = new MockConverter();
    }

    public void actionPerformed(AnActionEvent anActionEvent) {

        //Get all the required data from data keys
        final Editor editor = anActionEvent.getRequiredData(CommonDataKeys.EDITOR);
        final Project project = anActionEvent.getRequiredData(CommonDataKeys.PROJECT);
        //Access document, caret, and selection
        final Document document = editor.getDocument();

        final SelectionModel selectionModel = editor.getSelectionModel();



        //Select line if no selection is detected
        if(!selectionModel.hasSelection()){
            selectionModel.selectLineAtCaret();
        }

        int start = selectionModel.getSelectionStart();
        int end = selectionModel.getSelectionEnd();
        Runnable runnable = createRunnable(start, end, convertSelection(selectionModel), document);

        //Making the replacement
        WriteCommandAction.runWriteCommandAction(project, runnable);
        selectionModel.removeSelection();
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
