package com.taoding.service.subject;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import com.taoding.domain.menu.Menu;
import com.taoding.domain.subject.CpaCustomerImport;

public class CpaCustomerImportServiceImpl {
    
    /**
	 * 工作薄对象
	 */
	private Workbook wb;
	
	/**
	 * 工作表对象
	 */
	private Sheet sheet;
	
	/**
	 * 标题行号
	 */
	private int headerNum;
	
	/**
	 * 一个0
	 */
	BigDecimal zeroBigDecimal = new BigDecimal(0);
	
	/**
	 * 科目编号分割符号
	 */
	private String  separator;
	
	/**
	 * 总层级数
	 */
	private Integer  maxLevel;
	
	/**
	 * 每一个层级数字个数：（4.2.2）
	 */
	private String lengthPerLevel;
	
	/**
	 * 对象的各个属性字段对应的列
	 * 
	 */
	private Integer[] rowToObject = {-1,-1,-1,-1,-1,-1};
	
	/**
	 * excel列名固定值
	 * 
	 */
	private String[] rowFinal = {"科目编码","科目名称","本年累计发生额借方","本年累计发生额贷方","期末余额借方","期末余额贷方"};
	
	/**
	 * excel是否导入成功标记
	 * 
	 */
	private boolean isSuccess = true;
	
	/**
	 * excel是否导入提示信息
	 * 
	 */
	private String message = "导入成功";
	
	/**
	 * 构造函数
	 * @param path 导入文件对象
	 * @param sheetIndex 工作表编号
	 * @throws InvalidFormatException 
	 * @throws IOException 
	 */
	public CpaCustomerImportServiceImpl(String fileName, InputStream is, int sheetIndex) 
			throws InvalidFormatException, IOException {
		if (StringUtils.isBlank(fileName)){
			this.isSuccess = false;
			this.message = "导入文档为空!";
			return;
			//throw new RuntimeException("导入文档为空!");
		}else if(fileName.toLowerCase().endsWith("xls")){    
			this.wb = new HSSFWorkbook(is);    
        }else if(fileName.toLowerCase().endsWith("xlsx")){  
        	this.wb = new XSSFWorkbook(is);
        }else{  
        	this.isSuccess = false;
			this.message = "文档格式不正确!";
			return;
        	//throw new RuntimeException("文档格式不正确!");
        }  
		if (this.wb.getNumberOfSheets()<sheetIndex){
			this.isSuccess = false;
			this.message = "文档中没有工作表!";
			return;
			//throw new RuntimeException("文档中没有工作表!");
		}
		this.sheet = this.wb.getSheetAt(sheetIndex);
	}
	
	/**
	 * 构造函数
	 * @param file 导入文件对象
	 * @param sheetIndex 工作表编号
	 * @throws InvalidFormatException 
	 * @throws IOException 
	 */
	public CpaCustomerImportServiceImpl(MultipartFile multipartFile, int sheetIndex) 
			throws InvalidFormatException, IOException {
		this(multipartFile.getOriginalFilename(), multipartFile.getInputStream(), sheetIndex);
	}
	
	/**
	 * 构造函数
	 * @param file 导入文件对象
	 * @param sheetIndex 工作表编号
	 * @param separator 科目编码分隔符
	 * @param maxLevel  科目编码最大层级
	 * @param lengthPerLevel  科目编码层级编码个数
	 * @throws InvalidFormatException 
	 * @throws IOException 
	 */
	public CpaCustomerImportServiceImpl(MultipartFile multipartFile, int sheetIndex,String separator,Integer maxLevel,String lengthPerLevel) 
			throws InvalidFormatException, IOException {
		this(multipartFile.getOriginalFilename(), multipartFile.getInputStream(), sheetIndex);
		this.maxLevel = maxLevel;
		this.lengthPerLevel = lengthPerLevel;
		if(StringUtils.isNotEmpty(separator) && ".".equals(separator)){
			this.separator = "\\" + separator;
		}else{
			this.separator = separator;
		}
	}
	/**
	 * 获取最后一个数据行号
	 * @return
	 */
	public int getLastDataRowNum(){
		return this.sheet.getLastRowNum();
	}
	
	
	/**
	 * 获取行对象
	 * @param rownum
	 * @return
	 */
	public Row getRow(int rownum){
		return this.sheet.getRow(rownum);
	}
	
