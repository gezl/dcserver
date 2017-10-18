package com.dc.timer;

import java.util.Timer;
import java.util.TimerTask;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import com.dc.platform.Base4DBService;
import com.dc.platform.SEntity;
import com.dc.platform.SEntityList;
import com.dc.server.key.TypeEnum.DBType;
import com.dc.server.key.TypeEnum.EnabledType;
import com.dc.server.key.TypeEnum.UserType;
import com.dc.server.util.ToolsUtil;

@Component
public class ServerInitSqlTimer extends Base4DBService {

	public ServerInitSqlTimer() {

		new Timer().schedule(new TimerTask() {

			@Override
			public void run() {
				initUserConfig();
			}
		}, 0, 1000*60*1);

	}

	/**
	 * @Title: initUserConfig
	 * @Description: 循环用户配置生成同步SQL
	 * @author gezhiling
	 */
	public void initUserConfig() {
		SEntityList userList = new SEntityList("admin_user");
		userList.addCondition(" usertype = ?  ", UserType.USER.getKey());
		userList.addCondition(" enable = ?  ", EnabledType.ENABLE.getKey());
		baseDao.load(userList);
		//对所有用户进行配置查询
		for (SEntity sEntity : userList) {
			String userId = sEntity.getValueAsString("id");
			SEntityList list = new SEntityList("db_user_config");
			list.addCondition(" user_id = ? ", userId);
			list.addCondition(" status = ? ", EnabledType.ENABLE.getKey());
			baseDao.load(list);
			//对当前用户的所有配置进行初始化
			initSql(list, sEntity);
		}
	}

	/**
	 * 
	 * @Title: initSql
	 * @Description: 通过用户配置获取所需要同步数据
	 * @author gezhiling
	 * @param list
	 * @param user
	 */
	public void initSql(SEntityList list, SEntity user) {
		for (SEntity userConfig : list) {
			//通过配置获取提取数据源的同步表数据
			String initUserConfigSql = initSqlConfig(userConfig);
			//按条件同步数据 
			StringBuffer sb = new StringBuffer("select * from ( ");
			sb.append(initUserConfigSql);
			sb.append(" )  ");
			// 查询条件
			String hz_id = user.getValueAsString("hz_id");
			if (StringUtils.isNotBlank(hz_id)) {
				String hz_column = userConfig.getValueAsString("hz_column");
				if (StringUtils.isNotBlank(hz_column)) {
					String []hzId = hz_id.split(",");
					for (String id : hzId) {
						String sqlAppend = "  or " + hz_column + " = " + id;
						sb.append(sqlAppend);
					}
				}
			}

			String hd_id = user.getValueAsString("hd_id");
			if (StringUtils.isNotBlank(hd_id)) {
				String hd_column = userConfig.getValueAsString("hd_column");
				if (StringUtils.isNotBlank(hd_column)) {
					String []hdId = hd_id.split(",");
					for (String id : hdId) {
						String sqlAppend = "  or " + hd_column + " = " + id;
						sb.append(sqlAppend);
					}
				}
			}

			String lx_id = user.getValueAsString("lx_id");
			if (StringUtils.isNotBlank(lx_id)) {
				String lx_column = userConfig.getValueAsString("lx_column");
				if (StringUtils.isNotBlank(lx_column)) {
					String []lxId = lx_id.split(",");
					for (String id : lxId) {
						String sqlAppend = "  or " + lx_column + " = " + id;
						sb.append(sqlAppend);
					}
				}
			}

			String user_small_lx = user.getValueAsString("small_lx");
			if (StringUtils.isNotBlank(user_small_lx)) {
				String small_lx = userConfig.getValueAsString("small_lx");
				if (StringUtils.isNotBlank(small_lx)) {
					String[] slx = user_small_lx.split(",");
					for (String lxId : slx) {
						String sqlAppend = "  or " + small_lx + " = '" + lxId + "'";
						sb.append(sqlAppend);
					}
				}
			}

			String sql = sb.toString().replaceFirst("or", "where");

			// 表订阅字段
			String columns = userConfig.getValueAsString("columns");
			if (StringUtils.isNotBlank(columns)) {
				sql = "select data_status,data_time," + columns + " from ( " + sql
						+ " ) ";
			}
			//System.out.println(">>>>>>>>>>>>"+sql);
			//对数据进行拼装
			createSql(sql,userConfig,user.getValueAsString("db_type"));
		}
	}

