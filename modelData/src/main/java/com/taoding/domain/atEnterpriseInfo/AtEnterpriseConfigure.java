package com.taoding.domain.atEnterpriseInfo;

import org.hibernate.validator.constraints.Length;

import com.taoding.common.annotation.ValidationBean;
import com.taoding.common.entity.DataEntity;

import lombok.Data;
import lombok.ToString;

/**
 * 企业配置信息Entity
 * @author mhb
 * @version 2017-10-26
 */
@Data
@ToString
@ValidationBean
public class AtEnterpriseConfigure extends DataEntity<AtEnterpriseConfigure> {
	
	private static final long serialVersionUID = 1L;
	
	@Length(min=0, max=100, message="at_enterprise_info_id长度必须介于 0 和 100 之间")
	private String atEnterpriseInfoId;		// at_enterprise_info_id
	
	@Length(min=0, max=100, message="url长度必须介于 0 和 100 之间")
	private String url;		// url
	
	@Length(min=0, max=100, message="attribute_01长度必须介于 0 和 100 之间")
	private String attribute01;		// attribute_01
	
	@Length(min=0, max=100, message="attribute_02长度必须介于 0 和 100 之间")
	private String attribute02;		// attribute_02
	
	@Length(min=0, max=100, message="attribute_03长度必须介于 0 和 100 之间")
	private String attribute03;		// attribute_03
	
	@Length(min=0, max=100, message="attribute_04长度必须介于 0 和 100 之间")
	private String attribute04;		// attribute_04
	
	@Length(min=0, max=100, message="enterprise_marking长度必须介于 0 和 100 之间")
	private String enterpriseMarking;		// enterprise_marking
	
	private AtEnterpriseInfo atEnterpriseInfo;
		
}