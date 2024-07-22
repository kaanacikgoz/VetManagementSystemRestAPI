package com.acikgozKaan.VetRestAPI.core.utilies;

import com.acikgozKaan.VetRestAPI.core.result.Result;
import com.acikgozKaan.VetRestAPI.core.result.ResultData;

public class ResultHelper {

    public static Result notFound(String msg) {
        return new Result(false,msg,"404");
    }

    public static Result error(String message) {
        return new Result(false, message, "400");
    }

    public static <T> ResultData<T> notFound(T data) {
        return new ResultData<>(false,Msg.NOT_FOUND,"404",data);
    }

    public static <T> ResultData<T> created(T data) {
        return new ResultData<>(true,Msg.CREATED,"201",data);
    }

    public static <T> ResultData<T> validateError(T data) {
        return new ResultData<>(false,Msg.VALIDATE_ERROR,"400",data);
    }

    public static <T> ResultData<T> deleted(T data) {
        return new ResultData<>(true,Msg.DELETED,"200",data);
    }

    public static <T> ResultData<T> success(T data) {
        return new ResultData<>(true,Msg.OK,"200",data);
    }

    public static <T> ResultData<T> errorData(String message) {
        return new ResultData<>(false, message, "400",null);
    }

}
