package com.taoding.domain.customerTaxItem;

import java.util.Date;

import lombok.Data;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import com.taoding.common.annotation.ValidationBean;
import com.taoding.common.entity.DataEntity;
import com.taoding.domain.accountingbook.AccountingBook;

@ValidationBean
@Data
public class TaxValueAddedDetail extends DataEntity<TaxValueAddedDetail> {
	@Length(min = 1, max = 64, message = "账薄长度必须介于 1 和 64 之间")
	private String accountingId;// 账薄id
	private Double taxRate;// 税率
	@NotEmpty
	@Length(min = 1, max = 64, message = "关联销项税目长度必须介于 1 和 64 之间")
	private String outputSubjectId;// 关联销项税目
	@NotEmpty
	@Length(min = 1, max = 64, message = "关联未交增值税科目长度必须介于 1 和 64 之间")
	private String notPayVat;// 关联未交增值税科目
	@NotEmpty
	@Length(min = 1, max = 64, message = "关联收入科目长度必须介于 1 和 64 之间")
	private String inputSubjectId;// 关联收入科目
	@NotEmpty
	@Length(min = 1, max = 64, message = "关联成本科目长度必须介于 1 和 64 之间")
	private String costSubjectId;// 关联成本科目
	private Double individualExemption;// 单独免税政策

	/*
	 * 扩充字段
	 */
 
	private String outputSubjectName;// 关联销项税目 
	private String notPayVatName;// 关联未交增值税科目 
	private String inputSubjectName;// 关联收入科目 
	private String costSubjectName;// 关联成本科目
	

}
