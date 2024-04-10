package com.ruoci.mis.mapper;

import com.ruoci.mis.entity.Goods;
import com.ruoci.mis.entity.GoodsDTO;

import java.util.List;

/**
 * 操作goods相关数据接口
 */
public interface GoodsMapper {

    /**
     * 新增
     */
    int insert(Goods goods);

    /**
     * 删除
     */
    int deleteById(Integer id);

    /**
     * 修改
     */
    int updateById(Goods goods);

    /**
     * 根据ID查询
     */
    Goods selectById(Integer id);

    /**
     * 查询所有
     */
    List<Goods> selectAll(Goods goods);

    List<GoodsDTO> selectAllExceptStatus(GoodsDTO goodsDTO);
}