package com.yh.thread;

import com.yh.entity.MenuInfo;
import com.yh.feign.DataSynchronizeFeign;

import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

public class ThredQuery implements Callable<List<MenuInfo>> {

    /******需要通过构造方法把对应的业务service传进来 实际用的时候把类型变为对应的类型******/
    private DataSynchronizeFeign feign;
    /******查询条件 根据条件来定义该类的属性******/
    private Map<String, Object> params;

    /******分页index******/
    private int offset;
    /******数量******/
    private int num;

    public ThredQuery(DataSynchronizeFeign feign, Map<String, Object> params, int offset, int num) {
        this.feign = feign;
        this.params = params;
        this.offset = offset;
        this.num = num;
    }

    @Override
    public List<MenuInfo> call() throws Exception {
        /******通过service查询得到对应结果******/
        List<MenuInfo> menuInfos = feign.findMenuPage(offset,num);
        return menuInfos;
    }
}
