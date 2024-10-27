package com.satan.pay.controller;

import com.alipay.api.AlipayApiException;
import com.satan.pay.config.AliPayConfig;
import com.satan.pay.dto.AliPayDTO;
import com.satan.pay.service.AliPayService;
import com.satan.pay.vo.Result;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * @Author: Demon
 * @Date: 2024/10/11 14:09
 * @Description:
 **/
@RestController
@RequestMapping("/alipay")
@RequiredArgsConstructor
public class AliPayController {
    private final AliPayConfig aliPayConfig;
    private final AliPayService aliPayService;

    @Operation(summary = "支付宝支付接口")
    @GetMapping("/pay")  //  /alipay/pay?orderNo=xxx
    public Result pay(String orderNo, HttpServletResponse httpResponse) throws Exception {
        return aliPayService.pay(orderNo, httpResponse);
    }

    @PostMapping("/notify")  // 注意这里必须是POST接口
    @Operation(summary = "支付宝支付异步通知接口")
    public void payNotify(HttpServletRequest request) throws Exception {
        aliPayService.payNotify(request);
    }

    @Operation(summary = "支付宝退款接口")
    @PostMapping("/refund")
    public Result refund(@RequestBody AliPayDTO aliPay) throws AlipayApiException {
        return aliPayService.refund(aliPay);
    }


}
