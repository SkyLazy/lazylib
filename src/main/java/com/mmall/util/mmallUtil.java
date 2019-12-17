package com.mmall.util;

import java.util.Map;

public class mmallUtil {

    /**
     * @author lazy1
     * @param object
     * @return
     */
    public static boolean isNullorEmpty(Object object){
        if(object instanceof String){
            if(((String) object).length()==0 || object == ""){
                return true;
            }
        }
        if(object instanceof Map){
            if(((Map) object).size() == 0){
                return true;
            }
        }

        return false;
    }
}
