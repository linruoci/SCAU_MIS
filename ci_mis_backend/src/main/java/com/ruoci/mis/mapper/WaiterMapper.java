package com.ruoci.mis.mapper;

import com.ruoci.mis.entity.User;
import com.ruoci.mis.entity.Waiter;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 操作user相关数据接口
 */
public interface WaiterMapper {

     /**
     * 新增
     */
    int insert(User user);

    /**
     * 删除
     */
    int deleteById(Integer id);

    /**
     * 修改
     */
    int updateById(Waiter waiter);

    /**
     * 根据ID查询
     */
    Waiter selectById(Integer id);

    /**
     * 查询所有
     */
    List<Waiter> selectAll(Waiter waiter);

    @Select("select * from waiter where username = #{username}")
    Waiter selectByUsername(String username);

}