package com.taoding.common.utils;

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Logger;

/**   
 * @ClassName:  CacheMap   
 * @Description: 本地缓存类
 * @author: yaochenglong@taoding.cn 
 * @date:   2017年12月13日 上午11:52:18   
 *     
 * @Copyright: 2017 www.taoding.cn Inc. All rights reserved. 
 */
public class CacheMapUtils{

	private static Logger logger = Logger.getLogger(CacheMapUtils.class);

	private static CacheMapUtils cacheMap = null;

	private static Map<String, Object> currMap = new HashMap<String,Object>();

	private static Map<String, Object> cacheConfMap = new HashMap<String, Object>();

	private static byte[] lock = new byte[0];

	static {
		
		if (cacheMap == null) { 
			cacheMap = new CacheMapUtils();
			Thread t = new ClearCache();
			t.start();
		}
	}

	/*public static CacheMapUtils getInstance() {
		if (cacheMap == null) {
			cacheMap = new CacheMapUtils();
			Thread t = new ClearCache();
			t.start();
			return cacheMap;
		}
		return cacheMap;
	}*/


	/** 
	* 获取缓存数据
	*/
	public static Object getValue(String key) {
		Object ob = currMap.get(key);
		if (ob != null) {
			return ob;
		} else {
			return null;
		}
	}

	/**
	 * 将数据添加到缓存中<br/>
	 * CacheConfModel参数用于设定缓存数据的属性，{@link CacheConfModel}
	 */
	public  synchronized void add(String key, Object value, CacheConfModel ccm) {
		currMap.put(key, value);
		cacheConfMap.put(key, ccm);
	}

	/**
	 * 将数据添加到缓存中<br/>
	 * expired：过期时间 &nbsp;&nbsp;单位:秒
	 */
	public synchronized static void add(String key, Object value, long expired) {
		currMap.put(key, value);
		CacheConfModel ccm = new CacheConfModel();
		ccm.setBeginTime(new Date().getTime());
		ccm.setDurableTime(expired * 1000);
		ccm.setForever(false);
		cacheConfMap.put(key, ccm);
	}

	/**
	 * 将数据添加到缓存中，默认永久存储，没有过期时间<br/>
	 * CacheConfModel参数用于设定缓存数据的属性，{@link CacheConfModel}
	 */
	public  synchronized void add(String key, Object value) {
		currMap.put(key, value);
		CacheConfModel ccm = new CacheConfModel();
		ccm.setBeginTime(new Date().getTime());
		ccm.setForever(true);
		cacheConfMap.put(key, ccm);
	}

	/**
	 * 更新缓存
	 *
	 */
	public  synchronized void update(String key, Object value) {
		currMap.put(key, value);
	}

	/**   
	 * @Title: removeCusCache   
	 * @Description: 根据key移除缓存
	 * @param: @param key      
	 * @return: void      
	 * @throws   
	 */
	public  void removeCusCache(String key) {
		synchronized (lock) {
			currMap.remove(key);
			cacheConfMap.remove(key);
		}
	}

	/**   
	 * @ClassName:  ClearCache   
	 * @Description:启动一个线程定时清除缓存，如果满足清除条件，则清除，不满足，保留。
	 * @author: yaochenglong@taoding.cn 
	 * @date:   2017年12月13日 下午2:16:40   
	 *     
	 * @Copyright: 2017 www.taoding.cn Inc. All rights reserved. 
	 */
	private static class ClearCache extends Thread {
		@Override
		public void run() {
			while (true) {
				Set<String> tempSet = new HashSet<String>();
				Set<String> set = cacheConfMap.keySet();
				Iterator<String> it = set.iterator();
				while (it.hasNext()) {
					String key = it.next();
					CacheConfModel ccm = (CacheConfModel) cacheConfMap.get(key);
					// 比较是否需要清除
					if (!ccm.isForever()) {
						if ((new Date().getTime() - ccm.getBeginTime()) >= ccm.getDurableTime() * 60 * 1000) {
							// 可以清除，先记录下来
							tempSet.add(key);
						}
					}
				}

				// 真正清除
				Iterator<String> tempIt = tempSet.iterator();
				while (tempIt.hasNext()) {
					Object key = tempIt.next();
					currMap.remove(key);
					cacheConfMap.remove(key);

				}
				logger.info("now thread================>" + currMap.size());
				// 休息
				try {
					Thread.sleep(10 * 1000L);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
