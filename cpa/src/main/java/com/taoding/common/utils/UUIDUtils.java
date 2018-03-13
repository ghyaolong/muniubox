package com.taoding.common.utils;

import java.util.UUID;

/**
 * UUid工具类
 * 
 * @author hmily
 * @version 2017-11-23
 */
public class UUIDUtils {

	public static String getUUid() {
		return UUID.randomUUID().toString().replaceAll("-", "");
	}

}
