
package com.taoding.domain.atEnterpriseInfo;

import org.hibernate.validator.constraints.Length;
import com.taoding.common.annotation.ValidationBean;
import com.taoding.common.entity.DataEntity;
import lombok.Data;
import lombok.ToString;

/**
 * 法人信息Entity
 * @author Lin
 * @version 2017-09-28
 */
@Data
@ToString
@ValidationBean
public class AtLegalPersonInfo extends DataEntity<AtLegalPersonInfo> {
	
	private static final long serialVersionUID = 1L;
	
	private String atEnterpriseInfoId; // 企业id
	
	@Length(min=0, max=64, message="法人名称长度必须介于 0 和 64 之间")
	private String legalPersonName;		// 法人名称
	
	@Length(min=0, max=64, message="法人电话长度必须介于 0 和 64 之间")
	private String legalPersonPhone;		// 法人电话
	
	private String attribute2;		// attribute2
	private String attribute3;		// attribute3
	private String attribute4;		// attribute4
	private String attribute5;		// attribute5
	private String attribute6;		// attribute6
	
	private AtEnterpriseInfo atEnterpriseInfo;
	
	
}