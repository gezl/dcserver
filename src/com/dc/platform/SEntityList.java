package com.dc.platform;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import org.apache.commons.lang3.StringUtils;

/**
 * @company 东昌数码
 * <p>
 * Copyright: Copyright (c) 2012
 * </p>
 * @author sslin
 * @date 2013-4-12 上午10:46:09
 * @description : 持久化对象查询
 */
public class SEntityList extends ArrayList<SEntity> {

    /**
     * 
     */
    private static final long serialVersionUID = 6198133611971434132L;
    /**
     * @description :排序定义
     */
    public enum Sort_type {
        SORT_UP, // 升序
        SORT_DOWN // 降序
    }

    /**
     *  表名
     */
    private String                tableName;

    /**
     * 当前页数
     */
    private int                   pageNum          = 1;

    /**
     * 总页数
     */
    private int                   totalPage;

    /**
     * 每页显示行数
     */
    private int                   pageSize         = Integer.MAX_VALUE;

    /**
     * 总记录数
     */
    private int                   totalCount;

    /**
     *  查询的列
     */
    private List<String>          searchColumns    = new ArrayList<String>();

    /**
     * 查询、修改、删除 条件
     */
    private Map<String, Object[]> conditions       = new TreeMap<String, Object[]>();

    /**
     * 排序参数
     */
    private StringBuffer          orderByCondition = new StringBuffer("");

    public SEntityList(String tableName) {

        this.tableName = tableName.toLowerCase();
    }

    /**
     * 添加新条件  </br>
     * **要求不能直接把参数写在sql里，用法如PreparedStatment </br>
     * 新加参数间关系为and </br>
     * 示例:</br>
     * 1. addCondition("para1=?","参数") </br>
     * 2. addCondition("para1=? and para2=?","参数1","参数2")</br>
     * 2. addCondition("para1=? and para2=?",{"参数1","参数2"})</br>
     * 3. addCondition("para1 is not null")	
     * 
     * @param sql where后的sql 例如：para=? and para>? and para&lt;? and para like '%?%'
     * @param value sql 对应的参数，参数长度为可变参数</br>
     * @throws Exception 当参数不符合要求时抛出运行时异常
     */
    public void addCondition(String sql, Object... values) throws RuntimeException {

        if (sql == null) {
            throw new RuntimeException("sql不能为空");
        }
        sql = sql.trim();
        if ("".equals(sql)) {
            throw new RuntimeException("sql不能为空");
        }
        if (StringUtils.countMatches(sql, "?") != values.length) {
            throw new RuntimeException("sql的替代符与参数个数不一致");
        }
        conditions.put(sql, values);
    }

    /**
     * 删除相关查询条件
     * @param sql 添加条件时的SQL
     * @throws RuntimeException
     */
    public void removeCondition(String sql) throws RuntimeException {

        if (sql == null) {
            throw new RuntimeException("sql不能为空");
        }
        sql = sql.trim();
        if ("".equals(sql)) {
            throw new RuntimeException("sql不能为空");
        }
        conditions.remove(sql);
    }

    /**
     * 添加排序条件</br>
     * 多个排序条件将按照加入条件的先后顺序
     * @param column 需要排序的字段
     * @param value 升序、降序
     */
    public void addSortCondition(String column, Sort_type value) {

        if (orderByCondition.toString().equals("")) {
            orderByCondition.append(" order by ");
        }
        if (!orderByCondition.toString().equals(" order by ")) {
            orderByCondition.append(",");
        }
        orderByCondition.append(column);
        orderByCondition.append(" ");
        orderByCondition.append(value == Sort_type.SORT_DOWN ? "desc" : "asc");
    }

    /**
     * 设置要查询的字段
     * @param searchColumns
     */
    public void setSearchColumns(String... searchColumns) {

        for (String searchColumn : searchColumns) {
            this.searchColumns.add(searchColumn);
        }
    }

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

    public int getTotalCount() {

        return totalCount;
    }

    public void setTotalCount(int totalCount) {

        this.totalCount = totalCount;
    }

    public String getTableName() {

        return tableName;
    }

    public void setTableName(String tableName) {

        this.tableName = tableName;
    }

    public List<String> getSearchColumns() {

        return searchColumns;
    }

    public Map<String, Object[]> getConditions() {

        return conditions;
    }

    public StringBuffer getOrderByCondition() {

        return orderByCondition;
    }

    public int getTotalPage() {

        return totalPage;
    }

    public void setTotalPage(int totalPage) {

        this.totalPage = totalPage;
    }
}