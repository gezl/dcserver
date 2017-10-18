package com.dc.platform;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
/**
 * 
 * @company 北京东昌数码科技有限公司
 * <p>
 * Copyright: Copyright (c) 2014
 * </p>
 * @author sslin
 * @date 2014-12-3 下午5:12:40
 * @description : 统一管理系统配置项
 */
public class SystemConfig {

	private static Map<String, String> configs = new HashMap<String, String>();

	static{
		// 读取配置
		SAXReader saxReader = new SAXReader();
		Element rootElement = null;
		try {
			Document document = saxReader.read(SystemConfig.class.getResourceAsStream("../../../../SystemConfig.xml"));
			rootElement = document.getRootElement();
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		List<Element> elments = rootElement.elements();
		for (int i = 0; i < elments.size(); i++) {
			Element element = elments.get(i);
			String name = element.attributeValue("name");
			String value = element.getTextTrim();
			configs.put(name, value);
		}
	}
	public static String getConfig(String name){
		return configs.get(name);
	}
}
