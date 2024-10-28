package com.satan.pay.config;

import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.alipay.api.request.AlipayTradeRefundRequest;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author: Demon
 * @Date: 2024/10/11 10:46
 * @Description:
 **/
@Data
@Configuration
@ConfigurationProperties("alipay")
public class AliPayConfig {
    private static final String FORMAT = "json";
    private static final String CHARSET = "utf-8";
    private static final String SIGN_TYPE = "RSA2";
    // 支付宝的AppId
    private String appId;
    // 应用私钥
    private String appPrivateKey;
    // 支付宝公钥
    private String alipayPublicKey;
    // 支付宝通知本地的接口完整地址
    private String notifyUrl;
    // 沙箱网管地址
    private String gateWayUrl;
    //返回地址
    private String returnUrl;

    @Bean
    AlipayClient alipayClient() {
        AlipayClient alipayClient = new DefaultAlipayClient(gateWayUrl, appId,
                appPrivateKey, FORMAT, CHARSET, alipayPublicKey, SIGN_TYPE);
        return alipayClient;
    }

    @Bean
    AlipayTradePagePayRequest alipayTradePagePayRequest() {
        AlipayTradePagePayRequest alipayTradePagePayRequest = new AlipayTradePagePayRequest();
        alipayTradePagePayRequest.setNotifyUrl(notifyUrl);
        return alipayTradePagePayRequest;
    }

    @Bean
    AlipayTradeRefundRequest alipayTradeRefundRequest() {
        AlipayTradeRefundRequest alipayTradeRefundRequest = new AlipayTradeRefundRequest();
        return alipayTradeRefundRequest;
    }
}
