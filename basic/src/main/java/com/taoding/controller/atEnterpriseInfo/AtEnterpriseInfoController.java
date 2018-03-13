package com.taoding.controller.atEnterpriseInfo;

import java.io.IOException;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ClassUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.github.pagehelper.PageInfo;
import com.taoding.common.controller.BaseController;
import com.taoding.common.excel.ExportExcel;
import com.taoding.common.exception.LogicException;
import com.taoding.common.utils.DateUtils;
import com.taoding.common.utils.FileUtils;
import com.taoding.common.utils.MyFileUtiles;
import com.taoding.common.utils.QueryConditionFieldsArray;
import com.taoding.common.utils.QueryConditionSql;
import com.taoding.common.utils.StringUtils;
import com.taoding.configuration.Global;
import com.taoding.domain.atEnterpriseInfo.AtEnterpriseConfigure;
import com.taoding.domain.atEnterpriseInfo.AtEnterpriseInfo;
import com.taoding.domain.user.User;
import com.taoding.service.atEnterpriseInfo.AtEnterpriseConfigureService;
import com.taoding.service.atEnterpriseInfo.AtEnterpriseInfoDetailService;
import com.taoding.service.atEnterpriseInfo.AtEnterpriseInfoService;
import com.taoding.service.atEnterpriseInfo.AtLegalPersonInfoService;
import com.taoding.service.office.OfficeService;
import com.taoding.service.user.UserService;

/**
 * 企业信息Controller
 * @author Lin
 * @version 2017-11-11 16:06:41
 */
@RestController
@RequestMapping(value = "/atEnterpriseInfo")
public class AtEnterpriseInfoController extends BaseController {
	
	@Autowired
	AtEnterpriseInfoService atEnterpriseInfoService;

	@Autowired
	AtEnterpriseInfoDetailService AtEnterpriseInfoDetailService;

	@Autowired
	AtLegalPersonInfoService atLegalPersonInfoService;

	@Autowired
	AtEnterpriseConfigureService atEnterpriseConfigureService;

	@Autowired
	OfficeService officeService;

	@Autowired
	UserService userService;

	// 文件上传路径
	@Value("${userFileLogo.path}")
	private String userFileLogo;
	
	/**
	 * 获取企业的所有列表
	 * @param atEnterpriseInfo
	 * @author csl 2017-11-10 10:21:03
	 */
	@PostMapping("/list")
	public Object list(@RequestBody Map<String,Object> maps) {
		PageInfo<AtEnterpriseInfo> pages = atEnterpriseInfoService.findAllByPage(maps);
		return pages;
	}

	/**
	 * 保存添加企业信息，通过查询当前的公司账户是否是重复，然后项公司添加信息
	 * @param atEnterpriseInfo
	 * @return add csl 2017-10-24 14:52:30
	 */
	@PostMapping("/insertAtEnterpriseInfo")
	@Transactional(noRollbackFor = LogicException.class)
	public Object saveAtEnterpriseInfo(@RequestBody AtEnterpriseInfo atEnterpriseInfo) {
		if (null == atEnterpriseInfo) {
			return false;
		}
		if (StringUtils.isNotBlank(atEnterpriseInfo.getCompanyAccount())) {
			User enterpriseUser = userService
					.findEnterpriseforLoginName(atEnterpriseInfo.getCompanyAccount());
			if (enterpriseUser != null) {
				throw new LogicException("注册账户存在,请重新输入注册账户!");
			}
		} else {
			throw new LogicException("注册账户不能为空！");
		}
		try {
			atEnterpriseInfoService.save(atEnterpriseInfo);
		} catch (Exception e) {
			throw new LogicException("注册账号失败！");
		}
		return true;
	}
	
	/**
	 * 根据id修改详情中的信息
	 * @param atEnterpriseInfo
	 * @return add csl 2017-10-24 14:52:30
	 */
	@PostMapping("/updateAtEnterpriseInfo")
	@Transactional(noRollbackFor = LogicException.class)
	public Object updateAtEnterpriseInfo(@RequestBody AtEnterpriseInfo atEnterpriseInfo) {
		if (null == atEnterpriseInfo) {
			return false;
		}
		atEnterpriseInfoService.updateInfo(atEnterpriseInfo);
		return true;
	}

	/**
	 * 根据id查询公司的详细信息
	 * @param id  企业id
	 * @return add 
	 * cls 2017-10-20 13:37:32
	 */
	@GetMapping("/findById/{id}")
	public Object findById(@PathVariable("id") String id) {
		if(null==id&& "".equals(id)){
			return false;
		}
		return atEnterpriseInfoService.get(id);
	}

