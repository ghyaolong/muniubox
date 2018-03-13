package com.taoding.configuration;
import java.util.HashMap;
import java.util.Map;

public class Global {

	/**
	 * 当前对象实例
	 */
	private static Global global = new Global();
	
	/**
	 * 保存全局属性值
	 */
	private static Map<String, String> map = new HashMap<String, String>();
	

	/**
	 * 显示/隐藏
	 */
	public static final String SHOW = "1";
	public static final String HIDE = "0";

	/**
	 * 是/否
	 */
	public static final String YES = "1";
	public static final String NO = "0";
	
	/**
	 * 对/错
	 */
	public static final String TRUE = "true";
	public static final String FALSE = "false";
	
	/**
	 * 上传文件基础虚拟路径
	 */
	public static final String USERFILES_BASE_URL = "/userfiles/";
	
	/**
	 * 获取当前对象实例
	 */
	public static Global getInstance() {
		return global;
	}

//	/**
//	 * 获取上传文件的根目录
//	 * @return
//	 */
//	public static String getUserfilesBaseDir() {
//		String dir = getConfig("userfiles.basedir");
//		if (StringUtils.isBlank(dir)){
//			try {
//				dir = ServletContextFactory.getServletContext().getRealPath("/");
//			} catch (Exception e) {
//				return "";
//			}
//		}
//		if(!dir.endsWith("/")) {
//			dir += "/";
//		}
////		System.out.println("userfiles.basedir: " + dir);
//		return dir;
//	}
//	
//    /**
//     * 获取工程路径
//     * @return
//     */
//    public static String getProjectPath(){
//    	// 如果配置了工程路径，则直接返回，否则自动获取。
//		String projectPath = Global.getConfig("projectPath");
//		if (StringUtils.isNotBlank(projectPath)){
//			return projectPath;
//		}
//		try {
//			File file = new DefaultResourceLoader().getResource("").getFile();
//			if (file != null){
//				while(true){
//					File f = new File(file.getPath() + File.separator + "src" + File.separator + "main");
//					if (f == null || f.exists()){
//						break;
//					}
//					if (file.getParentFile() != null){
//						file = file.getParentFile();
//					}else{
//						break;
//					}
//				}
//				projectPath = file.toString();
//			}
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		return projectPath;
//    }
	
}
