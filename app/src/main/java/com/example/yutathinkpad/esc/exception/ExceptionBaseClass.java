package com.example.yutathinkpad.esc.exception;

/**
 * Created by Yuta on 2016/03/30.
 */
public class ExceptionBaseClass extends Exception {

    public void PostFailerException(){
        String errorMessage = "通信に失敗しました";
    }
}
