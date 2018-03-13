package com.taoding.service.ticket;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.taoding.domain.ticket.Ticket;
import com.taoding.domain.ticket.vo.Condition;

/**
 * 票据业务接口
 * @author 刘鑫
 * @category 票据业务接口
 *
 */
public interface TicketService extends BaseService {
	
	/**
	 * 普通发票
	 */
	public final static byte COMMON_TICKET = 4;
	/**
	 * 银行票据
	 */
	public final static byte BANK_TICKET = 0;
	/**
	 * 银行对账单
	 */
	public final static byte BANK_LIST_TICKET = 3;
	/**
	 * 证明
	 */
	public final static byte CERTIFY_TICKET = 5;
	/**
	 * 增值税普通发票
	 */
	public final static byte VAT_COMMON_TICKET = 1;
	
	/**
	 * 增值税专业发票
	 */
	public final static byte VAT_PROFESSIONAL_TICKET = 2;
	
	
	/**
	 * 根据票据ID获取一张票据信息
	 * @param bookId 账簿ID
	 * @param id 票据ID
	 * @return
	 */
	public Ticket geTicketById(String bookId, String id);
	
	/**
	 * 根据所属目录ID获得所选账期的一个票据集合
	 * @param bookId
	 * @param listId
	 * @param date 账期
	 * @return
	 */
	public List<Ticket> getTicketsByListId(String bookId, String listId, Date date, Condition condition);
	
	/**
	 * 获取一张票据信息，或者多张票据信息的集合
	 * @param condition 所有检索条件的集合
	 * 		bookId 账簿ID 不能为空
	 * @return
	 */
	public List<Ticket> getTickets(Map<String, Object> condition);
	
	/**
	 * <h1>根据摘要获取一个不在 目标目录下的所有票据集合</h1>
	 * <pre>注：只针对未作废、未做帐的票据集合</pre>
	 * @param bookId 	账簿ID
	 * @param summary	摘要
	 * @param listId	目标目录ID
	 * @return
	 */
	public List<Ticket> getTicketsBySummaryNotInList(String bookId, String summary, String listId);
	
	/**
	 * 获取目标账期内所有票据的数量
	 * @param bookId
	 * @param accountDate
	 * @return
	 */
	public long countByAccountDate(String bookId, Date accountDate);
	
	/**
	 * <h1>添加一张票据信息</h1>
	 * <h2>目录属性添加规则</h2>
	 * <pre>1.检查票据的合法性，如果不合法设置为问题票据<br>
	 *  a).票据的收款方或者付款方名称与账簿主体名称不相符<br>
	 *  b).票据的日期不能再账期之后</pre>
	 * <pre>2.检查票据摘要是否在摘要黑名单中，如果是，设置为作废票据</pre>
	 * <pre>3.根据票据类型设置票据目录<br>
	 *  a).如果票据类型为银行票据，请按银行票据查找票据所属目录<br>
	 *  b).如果票据类型为银行对账单，请添加至对账单目录下<br>
	 *  c).如果票据类型为证明，请按照证明查找票据所属目录<br>
	 *  d).如果票据类型非上述类型，请按照普通类型票据查找所属目录<br>
	 *  注: 具体查找目录规则，请参照模板实体类规则查找</pre>
	 * <pre>4.更新所属目录的票据数量信息</pre>
	 * @param ticket 经扫描解析图片获取票据信息生成的票据对象
	 * @return
	 */
	public void insertTicket(Ticket ticket);
	
	/**
	 * 批量添加票据信息
	 * @param tickets
	 * @return
	 */
	public void insertTickets(Ticket[] tickets);
	
	/**
	 * 批量添加票据信息
	 * @param tickets
	 * @return
	 */
	public void insertTickets(List<Ticket> tickets);
	
	/**
	 * <h1>修改票据属性</h1>
	 * <h2>如果票据摘要属性发生变化</h2>
	 * <pre>1.如果修改的摘要属性，模板中存在，则同时需要按照模板更新票据的目录、目录组属性</pre>
	 * <pre>2.如果修改的摘要属性，模板中不存在，则更新票据目录至 费用-->>其他 目录下，更新其目录组,<br>
	 *  及更新临时模板</pre>
	 * @param ticket
	 * @return
	 */
	public void updateTicket(Ticket ticket);
	
	/**
	 * <pre>修改票据的所属目录</pre>
	 * <pre>如果此票据摘要类的所有账期内票据统一移动至此目录，则票据目录临时模板需要更新规则</pre>
	 * @param bookId 账簿ID
	 * @param id	票据的ID
	 * @param listId	目标目录的ID
	 * @return
	 */
	public void moveTicketToList(String bookId, String id, String listId);
	
	/**
	 * 修改票据的所属目录<br>
	 * 如果此票据摘要类的所有账期内票据统一移动至此目录，则票据目录临时模板需要更新规则
	 * @param 账簿ID
	 * @param ids	需要移动目录的所有票据ID集合
	 * @param listId	目标目录的ID
	 * @return
	 */
	public void moveTicketsToList(String bookId, String[] ids, String listId);
	
	/**
	 * 修改票据的所属目录(把来源于 resource 目录下的所有票据移动至目标目录下)<br>
	 * 如果此票据摘要类的所有账期内票据统一移动至此目录，则票据目录临时模板需要更新规则
	 * @param 账簿ID
	 * @param resource	需要移动的票据所属的目录
	 * @param listId	目标目录的ID
	 * @return
	 */
	public void moveTicketsToList(String bookId, String resource, String listId, Date accountDate);
	
	/**
	 * <pre>1.按照定义的  目录--票据 关系模板(请参照Template.java) 重置所选账期现有的目录--票据之间的关系</pre>
	 * <pre>2.整理目录时需摘出有问题的票据，作废的票据，已做账的票据</pre>
	 * <pre>3.检查摘要黑名单，摘出黑名单中的票据设置为作废</pre>
	 * <pre>4.按照模板整理票据</pre>
	 * <pre>5.未找到模板关系，将票据整理至默认的归属目录中</pre>
	 * @param bookId 账簿ID
	 */
	public void reset(String bookId, Date accountDate);
	
	/**
	 * 按照目录ID 整理所选账期目录下属的所有票据的目录归类<br>
	 * 整理目录时需摘出有问题的票据，作废的票据，已做账的票据<br>
	 * 检查摘要黑名单，摘出黑名单中的票据设置为作废
	 * @param bookId
	 * @param listId
	 */
	public void resetByList(String bookId, String listId, Date accountDate);
	
	/**
	 * 删除账簿下所有票据<br>
	 * 此操作为逻辑删除,票据本没有删除业务功能，此删除提供给票据初始化使用，方便找回数据使用
	 * @param bookId
	 */
	public void deleteAll(String bookId);
	
	/**
	 * 判断当前账期下是否存在未做账的票据
	 * @param bookId
	 * @return
	 */
	public boolean hasUnComplatedTicket(String bookId);
	
}
