package com.dc.platform;

import org.apache.jcs.JCS;
import org.apache.jcs.access.exception.CacheException;
import org.apache.log4j.Logger;

/**
 * @company 东昌数码
 * <p>
 * Copyright: Copyright (c) 2013
 * </p>
 * @author sslin
 * @date 2013-6-9 下午4:00:43
 * @description : 缓存管理
 */
public class SCache {
	
	static Logger log = Logger.getLogger(SCache.class);
	
    private static JCS cache = null;
    static{
    	try {
    		cache = JCS.getInstance("DC");
		} catch (CacheException e) {
			e.printStackTrace();
			log.error( "Problem initializing cache for region name [city].", e );
		}
    }
    public static void put(Object key,Object value){
    	try {
			cache.put(key,	value);
		} catch (CacheException e) {
			e.printStackTrace();
		}
    }
    public static Object get(Object id){
    	return cache.get(id);
    }
    public static void main(String[] args) {
    	SCache.put("s", 2134324);
    	System.out.println(SCache.get("s"));
	}
}


