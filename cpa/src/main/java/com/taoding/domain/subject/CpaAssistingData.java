package com.taoding.domain.subject;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;
import lombok.ToString;

/**
 * 辅助核算项提交分享数据Entity
 * 
 * @author czy 2017年11月23日 下午4:03:29
 */
@Data
@ToString
public class CpaAssistingData {

	private String id;
	
	private Integer assistingSize ;
	
	private List<CpaAssistingEntity> lists = new ArrayList<CpaAssistingEntity>();

}
