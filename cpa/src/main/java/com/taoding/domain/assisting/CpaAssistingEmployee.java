
package com.taoding.domain.assisting;

import java.util.Date;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.taoding.common.annotation.ValidationBean;
import com.taoding.common.entity.DataEntity;
import lombok.Data;
import lombok.ToString;

/**
 * 辅助核算模块员工Entity
 * @author csl
 * @version 2017-11-20
 */
@Data
@ToString
@ValidationBean
public class CpaAssistingEmployee extends DataEntity<CpaAssistingEmployee> {
	
	private static final long serialVersionUID = 1L;
	
	@Length(min=0, max=64, message="员工编号长度必须介于 0 和 64 之间")
	private String employeeNo;		// 员工编号
	
	@Length(min=1, max=100, message="姓名长度必须介于 1 和 100 之间")
	private String name;		// 姓名
	
	@Length(min=1, max=1, message="男（true）或者女(false)长度必须介于 1 和 1 之间")
	private String gender;		// 男（true）或者女(false)
	
	@Length(min=1, max=64, message="所属部门长度必须介于 1 和 64 之间")
	private String departmentId;		// 所属部门
	
	@Length(min=1, max=64, message="职位长度必须介于 1 和 64 之间")
	private String positionId;		// 职位
	
	@Length(min=0, max=20, message="手机号长度必须介于 0 和 20 之间")
	private String phone;		// 手机号
	
	@Email
	@Length(min=0, max=200, message="邮箱长度必须介于 0 和 200 之间")
	private String email;		// 邮箱
	
	@Length(min=1, max=20, message="城镇户口/农村户口长度必须介于 1 和 20 之间")
	private String hukou;		// 城镇户口/农村户口
	
	@Length(min=0, max=40, message="身份证号码长度必须介于 0 和 40 之间")
	private String identity;		// 身份证号码
	
	@Length(min=0, max=16, message="正式，正式和离职长度必须介于 0 和 16 之间")
	private String status;		// 正式，正式和离职
	
	@JsonFormat(pattern = "yyyy-MM-dd")
	private Date onBoardData;		// 入职时间
	
	@Length(min=0, max=64, message="银行ID长度必须介于 0 和 64 之间")
	private String bankId;		// 银行ID
	
	@Length(min=0, max=64, message="银行账号长度必须介于 0 和 64 之间")
	private String creditCard;  //银行账号
	
	@Length(min=1, max=64, message="账薄id长度必须介于 1 和 64 之间")
	private String accountId;		// 账薄id
	
	private CpaCustomerAssisting cpaCustomerAssisting;  //辅助核算类型
	
	//以下字段只是用来存数数据
	private String no;
}