package com.mmall.util;

import java.util.Map;

public class mmallUtil {

    public static boolean isNullorEmpty(Object object){
        if(object instanceof String) {
            if (((String) object).length() == 0 || object == "") {
                return true;
            }
        }
        return false;
    }
}
