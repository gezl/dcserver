package com.dc.platform.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.dc.platform.SEntity;
import com.dc.platform.permission.Module;

/**
 * @company 东昌数码
 *          <p>
 *          Copyright: Copyright (c) 2013
 *          </p>
 * @author sslin
 * @date 2013-12-3 下午2:12:05
 * @description : 系统工具
 */
public class SUtil {
	
	static Logger log = Logger.getLogger(SUtil.class);

	private  final static double p=6;
	/**
	 * 判断当前字符串是否为空或为空字符串
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isEmptyString(String str) {
		if (str == null || "".equals(str.trim())) {
			return true;
		}
		return false;
	}

	public static String getTypeCheck(String type) {
		if ("java.lang.Integer".equals(type) || "java.lang.Long".equals(type)) {
			return " digits";
		}
		if ("java.lang.Float".equals(type) || "java.lang.Double".equals(type)) {
			return " number";
		}
		/*
		 * String jType = null; switch (type) { case Types.VARCHAR: case
		 * Types.CHAR: jType = "java.lang.String"; break; case Types.BOOLEAN:
		 * jType = "java.lang.Boolean"; break; case Types.INTEGER: case
		 * Types.SMALLINT: case Types.TINYINT: jType = "java.lang.Integer";
		 * break; case Types.BIGINT: jType = "java.lang.Long"; break; case
		 * Types.FLOAT: jType = "java.lang.Float"; break; case Types.DECIMAL:
		 * jType = "java.lang.Integer"; if(digits > 0){ jType =
		 * "java.lang.Double"; }else if(size > 10){ jType = "java.lang.Long"; }
		 * break; case Types.NUMERIC: case Types.DOUBLE: jType =
		 * "java.lang.Double"; break; case Types.DATE: jType = "java.sql.Date";
		 * break; case Types.TIME: jType = "java.sql.Time"; break; case
		 * Types.TIMESTAMP: jType = "java.sql.Timestamp"; break; case
		 * Types.LONGVARCHAR: case Types.CLOB: jType = "CLOB"; break; case
		 * Types.BINARY: case Types.VARBINARY: case Types.LONGVARBINARY: case
		 * Types.BLOB: jType = "BLOB"; break; case Types.OTHER: jType =
		 * "java.lang.Object"; break; default: throw new
		 * RuntimeException("无法处理的类型，" + getJdbcTypeName(type)); } return jType;
		 */
		return "";
	}

	/**
	 * 获取文本或文本域验证方式
	 * 
	 * @param module
	 * @param dmlColFormat
	 * @param type
	 * @return
	 */
	public static String checkClass(Module module, SEntity dmlColFormat,
			Integer type) {
		String dwzClass = "";
		if (1 == type || 2 == type) {
			Integer checkClass = dmlColFormat.getValueAsInteger("checkclass");
			if (checkClass != null) {
				String isrequired = "";
				if (1 == dmlColFormat.getValueAsInteger("isRequired"))
					isrequired = " required ";
				switch (checkClass) {
				// 只读验证
				case 1:
					dwzClass = " readonly";
					break;
				// 必填电子邮件验证
				case 2:
					dwzClass = " class=\" " + isrequired + " email\"";
					break;
				// 电话验证
				case 3:
					dwzClass = " class=\" " + isrequired + " phone\"";
					break;
				// 密码验证
				case 4:
					dwzClass = "type=\"password\" class=\" "
							+ isrequired
							+ "alphanumeric\" id=\"pwd\" minlength=\"6\" maxlength=\"20\" ";
					break;
				// 重复输入密码验证
				case 5:
					dwzClass = " class=\"" + isrequired
							+ "\" type=\"password\" equalto=\"#pwd\"";
					break;
				// 整数验证
				case 6:
					dwzClass = " class=\"" + isrequired + " digits\"";
					if (!SUtil.isEmptyString(dmlColFormat
							.getValueAsString("minnum")))
						dwzClass += " min=\""
								+ dmlColFormat.getValueAsInteger("minnum")
								+ "\"";
					if (!SUtil.isEmptyString(dmlColFormat
							.getValueAsString("maxnum")))
						dwzClass += " max=\""
								+ dmlColFormat.getValueAsInteger("maxnum")
								+ "\"";
					break;
				// 浮点数验证
				case 7:
					dwzClass = " class=\"" + isrequired + " number\"";
					if (!SUtil.isEmptyString(dmlColFormat
							.getValueAsString("minnum")))
						dwzClass += " min=\""
								+ dmlColFormat.getValueAsInteger("minnum")
								+ "\"";
					if (!SUtil.isEmptyString(dmlColFormat
							.getValueAsString("maxnum")))
						dwzClass += " max=\""
								+ dmlColFormat.getValueAsInteger("maxnum")
								+ "\"";
					break;
				// 信用卡
				case 8:
					dwzClass = " class=\"" + isrequired + " creditcard\"";
					break;
				// 字母数字下划线
				case 9:
					dwzClass = " class=\"" + isrequired + " alphanumeric\"";
					break;
				// 字母 验证
				case 10:
					dwzClass = " class=\"" + isrequired + " lettersonly\"";
					break;
				// 网址 验证
				case 11:
					dwzClass = " class=\"" + isrequired + " url\"";
					break;
				// 后台验证
				case 12:
					dwzClass = "remote=\"s?md=" + module.getSn()
							+ "&ac=checkUnique&checkName="
							+ dmlColFormat.getValueAsString("fkcolumn") + "\"";
					if (!SUtil.isEmptyString(isrequired))
						dwzClass += " class=\"" + isrequired + "\"";
					break;
				default:
					break;
				}
			}
		}
		return dwzClass;
	}

	/*
	 * 判断是否为数字
	 * 
	 * @return 是数字返回true,否则返回false
	 */
	public final static boolean isNumeric(String s) {
		if (s != null && !"".equals(s.trim()))
			return s.matches("^[-0-9]*");
		else
			return false;
	}

	// 增加或减少天数
	public static Date addDay(Date date, int num) {
		Calendar startDT = Calendar.getInstance();
		startDT.setTime(date);
		startDT.add(Calendar.DAY_OF_MONTH, num);
		return startDT.getTime();
	}
    /**
     * 格式化日期
     * @param date
     * @param pattern
     * @return
     */
	public static String getFormatDate(Date date, String pattern) {
		if (date == null || date.equals(""))
			date = new Date();// 当前时间
		if (pattern == null || pattern.equals(""))
			pattern = "yyyy-MM-dd HH:mm:ss";
		SimpleDateFormat format = new SimpleDateFormat(pattern);
		return format.format(date);
	}
	
	/**
	 * 字符串转日期
	 */
	public static Date getParseDate(String sDate, String sPattern) {
		
		if (sDate == null || sDate.equals(""))
			sDate =getFormatDate(null, null) ;// 当前时间
		if (sPattern == null || sPattern.equals(""))
			sPattern = "yyyy-MM-dd HH:mm:ss";
		SimpleDateFormat format = new SimpleDateFormat(sPattern);
		try {
			return format.parse(sDate);
		} catch (ParseException e) {			
			e.printStackTrace();
			return null;
		}
		
	}
	
	/**
	 * 获取任意时间下一天的时间
	 * @param d
	 * @return
	 * @throws ParseException 
	 */
	public static String getNextDate(String t){
		Date d;
		try {
			d = new SimpleDateFormat("yyyy-MM-dd").parse(t);
			long addTime = 1;
			addTime *= 1;
			addTime *= 24;
			addTime *= 60;
			addTime *= 60;
			addTime *= 1000;
			Date date = new Date(d.getTime() + addTime);
			return new SimpleDateFormat("yyyy-MM-dd").format(date);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "";
		}
		
	}
	/**
	 * 获取任意时间上一天的时间
	 * @param d
	 * @return
	 * @throws ParseException 
	 */
	public static String getbeforeDate(String t){
		Date d;
		try {
			d = new SimpleDateFormat("yyyy-MM-dd").parse(t);
			long addTime = 1;
			addTime *= 1;
			addTime *= 24;
			addTime *= 60;
			addTime *= 60;
			addTime *= 1000;
			Date date = new Date(d.getTime() - addTime);
			return new SimpleDateFormat("yyyy-MM-dd").format(date);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "";
		}
		
	}
	/**
	 * 获取当前时间前一天的时间
	 * @param d
	 * @return
	 * @throws ParseException 
	 */
	public static String getBeforeDate(){
		Date d;
		d = new Date();
		long addTime = 1;
		addTime *= 1;
		addTime *= 24;
		addTime *= 60;
		addTime *= 60;
		addTime *= 1000;
		Date date = new Date(d.getTime() - addTime);
		return new SimpleDateFormat("yyyy-MM-dd").format(date);
		
	}
	/**
	 * 获取当前时间下一天的时间
	 * @param d
	 * @return
	 * @throws ParseException 
	 */
	public static String getAfterDate(){
		Date d;
		d = new Date();
		long addTime = 1;
		addTime *= 1;
		addTime *= 24;
		addTime *= 60;
		addTime *= 60;
		addTime *= 1000;
		Date date = new Date(d.getTime() + addTime);
		return new SimpleDateFormat("yyyy-MM-dd").format(date);
		
	}
	/**
	 * 获取本周的开始时间
	 */
	public static Date getWeekStart() {// 当周开始时间
		Calendar currentDate = Calendar.getInstance();
		currentDate.setFirstDayOfWeek(Calendar.MONDAY);
		currentDate.set(Calendar.HOUR_OF_DAY, 0);
		currentDate.set(Calendar.MINUTE, 0);
		currentDate.set(Calendar.SECOND, 0);
		currentDate.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
		return (Date) currentDate.getTime();
	}

	/**
	 * 获取本周的结束时间
	 */
	public static Date getWeekEnd() {// 当周结束时间
		Calendar currentDate = Calendar.getInstance();
		currentDate.setFirstDayOfWeek(Calendar.MONDAY);
		currentDate.set(Calendar.HOUR_OF_DAY, 23);
		currentDate.set(Calendar.MINUTE, 59);
		currentDate.set(Calendar.SECOND, 59);
		currentDate.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
		return (Date) currentDate.getTime();
	}
   	/**
   	 * 去除string重复元素
   	 * @param str 需处理字符
   	 * @param symbol 分割符号
   	 * @return
   	 */
	public static String removeSameData(String str,String symbol){
		if(!SUtil.isEmptyString(str)){
			List<String> sList = new ArrayList<String>();
			String []strArray = str.split(symbol);
			for(int i=0;i<strArray.length;i++){
				if(!sList.contains(strArray[i]))
					sList.add(strArray[i]);
			}
			str="";
			for(int i=0;i<sList.size();i++){
				str+=sList.get(i);
				if(i<sList.size()-1)
					str+=symbol;
			}
		}
		return str;
	}
	/**  
	 * 得到本月的第一天  
	 * @return  
	 */  
	public static String getMonthFirstDay() {   
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Calendar c = Calendar.getInstance();    
        c.add(Calendar.MONTH, 0);
        c.set(Calendar.DAY_OF_MONTH,1);//设置为1号,当前日期既为本月第一天 
        return format.format(c.getTime());  
	}   
	/**
	 * 获取下月第一天
	 * @return
	 */
	public static String getNextMonthFirstDay(){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Calendar c = Calendar.getInstance();
		c.add(Calendar.MONTH, 1);
		c.set(Calendar.DAY_OF_MONTH,1);
		return sdf.format(c.getTime());
	}
	/**  
	 * 得到本月的最后一天  
	 *   
	 * @return  
	 */  
	public static String getMonthLastDay() {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd"); 
		Calendar ca = Calendar.getInstance();    
        ca.set(Calendar.DAY_OF_MONTH, ca.getActualMaximum(Calendar.DAY_OF_MONTH));  
	    return format.format(ca.getTime());   
	}   
	/**
	 * 获取上月
	 * @return(yyyy-MM)
	 */
	public static String getMonthBefore(){
		Calendar c = Calendar.getInstance();
		c.add(Calendar.MONTH, -1);
	    SimpleDateFormat format =  new SimpleDateFormat("yyyy-MM");
		return format.format(c.getTime());
	}
	 /** 获取上月
	 * @return(yyyy-MM)
	 */
	public static String getMonthBeforeV(int iN){
		Calendar c = Calendar.getInstance();
		c.add(Calendar.MONTH, iN);
	    SimpleDateFormat format =  new SimpleDateFormat("yyyy-MM");
		return format.format(c.getTime());
	}
	
	/**
	 * 获取本月
	 * @return(yyyy-MM)
	 */
	public static String getMonthNow(){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
		return sdf.format(new Date());	
	}
	
	public  static String getDoubleValue(double number){
		BigDecimal bd = new BigDecimal(number);
		bd.setScale(2);
		return bd.toPlainString();
	}
	/**
	 * 读取url返回数据
	 * @param _url
	 * @return
	 */
	public  static String readContentByUrL(String _url){
		StringBuffer sb = new StringBuffer();
		String str = "";
		try {
			URL url = new URL(_url);
			BufferedReader in = new BufferedReader(new InputStreamReader(url
					.openStream(), "GB2312"));	
			while ((str = in.readLine()) != null) {
				sb.append(str);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return sb.toString();
	}
	
	
	 /**
     * 获取gisUrl
     * @param name
     * @return
     */
	public static String getProperty(String name) {
		InputStream inputStream = SUtil.class
				.getResourceAsStream("/config.properties");
		Properties p = new Properties();
		try {
			p.load(inputStream);
			inputStream.close();
		} catch (IOException  e) {
			e.printStackTrace();
		}
		return p.getProperty(name);
	}
	/**
	 * 获取日期差，返回相差天数。  
	 * @param startDate
	 * @param endDate
	 * @return
	 * @throws ParseException
	 */
	public static long getCompareDate(String startDate,String endDate) throws ParseException {  
	     SimpleDateFormat formatter=new SimpleDateFormat("yyyy-MM-dd");  
	     Date date1=formatter.parse(startDate);      
	     Date date2=formatter.parse(endDate);  
	     long l = date2.getTime() - date1.getTime();  
	     long d = l/(24*60*60*1000);  
	     return d;  
	 } 
	
	/**
	 * 美元兑人民币
	 */
	public static String dollarsToRMB(String dAmount) throws ParseException {  
	    return  new BigDecimal(dAmount).multiply(new BigDecimal(p)).toPlainString();
	}
	
	/**
	 * 人民币兑美元
	 */
	public static String RMBToDollars(String dAmount) throws ParseException {  
	    return new BigDecimal(dAmount).divide(new BigDecimal(p),3,BigDecimal.ROUND_HALF_UP).toPlainString();
	}
	
	public static String formatAmount(String sAmount){
		  if(sAmount==null || sAmount.equals("")){
			  sAmount="0";
		  }
		  return sAmount;
	 }
	/**
	 * 根据时分得到对应时间
	 * 如果此时间今天已经经过，返回明天的此时间
	 * @param hour
	 * @param minute
	 * @return
	 */
	public static Calendar getIfOverDayCalendar(int hour,int minute){
		Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        calendar.set(year, month, day, hour, minute, 00);
        
        if(calendar.compareTo(Calendar.getInstance())<=0){
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }
        return calendar;
	}	
	 
	
	/**
	 * 获取当前时间
	 * @return
	 */
	public static String getNowTimeString(){
		return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
	}
	
	/**
	 * 中文字符转成英文字符
	 * @param str
	 * @return
	 */
	public static String convertStrCn2En(String str){
		String comma_Cn = "，"; 
		String comma_En = ","; 
		log.info("原来["+str+"]");
		String result = org.springframework.util.StringUtils.replace(str, comma_Cn, comma_En).trim();
		if(!isEmptyString(str)&&!str.equals(result)){
			 //跟踪日志用户操作错误的垛位的情况，暂时日志
			 log.info("装车模块垛位pile过滤操作，原来["+str+"],改为["+result+"]");
		}
		return result;
	}
	
	/**
	 * 格式化数字前置补0
	 * @param str
	 * @return
	 */
	public static String decimalFormat(Object o,String sFormat){
		DecimalFormat _df = new DecimalFormat(sFormat);
	    return _df.format(o);
	}
}
