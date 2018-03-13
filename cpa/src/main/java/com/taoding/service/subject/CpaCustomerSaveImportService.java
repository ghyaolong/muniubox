package com.taoding.service.subject;

import java.util.List;

import com.taoding.domain.subject.CpaCustomerSubject;

public interface CpaCustomerSaveImportService {

	/**
	 * 保存导入后关联好的信息
	 * @author fc 
	 * @version 2017年12月22日 下午3:06:39 
	 * @return
	 */
	public boolean saveImport(List<CpaCustomerSubject> cpaCustomerSubjects);
}
