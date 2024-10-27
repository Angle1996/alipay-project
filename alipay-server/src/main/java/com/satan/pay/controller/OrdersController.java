package com.satan.pay.controller;

import com.satan.pay.entity.Orders;
import com.satan.pay.service.OrdersService;
import com.satan.pay.vo.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author: Demon
 * @Date: 2024/10/11 10:54
 * @Description:
 **/
@RestController
@Tag(name = "订单接口", description = "订单接口")
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrdersController {
    private final OrdersService ordersService;

    /**
     * 新增
     */
    @PostMapping("/add")
    @Operation(summary = "新增")
    public Result add(@RequestBody Orders orders) {
        ordersService.add(orders);
        return Result.success();
    }

    /**
     * 删除
     */
    @Operation(summary = "根据ID删除")
    @DeleteMapping("/delete/{id}")
    public Result deleteById(@PathVariable Integer id) {
        ordersService.removeById(id);
        return Result.success();
    }

    /**
     * 批量删除
     */
    @Operation(summary = "批量删除")
    @DeleteMapping("/delete/batch")
    public Result deleteBatch(@RequestBody List<Integer> ids) {
        ordersService.removeBatchByIds(ids);
        return Result.success();
    }

    /**
     * 修改
     */
    @PutMapping("/update")
    public Result updateById(@RequestBody Orders orders) {
        ordersService.updateById(orders);
        return Result.success();
    }

    /**
     * 查询订单列表
     */
    @GetMapping("/list")
    public Result<List<Orders>> list() {
        return Result.success(ordersService.list());
    }
}
