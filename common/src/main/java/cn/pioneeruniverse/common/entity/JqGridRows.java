package cn.pioneeruniverse.common.entity;

import java.io.Serializable;

/**
 * Created by admin on 2018/11/1.
 */
public class JqGridRows<T extends BaseEntity> implements Serializable {
    private static final long serialVersionUID = -1804682498902577169L;

    private T cell;

    public JqGridRows(T cell){
       this.cell=cell;
    }

    public T getCell() {
        return cell;
    }

    public void setCell(T cell) {
        this.cell = cell;
    }
}
