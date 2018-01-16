package com.silencezwm.openallcompiler;

class ClassInfoBind {

    private String mPackageName;
    private String mClassName;

    ClassInfoBind(String packageName, String className) {
        mPackageName = packageName;
        mClassName = className;
    }

    String getPackageName() {
        return mPackageName;
    }

    String getClassName() {
        return mClassName;
    }

}
