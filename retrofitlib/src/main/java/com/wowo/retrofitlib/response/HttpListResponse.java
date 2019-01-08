package com.wowo.retrofitlib.response;

import java.util.ArrayList;

/**
 * 列表返回数据.
 *
 * @author Lei.Jiang
 * @version [1.0.0]
 * @see [HttpListResponse]
 * @since [1.0.0]
 */
public class HttpListResponse<T> {

    /**
     * 页码
     */
    private int pageNum;

    /**
     * 页数
     */
    private int pageSize;

    /**
     * 总数
     */
    private long total;

    /**
     * 列表数据
     */
    private ArrayList<T> list;

    public int getPageNum() {
        return pageNum;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public ArrayList<T> getList() {
        return list;
    }

    public void setList(ArrayList<T> list) {
        this.list = list;
    }
}
