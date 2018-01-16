package com.silencezwm.openallcompiler;

import java.util.List;

import javax.lang.model.type.TypeMirror;

public class FieldBind {

    private String mName;
    private TypeMirror mTypeMirror;
    private List<ClassInfoBind> mClassInfoBind;

    public FieldBind(String name, TypeMirror typeMirror, List classInfoBind) {
        mName = name;
        mTypeMirror = typeMirror;
        mClassInfoBind = classInfoBind;
    }

    public String getName() {
        return mName;
    }

    public TypeMirror getTypeMirror() {
        return mTypeMirror;
    }

    public List getClassInfoBind() {
        return mClassInfoBind;
    }
}