	/**
	 * 获取最后一个列号
	 * @return
	 */
	public int getLastCellNum(){
		return this.getRow(headerNum).getLastCellNum();
	}
	
	/**
	 * 获取单元格值
	 * @param row 获取的行
	 * @param column 获取单元格列号
	 * @return 单元格值
	 */
	public Object getCellValue(Row row, int column){
		Object val = "";
		try{
			Cell cell = row.getCell(column);
			if (cell != null){
				if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC){
					val = cell.getNumericCellValue();
				}else if (cell.getCellType() == Cell.CELL_TYPE_STRING){
					val = cell.getStringCellValue().trim();
				}else if (cell.getCellType() == Cell.CELL_TYPE_FORMULA){
					val = cell.getCellFormula();
				}else if (cell.getCellType() == Cell.CELL_TYPE_BOOLEAN){
					val = cell.getBooleanCellValue();
				}else if (cell.getCellType() == Cell.CELL_TYPE_ERROR){
					val = cell.getErrorCellValue();
				}
			}
		}catch (Exception e) {
			return val;
		}
		return val;
	}
	
	
	/**
	 * 对象属性赋值
	 * @param cpaCustomerImport
	 * @param cell
	 * @param cellNum
	 * @return
	 */
	public CpaCustomerImport getCpaCustomerImport(CpaCustomerImport cpaCustomerImport,Object cell,int cellNum){
		if(cellNum == this.rowToObject[0]){
			if(cell instanceof Double){
				DecimalFormat df = new DecimalFormat("0");
				Integer it = Integer.parseInt(df.format(cell));
				Double  du = Double.parseDouble(it + "");
				if(cell.equals(du)){ 
			        String subjectNo = df.format(cell); 
			        cpaCustomerImport.setSubjectNo(subjectNo); 
				}else{
					cpaCustomerImport.setSubjectNo(cell + "");
				}
			}else{
				cpaCustomerImport.setSubjectNo(cell + "");
			}
			getParentNo(cpaCustomerImport);
		}
		if(cellNum == this.rowToObject[1]){
			cpaCustomerImport.setSubjectName(String.valueOf(cell));
		}
		if(cellNum == this.rowToObject[2]){
			if(!"".equals(String.valueOf(cell))){
				BigDecimal bd = new BigDecimal(String.valueOf(cell));
				cpaCustomerImport.setCurrentYearDebit(bd);
			}
		}
		if(cellNum == this.rowToObject[3]){
			if(!"".equals(String.valueOf(cell))){
				BigDecimal bd = new BigDecimal(String.valueOf(cell));
				cpaCustomerImport.setCurrentYearCredit(bd);
			}
			
		}
		if(cellNum == this.rowToObject[4]){
			if(!"".equals(String.valueOf(cell))){
				BigDecimal bd = new BigDecimal(String.valueOf(cell));
				cpaCustomerImport.setEndBalancesDebit(bd);
			}
		}
		if(cellNum == this.rowToObject[5]){
			if(!"".equals(String.valueOf(cell))){
				BigDecimal bd = new BigDecimal(String.valueOf(cell));
				cpaCustomerImport.setEndBalancesCredit(bd);
			}
		}
		return cpaCustomerImport;
	}
	
	/**
	 * 计算headerNum和构造对象的各个属性字段对应的列
	 */
	public void makeHeader(){
		//默认表头是单行
		this.headerNum = 0;
		Row rowFirst = this.getRow(0);
		if(rowFirst != null){
			for(int cellNum = 0;cellNum < this.getLastCellNum();cellNum++){
				Object val = this.getCellValue(rowFirst,cellNum);
				if(!"".equals(val.toString().trim())){
					if(isMergedRegion(this.sheet,0,cellNum)){
						//如果存在单元格合并，则认为表头是两行的表头
						this.headerNum = 1;
						break;
					}
				}
			}
		}
		if(this.headerNum == 0){
			Row rowSingle = this.getRow(0);
			if(rowSingle != null){
				for(int cellNum = 0;cellNum < this.getLastCellNum();cellNum++){
					Object val = this.getCellValue(rowSingle,cellNum);
					String rowName = val.toString().trim();
					if(!"".equals(rowName)){
						for(int i = 0;i < this.rowFinal.length;i++){
							if(rowName.equals(this.rowFinal[i])){
								rowToObject[i] = cellNum;
								break;
							}
						}
					}
				}
			}
		}
		if(this.headerNum == 1){
			Row rowOne = this.getRow(0);
			Row rowTwo = this.getRow(1);
			if(rowOne != null && rowTwo != null){
				for(int cellNum = 0;cellNum < this.getLastCellNum();cellNum++){
					String rowName = "";
					if(!isMergedRegion(this.sheet,0,cellNum)){
						Object val1 = this.getCellValue(rowOne,cellNum);
						Object val2 = this.getCellValue(rowTwo,cellNum);
						rowName = val1.toString().trim() + val2.toString().trim();
					}else{
						Object val = this.getCellValue(rowOne,cellNum);
						rowName = val.toString().trim();
					}
					if(!"".equals(rowName)){
						for(int i = 0;i < this.rowFinal.length;i++){
							if(rowName.equals(this.rowFinal[i])){
								rowToObject[i] = cellNum;
								break;
							}
						}
					}
				}
			}
		}	
	}
	
	
	/**
	 * 获取对象list
	 * @return
	 */
	public Map<String,Object> getImportCustomer(){
		Map<String,Object> resultMap = new HashMap<String,Object>();
		if(this.isSuccess){
			this.makeHeader();
			if(this.headIsRigth()){
				List<CpaCustomerImport> list = new ArrayList<CpaCustomerImport>();
				for(int rowNum = headerNum+1;rowNum < this.getLastDataRowNum();rowNum++){
					Row row = this.getRow(rowNum);
					if(row != null){
						CpaCustomerImport cpaCustomerImport = new CpaCustomerImport();
						for(int cellNum = 0;cellNum < this.getLastCellNum();cellNum++){
							Object val = this.getCellValue(row,cellNum);
							if(!"".equals(val.toString().trim())){
								this.getCpaCustomerImport(cpaCustomerImport,val,cellNum);
							}
						}
						this.fillCustomerNull(cpaCustomerImport);
						list.add(cpaCustomerImport);
					}
				}
				resultMap.put("subjectList", getCustomerTree(list));
			}else{
				this.isSuccess = false;
				this.message = "导入的Excel表格的表头不符合要求！";
			}
		}
		resultMap.put("isSuccess", this.isSuccess);
		resultMap.put("message", this.message);
		return resultMap;
	}
	
	
	
	/**
	 * 填充对象中的null值
	 * @param cpaCustomerImport
	 */
	public void fillCustomerNull(CpaCustomerImport cpaCustomerImport){
		if(cpaCustomerImport.getCurrentYearCredit() == null){
			cpaCustomerImport.setCurrentYearCredit(this.zeroBigDecimal);
		}
		if(cpaCustomerImport.getCurrentYearDebit() == null){
			cpaCustomerImport.setCurrentYearDebit(this.zeroBigDecimal);
		}
		if(cpaCustomerImport.getEndBalancesCredit() == null){
			cpaCustomerImport.setEndBalancesCredit(this.zeroBigDecimal);
		}
		if(cpaCustomerImport.getEndBalancesDebit() == null){
			cpaCustomerImport.setEndBalancesDebit(this.zeroBigDecimal);
		}
	}
	/**
	 * 构造父节点编号
	 * @param cpaCustomerImport
	 */
	public void getParentNo(CpaCustomerImport cpaCustomerImport){
		String cci = cpaCustomerImport.getSubjectNo();
		if("|".equals(this.separator)){
			String[] leaves =  this.lengthPerLevel.split("\\.");
			//每一个层级对应的字符串长度
			int[]  everyLeaves = new int[leaves.length];
			int everyLeave = 0;
			for(int i = 0;i < leaves.length;i++){
				everyLeave = everyLeave + Integer.parseInt(leaves[i]);
				everyLeaves[i] = everyLeave;
			}
			//存储带“。”的科目编号
			String  subjectNoWithSeparator = "";
			boolean isRight = false;
			for(int j = 0;j < everyLeaves.length;j++){
				if(cci.length() < everyLeaves[j]){
					break;
				}
				if(cci.length() == everyLeaves[j]){
					if(j != 0){
						subjectNoWithSeparator = subjectNoWithSeparator + "." + cci.substring(everyLeaves[j-1], everyLeaves[j]);
					}else{
						subjectNoWithSeparator = subjectNoWithSeparator + "." + cci.substring(0, everyLeaves[j]);
					}
					if(j > 0){
						subjectNoWithSeparator = subjectNoWithSeparator.substring(1,subjectNoWithSeparator.length());
						cpaCustomerImport.setSubjectNo(subjectNoWithSeparator);
						cpaCustomerImport.setParent(subjectNoWithSeparator.substring(0,everyLeaves[j-1]+j-1));
					}
					isRight = true;
					break;
				}
				if(cci.length() > everyLeaves[j]){
					if(j != 0){
						subjectNoWithSeparator = subjectNoWithSeparator + "." + cci.substring(everyLeaves[j-1], everyLeaves[j]);
					}else{
						subjectNoWithSeparator = subjectNoWithSeparator + "." + cci.substring(0, everyLeaves[j]);
					}
				}
			}
			if(!isRight){
				cpaCustomerImport.setSubjectNo("与设置的格式不匹配"+cci);
			}
		}else{
			String[] subjectNums = cci.split(this.separator);
			String subjectWithSeparator ="";
			for(int i = 0;i < subjectNums.length;i++){
				subjectWithSeparator = subjectWithSeparator + "." + subjectNums[i];
				if(i == subjectNums.length - 2){
					cpaCustomerImport.setParent(subjectWithSeparator.substring(1, subjectWithSeparator.length()));
				}
			}
			if(subjectNums.length > 1){
				cpaCustomerImport.setSubjectNo(subjectWithSeparator.substring(1, subjectWithSeparator.length()));
			}
		}
	}
	
	/**
	 * 构造树结构
	 * @param cciList
	 */
	public List<CpaCustomerImport> getCustomerTree(List<CpaCustomerImport> cciList){
		List<CpaCustomerImport> cFirstList = new ArrayList<CpaCustomerImport>();
		List<CpaCustomerImport> cOtherList = new ArrayList<CpaCustomerImport>();
		for(CpaCustomerImport c : cciList){
			if(StringUtils.isEmpty(c.getParent())){
				cFirstList.add(c);
			}else{
				cOtherList.add(c);
			}
		}
		for(CpaCustomerImport cci : cFirstList){
			makeCustomerTree(cOtherList,cci);
		}
		return cFirstList;
	}
	
	/**
	 * 递归构造树结构
	 * @param cciList
	 * @param cci
	 */
	public void makeCustomerTree(List<CpaCustomerImport> cciList,CpaCustomerImport cci){
		List<CpaCustomerImport> cList = new ArrayList<CpaCustomerImport>();
		for(CpaCustomerImport c : cciList){
			if(StringUtils.isNotEmpty(c.getParent())){
				if(c.getParent().equals(cci.getSubjectNo())){
					cList.add(c);
				}
			}
		}
		if(cList.size() <= 0){
			return;
		}else{
			cci.setSubCustomerSubject(cList);
			for(int i=0; i<cci.getSubCustomerSubject().size(); i++){
				makeCustomerTree(cciList,cci.getSubCustomerSubject().get(i));
			}
		}
	}
	
	/**
	 * 判断当前单元格是否是合并的单元格
	 * @param sheet
	 * @param row
	 * @param column
	 * @return
	 */
	private boolean isMergedRegion(Sheet sheet,int row ,int column) {   
	       int sheetMergeCount = sheet.getNumMergedRegions();   
	       for (int i = 0; i < sheetMergeCount; i++) {   
	             CellRangeAddress range = sheet.getMergedRegion(i);   
	             int firstColumn = range.getFirstColumn(); 
	             int lastColumn = range.getLastColumn();   
	             int firstRow = range.getFirstRow();   
	             int lastRow = range.getLastRow();   
	             if(row >= firstRow && row <= lastRow){ 
	                 if(column >= firstColumn && column <= lastColumn){ 
	                	 return true;   
	                 } 
	             }
	       } 
	       return false;  
	 } 

	/**
	 * 判断表头是否正确
	 * @author fc 
	 * @version 2017年12月21日 上午10:10:34 
	 * @return
	 */
	private boolean headIsRigth(){
		for(int i = 0;i < this.rowToObject.length;i++){
			if(this.rowToObject[i] == -1){
				return false;
			}
		}
		return true;
	}
}
