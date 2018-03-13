package com.taoding.domain.subject;

import com.taoding.common.entity.DataEntity;

import lombok.Data;
import lombok.ToString;

/**
 * @author fc
 */

@Data
@ToString
public class CpaSubjectFormatSetting extends DataEntity<CpaSubjectFormatSetting> {

	//客户id
	private String customerId;
	
	//分隔符
	private String seperator;
	
	//最大级别
	private Integer maxLevel;
	
	//每一级长度
	private String lengthPerLevel;
}
