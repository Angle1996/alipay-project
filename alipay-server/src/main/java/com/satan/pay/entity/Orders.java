package com.satan.pay.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @TableName tb_orders
 */
@TableName(value = "tb_orders")
@Data
public class Orders implements Serializable {
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
    /**
     *
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     *
     */
    @TableField(value = "order_no")
    private String orderNo;
    /**
     *
     */
    @TableField(value = "goods_name")
    private String goodsName;
    /**
     *
     */
    @TableField(value = "num")
    private Integer num;
    /**
     *
     */
    @TableField(value = "total")
    private BigDecimal total;
    /**
     *
     */
    @TableField(value = "create_time")
    private String createTime;
    /**
     *
     */
    @TableField(value = "pay_no")
    private String payNo;
    /**
     *
     */
    @TableField(value = "pay_time")
    private String payTime;
    /**
     *
     */
    @TableField(value = "status")
    private Integer status;
}