
package com.taoding.domain.salary;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import com.taoding.common.annotation.ValidationBean;
import com.taoding.common.entity.DataEntity;

import lombok.Data;
import lombok.ToString;

/**
 * 社保公积金缴纳项目表Entity
 * @author csl
 * @version 2017-11-24
 */
@Data
@ToString
@ValidationBean
public class CpaSalaryWelfareType extends DataEntity<CpaSalaryWelfareType> {
	
	private static final long serialVersionUID = 1L;
	
	@Length(min=1, max=200, message="社保公积金缴纳项目名长度必须介于 1 和 200 之间")
	@NotNull(message="社保公积金缴纳项目名不能为空！")
	private String name;		// 社保公积金缴纳项目名
	
	@Length(min=0, max=4, message="类型编号的目的：为了能免快速找到某一个社保项            1：养老保险；            2：医疗保险；            3：失业保险；            4：生育保险；            5：工商保险；            6：公积金长度必须介于 0 和 4 之间")
	private String no;		// 类型编号的目的：为了能免快速找到某一个社保项            1：养老保险；            2：医疗保险；            3：失业保险；            4：生育保险；            5：工商保险；            6：公积金
	
}