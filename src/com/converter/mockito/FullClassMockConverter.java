package com.converter.mockito;


import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.editor.Editor;
import com.intellij.psi.JavaPsiFacade;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiJavaFile;
import com.intellij.psi.util.PsiUtil;

/**
 * Created by muhammadraza on 26/01/2016
 */
public class FullClassMockConverter {

    private final FieldsConverter fieldsConverter;
    private final MethodsConverter methodsConverter;
    private final ClassLevelAnnotation classAnnotation;

    public FullClassMockConverter() {
        fieldsConverter = new FieldsConverter();
        methodsConverter = new MethodsConverter();
        classAnnotation = new ClassLevelAnnotation();
    }

    public void doConvert(Editor editor, DataContext dataContext) {

        PsiJavaFile psiJavaFile = (PsiJavaFile) dataContext.getData(CommonDataKeys.PSI_FILE.getName());
        JavaPsiFacade javaPsiFacade = JavaPsiFacade.getInstance(psiJavaFile.getProject());
        ImportOrganizer importOrganizer = ImportOrganizer.getInstance(javaPsiFacade, editor);

        PsiClass psiClass = MockitoConverterActionHandler.getUnitTestClass(psiJavaFile);

        ImportsInjector.inject(psiJavaFile, importOrganizer);
        classAnnotation.annotate(importOrganizer, psiJavaFile);

        fieldsConverter.convert(psiClass, javaPsiFacade);
        methodsConverter.convert(psiClass, javaPsiFacade);

    }

}
