package com.taoding.service.ticket;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.taoding.common.exception.LogicException;
import com.taoding.domain.ticket.TicketList;
import com.taoding.domain.ticket.vo.TicketListVo;

/**
 * 票据目录业务接口
 * @author 刘鑫
 * @category 票据目录业务接口
 */
public interface TicketListService extends BaseService {
	
	public final static String BANK_LIST = "银行票据";
	public final static String BANK_LIST_LIST = "银行对账单";
	public final static String CERTIFY_LIST = "证明";
	public final static String COST_LIST = "费用";
	public final static String INCOME_LIST = "收入";
	public final static String LLLEGAL_LIST = "问题票据";
	public final static String UNIDENTIFY_LIST = "未识别票据";
	public final static String STOCK_LIST = "存货";
	
	/**
	 * 为一个账簿初始化预置的目录
	 * @param bookId
	 * @exception 如果目标账簿已存在目录，则抛出此异常
	 */
	public void init(String bookId) throws LogicException;
	
	/**
	 * 根据获取一个票据目录
	 * @param id
	 */
	public TicketList getTicketListById(String id);
	
	/**
	 * 根据名称获取一个票据目录
	 * @param bookId 账簿Id
	 * @param name 目录名称
	 * @return
	 */
	public TicketList getTicketListByName(String bookId, String name);
	
	/**
	 * 根据父节点ID获取一个票据目录集合
	 * @param bookId 账簿ID 可选 获取根节点时必须传值
	 * @param parentId
	 * @return
	 */
	public List<TicketList> getTicketListsByParentId(String parentId, String bookId);
	
	/**
	 * 根据父节点ID 获取一个票据目录集合 (含此目录下票据的数量)
	 * @param bookId 账簿ID 可选 获取根节点时必须传值
	 * @param parentId
	 * @param accountDate 账期
	 * @return
	 */
	public List<TicketListVo> getTicketListVosByParentId(String parentId, String bookId, Date accountDate);
	
	/**
	 * 获取一个票据目录集合
	 * @param condition 检索条件的集合
	 * @return
	 */
	public List<TicketList> getTicketLists(Map<String, Object> condition);
	
	/**
	 * 获取一个票据目录集合 (含此目录下票据的数量)
	 * @param condition 检索条件的集合
	 * @return
	 */
	public List<TicketListVo> getTicketListVos(Map<String, Object> condition);
	
	/**
	 * 添加一个新的目录
	 * 不能添加根节点目录, 根节点目录为预设置
	 * @param name
	 * @param parentId
	 * @param bookId
	 */
	public void insertTicketList(TicketList ticketList);
	
	/**
	 * 编辑一个票据目录
	 * 预设的目录不能修改名称
	 * @param ticketList
	 */
	public void updateTicketList(TicketList ticketList);
	
	/**
	 * <h1>删除一个票据目录</h1>
	 * <pre>1.预设值的目录，不能删除</pre>
	 * <pre>2.<br>
	 *  a)如果自定义的目录中有关联的票据，下属的所有票据设置为作废<br>
	 *  b)如果此目录与下属票据摘要有对应关系，则删除此对应关系</pre>
	 * @param id
	 * @param date 账期
	 */
	public void deleteTicketList(String id, Date date);
	
	/**
	 * 获取此目录树层结构中的级别
	 * @param ticketList
	 * @return
	 */
	public int getTicketListLevel(TicketList ticketList);

}
