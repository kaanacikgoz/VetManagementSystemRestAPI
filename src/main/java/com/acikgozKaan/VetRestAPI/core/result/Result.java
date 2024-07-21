package com.acikgozKaan.VetRestAPI.core.result;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Result {

    private boolean status;
    private String message;
    private String code;

}
