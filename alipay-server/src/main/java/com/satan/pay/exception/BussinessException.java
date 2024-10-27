package com.satan.pay.exception;

/**
 * @Author: Demon
 * @Date: 2024/10/4 22:54
 * @Description:
 **/

public class BussinessException extends RuntimeException {
    public BussinessException() {
        super();
    }
    public BussinessException(String message) {
        super(message);
    }
}
