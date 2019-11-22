package com.cognition.bit.common.until;

import java.io.Serializable;
import java.util.List;

/**
 * 分页工具
 *
 * @author taoya
 */
public class PageUtils implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 页数参数（第几页）
     * <p>
     * total
     */
    private int total;
    /**
     * 页数（总共几页）
     * <p>
     * rows
     */
    private List<?> rows;

    public PageUtils(List<?> list, int total) {
        this.rows = list;
        this.total = total;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<?> getRows() {
        return rows;
    }

    public void setRows(List<?> rows) {
        this.rows = rows;
    }

}
