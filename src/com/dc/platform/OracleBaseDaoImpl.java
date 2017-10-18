package com.dc.platform;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.annotation.Resource;
import javax.sql.DataSource;

import org.apache.commons.lang.ArrayUtils;
import org.apache.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.dc.platform.DBUtils.DBType;
import com.dc.platform.dbinfo.DatabaseManager;
import com.dc.platform.dbinfo.Table;
import com.dc.platform.permission.ThreadLocalSession;

/**
 * @company 东昌数码
 * <p>
 * Copyright: Copyright (c) 2012
 * </p>
 * @author sslin
 * @date 2013-5-14 下午4:53:25
 * @description :
 */
 @Repository("baseDao")
public class OracleBaseDaoImpl extends JdbcDaoSupport implements BaseDao {

    static Logger           log = Logger.getLogger(OracleBaseDaoImpl.class);

     @Resource
    private DatabaseManager databaseManager;

    public void setDatabaseManager(DatabaseManager databaseManager) {

        this.databaseManager = databaseManager;
    }

     @Resource
     public void setDefaultDataSource(DataSource dataSource){
     super.setDataSource(dataSource);
     }
    // 把结果结转换成SEntityList
    protected SEntityListResultSetExtractor sEntityListResultSetExtractor = new SEntityListResultSetExtractor();

    // 把结果结转换成list<Map<String,Object>>
    protected MapResultSetExtractor         mapResultSetExtractor         = new MapResultSetExtractor();

    // 把结果结转换成list<Ojbect[]>
    protected ListResultSetExtractor        listResultSetExtractor        = new ListResultSetExtractor();

    // 把结果结转换成Object
    protected UniqueResultSetExtractor      uniqueResultSetExtractor      = new UniqueResultSetExtractor();

    @Override
    public int save(final String sql, final List<Object> parameters, final String... generatedKey) {

        // 如果generatedKey为空，则直接保存，并返回-1
        if (generatedKey == null || generatedKey.length == 0) {
            excuteUpdate(sql, parameters);
            return -1;
        }
        if (ThreadLocalSession.getUser() != null) {
        	log.info("用户：" + ThreadLocalSession.getUser().getValueAsString("id") + " " + ThreadLocalSession.getUser().getValueAsString("account") + " SQL:" + sql);
        } else {
            log.info("系统SQL:" + sql);
        }
        if (parameters != null) {
            log.info("##SQL_PARAM##: " + ArrayUtils.toString(parameters.toArray()));
        }
        KeyHolder keyHolder = new GeneratedKeyHolder();
        getJdbcTemplate().update(new PreparedStatementCreator() {

            public PreparedStatement createPreparedStatement(Connection conn) throws SQLException {

                PreparedStatement stmt = null;
                stmt = conn.prepareStatement(sql, generatedKey);
                // 设置参数
                if (parameters != null) {
                    for (int i = 0; i < parameters.size(); i++) {
                        stmt.setObject(i + 1, parameters.get(i));
                    }
                }
                return stmt;
            }
        }, keyHolder);
        try {
            return keyHolder.getKey().intValue();
        } catch (Exception e) {
            throw new RuntimeException("generatedKey return error!");
        }
    }

    @Override
    public void update(String sql, List<Object> parameters) {

        excuteUpdate(sql, parameters);
    }

    @Override
    public void delete(String sql, List<Object> parameters) {

        excuteUpdate(sql, parameters);
    }

