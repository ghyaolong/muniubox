
package com.taoding.domain.atEnterpriseInfo;

import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;
import com.taoding.common.annotation.ValidationBean;
import com.taoding.common.entity.DataEntity;
import lombok.Data;
import lombok.ToString;

/**
 * 联系人信息Entity
 * @author csl
 * @version 2017-10-25
 */
@Data
@ToString
@ValidationBean
public class AtLinkman extends DataEntity<AtLinkman> {
	
	private static final long serialVersionUID = 1L;
	
	@Length(min=0, max=64, message="企业信息id长度必须介于 0 和 64 之间")
	private String atEnterpriseInfoId;		// 企业信息id
	
	@NotNull(message="联系人不能为空！")
	@Length(min=0, max=64, message="联系人长度必须介于 0 和 64 之间")
	private String linkmanName;		// 联系人
	
	@NotNull(message="联系电话不能为空！")
	@Length(min=0, max=64, message="联系电话长度必须介于 0 和 64 之间")
	private String linkmanPhone;		// 联系电话
	
	@Length(min=0, max=64, message="企业标识长度必须介于 0 和 64 之间")
	private String enterpriseMarking;		// 企业标识
	
	private AtEnterpriseInfo atEnterpriseInfo; //企业信息实体
	
}