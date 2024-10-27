package com.satan.pay.vo;


import com.satan.pay.constant.ResultCode;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * 统一返回结果实体类
 *
 * @author xxx
 * @date 2021/10/25
 */
@Data
public class Result<T> implements Serializable {

    private static final long serialVersionUID = 1L;
    @Schema(description = "状态码")
    private int code;
    @Schema(description = "返回信息")
    private String message;

    private T data;

    public Result() {
    }

    public Result(ResultCode resultCode, T data) {
        this.code = resultCode.getCode();
        this.message = resultCode.getMessage();
        this.data = data;
    }

    public static <T> Result<T> success(T data) {
        return new Result<>(ResultCode.SUCCESS, data);
    }

    public static <T> Result<T> success() {
        return new Result<>(ResultCode.SUCCESS, null);
    }

    public static <T> Result<T> fail(ResultCode resultCode) {
        return new Result<>(resultCode, null);
    }

}
