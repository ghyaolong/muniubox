package com.taoding.domain.report.assetliability;

import com.taoding.common.entity.DataEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * 资产负债表的重分类配置
 * @author YangJiQiang
 *
 */

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class ReclassifySetting extends DataEntity<ReclassifySetting>{

	private static final long serialVersionUID = 5126985449326990753L;

	//客户Id
	private String customerId;
	
	//应收账款和预收账款
	private Boolean yingshouAndYushou;
	
	//应付账款和预付账款
	private Boolean yingfuAndYuFu;
	
	//其它应收款和其它应付
	private Boolean otherYingshouAndYingfu;
}
