package com.satan.pay.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.satan.pay.entity.Orders;

/**
 * @author Demon
 * @description 针对表【tb_orders】的数据库操作Service
 * @createDate 2024-10-11 10:35:08
 */
public interface OrdersService extends IService<Orders> {

    void add(Orders orders);
}
