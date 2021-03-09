package com.yh.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yh.entity.MenuInfo;
import com.yh.view.MenuVO;
import org.apache.ibatis.annotations.Mapper;


import java.util.List;

@Mapper
public interface SingleQueryDao extends BaseMapper<MenuInfo> {


    /**
     * 组装menus
     * @return
     */
    List<MenuVO> menus();
}
