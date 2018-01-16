package com.silencezwm.openallcompiler;

import com.google.auto.service.AutoService;
import com.silencezwm.openallannotation.OpenActivity;
import com.silencezwm.openallparam.IParamType;
import com.silencezwm.openallparam.OpenAllParams;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.MirroredTypesException;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;


@AutoService(Processor.class)
public class BindViewProcessor extends AbstractProcessor {

    private Elements mElements;
    private Types mTypes;
    private Filer mFiler;

    private ClassName mIntentClass;
    private ClassName mContextClass;

    /**
     * Initialization of related parameters
     * @param processingEnvironment
     */
    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        mElements = processingEnvironment.getElementUtils();
        mTypes = processingEnvironment.getTypeUtils();
        mFiler = processingEnvironment.getFiler();
        mIntentClass = ClassName.get("android.content", "Intent");
        mContextClass = ClassName.get("android.content", "Context");
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        return initSupportedAnnotationTypes();
    }

    private Set<String> initSupportedAnnotationTypes() {
        return addSupportedAnnotationTypes();
    }

    private Set<String> addSupportedAnnotationTypes() {
        Set<String> types = new LinkedHashSet<>();
        types.add(OpenActivity.class.getCanonicalName());
        return types;
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        parseOpenActivity(roundEnvironment);
        return false;
    }

    /**
     * Parse {@link OpenActivity} by RoundEnvironment
     * @param roundEnvironment
     */
    private void parseOpenActivity(RoundEnvironment roundEnvironment) {
        for (Map.Entry<Element, List<FieldBind>> map : getTargetClassMap(roundEnvironment).entrySet()) {
            if (map.getValue() == null || map.getValue().size() == 0) continue;

            TypeSpec.Builder classBuilder = createNewClass(getClassName(map.getKey()));
            MethodSpec.Builder processParamMethod = createProcessParamMethod();
            MethodSpec.Builder openMethodBuilder = createOpenMethod(map.getValue());

            classBuilder.addMethod(openMethodBuilder.build());
            classBuilder.addMethod(processParamMethod.build());

            try {
                JavaFile.builder(getPackageName(map.getKey()), classBuilder.build())
                        .addFileComment("This class is OpenAll auto create, Not modify!")
                        .build().writeTo(mFiler);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    /**
     * Get all the target classes
     * @param roundEnvironment
     * @return
     */
    private Map<Element, List<FieldBind>> getTargetClassMap(RoundEnvironment roundEnvironment) {
        Map<Element, List<FieldBind>> targetClassMap = new HashMap<>();

        for (Element element : roundEnvironment.getElementsAnnotatedWith(OpenActivity.class)) {
            List<FieldBind> fieldBindList = targetClassMap.get(element);
            if (fieldBindList == null) {
                fieldBindList = new ArrayList<>();
                targetClassMap.put(element, fieldBindList);
            }

            List<ClassInfoBind> classInfoBindList = new ArrayList<>();
            try {
                element.getAnnotation(OpenActivity.class).target();
            } catch (MirroredTypesException e) {
                List<DeclaredType> classTypeMirror = (List<DeclaredType>) e.getTypeMirrors();
                if (classTypeMirror != null && classTypeMirror.size() > 0) {
                    for (int i = 0; i < classTypeMirror.size(); i++) {
                        ClassName className = ClassName.bestGuess(classTypeMirror.get(i).toString());
                        ClassInfoBind classInfoBind = new ClassInfoBind(
                                className.packageName(), className.simpleName());
                        classInfoBindList.add(classInfoBind);
                    }
                }
            }

            String fieldName = element.getSimpleName().toString();
            TypeMirror typeMirror = element.asType();
            FieldBind fieldViewBinding = new FieldBind(fieldName, typeMirror, classInfoBindList);
            fieldBindList.add(fieldViewBinding);

        }

        return targetClassMap;
    }

    /**
     * Return the newly created class file
     * @param clsName class name
     * @return new class
     */
    private TypeSpec.Builder createNewClass(String clsName){
        ClassName className = ClassName.bestGuess(clsName);
        ClassName viewBinder = ClassName.get("com.silencezwm.openall", "IOpenType");

        return TypeSpec.classBuilder(className + "$$OpenAll")
                .addModifiers(Modifier.PUBLIC)
                .superclass(className)
                .addSuperinterface(viewBinder);
    }

    /**
     * cReturn the newly created method file
     * @return process param method object
     */
    private MethodSpec.Builder createProcessParamMethod() {
        MethodSpec.Builder processParamMethod = MethodSpec.methodBuilder("processParamMethod")
                .addModifiers(Modifier.PRIVATE)
                .returns(TypeName.VOID)
                .addParameter(OpenAllParams.class, "openAllParams")
                .addParameter(mIntentClass, "intent");

        processParamMethod.addCode("for ($T.Entry<String, Map<String, Object>> entry : " +
                "openAllParams.getMap().entrySet()){\n", Map.class);
        processParamMethod.addCode("switch (entry.getKey()) {\n");
        processParamType(processParamMethod, IParamType.INT_PARAM, int.class);
        processParamType(processParamMethod, IParamType.STRING_PARAM, String.class);
        processParamType(processParamMethod, IParamType.LONG_PARAM, long.class);
        processParamType(processParamMethod, IParamType.BOOLEAN_PARAM, boolean.class);
        processParamType(processParamMethod, IParamType.SHORT_PARAM, short.class);
        processParamType(processParamMethod, IParamType.FLOAT_PARAM, float.class);
        processParamType(processParamMethod, IParamType.DOUBLE_PARAM, double.class);
        processParamType(processParamMethod, IParamType.BYTE_PARAM, byte.class);
        processParamType(processParamMethod, IParamType.CHAR_PARAM, char.class);
        processParamMethod.addCode("\n}\n");
        processParamMethod.addCode("\n}\n");

        return processParamMethod;
    }

    /**
     * Return the newly created method file
     * @param fieldBindList
     * @return
     */
    private MethodSpec.Builder createOpenMethod(List<FieldBind> fieldBindList) {
        MethodSpec.Builder openMethodBuilder = MethodSpec.methodBuilder("open")
                .addModifiers(Modifier.PUBLIC)
                .returns(TypeName.VOID)
                .addAnnotation(Override.class)
                .addParameter(mContextClass, "context")
                .addParameter(ClassName.bestGuess(String.class.getSimpleName()),
                        "targetClass", Modifier.FINAL)
                .addParameter(OpenAllParams.class, "openAllParams", Modifier.FINAL);

        for (int i = 0; i < fieldBindList.size(); i++) {
            if (fieldBindList.size() <= 0) continue;

            FieldBind fieldBind = fieldBindList.get(i);

            List<ClassInfoBind> packageNameStr = fieldBind.getClassInfoBind();
            openMethodBuilder.addCode("switch ($L) {\n", "targetClass");

            for (ClassInfoBind tempName : packageNameStr) {

                if (tempName == null) continue;

                ClassName targetClazz = ClassName.get(tempName.getPackageName(),
                        tempName.getClassName());
                openMethodBuilder.addCode("case $S:\n", tempName.getClassName());
                openMethodBuilder.addStatement("$T $L = new Intent(context, $T.class)",
                        mIntentClass, "intentTo" + tempName.getClassName(), targetClazz);
                openMethodBuilder.addStatement("processParamMethod(openAllParams, $L)",
                        "intentTo" + tempName.getClassName());
                openMethodBuilder.addStatement("context.startActivity($L)",
                        "intentTo" + tempName.getClassName());
                openMethodBuilder.addStatement("context = null");
                openMethodBuilder.addCode("break;\n");
            }
            openMethodBuilder.addCode("\n}\n");
        }

        return openMethodBuilder;
    }

    /**
     * Generating code based on different parameter types
     * @param methodBuilder
     * @param type
     * @param typeClassName
     */
    private void processParamType(MethodSpec.Builder methodBuilder, String type, Class typeClassName) {
        methodBuilder.addCode("case $L:\n", "\"" + type + '"');
        methodBuilder.addCode("for (Map.Entry<String, Object> entryParam " +
                ": entry.getValue().entrySet()){\n");
        methodBuilder.addCode("intent.putExtra(entryParam.getKey(), ($L) entryParam.getValue());\n",
                typeClassName.getSimpleName());
        methodBuilder.addCode("}\n");
        methodBuilder.addCode("break;\n");
    }

    /**
     * Return class name
     * @param typeElement
     * @return
     */
    private String getClassName(Element typeElement) {
        return typeElement.getSimpleName().toString();
    }

    /**
     * Return packageName
     * @param typeElement
     * @return
     */
    private String getPackageName(Element typeElement) {
        return mElements.getPackageOf(typeElement).getQualifiedName().toString();
    }

}
