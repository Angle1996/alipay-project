package com.satan.pay.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.satan.pay.entity.Orders;
import com.satan.pay.mapper.OrdersMapper;
import com.satan.pay.service.OrdersService;
import org.springframework.stereotype.Service;

/**
 * @author Demon
 * @description 针对表【tb_orders】的数据库操作Service实现
 * @createDate 2024-10-11 10:35:08
 */
@Service
public class OrdersServiceImpl extends ServiceImpl<OrdersMapper, Orders>
        implements OrdersService {

    @Override
    public void add(Orders orders) {
        orders.setOrderNo(IdUtil.getSnowflakeNextIdStr());
        orders.setStatus(0);
        orders.setCreateTime(DateUtil.now());
        save(orders);

    }
}




