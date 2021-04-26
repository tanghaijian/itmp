package cn.pioneeruniverse.dev.vo.task;

import cn.pioneeruniverse.dev.entity.DevTaskVo;

import java.util.List;

/**
 * @author by qingcheng
 * @version 2020/8/17 9:08
 * @description
 */
public class DevTaskRes {

    //======================================================
    // 属性
    private List<DevTaskVo> rows;

    //======================================================
    // 构造
    public DevTaskRes(){}
    public DevTaskRes(List<DevTaskVo> rows){
        this.rows = rows;
    }

    //======================================================
    // GETTER & SETTER
    public List<DevTaskVo> getRows() {
        return rows;
    }

    public void setRows(List<DevTaskVo> rows) {
        this.rows = rows;
    }

}
