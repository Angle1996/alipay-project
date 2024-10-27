package com.satan.pay.dto;

import lombok.Data;

@Data
public class AliPayDTO {
    private String traceNo;
    private double totalAmount;
    private String subject;
    private String alipayTraceNo;
}