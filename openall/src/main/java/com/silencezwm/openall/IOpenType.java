package com.silencezwm.openall;


import android.content.Context;

import com.silencezwm.openallparam.OpenAllParams;

public interface IOpenType {

    void open(Context context, String targetClassName, OpenAllParams paramsMap);

}
