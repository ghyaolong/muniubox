package com.taoding.controller.dict;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.pagehelper.PageInfo;
import com.taoding.common.utils.StringUtils;
import com.taoding.domain.dict.Dict;
import com.taoding.service.dict.DictService;

@RestController
@RequestMapping(value = "/dict")
public class DictController {

	@Autowired
	private DictService dictService;
	
	/**
	 * 获取所有的数据字典的数据包含按照type准确查询和按照description模糊查询
	 * @param dict 
	 * @return pages
	 * add csl
	 * 2017-11-8 11:55:33
	 */
	@PostMapping("/list")
	public Object list(@RequestBody Map<String,Object> queryMap) {
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
	@PostMapping("/saveDict")
	public Object saveDict(@RequestBody Dict dict) {
		if (null==dict) {
			return false;
		}
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
	@DeleteMapping("/deleteDict/{id}")
	public Object deleteDict(@PathVariable("id") String id) {
		if (StringUtils.isBlank(id)) {
			return false;
		}
		Dict dict =dictService.get(id);
		dictService.delete(dict);
		return true;
	}
}
