package com.dc.server.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import org.apache.commons.lang3.StringUtils;

import com.dc.platform.SEntity;

public class ToolsUtil {
	 /**
     * 字符串数组==》以符号间隔的字符串
     * @param join 间隔符号
     * @param strAry 字符串数组
     * @return
     */
    public static String join(String join, String[] strAry) {

    	if(strAry==null) return "";
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < strAry.length; i++) {
            if (i == (strAry.length - 1)) {
                sb.append(strAry[i]);
            } else {
                sb.append(strAry[i]).append(join);
            }
        }
        return StringUtils.isNotBlank(sb)? new String(sb):"";
    }
    
    /**
     * 
    * @Title: getValueStr
    * @Description:  根据数据类型转换成MySQL数据库类型
    * @param columnName
    * @param data_type
    * @param entity
    * @return
     */
    public static String parseObject4MySql(String columnName, String data_type, SEntity entity) {

        String value = "";
        String columnValue = StringUtils.isBlank(entity.getValueAsString(columnName)) ? null : entity.getValueAsString(columnName);
        if (data_type.equals("VARCHAR2") || data_type.equals("DATE")) {
            if (StringUtils.isBlank(entity.getValueAsString(columnName)))
                value = null;
            else
                value = " '" + columnValue + "' ";
        } else {
            value = columnValue;
        }
        return value;
    }
    
    
    /**
     * 
    * @Title: parseObject4Oracle
    * @Description: 根据数据类型转换成Oracle数据库类型
    * @param data_type 数据类型
    * @param columnName 字段名称
    * @param  entity 当前实体对象
    * @return
     */
    public static String parseObject4Oracle(String columnName, String data_type, SEntity entity){
    	Object object = entity.getValue(columnName);
		String value = (String)object;
		 if(data_type.equalsIgnoreCase("java.lang.String")){
			return "'"+value+"'";
		}  else if(data_type.equalsIgnoreCase("java.sql.Date")||data_type.equalsIgnoreCase("java.sql.Timestamp")){
			return	StringUtils.isBlank(value)?value:"to_date('"+value+"','yyyy-MM-dd hh24:mi:ss')";
		}else{
			return value; 
		}
	}
    
    
    
    public static boolean createFile(String path,String fileName,String filecontent){
        Boolean bool = false;
       String  filenameTemp = "../webapps/cfdserver/WEB-INF/sql/"+fileName+".sql";//文件路径+名称+文件类型
        File file = new File(filenameTemp);
        try {
            //如果文件不存在，则创建新的文件
            if(!file.exists()){
                file.createNewFile();
                bool = true;
                System.out.println("success create file,the file is "+filenameTemp+"\r\n");
                //创建文件成功后，写入内容到文件里
                writeFileContent(filenameTemp, filecontent);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return bool;
    }
    
    
    public static boolean writeFileContent(String filepath,String newstr) throws IOException{
        Boolean bool = false;
        String filein = newstr+"\r\n";//新写入的行，换行
       // String temp  = "";
        
        FileInputStream fis = null;
        InputStreamReader isr = null;
        BufferedReader br = null;
        FileOutputStream fos  = null;
        PrintWriter pw = null;
        try {
            File file = new File(filepath);//文件路径(包括文件名称)
            //将文件读入输入流
            fis = new FileInputStream(file);
            isr = new InputStreamReader(fis);
//            br = new BufferedReader(isr);
//            StringBuffer buffer = new StringBuffer();
//            
//            //文件原有内容
//            for(int i=0;(temp =br.readLine())!=null;i++){
//                buffer.append(temp);
//                // 行与行之间的分隔符 相当于“\n”
//                buffer = buffer.append(System.getProperty("line.separator"));
//            }
//            buffer.append(filein);
//            
            fos = new FileOutputStream(file);
            pw = new PrintWriter(fos);
            pw.write(filein.toCharArray());
            pw.flush();
            bool = true;
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }finally {
            //不要忘记关闭
            if (pw != null) {
                pw.close();
            }
            if (fos != null) {
                fos.close();
            }
            if (br != null) {
                br.close();
            }
            if (isr != null) {
                isr.close();
            }
            if (fis != null) {
                fis.close();
            }
        }
        return bool;
    }
    
 
    
}
