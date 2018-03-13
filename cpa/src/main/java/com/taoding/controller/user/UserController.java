 
package com.taoding.controller.user;


import java.io.IOException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.pagehelper.PageInfo;
import com.taoding.common.controller.BaseController;
import com.taoding.common.entity.BaseEntity;
import com.taoding.common.excel.ExportExcel;
import com.taoding.common.utils.DateUtils;
import com.taoding.domain.user.User;
import com.taoding.service.user.UserService;

@RestController
@RequestMapping(value = "/user")
public class UserController extends BaseController {
	
	@Autowired
	private UserService userService;
	
    /**
     * 用户列表
     * @return
     */
    @PostMapping("/listData")
    public Object listData(HttpServletRequest request)  {
		Map<String,Object> queryMap = getRequestParams(request);
    	PageInfo<User> pages = userService.findAllUserByPage(queryMap);
    	return pages;
    } 
    
    /**
     * 用户详细
     * @return
     */
    @GetMapping("/getInfo/{id}")
    public Object getUserInfo(@PathVariable("id") String id)  {
    	return userService.getUserInfo(id,BaseEntity.DEL_FLAG_NORMAL);
    } 
	
    /**
     * 用户启用/禁用
     * @param user
     * @return
     */
    @PutMapping("/updateLoginFlag")
	public Object updateLoginFlag(@RequestBody User user) {
    	return userService.updateUserLoginFlag(user);
	}
    
    /**
     * 用户导出
     * @return
     * @throws IOException 
     */
    @PostMapping("/exportData")
    public void exportData(HttpServletRequest request,HttpServletResponse response)  {
    	
		Map<String,Object> queryMap = getRequestParams(request);
    	PageInfo<User> pages = userService.findAllUserByPage(queryMap);
		String fileName = "用户数据" + DateUtils.getDate("yyyyMMddHHmmss") + ".xlsx";
		try {
			new ExportExcel("用户数据", User.class).setDataList(pages.getList()).write(response, fileName).dispose();
		} catch (IOException e) {
			e.printStackTrace();
		}
    } 
    
}