    @Override
    public void excuteUpdate(String sql, List<Object> parameters) {

        if (ThreadLocalSession.getUser() != null) {
            log.info("用户：" + ThreadLocalSession.getUser().getValueAsString("id") + " " + ThreadLocalSession.getUser().getValueAsString("account") + " SQL:" + sql);
        } else {
            log.info("系统SQL:" + sql);
        }
        if (parameters != null) log.info("##SQL_PARAM##: " + ArrayUtils.toString(parameters.toArray()));
        // log.info("##SQL_PARAM##: " + parameters == null ?"":ArrayUtils.toString(parameters.toArray()));
        if (parameters == null || parameters.size() == 0) {
            getJdbcTemplate().update(sql);
        } else {
            getJdbcTemplate().update(sql, parameters.toArray());
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public SEntityList search(String sql, List<Object> parameters, int... pageModel) {

        SEntityList entityList = new SEntityList("");
        if (parameters == null) {
            parameters = new ArrayList<Object>();
        }
        sql = DBUtils.createPageSQL(sql, DBType.oracle, pageModel);
        log.info("##SQL##: " + sql);
        log.info("##SQL_PARAM##: " + ArrayUtils.toString(parameters.toArray()));
        List<SEntity> list = (List<SEntity>) getJdbcTemplate().query(sql, parameters.toArray(), sEntityListResultSetExtractor);
        entityList.addAll(list);
        return entityList;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Object[]> searchToList(String sql, List<Object> parameters, int... pageModel) {

        if (parameters == null) {
            parameters = new ArrayList<Object>();
        }
        log.info("##SQL##: " + sql);
        log.info("##SQL_PARAM##: " + ArrayUtils.toString(parameters.toArray()));
        sql = DBUtils.createPageSQL(sql, DBType.oracle, pageModel);
        return (List<Object[]>) getJdbcTemplate().query(sql, parameters.toArray(), listResultSetExtractor);
    }

    @Override
    public Object loadUniqueResult(String sql, List<Object> parameters) {

        if (parameters == null) {
            parameters = new ArrayList<Object>();
        }
        log.info("##SQL##: " + sql);
        log.info("##SQL_PARAM##: " + ArrayUtils.toString(parameters.toArray()));
        return getJdbcTemplate().query(sql, parameters.toArray(), uniqueResultSetExtractor);
    }

    @Override
    public String loadUniqueResultAsString(String sql, List<Object> parameters) {

        Object obj = loadUniqueResult(sql, parameters);
        if (obj == null) {
            return null;
        }
        return obj.toString();
    }

    @Override
    public int loadUniqueResultAsInteger(String sql, List<Object> parameters) {

        Object obj = loadUniqueResult(sql, parameters);
        if (obj == null) {
            throw new RuntimeException("search column as integer is null");
        }
        return ((BigDecimal) obj).intValue();
    }

    @Override
    public long loadUniqueResultAsLong(String sql, List<Object> parameters) {

        Object obj = loadUniqueResult(sql, parameters);
        if (obj == null) {
            throw new RuntimeException("search column as long is null");
        }
        return ((BigDecimal) obj).longValue();
    }

    @Override
    public double loadUniqueResultAsDouble(String sql, List<Object> parameters) {

        Object obj = loadUniqueResult(sql, parameters);
        if (obj == null) {
            throw new RuntimeException("search column as double is null");
        }
        return ((BigDecimal) obj).doubleValue();
    }

    @Override
    public float loadUniqueResultAsFloat(String sql, List<Object> parameters) {

        Object obj = loadUniqueResult(sql, parameters);
        if (obj == null) {
            throw new RuntimeException("search column as float is null");
        }
        return ((BigDecimal) obj).floatValue();
    }

    @Override
    public boolean loadUniqueResultAsBoolean(String sql, List<Object> parameters) {

        Object obj = loadUniqueResult(sql, parameters);
        if (obj == null) {
            throw new RuntimeException("search column as boolean is null");
        }
        return ((BigDecimal) obj).intValue() > 0 ? true : false;
    }

    @Override
    public byte loadUniqueResultAsByte(String sql, List<Object> parameters) {

        Object obj = loadUniqueResult(sql, parameters);
        if (obj == null) {
            throw new RuntimeException("search column as byte is null");
        }
        return ((BigDecimal) obj).byteValue();
    }

    @Override
    public short loadUniqueResultAsShort(String sql, List<Object> parameters) {

        Object obj = loadUniqueResult(sql, parameters);
        if (obj == null) {
            throw new RuntimeException("search column as short is null");
        }
        return ((BigDecimal) obj).shortValue();
    }

    @Override
    public int save(SEntity entity, String... databaseName) {

        if (entity.getColumns().size() == 0) {
            // throw new RuntimeException("没有任何可保存的数据");
        }
        // 格式化参数类型
        entity = removeNullSEntityParams(entity);
        entity = correctSEntityParams(entity, databaseName);
        StringBuffer sqlBuffer = new StringBuffer();
        StringBuffer sqlValueBuffer = new StringBuffer();
        List<Object> values = new ArrayList<Object>();
        sqlBuffer.append("insert into ");
        sqlBuffer.append(entity.getTableName());
        sqlBuffer.append(" (");
        sqlValueBuffer.append(" values (");
        // /////---------------------------------添加数据权限开始-----------------------------------
//        if (databaseManager.getTable(entity.getTableName()).getColumnNames().contains("DATAPERMISSION")) {
//            entity.setValue("DATAPERMISSION", ((User) ThreadLocalSession.getUser()).getCode());
//        }
        // //-----------------------------------结束---------------------------------------------
        Set<Entry<String, Object>> entrySet = entity.getColumns().entrySet();
        for (Iterator iterator = entrySet.iterator(); iterator.hasNext();) {
            Entry<String, Object> entry = (Entry<String, Object>) iterator.next();
            sqlBuffer.append(entry.getKey());
            sqlValueBuffer.append("?");
            if (iterator.hasNext()) {
                sqlBuffer.append(",");
                sqlValueBuffer.append(",");
            }
            values.add(entry.getValue());
        }
        sqlBuffer.append(") ");
        sqlValueBuffer.append(")");
        sqlBuffer.append(sqlValueBuffer);
        try {
            Table table = databaseManager.getTable(entity.getTableName(), databaseName);
            String[] generatedKey = new String[table.getPkColumns().size()];
            if (generatedKey.length == 0) {
                int id = this.save(sqlBuffer.toString(), values);
                return id;
            } else {
                String key = entity.getValueAsString(table.getPkColumns().toArray()[0].toString());
                int id = -1;
                if (key == null || "".equals(key)) {
                    generatedKey = table.getPkColumns().toArray(generatedKey);
                    id = this.save(sqlBuffer.toString(), values, generatedKey);
                    entity.setValue((String) table.getPkColumns().toArray()[0], id);
                } else {
                    this.save(sqlBuffer.toString(), values);
                }
                return id;
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.info("entity.getTableName()'s id is not auto increment column!");
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public void update(SEntity entity, String... databaseName) {

        if (entity.getColumns().size() == 0) {
            throw new RuntimeException("没有任何可更新的数据");
        }
        entity = correctSEntityParams(entity, databaseName);
        // 组合更新sql
        StringBuffer sqlBuffer = new StringBuffer();
        List<Object> values = new ArrayList<Object>();
        sqlBuffer.append("update ");
        sqlBuffer.append(entity.getTableName());
        sqlBuffer.append(" set ");
        Table table = databaseManager.getTable(entity.getTableName(), databaseName);
        Set<String> pkColumns = table.getPkColumns();
        Set<Entry<String, Object>> entrySet = entity.getColumns().entrySet();
        for (Iterator iterator = entrySet.iterator(); iterator.hasNext();) {
            Entry<String, Object> entry = (Entry<String, Object>) iterator.next();
            if (pkColumns.contains(entry.getKey() instanceof String ? entry.getKey().toUpperCase() : entry.getKey())) {
                entity.addCondition(entry.getKey() + "=?", entry.getValue());
                continue;
            }
            sqlBuffer.append(entry.getKey());
            sqlBuffer.append("=? ");
            if (iterator.hasNext()) {
                sqlBuffer.append(",");
            }
            values.add(entry.getValue());
        }
        // 组合where sql
        sqlBuffer.append(" where ");
        Set<Entry<String, Object[]>> conditionEntrySet = entity.getConditions().entrySet();
        for (Iterator iterator = conditionEntrySet.iterator(); iterator.hasNext();) {
            Entry<String, Object[]> entry = (Entry<String, Object[]>) iterator.next();
            sqlBuffer.append(entry.getKey());
            if (iterator.hasNext()) {
                sqlBuffer.append(" and ");
            }
            Object[] objs = entry.getValue();
            for (Object object : objs) {
                values.add(object);
            }
        }
        this.update(sqlBuffer.toString(), values);
    }

    @Override
    public void delete(SEntity entity, String... databaseName) {

        // 组合删除sql
        entity = correctSEntityParams(entity, databaseName);
        StringBuffer sqlBuffer = new StringBuffer();
        List<Object> values = new ArrayList<Object>();
        sqlBuffer.append("delete from ");
        sqlBuffer.append(entity.getTableName());
        sqlBuffer.append(" where ");
        if (entity.getConditions().size() > 0) {
            Set<Entry<String, Object[]>> conditionEntrySet = entity.getConditions().entrySet();
            for (Iterator iterator = conditionEntrySet.iterator(); iterator.hasNext();) {
                Entry<String, Object[]> entry = (Entry<String, Object[]>) iterator.next();
                sqlBuffer.append(entry.getKey());
                if (iterator.hasNext()) {
                    sqlBuffer.append(" and ");
                }
                Object[] objs = entry.getValue();
                for (Object object : objs) {
                    values.add(object);
                }
            }
        } else {
            Table table = databaseManager.getTable(entity.getTableName(), databaseName);
            Set<String> pkColumns = table.getPkColumns();
            Set<Entry<String, Object>> entrySet = entity.getColumns().entrySet();
            for (Iterator iterator = entrySet.iterator(); iterator.hasNext();) {
                Entry<String, Object> entry = (Entry<String, Object>) iterator.next();
                if (pkColumns.contains(entry.getKey() instanceof String ? entry.getKey().toUpperCase() : entry.getKey())) {
                    if (sqlBuffer.toString().endsWith("=? ")) {
                        sqlBuffer.append(" and ");
                    }
                    sqlBuffer.append(entry.getKey());
                    sqlBuffer.append("=? ");
                    values.add(entry.getValue());
                }
            }
        }
        this.delete(sqlBuffer.toString(), values);
    }

    @Override
    public void load(SEntityList entityList) {

        StringBuffer sqlBuffer = new StringBuffer();
        sqlBuffer.append(" from ");
        List<Object> parameters = new ArrayList<Object>();
        sqlBuffer.append(entityList.getTableName());
        // 组合where sql
        sqlBuffer.append(" where 1=1 ");
        Set<Entry<String, Object[]>> conditionEntrySet = entityList.getConditions().entrySet();
        for (Iterator iterator = conditionEntrySet.iterator(); iterator.hasNext();) {
            Entry<String, Object[]> entry = (Entry<String, Object[]>) iterator.next();
            if (!entry.getKey().toUpperCase().trim().startsWith("OR ") && !entry.getKey().toUpperCase().trim().startsWith("AND ")) {
                sqlBuffer.append(" and ");
            }
            sqlBuffer.append(" " + entry.getKey().replace("$", "") + " ");
            Object[] objs = entry.getValue();
            for (Object object : objs) {
                parameters.add(object);
            }
        }
        // 查询总行数
        String countSql = "select count(*) " + sqlBuffer.toString();
        entityList.setTotalCount(((BigDecimal) this.loadUniqueResult(countSql, parameters)).intValue());
        StringBuffer select = new StringBuffer();
        if (entityList.getSearchColumns().size() != 0) {
            select.append("select ");
            select.append(listToString(entityList.getSearchColumns()));
            select.append(" ");
        } else {
            select.append("select * ");
        }
        sqlBuffer.append(" ");
        sqlBuffer.append(entityList.getOrderByCondition());
        select.append(sqlBuffer);
        if (entityList.getPageNum() < 1) {
            entityList.setPageNum(1);
        }
        entityList.setTotalPage(Double.valueOf(Math.ceil(entityList.getTotalCount() / new Double(entityList.getPageSize()))).intValue());
        if (entityList.getPageNum() > entityList.getTotalPage()) {
            entityList.setPageNum(Double.valueOf(Math.ceil(entityList.getTotalCount() / new Double(entityList.getPageSize()))).intValue());
        }
        // 查询数据
        if (entityList.getPageSize() == Integer.MAX_VALUE) {
            entityList.addAll(this.search(select.toString(), parameters));
        } else {
            entityList.addAll(this.search(select.toString(), parameters, entityList.getPageNum(), entityList.getPageSize()));
        }
    }

    @Override
    public void load(SEntity entity) {

        StringBuffer sqlBuffer = new StringBuffer();
        if (entity.getSearchColumns().size() != 0) {
            sqlBuffer.append("select ");
            sqlBuffer.append(listToString(entity.getSearchColumns()));
            sqlBuffer.append(" from ");
        } else {
            sqlBuffer.append("select * from ");
        }
        List<Object> parameters = new ArrayList<Object>();
        sqlBuffer.append(entity.getTableName());
        // 组合where sql
        sqlBuffer.append(" where 1=1 ");
        Set<Entry<String, Object[]>> conditionEntrySet = entity.getConditions().entrySet();
        for (Iterator iterator = conditionEntrySet.iterator(); iterator.hasNext();) {
            Entry<String, Object[]> entry = (Entry<String, Object[]>) iterator.next();
            sqlBuffer.append(" and ");
            sqlBuffer.append(entry.getKey());
            Object[] objs = entry.getValue();
            for (Object object : objs) {
                parameters.add(object);
            }
        }
        if (parameters == null) {
            parameters = new ArrayList<Object>();
        }
        log.info("##SQL##: " + sqlBuffer.toString());
        log.info("##SQL_PARAM##: " + ArrayUtils.toString(parameters.toArray()));
        entity = (SEntity) getJdbcTemplate().query(sqlBuffer.toString(), parameters.toArray(), new SEntityResultSetExtractor(entity));
    }

    @Override
    public DatabaseManager getDatabaseManager() {

        return databaseManager;
    }

    // 把list转换为以，连接的字符串 串
    private String listToString(List<String> list) {

        StringBuffer ls = new StringBuffer(" ");
        for (Iterator iterator = list.iterator(); iterator.hasNext();) {
            String string = (String) iterator.next();
            ls.append(string);
            if (iterator.hasNext()) {
                ls.append(",");
            }
            ls.append(" ");
        }
        return ls.toString();
    }

    /**
     * 在数据对象与数据库同步前，对数据的类型最后一次验证与转换
     * @param sEntity
     */
    private SEntity correctSEntityParams(SEntity entity, String... databaseName) {

        Table table = databaseManager.getTable(entity.getTableName(), databaseName);
        Set<Entry<String, Object>> entrySet = entity.getColumns().entrySet();
        for (Iterator<Entry<String, Object>> iterator = entrySet.iterator(); iterator.hasNext();) {
            Entry<String, Object> entry = iterator.next();
            entity.setValue(entry.getKey(), DBUtils.parseObject(table.getColumn(entry.getKey()).getType(), entry.getValue()));
        }
        return entity;
    }

    /**
     * 在数据对象与数据库同步前，对数据的类型最后一次验证与转换
     * @param sEntity
     */
    private SEntity removeNullSEntityParams(SEntity entity) {

        Set<Entry<String, Object>> entrySet = entity.getColumns().entrySet();
        for (Iterator<Entry<String, Object>> iterator = entrySet.iterator(); iterator.hasNext();) {
            Entry<String, Object> entry = iterator.next();
            if (entry.getValue() instanceof String) {
                String value = (String) entry.getValue();
                if ("".equals(value)) {
                    iterator.remove();
                }
            }
        }
        return entity;
    }
    // 把结果结转换成SEntity
    class SEntityResultSetExtractor implements ResultSetExtractor {

        private SEntity entity;

        public SEntityResultSetExtractor(SEntity sEntity) {

            this.entity = sEntity;
        }

        public Object extractData(ResultSet rs) throws SQLException, DataAccessException {

            List<SEntity> list = new ArrayList<SEntity>();
            ResultSetMetaData rsmd = rs.getMetaData();
            int cols = rsmd.getColumnCount();
            if (rs.next()) {
                for (int i = 1; i <= cols; i++) {
                    String columnName = rsmd.getColumnName(i).toLowerCase();
                    if (!columnName.equalsIgnoreCase("rownum_")) {
                        if (!columnName.equalsIgnoreCase("rownum_")) {
                            if (rsmd.getColumnType(i) == 91) {
                                entity.setValue(rsmd.getColumnName(i).toLowerCase(), rs.getTimestamp(i));
                            } else {
                                entity.setValue(rsmd.getColumnName(i).toLowerCase(), rs.getObject(i));
                            }
                        }
                    }
                }
                if (rs.next()) {
                    throw new RuntimeException("more than one result has been fond,please cheack your conditions!");
                }
                return entity;
            }
            entity = null;
            return null;
        }
    }
    // 把结果结转换成SEntityList
    class SEntityListResultSetExtractor implements ResultSetExtractor {

        public Object extractData(ResultSet rs) throws SQLException, DataAccessException {

            List<SEntity> list = new ArrayList<SEntity>();
            ResultSetMetaData rsmd = rs.getMetaData();
            int cols = rsmd.getColumnCount();
            while (rs.next()) {
                SEntity entity = new SEntity("");
                for (int i = 1; i <= cols; i++) {
                    String columnName = rsmd.getColumnName(i).toLowerCase();
                    if (!columnName.equalsIgnoreCase("rownum_")) {
                        if (rsmd.getColumnType(i) == 91) {
                            entity.setValue(rsmd.getColumnName(i).toLowerCase(), rs.getTimestamp(i));
                        } else {
                            entity.setValue(rsmd.getColumnName(i).toLowerCase(), rs.getObject(i));
                        }
                    }
                }
                list.add(entity);
            }
            return list;
        }
    }
    // 把结果结转换成List<Map<String,Object>>
    class MapResultSetExtractor implements ResultSetExtractor {

        public Object extractData(ResultSet rs) throws SQLException, DataAccessException {

            List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
            ResultSetMetaData rsmd = rs.getMetaData();
            int cols = rsmd.getColumnCount();
            while (rs.next()) {
                Map<String, Object> result = new HashMap<String, Object>();
                for (int i = 1; i <= cols; i++) {
                    String columnName = rsmd.getColumnName(i).toLowerCase();
                    if (!columnName.equalsIgnoreCase("rownum_")) {
                        if (rsmd.getColumnType(i) == 91) {
                            result.put(rsmd.getColumnName(i).toLowerCase(), rs.getTimestamp(i));
                        } else {
                            result.put(rsmd.getColumnName(i).toLowerCase(), rs.getObject(i));
                        }
                    }
                }
                list.add(result);
            }
            return list;
        }
    }
    // 把结果结转换成List<Object[]>
    class ListResultSetExtractor implements ResultSetExtractor {

        public Object extractData(ResultSet rs) throws SQLException, DataAccessException {

            List<Object[]> list = new ArrayList<Object[]>();
            ResultSetMetaData rsmd = rs.getMetaData();
            int cols = rsmd.getColumnCount();
            while (rs.next()) {
                List<Object> result = new ArrayList<Object>();
                for (int i = 1; i <= cols; i++) {
                    String columnName = rsmd.getColumnName(i).toLowerCase();
                    if (!columnName.equalsIgnoreCase("rownum_")) {
                        if (rsmd.getColumnType(i) == 91) {
                            result.add(rs.getTimestamp(i));
                        } else {
                            result.add(rs.getObject(i));
                        }
                    }
                }
                list.add(result.toArray());
            }
            return list;
        }
    }
    // 把结果结转换成Object
    class UniqueResultSetExtractor implements ResultSetExtractor {

        public Object extractData(ResultSet rs) throws SQLException, DataAccessException {

            ResultSetMetaData rsmd = rs.getMetaData();
            if (rs.next()) {
                if (rsmd.getColumnType(1) == 91) {
                    return rs.getTimestamp(1);
                } else {
                    return rs.getObject(1);
                }
            } else {
                return null;
            }
        }
    }

    @Override
    public Connection getDBConnection() {

        return this.getConnection();
    }

    @Override
    public void releaseConn(Connection conn) {

        this.releaseConnection(conn);
    }
}
