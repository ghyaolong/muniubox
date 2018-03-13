package com.taoding.controller.dict;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.github.pagehelper.PageInfo;
import com.taoding.domain.dict.Dict;
import com.taoding.service.dict.DictService;

@RestController
public class DictController {

	@Autowired
	private DictService dictService;
	
	/**
	 * 获取所有的数据字典的数据
	 * @param dict
	 * @return page
	 * add csl
	 * 2017-11-8 11:55:33
	 */
	@GetMapping("/dict/list")
	public Object list(HttpServletRequest request) {
		Map<String,Object> queryMap = new HashMap<String, Object>();
		queryMap.put("pageNo", 1);
		queryMap.put("pageSize", 10);
		PageInfo<Dict> pages = dictService.findAllDictByPage(queryMap);
    	return pages;
	}
	

	/**
	 * 修改或者新增数据字典的数据
	 * @param dict
	 * @return
	 * add csl 
	 * 2017-11-8 13:35:07
	 */
	@PostMapping("/dict/save")// @PutMapping改
	public Object save(@RequestBody Dict dict) {
		dictService.save(dict);
		return true;
	}
	
	/**
	 * 删除数据字典的数据
	 * @param dict
	 * @return
	 * add csl 
	 * 2017-11-8 13:35:45
	 */
	@DeleteMapping("/dict/delete/{id}")
	public Object delete(@PathVariable("id") String id) {
		Dict dict =dictService.get(id);
		dictService.delete(dict);
		return true;
	}
}
