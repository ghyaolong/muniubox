package com.taoding.controller.user;

import java.io.IOException;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.pagehelper.PageInfo;
import com.taoding.common.controller.BaseController;
import com.taoding.common.entity.BaseEntity;
import com.taoding.common.excel.ExportExcel;
import com.taoding.common.utils.AjaxJson;
import com.taoding.common.utils.DateUtils;
import com.taoding.common.utils.ValidCodeUtils;
import com.taoding.domain.user.User;
import com.taoding.service.user.UserService;

@RestController
@RequestMapping(value = "/user")
public class UserController extends BaseController {

	@Autowired
	private UserService userService;

	/**
	 * 用户列表
	 * 
	 * @return
	 */
	@PostMapping("/listData")
	public Object listData(@RequestBody Map<String,Object> queryMap) {
		PageInfo<User> pages = userService.findAllUserByPage(queryMap);
		return pages;
	}

	/**
	 * 用户详细
	 * 
	 * @return
	 */
	@GetMapping("/getInfo/{id}")
	public Object getUserInfo(@PathVariable("id") String id) {
		return userService.getUserInfo(id, BaseEntity.DEL_FLAG_NORMAL);
	}

	//
	/**
	 * 用户启用/禁用
	 * 
	 * @param user
	 * @return
	 */
	@PostMapping("/updateLoginFlag")
	public Object updateLoginFlag(@RequestBody Map<String,Object> maps) {
		return userService.updateUserLoginFlag(maps);
	}

	/**
	 * 用户导出
	 * 
	 * @return
	 * @throws IOException
	 */
	@PostMapping("/exportData")
	public void exportData(@RequestBody Map<String,Object> queryMap,
			HttpServletResponse response) {
		
		PageInfo<User> pages = userService.findAllUserByPage(queryMap);
		String fileName = "用户数据" + DateUtils.getDate("yyyyMMddHHmmss")
				+ ".xlsx";
		try {
			new ExportExcel("用户数据", User.class).setDataList(pages.getList())
					.write(response, fileName).dispose();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**   
	 * @Title: getValidCode   
	 * @Description: 获取手机验证码,找回密码时候用
	 * @param: @param mobile
	 * @param: @return      
	 * @return: String      
	 * @throws   
	 */  
	@GetMapping(value="/getValidCode")
	public AjaxJson getValidCode(String mobile) {
		AjaxJson j = new AjaxJson();
		if(StringUtils.isNotEmpty(mobile)) {
			// XXX 判断手机是否存在
			/**
			 * User user = userService.getUserByMobile(mobile);
			 * if(user == null){
			 * 	  j.setSuccess(false);
			 * 	  j.setMsg("手机号不存在");
			 *    return j;
			 * }
			 */
			String validCode =  ValidCodeUtils.getValidCode(mobile);
			j.setData(validCode);
			logger.info(mobile+"的验证码是:"+validCode);
			return j;
		}
		return j;
	}
	
	/**   
	 * @Title: getValidCodeForReplaceMobile   
	 * @Description: 获取手机验证码，更换手机时候调用
	 * @param: @param mobile
	 * @param: @return      
	 * @return: AjaxJson      
	 * @throws   
	 */  
	@GetMapping(value="/getValidCodeForReplaceMobile")
	public AjaxJson getValidCodeForReplaceMobile(String mobile) {
		AjaxJson j = new AjaxJson();
		if(StringUtils.isNotEmpty(mobile)) {
			// XXX 判断手机是否存在
			/**
			 * User user = userService.getUserByMobile(mobile);
			 * if(user != null){
			 * 	  j.setSuccess(false);
			 * 	  j.setMsg("手机号已存在");
			 *    return j;
			 * }
			 */
			String validCode =  ValidCodeUtils.getValidCode(mobile);
			j.setData(validCode);
			logger.info(mobile+"的验证码是:"+validCode);
			return j;
		}
		return j;
	}
	
	/**   
	 * @Title: getValidCodeByKey   
	 * @Description: 测试使用，请勿删除
	 * @param: @param key
	 * @param: @return      
	 * @return: String      
	 * @throws   
	 */  
	/*@PostMapping("/getValidCodeByKey")
	public String getValidCodeByKey(String mobile) {
		String validCode = CacheMapUtils.getInstance().getValue(key).toString();
		CacheMapUtils cacheMapUtils = CacheMapUtils.getInstance();
		Object o  = cacheMapUtils.getValue(mobile);
		String validCode = null;
		if(null != o) {
			validCode = o.toString();
		}
		logger.info("从缓冲中获取["+mobile+"]的验证码是："+validCode);
		return validCode;
	}*/
	
	
	/**
	 * 获取手机验证码(更换手机号)
	 * @param mobile 手机号
	 * @return
	 */
	@PostMapping(value = "/updateMobile/getValidCode")
	public Object updateMobileGetCode(String mobile){
		String result = "";
		User user = (User)userService.updateMobileGetCode(mobile);
		if(user != null){
			result = "手机号码已存在";
			return result;
		}else {
			result =  ValidCodeUtils.getValidCode(mobile);
			return result;
		}
	}
	
	/**
	 * 获取验证码(找回密码)
	 * @param mobile 手机号
	 * @return
	 */
	@PostMapping(value = "/findPassword/getValidCode")
	public Object findPasswordGetCode(String mobile){
		String result = "";
		User user = (User)userService.findPasswordGetCode(mobile);
		if(user == null){
			result = "手机号码不存在";
			return result;
		}else {
			result =  ValidCodeUtils.getValidCode(mobile);
			return result;
		}
		/*String result  =  ValidCodeUtils.getValidCode(mobile);
		if(StringUtils.isEmpty(result)){
			return "手机号码不存在";
		}else {
			return result;
		}*/
	}
	
	/**
	 * 根据手机验证码修改密码
	 * @param loginName
	 * @param validateCode
	 * @param newPassword
	 * @return
	 */
	@PostMapping(value = "/updatePasswordByPhone")
	public Object updatePasswordByPhone(String loginName, String validateCode,
			String newPassword){
		Map<Object, String> map = userService.updatePasswordByPhone(loginName, validateCode, newPassword);
		if(map.get("flag").equals("true")){
			return true;
		}else {
			return map.get("msg");
		}
	}
	
	/**
	 * 修改用户密码
	 * @param loginName
	 * @param password
	 * @return
	 */
	@PostMapping(value = "/updatePassword")
	public Object updateUserPassword(String loginName,String oriPassword,String newPassword) {
		if(StringUtils.isNotEmpty(loginName) && StringUtils.isNotEmpty(oriPassword) && StringUtils.isNotEmpty(newPassword)){
			return userService.updateUserPassword(loginName,oriPassword,newPassword);
		}
		//传入值为空
		return false;
		
	}

	/**
	 * 验证原密码
	 * @param name 姓名
	 * @param password 密码
	 * @return
	 */
	@PostMapping(value = "/validOriPassword")
	public Object validOriPassword(String name, String password){
		Object a = userService.validOriPassword(name, password);
		return a;
	}

}
