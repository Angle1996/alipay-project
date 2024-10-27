package com.satan.pay.controller;

import com.satan.pay.entity.Goods;
import com.satan.pay.service.GoodsService;
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
@RequestMapping("/goods")
@Tag(name = "商品接口", description = "商品接口")
@RequiredArgsConstructor
public class GoodsController {
    private final GoodsService goodsService;

    @PostMapping("/add")
    @Operation(summary = "添加商品")
    public Result add(@RequestBody Goods goods) {
        goodsService.save(goods);
        return Result.success();
    }

    /**
     * 删除
     */
    @Operation(summary = "删除商品")
    @DeleteMapping("/delete/{id}")
    public Result deleteById(@PathVariable Integer id) {
        goodsService.removeById(id);
        return Result.success();
    }

    /**
     * 批量删除
     */
    @Operation(summary = "批量删除商品")
    @DeleteMapping("/delete/batch")
    public Result deleteBatch(@RequestBody List<Integer> ids) {
        goodsService.removeBatchByIds(ids);
        return Result.success();
    }

    /**
     * 修改
     */
    @Operation(summary = "修改商品")
    @PutMapping("/update")
    public Result updateById(@RequestBody Goods goods) {
        goodsService.updateById(goods);
        return Result.success();
    }

    /**
     * 查询商品列表
     */
    @Operation(summary = "查询商品列表")
    @GetMapping("/list")
    public Result<List<Goods>> list() {
        return Result.success(goodsService.list());
    }


}
