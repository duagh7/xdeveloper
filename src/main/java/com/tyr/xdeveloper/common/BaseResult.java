package com.tyr.xdeveloper.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.Accessors;

import static com.tyr.xdeveloper.common.CommonConstants.EMPTY;

@AllArgsConstructor
@Data
@Accessors(chain = true)
public class BaseResult<T> {

    private int code;

    private String msg;

    private T data;

    public static BaseResult ok(){
        return new BaseResult<Void>(0, EMPTY, null);
    }

    public static <T> BaseResult ok(T data){
        return new BaseResult<T>(0, EMPTY, data);
    }

    public static BaseResult error(String msg){
        return new BaseResult(-1, msg, null);
    }

}