	/**
	 * 
	 * @Title: initSqlConfig
	 * @Description: 通过配置获取提取数据源的同步表数据
	 * @author gezhiling
	 * @param userConfig
	 */
	public String initSqlConfig(SEntity userConfig) {
		// 提取数据源名称
		String tableName = userConfig.getValueAsString("table_view_name");
		// 提取数据源关联主表字段
		String relation_fk_id = userConfig.getValueAsString("relation_fk_id");
		// 获取主表名称
		String db_trigger_id = userConfig.getValueAsString("db_trigger_id");
		SEntity entity = new SEntity("db_trigger");
		entity.addCondition(" id = ? ", db_trigger_id);
		baseDao.load(entity);
		String primary_table_name = entity
				.getValueAsString("primary_table_name");
		// 根据配置信息获取用户数据源的同步数据sql
		StringBuffer sb = new StringBuffer(
				" select db.*, temp.data_status,temp.op_time data_time  from  db_temp temp  join ");
		sb.append(tableName);
		sb.append(" db  on temp.table_id=db.");
		sb.append(relation_fk_id);
		sb.append(" where temp.table_name ='");
		sb.append(primary_table_name);
		sb.append("'");
		//从上次同步时间之后开始同步
		String opTime = userConfig.getValueAsString("time");
		sb.append(" and temp.op_time >=  to_date('");
		sb.append(opTime);
		sb.append("', 'yyyy-mm-dd hh24:mi:ss')");
		return sb.toString();
	}

	/**
	 * 
	 * @Title: createSql
	 * @Description: 根据配置生成的SQL,生成同步数据sql语句并放入临时同步数据表
	 * @author gezhiling
	 * @date 2017-7-10 下午5:46:12
	 * @param sqlConfig
	 */
	public void createSql(String sqlConfig, SEntity userConfig,String dbType){
			//提取数据源名称
			String tableName = userConfig.getValueAsString("table_view_name");
			//用户
			String userId = userConfig.getValueAsString("user_id");
			//
			StringBuffer sb = new StringBuffer(" SELECT * FROM (SELECT RANK() OVER (ORDER BY ID ASC) RK,T.* FROM ( ");
			sb.append(sqlConfig);
			sb.append(" ) T ORDER BY T.ID) ");
			String sbSql =	sb.toString();
			//
			StringBuffer sbCount = new StringBuffer(" SELECT COUNT(*) FROM (SELECT RANK() OVER (ORDER BY ID ASC) RK,T.* FROM ( ");
			sbCount.append(sqlConfig);
			sbCount.append(" ) T ORDER BY T.ID) ");
			int size = baseDao.loadUniqueResultAsInteger(sbCount.toString(),null);
			System.out.println("size:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::"+size);
			if (size > 0) {
				 //总条数
				int iTotalCount = size;
				//总页数
				int iCNt = iTotalCount % 100>0?iTotalCount / 100 + 1:iTotalCount / 100;
			     for (int j = 1; j <= iCNt; j++) {
			    	 String sSql = "  where  rk between " + ((j - 1) * 100 + 1) + " and " + (j * 100) + " order by rk asc";
			    	 SEntityList sEntityList = baseDao.search(sbSql+sSql, null);
	                 for (SEntity sEntity : sEntityList) {
	                     String sqlTable = "select column_name,data_type from user_tab_columns where table_name='"+tableName+"'  ";
	                     SEntityList tableFiled =baseDao.search(sqlTable, null);
	                     //数据状态
	                     String dataStatus = sEntity.getValueAsString("data_status");
	                     //根据用户本地数据库生成SQL
	                     //数据库类型
	                     String dataSql = "";
	                     if(DBType.mysql.getKey()==Integer.parseInt(dbType)){
	                    	 	dataSql = getDataSql4Mysql(tableFiled, sEntity, userConfig);
	                     }else if(DBType.oracle.getKey()==Integer.parseInt(dbType)){
	                    	 dataSql = getDataSql4Oracle(tableFiled, sEntity, userConfig);
	                     }else if(DBType.sqlserver.getKey()==Integer.parseInt(dbType)){
	                    	 //TODO
	                     }
	                     
	                     
	                     //把生成的Sql放入临时表
	                     SEntity tempInfo = new SEntity("db_temp_info");
	                     tempInfo.setValue("user_id", userId);
	                     tempInfo.setValue("user_info", dataSql);
	                     tempInfo.setValue("info_type", dataStatus);
	                     tempInfo.setValue("user_config_id", userConfig.getValueAsString("id"));
	                     baseDao.save(tempInfo);
	                     //当前数据的同步时间 放入配置表下次从当前时间更新数据
	                     SEntity dbUserConfig = new SEntity("db_user_config");
	                     dbUserConfig.setValue("id", userConfig.getValueAsString("id"));
	                     dbUserConfig.setValue("time", sEntity.getValueAsString("data_time"));
	                     baseDao.update(dbUserConfig);
	                 }
			     }
		 }
	}
	
	
	/**
	 * 
	* @Title: getDataSql4Mysql
	* @Description: 返回MySQL语句
	* @author gezhiling
	* @date  2017-7-18 下午5:40:18
	* @param tableFiled
	* @param sEntity
	* @param userConfig
	* @return
	 */
	private String  getDataSql4Mysql(SEntityList tableFiled,SEntity sEntity,SEntity userConfig){
		//目标数据库表名
		String totablename = userConfig.getValueAsString("totablename");
		//订阅字段
		String columns = userConfig.getValueAsString("columns");
		String column[] = columns.split(",");
		//字段
		StringBuffer sbFiled = new StringBuffer();
		//值
		StringBuffer sbValue = new StringBuffer();
		//duplicate key 语句拼装
		StringBuffer sbKey = new StringBuffer();
		
		//数据状态
        String dataStatus = sEntity.getValueAsString("data_status");
        
        
         //进行部分SQL拼装
		 for (int i = 0; i < tableFiled.size(); i++) {
			  //获取字段名称
			 String filed = tableFiled.get(i).getValueAsString("COLUMN_NAME");
			  //获取字段数据类型
			  String data_type = tableFiled.get(i).getValueAsString("DATA_TYPE");
			  //初始化数据格式
			  for (String col : column) {
			 	 if(col.equals(filed))
			 	 {
		              sbFiled.append(filed).append(",");
					  sbValue.append(ToolsUtil.parseObject4MySql(filed, data_type, sEntity)).append(",");
					  sbKey.append(filed + "=VALUES(" + filed + ")").append(",");
			     	 }
					}
		  }
		 String dataSql = "";
          if("insert".equals(dataStatus)||"update".equals(dataStatus)){
        	  //生成insert duplicate 语句 有则修改 无则插入
        	 String sbFiledStr = sbFiled.toString().substring(0,sbFiled.toString().length()-1);
        	 String sbValueStr = sbValue.toString().substring(0,sbValue.toString().length()-1);
        	 String sbKeyStr = sbKey.toString().substring(0,sbKey.toString().length()-1);
        	 dataSql = " insert into "+totablename+"(" + sbFiledStr + ") values ( " + sbValueStr + " ) on duplicate key update " + sbKeyStr;
         } else if("delete".equals(dataStatus)){
        	 String relation_fk_id = sEntity.getValueAsString("relation_fk_id");
        	 dataSql = "delete from "+totablename+" where id=" + sEntity.getValueAsString(relation_fk_id);
         }
		return dataSql;
	}
	
