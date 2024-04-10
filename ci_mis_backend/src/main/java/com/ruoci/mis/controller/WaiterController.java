package com.ruoci.mis.controller;

import cn.hutool.core.util.ObjectUtil;
import com.github.pagehelper.PageInfo;
import com.ruoci.mis.common.Result;
import com.ruoci.mis.common.enums.ResultCodeEnum;
import com.ruoci.mis.entity.User;
import com.ruoci.mis.entity.Waiter;
import com.ruoci.mis.service.UserService;
import com.ruoci.mis.service.WaiterService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 服务员操作接口
 **/
@RestController
@RequestMapping("/waiter")
public class WaiterController {

    @Resource
    private WaiterService waiterService;

    /**
     * 新增
     */
    @PostMapping("/add")
    public Result add(@RequestBody User user) {
        // 数据校验
        if (ObjectUtil.isEmpty(user.getUsername()) || ObjectUtil.isEmpty(user.getPassword())) {
            return Result.error(ResultCodeEnum.PARAM_LOST_ERROR);
        }
        waiterService.add(user);
        return Result.success();
    }

    /**
     * 删除
     */
    @DeleteMapping("/delete/{id}")
    public Result deleteById(@PathVariable Integer id) {
        waiterService.deleteById(id);
        return Result.success();
    }

    /**
     * 批量删除
     */
    @DeleteMapping("/delete/batch")
    public Result deleteBatch(@RequestBody List<Integer> ids) {
        waiterService.deleteBatch(ids);
        return Result.success();
    }

    /**
     * 修改
     */
    @PutMapping("/update")
    public Result updateById(@RequestBody User user) {
        waiterService.updateById(user);
        return Result.success();
    }

    /**
     * 根据ID查询
     */
    @GetMapping("/selectById/{id}")
    public Result selectById(@PathVariable Integer id) {
        Waiter waiter = waiterService.selectById(id);
        return Result.success(waiter);
    }

    /**
     * 查询所有
     */
    @GetMapping("/selectAll")
    public Result selectAll(Waiter waiter) {
        List<Waiter> list = waiterService.selectAll(waiter);
        return Result.success(list);
    }

    /**
     * 分页查询
     */
    @GetMapping("/selectPage")
    public Result selectPage(Waiter waiter,
                             @RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "10") Integer pageSize) {
        PageInfo<Waiter> page = waiterService.selectPage(waiter, pageNum, pageSize);
        return Result.success(page);
    }

}