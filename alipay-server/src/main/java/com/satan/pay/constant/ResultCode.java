package com.satan.pay.constant;

/**
 * @Author: Demon
 * @Date: 2024/10/4 23:28
 * @Description:
 **/
public enum ResultCode {
    SUCCESS(200, "操作成功"),
    FAIL(500, "操作失败"),
    PARAMS_ERROR(400, "参数错误"),
    NOT_FOUND(404, "未找到"),
    FORBIDDEN(403, "权限不足"),
    FILE_SIZE_EXCEED(405, "文件大小超过限制"),
    ORDER_NOT_EXIST(500, "订单不存在"),
    ORDER_ALREADY_PAID(500, "订单已支付"),
    //不允许退款
    ORDER_NOT_ALLOW_REFUND(500, "订单不允许退款"),
    //退款失败
    REFUND_FAIL(500, "退款失败"),
    PAYMENT_API_ERROR(500, "支付接口调用失败"),
    REFUND_API_ERROR(500, "退款接口调用失败");
    private final int code;
    private final String message;

    ResultCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
