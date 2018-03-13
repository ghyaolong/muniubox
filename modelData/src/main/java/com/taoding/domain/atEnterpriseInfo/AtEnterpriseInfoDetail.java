package com.taoding.domain.atEnterpriseInfo;

import java.util.Date;

import javax.validation.constraints.NotNull;

import lombok.Data;
import lombok.ToString;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.taoding.common.annotation.ValidationBean;
import com.taoding.common.entity.DataEntity;

/**
 * 企业详细信息管理Entity
 * @author csl
 * @version 2017-10-20
 */
@Data
@ToString
@ValidationBean
public class AtEnterpriseInfoDetail extends DataEntity<AtEnterpriseInfoDetail> {
	
	private static final long serialVersionUID = 1L;
	
	private String atEnterpriseInfoId;		// 企业id
	
	@Length(min=0, max=64, message="公司名称长度必须介于 0 和 64 之间")
	private String companyName;		// 公司名称
	
	@Length(min=0, max=64, message="公司电话长度必须介于 0 和 64 之间")
	private String companyPhone;		// 公司电话
	
	@Length(min=0, max=64, message="所属行业长度必须介于 0 和 64 之间")
	private String industryInvolved;		// 所属行业
	
	@Length(min=0, max=64, message="公司规模长度必须介于 0 和 64 之间")
	private String companyScale;		// 公司规模
	
	@Length(min=0, max=64, message="公司规模长度必须介于 0 和 64 之间")
	private String registeredAddress;		// 注册地址
	
	@Length(min=0, max=255, message="详细公司地址长度必须介于 0 和 255 之间")
	private String companyAddress;		// 详细公司地址
	
	@Length(min=0, max=64, message="记账会计id长度必须介于 0 和 64 之间")
	private String accountId;		// 记账会计id
	
	@Length(min=0, max=64, message="审核人id长度必须介于 0 和 64 之间")
	private String verifierId;		// 审核人id
	
	@Length(min=0, max=64, message="区域id长度必须介于 0 和 64 之间")
	private String regionId;		// 区域id
	
	@Length(min=0, max=64, message="统一社会信用代码长度必须介于 0 和 64 之间")
	private String socialCreditCode;		// 统一社会信用代码
	
	@Length(min=0, max=64, message="经营状态长度必须介于 0 和 64 之间")
	private String manageForm;		// 经营状态
	
	@Length(min=0, max=64, message="公司性质长度必须介于 0 和 64 之间")
	private String companyType;		// 公司性质
	
	private String registeredAssets;		// 注册资本
	
	@Length(min=0, max=64, message="公司网站长度必须介于 0 和 64 之间")
	private String companyWebsite;		// 公司网站
	
	@Length(min=0, max=64, message="公司传真长度必须介于 0 和 64 之间")
	private String companyFax;		// 公司传真

	@Length(min=0, max=64, message="纳税人性质长度必须介于 0 和 64 之间")
	private String taxpayeProperty;		// 纳税人性质
	
	@Length(min=0, max=64, message="企业标识长度必须介于 0 和 64 之间")
	private String enterpriseMarking;		// 企业标识
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date registerDate;		// 成立日期
	
	@NotNull(message="公司邮箱不能为空！")
	@Email(message="邮箱格式不正确")
	@Length(min=0, max=64, message="公司邮箱长度必须介于 0 和 64 之间")
	private String companyMail;		// 公司邮箱
	
	private AtEnterpriseInfo atEnterpriseInfo; //公司实体信息
	private AtLegalPersonInfo atLegalPersonInfo; //法人实体信息 
	
	
	
}