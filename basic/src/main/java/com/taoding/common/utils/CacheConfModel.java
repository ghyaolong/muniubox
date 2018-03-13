package com.taoding.common.utils;

import java.io.Serializable;

import lombok.Data;

/**   
 * @ClassName:  CacheConfModel   
 * @Description 该类用于设定缓存数据的属性，<br/>
 * beginTime:缓存开始时间<br/>
 * durableTime:缓存持续时间,单位：秒<br/>
 * isForever:是否持久,如果为true，则数据永久存储在缓冲中
 * @author: yaochenglong@taoding.cn 
 * @date:   2017年12月13日 下午2:41:41   
 *     
 * @Copyright: 2017 www.taoding.cn Inc. All rights reserved. 
 */
@Data
public class CacheConfModel implements Serializable {

	/**   
	 * @Fields serialVersionUID : TODO
	 */
	private static final long serialVersionUID = -2352716615745296904L;
	
	private long beginTime;//缓存开始时间  
    private boolean isForever = false;//是否持久  
    private long durableTime;//持续时间  
    

}
