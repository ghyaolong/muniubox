package com.taoding.domain.atEnterpriseInfo;

import java.util.Date;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import com.taoding.common.annotation.ValidationBean;
import com.taoding.common.entity.DataEntity;
import com.taoding.common.excel.ExcelField;

import lombok.Data;
import lombok.ToString;

/**
 * 企业信息Entity
 * @author csl
 * @version 2017-11-9 16:50:44
 */
@Data
@ToString
@ValidationBean
public class AtEnterpriseInfo extends DataEntity<AtEnterpriseInfo> {
	
	private static final long serialVersionUID = 1L;
	
	@Length(min=0, max=64, message="公司编码长度必须介于 0 和 64 之间")
	private String companyCode;		// 公司编码
	
	@NotNull(message="公司名称不能为空！")
	@Length(min=0, max=64, message="公司名称长度必须介于 0 和 64 之间")
	@ExcelField(title="公司名称", align=2, sort=10)
	private String companyName;		// 公司名称
	
	@Length(min=0, max=64, message="公司规模长度必须介于 0 和 64 之间")
	@ExcelField(title="公司规模", align=2, sort=20)
	private String companyScale;	// 公司规模
	
	@Length(min=0, max=64, message="公司账号长度必须介于 0 和 64 之间")
	@ExcelField(title="公司账号", align=2, sort=110)
	private String companyAccount;		// 公司账号
	
	@Length(min=0, max=64, message="客户数量长度必须介于 0 和 64 之间")
	@ExcelField(title="客户数量", align=2, sort=90)
	private String customerNum;		// 客户数量
	
	@Length(min=0, max=6, message="核名认证长度必须介于 0 和 6 之间")
	private String isNameAutherticate;		// 核名认证
	
	@Length(min=0, max=6, message="状态长度必须介于 0 和 6 之间")
	@ExcelField(title="公司状态", align=2, sort=100)
	private String companyState;		// 公司状态

	private String queryCondition;//查询条件;
	private String enterpriseMarking; //企业标识
	
	private AtLegalPersonInfo atLegalPersonInfo; //法人类,通过实体类查询其下面的属性
	private AtEnterpriseInfoDetail atEnterpriseInfoDetail; //公司详细信息实体
	private AtLinkman atLinkman; //联系人实体
	
	public AtLegalPersonInfo getAtLegalPersonInfo() {
		return atLegalPersonInfo;
	}
	public void setAtLegalPersonInfo(AtLegalPersonInfo atLegalPersonInfo) {
		this.atLegalPersonInfo = atLegalPersonInfo;
	}
	public AtEnterpriseInfoDetail getAtEnterpriseInfoDetail() {
		return atEnterpriseInfoDetail;
	}
	public void setAtEnterpriseInfoDetail(AtEnterpriseInfoDetail atEnterpriseInfoDetail) {
		this.atEnterpriseInfoDetail = atEnterpriseInfoDetail;
	}
	public AtLinkman getAtLinkman() {
		return atLinkman;
	}
	public void setAtLinkman(AtLinkman atLinkman) {
		this.atLinkman = atLinkman;
	}

	//导出AtEnterpriseInfoDetail表中的公司电话
	@ExcelField(title="公司电话", align=2, sort=30,value="atEnterpriseInfoDetail.companyPhone")
	public AtEnterpriseInfoDetail getAtEnterpriseInfoDetailPhone() {
		return atEnterpriseInfoDetail;
	}
	
	//导出AtEnterpriseInfoDetail表中的公司地址
	@ExcelField(title="公司地址", align=2, sort=80,value="atEnterpriseInfoDetail.companyAddress")
	public AtEnterpriseInfoDetail getAtEnterpriseInfoDetailAddress() {
		return atEnterpriseInfoDetail;
	}
	
	//导出AtEnterpriseInfoDetail表中的公司传真
	@ExcelField(title="公司传真", align=2, sort=50,value="atEnterpriseInfoDetail.companyFax")
	public AtEnterpriseInfoDetail getAtEnterpriseInfoDetailFax() {
		return atEnterpriseInfoDetail;
	}
	
	//导出AtEnterpriseInfoDetail表中的公司所属行业
//	@ExcelField(title="所属行业", dictType="at_trades_type", align=2, sort=70,value="atEnterpriseInfoDetail.industryInvolved")
	@ExcelField(title="所属行业",align=2,sort=70,value="atEnterpriseInfoDetail.industryInvolved")
	public AtEnterpriseInfoDetail getAtEnterpriseInfoDetailIndustry() {
		return atEnterpriseInfoDetail;
	}
	
	//导出AtEnterpriseInfoDetail表中的公司邮箱
	@ExcelField(title="公司邮箱", align=2, sort=40,value="atEnterpriseInfoDetail.companyMail")
	public AtEnterpriseInfoDetail getAtEnterpriseInfoDetailMail() {
		return atEnterpriseInfoDetail;
	}
	
	//导出AtLegalPersonInfo表中的法人姓名
	@ExcelField(title="法人", align=2, sort=60,value="atLegalPersonInfo.legalPersonName")
	public AtLegalPersonInfo getAtLegalPersonInfoName() {
		return atLegalPersonInfo;
	}
	
	//导出保存添加后的日期
	@ExcelField(title="创建时间", align=2, sort=120)
	public Date getCreateDate() {
		return createDate;
	}
	
}