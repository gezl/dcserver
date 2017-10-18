package com.dc.platform.utils;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import oracle.sql.BLOB;
/**
 * 文件工具类
 * @author zl
 *
 */
public class FileUtils {
	static String  allowType []={"doc","docx","xls","xlsx","ppt","pptx","txt"};  //可预览文件类型数组
   /**
    * 可预览文件格式判断
    */
   public static boolean checkAllowFile(String fileType){
	    for(String s:allowType){
	    	if(s.equalsIgnoreCase(fileType))
	    		return true;
	    }
	    return false;
   }
   /**
	 * 文件转二进制流
	 * 
	 * @param file
	 * @return
	 */
	public static byte[] getFileToByte(InputStream is) {
		try {
			ByteArrayOutputStream bytestream = new ByteArrayOutputStream();
			byte[] bb = new byte[20480];
			int ch;
			ch = is.read(bb);
			while (ch != -1) {
				bytestream.write(bb, 0, ch);
				ch = is.read(bb);
			}
		return  bytestream.toByteArray();
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}

		
	}
	/**
	 * 将byte[]转成文件
	 * @param path
	 * @param content
	 * @throws IOException
	 */
	public static void createFile(String path, byte[] content) throws IOException {
		FileOutputStream fos = new FileOutputStream(path);
		fos.write(content);
		fos.close();
	}


 	/**
 	 * 将blob转化为byte[],可以转化二进制流的
 	 * 
 	 * @param blob
 	 * @return
 	 */
 	public static byte[] blobToBytes(BLOB blob) {
 		InputStream is = null;
 		byte[] b = null;
 		try {
 			if(blob!=null){
	 			is = blob.getBinaryStream();
	 			b = new byte[(int) blob.length()];
	 			is.read(b);
 			}
 			return b;
 		} catch (Exception e) {
 			e.printStackTrace();
 		} finally {
 			try {
 				if(is!=null)
 				is.close();
 				is = null;
 			} catch (IOException e) {
 				e.printStackTrace();
 			}
 		}
 		return b;
 	}
 	
 	/**
	 * 对字节流进行处理，word下载，pdf显示
	 * @param SEntity 文件对象
	 * @param type 文件类型 
	 * @return
	 */
//	public static void StreamOper(HttpServletResponse response, SEntity sEntity,String type)
//		throws IOException {
//		String mime=getMime(sEntity.getValueAsString("FILENAME"));
//		response.setContentType(mime);
//		String columnName=(type+"content").toUpperCase();
//		byte [] bytes=  FileUtils.blobToBytes(sEntity.getValueAsBLOB(columnName));
//		//word文档下载
//        if("doc".equals(type)){
//        	response.setHeader("Content-Disposition", "attachment; filename=" +URLEncoder.encode(sEntity.getValueAsString("FILENAME"), "UTF-8"));
//        }
//        if(bytes!=null){
//			OutputStream outStream = response.getOutputStream();
//			outStream.write(bytes, 0, bytes.length);
//			outStream.flush();
//			outStream.close();
//        }
//		}
}
