package com.dc.platform.utils;

/**
 * doc docx格式转换
 * 
 * @author zl
 * 
 */
public interface FlexPaperAct{
	/**
	 * 将doc、xls、ppt等office文件转换为PDF
	 * @param bytes
	 * @param fileType
	 * @return
	 */
	public byte[] convertFileToPDF(byte[] bytes,String fileType);
	/**
	 * 将PDF文件转换为SWF，以此可以通过网页FALSH控件直接预览
	 * @param bytes
	 * @param fileType
	 * @return
	 */
	public byte[] convertPDFToSWF(byte[] bytes,String fileType);
}