	/**
	 * 更改企业是否可用的状态
	 * @param id  企业id
	 * @return
	 * @author csl
	 * @date 2017-11-14 09:22:38
	 */
	@PostMapping("/forbidenAtEnterpriseInfo/{id}")
	public Object forbiden(@PathVariable("id") String id) {
		if (StringUtils.isEmpty(id)) {
			return false;
		}
		AtEnterpriseInfo atEnterpriseInfo = atEnterpriseInfoService.getInfo(id);
		// 获取到当前用户的状态，并更改用户的状态,UNUSEABLE表示不可用，USEABLE表示可用
		if (atEnterpriseInfo.getCompanyState().equals(Global.UNUSEABLE)) {
			atEnterpriseInfo.setCompanyState(Global.USEABLE);
			atEnterpriseInfoService.update(atEnterpriseInfo);
			return true;
		}
		if (atEnterpriseInfo.getCompanyState().equals(Global.USEABLE)) {
			atEnterpriseInfo.setCompanyState(Global.UNUSEABLE);
			atEnterpriseInfoService.update(atEnterpriseInfo);
		}
		return true;
	}

	/**
	 * 公司详情页面个性化设置上传图图片
	 * @param request   请求
	 * @param file 文件
	 * @param id 企业ID
	 * @return add
	 * csl 2017-11-13 09:34:10
	 */
	@PostMapping(value = "/uploadImg/{id}")
	@Transactional(noRollbackFor = LogicException.class)
	public Object uploadImage(@RequestParam("file") MultipartFile file,@PathVariable("id") String id) {
		String newFileName = null;
		if (file != null) {
			//获取图片的名称
			String fileName = file.getOriginalFilename();
			String ext = MyFileUtiles.getExtensionName(fileName);
			if (ext == null) {
				//上传的文件的扩展名不属于图片的范畴
				throw new LogicException("无法识别的文件!+fileName");
			}else{
				boolean isImg = MyFileUtiles.isImg(ext);
				if (!isImg) {
					throw new LogicException("MyFileUtiles.isImg(ext)+'不是图片，请上传图片'");
				}
				//判断文件大小
				if (!MyFileUtiles.isAllowSize(file.getSize())) {
					throw new LogicException("file.getOriginalFilename()+'图片文件过大，不能超过4M'");
				}
			}
			//将文件名称修改为企业id
			newFileName = id+ext;
			try {
				String filePath = ClassUtils.getDefaultClassLoader().getResource("").getPath();
				FileUtils.uploadFile(file.getBytes(), filePath + userFileLogo, newFileName);
			} catch (Exception e) {
				throw new LogicException("上传失败！，请稍后再试!");
			}
		}
			//根据atEnterpriseInfo的id将filePath保存到atEnterpriseConfigure中
			AtEnterpriseConfigure configure = new AtEnterpriseConfigure();
			if (StringUtils.isEmpty(id)) {
				throw new LogicException("传入的id为空！");
			}
			configure.setAtEnterpriseInfoId(id);
			configure.setUrl(userFileLogo.substring(7,userFileLogo.length())+"/"+newFileName);
			atEnterpriseConfigureService.updateConfigure(configure);
		return true;
	}

	/**
	 * 根据id查询logo图片
	 * @param atEnterpriseConfigure
	 * @return add csl 
	 * 2017-10-20 17:07:27
	 */
	@PostMapping("/showImage")
	@Transactional(noRollbackFor = LogicException.class)
	public Object showImage(@RequestBody AtEnterpriseConfigure configure,HttpServletResponse response) {
		if (null==configure) {
			return false;
		}
		AtEnterpriseConfigure atEnc = atEnterpriseConfigureService
				.findAtEnterpriseConfigureForAtEnterpriseInfoId(configure);
		if (atEnc.getUrl() == null) {
			throw new LogicException("查询的图片不存在！");
		}
		return atEnc;
	}

	/**
	 * 企业信息导出
	 * @param response
	 * @return
	 * @author 
	 * csl 2017-11-13 13:45:24
	 */
	@PostMapping("/exportData")
	public Object exportData(@RequestBody Map<String, Object> queryMap,HttpServletResponse response) {
		queryMap.put("queryConditionSql",
				QueryConditionSql.getQueryConditionSql(QueryConditionFieldsArray.atEnterpriserFields, ""));
		PageInfo<AtEnterpriseInfo> pages = atEnterpriseInfoService.findAllByPage(queryMap);
		String fileName = "用户数据" + DateUtils.getDate("yyyyMMddHHmmss") + ".xlsx";
		try {
			new ExportExcel("用户数据", AtEnterpriseInfo.class).setDataList(pages.getList()).write(response, fileName)
					.dispose();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return true;
	}
}