package com.satan.pay.service;

import com.alipay.api.AlipayApiException;
import com.satan.pay.dto.AliPayDTO;
import com.satan.pay.vo.Result;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * @Author: Demon
 * @Date: 2024/10/11 14:14
 * @Description:
 **/
public interface AliPayService {

    Result pay(String orderNo, HttpServletResponse httpResponse) throws IOException;

    void payNotify(HttpServletRequest request) throws AlipayApiException;

    Result refund(AliPayDTO aliPay) throws AlipayApiException;
}
