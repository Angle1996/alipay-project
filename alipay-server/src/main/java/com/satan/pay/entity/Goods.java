package com.satan.pay.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @TableName tb_goods
 */
@TableName(value = "tb_goods")
@Data
public class Goods implements Serializable {
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
    @TableField(value = "name")
    private String name;
    /**
     *
     */
    @TableField(value = "no")
    private String no;
    /**
     *
     */
    @TableField(value = "price")
    private BigDecimal price;
    /**
     *
     */
    @TableField(value = "date")
    private String date;
}