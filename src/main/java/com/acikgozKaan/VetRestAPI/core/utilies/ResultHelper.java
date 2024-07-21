package com.acikgozKaan.VetRestAPI.core.utilies;

import com.acikgozKaan.VetRestAPI.core.result.Result;

public class ResultHelper {

    public static Result notFound(String msg) {
        return new Result(false,msg,"404");
    }

}
