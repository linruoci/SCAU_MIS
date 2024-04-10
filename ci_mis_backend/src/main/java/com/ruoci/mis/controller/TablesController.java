package com.ruoci.mis.controller;

import cn.hutool.core.util.ObjectUtil;
import com.github.pagehelper.PageInfo;
import com.ruoci.mis.common.Result;
import com.ruoci.mis.common.enums.ResultCodeEnum;
import com.ruoci.mis.entity.OrdersDTO;
import com.ruoci.mis.entity.Tables;
import com.ruoci.mis.service.OrdersService;
import com.ruoci.mis.service.TablesService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 用户表前端操作接口
 **/
@RestController
@RequestMapping("/tables")
public class TablesController {

    @Resource
    private TablesService tablesService;


    @Resource
    private OrdersService ordersService;


    /**
     * 新增
     */
    @PostMapping("/add")
    public Result add(@RequestBody Tables tables) {
        // 数据校验
        tablesService.add(tables);
        return Result.success();
    }

    /**
     * 删除
     */
    @DeleteMapping("/delete/{id}")
    public Result deleteById(@PathVariable Integer id) {
        tablesService.deleteById(id);
        return Result.success();
    }

    /**
     * 批量删除
     */
    @DeleteMapping("/delete/batch")
    public Result deleteBatch(@RequestBody List<Integer> ids) {
        tablesService.deleteBatch(ids);
        return Result.success();
    }

    /**
     * 修改
     */
    @PutMapping("/update")
    public Result updateById(@RequestBody Tables tables) {
        tablesService.updateById(tables);
        return Result.success();
    }

    /**
     * 根据ID查询
     */
    @GetMapping("/selectById/{id}")
    public Result selectById(@PathVariable Integer id) {
        Tables tables = tablesService.selectById(id);
        return Result.success(tables);
    }

    /**
     * 查询所有
     */
    @GetMapping("/selectAll")
    public Result selectAll(Tables tables ) {
        List<Tables> list = tablesService.selectAll(tables);
        return Result.success(list);
    }

    /**
     * 分页查询
     */
    @GetMapping("/selectPage")
    public Result selectPage(Tables tables,
                             @RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "10") Integer pageSize) {
        PageInfo<Tables> page = tablesService.selectPage(tables, pageNum, pageSize);
        return Result.success(page);
    }

    @PostMapping("/addOrder")
    public Result addOrder(@RequestBody OrdersDTO orders) {
        ordersService.addOrder(orders);
        return Result.success();
    }


}