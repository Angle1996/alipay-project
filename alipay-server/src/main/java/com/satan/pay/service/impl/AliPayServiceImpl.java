package com.satan.pay.service.impl;

import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import cn.hutool.json.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.alipay.api.request.AlipayTradeRefundRequest;
import com.alipay.api.response.AlipayTradeRefundResponse;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.satan.pay.config.AliPayConfig;
import com.satan.pay.constant.ResultCode;
import com.satan.pay.dto.AliPayDTO;
import com.satan.pay.entity.Orders;
import com.satan.pay.mapper.OrdersMapper;
import com.satan.pay.service.AliPayService;
import com.satan.pay.vo.Result;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author: Demon
 * @Date: 2024/10/11 14:14
 * @Description:
 **/
@Service
@RequiredArgsConstructor
@Slf4j
public class AliPayServiceImpl implements AliPayService {

    private final OrdersMapper ordersMapper;
    private final AliPayConfig aliPayConfig;
    private final AlipayClient alipayClient;
    private final AlipayTradeRefundRequest alipayTradeRefundRequest;
    private final AlipayTradePagePayRequest alipayTradePagePayRequest;

    @Override
    public Result pay(String orderNo, HttpServletResponse httpResponse) throws IOException {
        // 查询订单信息
        LambdaQueryWrapper<Orders> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Orders::getOrderNo, orderNo);
        Orders orders = ordersMapper.selectOne(queryWrapper);

        if (orders == null) {
            log.warn("订单不存在: {}", orderNo);  // 增加日志记录
            return Result.fail(ResultCode.ORDER_NOT_EXIST);
        }

        // 判断订单状态
        if (orders.getStatus() != 0) {
            log.warn("订单已支付: {}", orderNo);  // 增加日志记录
            return Result.fail(ResultCode.ORDER_ALREADY_PAID);
        }

