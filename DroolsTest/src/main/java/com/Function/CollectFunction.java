package com.Function;

/**
 * Created by lujq on 1/22/2021.
 */
public class CollectFunction {
    /**
     * 是否为正整数
     */
    public Boolean isPositive(Object object){
        if(object==null){
            return false;
        }
        Long value ;
        try {
            value = Long.valueOf(object.toString());
        }catch (Exception e){
            return false;
        }
        if(value>0){return true;}
        else{return false;}
    }

}