	/**
	 * 
	* @Title: getDataSql4Oracle
	* @Description: 返回Oracle语句
	* @author gezhiling
	* @date  2017-7-18 下午5:40:49
	* @param tableFiled
	* @param sEntity
	* @param userConfig
	* @return
	 */
	private String  getDataSql4Oracle(SEntityList tableFiled,SEntity sEntity,SEntity userConfig){
		//目标数据库表名
		String totablename = userConfig.getValueAsString("totablename");
		//订阅字段
		String columns = userConfig.getValueAsString("columns");
		String column[] = columns.split(",");
		//数据状态
        String dataStatus = sEntity.getValueAsString("data_status");
		//字段
		StringBuffer sbFiled = new StringBuffer();
		//值
		StringBuffer sbValue = new StringBuffer();
		// update set filed
		StringBuffer filedSet = new StringBuffer();
		 //进行部分SQL拼装
		 for (int i = 0; i < tableFiled.size(); i++) {
			  //获取字段名称
			 String filed = tableFiled.get(i).getValueAsString("COLUMN_NAME");
			  //获取字段数据类型
			  String data_type = tableFiled.get(i).getValueAsString("DATA_TYPE");
			  //初始化数据格式
			  for (String col : column) {
			 	 if(col.equals(filed))
			 	 {
			 		  //insert
		              sbFiled.append(filed).append(",");
		              sbValue.append(ToolsUtil.parseObject4Oracle(filed, data_type, sEntity)).append(",");
		              //udpate
		              filedSet.append("T1.");
		              filedSet.append(filed).append("=");
		              filedSet.append(ToolsUtil.parseObject4Oracle(filed, data_type, sEntity)).append(",");
		     	 }
			 }
		  }
		 
		 //组合sql 无则插入有则修改 ORACLE MERGE 用法
		 String primaryKey = sEntity.getValueAsString("id");
		 String sbFiledSql = sbFiled.toString().substring(0,sbFiled.toString().length()-1);
       	 String sbValueSql = sbValue.toString().substring(0,sbValue.toString().length()-1);
       	String filedSetSql = filedSet.toString().substring(0,filedSet.toString().length()-1);
       	 
		 StringBuffer unionSql = new StringBuffer(" MERGE INTO ").append(totablename);
		 unionSql.append(" T1 USING(SELECT ").append(sbFiledSql);
		 unionSql.append(" FROM ").append(totablename).append(" ) T2  ON(T1.id=T2.id) WHEN MATCHED THEN UPDATE SET ");
		 unionSql.append(filedSetSql).append(" WHERE T1.ID =  ").append(primaryKey);
		 unionSql.append(" WHEN NOT MATCHED THEN  INSERT(").append(sbFiledSql);
		 unionSql.append(" ) VALUES( ").append(sbValueSql);
		 
		 //返回结果sql
		 String dataSql = "";
		 if("insert".equals(dataStatus)||"update".equals(dataStatus)){
			 dataSql = unionSql.toString();
		 }else if("delete".equals(dataStatus)){
	       	 dataSql = "delete from "+totablename + "where id = "+primaryKey;
        }
		return dataSql;
	}
	
}