        // 订单未支付
        // 1. 创建Client，通用SDK提供的Client，负责调用支付宝的API
        JSONObject bizContent = new JSONObject();
        validateOrderFields(orders);  // 新增参数有效性验证方法
        bizContent.set("out_trade_no", orders.getOrderNo());  // 我们自己生成的订单编号
        bizContent.set("total_amount", orders.getTotal()); // 订单的总金额
        bizContent.set("subject", orders.getGoodsName());   // 支付的名称
        bizContent.set("product_code", "FAST_INSTANT_TRADE_PAY");  // 固定配置
        alipayTradePagePayRequest.setBizContent(bizContent.toString());
        alipayTradePagePayRequest.setReturnUrl(aliPayConfig.getReturnUrl()); // 从配置文件获取支付完成后的回调地址
        String form = "";
        try {
            form = alipayClient.pageExecute(alipayTradePagePayRequest).getBody(); // 调用SDK生成表单
        } catch (AlipayApiException e) {
            log.error("支付宝支付接口调用失败: {}", e.getMessage(), e);  // 记录异常信息
            return Result.fail(ResultCode.PAYMENT_API_ERROR);
        }
        return Result.success(form);
    }

    private void validateOrderFields(Orders orders) {
        if (orders.getOrderNo() == null || orders.getTotal() == null || orders.getGoodsName() == null) {
            throw new IllegalArgumentException("订单信息不完整");
        }
    }


    @Override
    public void payNotify(HttpServletRequest request) throws AlipayApiException {
        if (request.getParameter("trade_status").equals("TRADE_SUCCESS")) {
            System.out.println("=========支付宝异步回调========");

            Map<String, String> params = new HashMap<>();
            Map<String, String[]> requestParams = request.getParameterMap();
            for (String name : requestParams.keySet()) {
                params.put(name, request.getParameter(name));
            }

            String sign = params.get("sign");
            String content = AlipaySignature.getSignCheckContentV1(params);
            try {
                boolean checkSignature = AlipaySignature.rsa256CheckContent(content, sign, aliPayConfig.getAlipayPublicKey(), "UTF-8"); // 验证签名
                // 支付宝验签
                if (checkSignature) {
                    // 验签通过
                    log.info("交易名称:{} ", params.get("subject"));
                    log.info("交易状态:{}", params.get("trade_status"));
                    log.info("支付宝交易凭证号:{} ", params.get("trade_no"));
                    log.info("商户订单号:{} ", params.get("out_trade_no"));
                    log.info("交易金额:{} ", params.get("total_amount"));
                    log.info("买家在支付宝唯一id:{} ", params.get("buyer_id"));
                    log.info("买家付款时间:{} ", params.get("gmt_payment"));
                    log.info("买家付款金额:{} ", params.get("buyer_pay_amount"));

                    String tradeNo = params.get("out_trade_no");  // 订单编号
                    String gmtPayment = params.get("gmt_payment");  // 支付时间
                    String alipayTradeNo = params.get("trade_no");  // 支付宝交易编号
                    // 更新订单状态为已支付，设置支付信息
                    LambdaQueryWrapper<Orders> queryWrapper = new LambdaQueryWrapper<Orders>().eq(Orders::getOrderNo, tradeNo);
                    Orders orders = ordersMapper.selectOne(queryWrapper);
                    if (orders != null) {
                        orders.setStatus(1);
                        orders.setPayTime(gmtPayment);
                        orders.setPayNo(alipayTradeNo);
                        ordersMapper.updateById(orders);
                    } else {
                        log.warn("未找到对应订单: {}", tradeNo);
                    }
                } else {
                    log.warn("验签失败");
                }
            } catch (Exception e) {
                log.error("验签异常", e);
            }
        } else {
            log.warn("交易状态不是TRADE_SUCCESS");
        }
    }

    @Override
    public Result refund(AliPayDTO aliPay) throws AlipayApiException {
        // 7天无理由退款
        String now = DateUtil.now();
        String traceNo = aliPay.getTraceNo();
        LambdaQueryWrapper<Orders> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Orders::getOrderNo, traceNo);
        Orders orders = ordersMapper.selectOne(queryWrapper);
        if (orders != null) {
            long between = DateUtil.between(DateUtil.parseDateTime(orders.getPayTime()), DateUtil.parseDateTime(now), DateUnit.DAY);
            if (between > 7) {
                // 订单超过七天不支持退款
                return Result.fail(ResultCode.ORDER_NOT_ALLOW_REFUND);
            }

        }
        // 1 创建 Request，设置参数
        JSONObject bizContent = new JSONObject();
        bizContent.set("trade_no", aliPay.getAlipayTraceNo());  // 支付宝回调的订单流水号
        bizContent.set("refund_amount", aliPay.getTotalAmount());  // 订单的总金额
        bizContent.set("out_request_no", aliPay.getTraceNo());   // 我的订单编号

        // 返回参数选项，按需传入
        //JSONArray queryOptions = new JSONArray();
        //queryOptions.add("refund_detail_item_list");
        //bizContent.put("query_options", queryOptions);

        alipayTradeRefundRequest.setBizContent(bizContent.toString());

        // 2. 执行请求
        try {
            AlipayTradeRefundResponse response = alipayClient.execute(alipayTradeRefundRequest);
            if (response.isSuccess()) {  // 退款成功，isSuccess 为true
                log.info("调用成功");
                // 4. 更新数据库状态
                final int REFUNDED_STATUS = 2; // 定义常量
                orders.setStatus(REFUNDED_STATUS);
                orders.setPayTime(now);
                ordersMapper.updateById(orders);
                return Result.success();
            } else {   // 退款失败，isSuccess 为false
                log.error("退款失败: {}", response.getBody());
                return Result.fail(ResultCode.REFUND_FAIL);
            }
        } catch (AlipayApiException e) {
            // 处理API调用异常
            log.error("API调用异常: {}", e.getMessage());
            return Result.fail(ResultCode.REFUND_API_ERROR);
        }
    }

}
