package com.silencezwm.openall;

import android.content.Context;

import com.silencezwm.openallparam.IParamType;
import com.silencezwm.openallparam.OpenAllParams;

import java.util.HashMap;
import java.util.Map;

/**
 * @author silencezwm on 2018/1/16 上午12:58
 * @email silencezwm@gmail.com
 * @description OpenAll use is simplify the code to open the component in Android.
 * <p>
 * --> Now only support open activity in activity.
 * <p>
 * <p>
 * Usage method:
 * Step 1:
 * @OpenActivity(target = {TargetOneActivity.class, TargetTwoActivity.class})
 * public class NowActivity extends Activity {}
 * <p>
 * Then build, OpenAll is can auto create class NowActivity$$OpenAll, And auto create
 * jump target activity code.
 * <p>
 * Step 2:
 * OpenAll.getInstance().open(this, TargetActivity.class);
 * <p>
 * or
 * <p>
 * OpenAll.getInstance().addIntParam("one", 1)
 * .addIntParam("two", 2)
 * .addIntParam("three", 3)
 * .addStringParam("four", "success")
 * .open(this, TargetActivity.class);
 */
public class OpenAll {

    private static volatile OpenAll mOpenAll;

    /**
     * Temporary storage Map
     */
    private Map<String, Map<String, Object>> paramsMap = new HashMap<>();

    private Map<String, Object> intParamMap = new HashMap<>();
    private Map<String, Object> stringParamMap = new HashMap<>();
    private Map<String, Object> longParamMap = new HashMap<>();
    private Map<String, Object> booleanParamMap = new HashMap<>();
    private Map<String, Object> shortParamMap = new HashMap<>();
    private Map<String, Object> floatParamMap = new HashMap<>();
    private Map<String, Object> doubleParamMap = new HashMap<>();
    private Map<String, Object> byteParamMap = new HashMap<>();
    private Map<String, Object> charParamMap = new HashMap<>();

    /**
     * This constructor is not used and prevents the default constructor being generated.
     */
    private OpenAll() {
        throw new RuntimeException("Stub!");
    }

    /**
     * Return OpenAll singleton object.
     *
     * @return OpenAll singleton obejct.
     */
    public static OpenAll getInstance() {
        if (mOpenAll == null) {
            synchronized (OpenAll.class) {
                if (mOpenAll == null) {
                    mOpenAll = new OpenAll();
                }
            }
        }
        return mOpenAll;
    }

    /**
     * Add int param.
     *
     * @param intParamName
     * @param intParamValue
     * @return
     */
    public OpenAll addIntParam(String intParamName, int intParamValue) {
        intParamMap.put(intParamName, intParamValue);
        paramsMap.put(IParamType.INT_PARAM, intParamMap);
        return this;
    }

    /**
     * Add String param
     *
     * @param stringParamName
     * @param stringParamValue
     * @return
     */
    public OpenAll addStringParam(String stringParamName, String stringParamValue) {
        stringParamMap.put(stringParamName, stringParamValue);
        paramsMap.put(IParamType.STRING_PARAM, stringParamMap);
        return this;
    }

    /**
     * Add long param
     *
     * @param longParamName
     * @param longParamValue
     * @return
     */
    public OpenAll addLongParam(String longParamName, int longParamValue) {
        longParamMap.put(longParamName, longParamValue);
        paramsMap.put(IParamType.LONG_PARAM, longParamMap);
        return this;
    }

    /**
     * Add boolean param
     *
     * @param booleanParamName
     * @param booleanParamValue
     * @return
     */
    public OpenAll addBooleanParam(String booleanParamName, int booleanParamValue) {
        booleanParamMap.put(booleanParamName, booleanParamValue);
        paramsMap.put(IParamType.BOOLEAN_PARAM, booleanParamMap);
        return this;
    }

    /**
     * Add short param
     *
     * @param shortParamName
     * @param shortParamValue
     * @return
     */
    public OpenAll addShortParam(String shortParamName, int shortParamValue) {
        shortParamMap.put(shortParamName, shortParamValue);
        paramsMap.put(IParamType.SHORT_PARAM, shortParamMap);
        return this;
    }

    /**
     * Add float param
     *
     * @param floatParamName
     * @param floatParamValue
     * @return
     */
    public OpenAll addFloatParam(String floatParamName, int floatParamValue) {
        floatParamMap.put(floatParamName, floatParamValue);
        paramsMap.put(IParamType.FLOAT_PARAM, floatParamMap);
        return this;
    }

    /**
     * Add double param
     *
     * @param doubleParamName
     * @param doubleParamValue
     * @return
     */
    public OpenAll addDoubleParam(String doubleParamName, int doubleParamValue) {
        doubleParamMap.put(doubleParamName, doubleParamValue);
        paramsMap.put(IParamType.DOUBLE_PARAM, doubleParamMap);
        return this;
    }

    /**
     * Add byte param
     *
     * @param byteParamName
     * @param byteParamValue
     * @return
     */
    public OpenAll addByteParam(String byteParamName, int byteParamValue) {
        byteParamMap.put(byteParamName, byteParamValue);
        paramsMap.put(IParamType.BYTE_PARAM, byteParamMap);
        return this;
    }

    /**
     * Add char param
     *
     * @param charParamName
     * @param charParamValue
     * @return
     */
    public OpenAll addCharParam(String charParamName, int charParamValue) {
        charParamMap.put(charParamName, charParamValue);
        paramsMap.put(IParamType.CHAR_PARAM, charParamMap);
        return this;
    }

    /**
     * Open target Activity
     *
     * @param context
     * @param targetClassName
     */
    public void open(Context context, Class<?> targetClassName) {
        try {
            Class<?> openClass = Class.forName(context.getClass().getName() + "$$OpenAll");
            IOpenType openType = (IOpenType) openClass.newInstance();
            OpenAllParams openAllParams = new OpenAllParams();
            openAllParams.setMap(paramsMap);
            openType.open(context, targetClassName.getSimpleName(), openAllParams);
            clearAllMap();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
    }

    /**
     * Empty a container
     */
    private void clearAllMap() {
        intParamMap.clear();
        stringParamMap.clear();
        longParamMap.clear();
        booleanParamMap.clear();
        shortParamMap.clear();
        floatParamMap.clear();
        doubleParamMap.clear();
        byteParamMap.clear();
        charParamMap.clear();
        paramsMap.clear();
    }

}
