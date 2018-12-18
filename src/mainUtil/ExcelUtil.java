package mainUtil;

import dao.jdbc.FnBigTradeDao;
import model.TradePrivilege;
import operation.ExcelOperation;

import sun.nio.cs.ext.TIS_620;
import util.DAOBeanFactory;

/**
 * Excel操作类
 * 
 * @author Administrator
 * 
 */
public class ExcelUtil {

	/** 对象实例 */
	private static ExcelUtil instance;

	/** Excel表格导出，每页的行数 */
	private String maxLine;

	/** Excel导出保存路径 */
	private String outputPath;

	/** Excel导出的sql语句 */
	private String sql;

	/** 表格所在路径 */
	private String csvPath;

	/** Excel业务接口 */
	private ExcelOperation excelOperation;

	public String getMaxLine() {
		return maxLine;
	}

	public String getOutputPath() {
		return outputPath;
	}

	public String getSql() {
		return sql;
	}

	public String getCsvPath() {
		return csvPath;
	}

	public ExcelOperation getExcelOperation() {
		return excelOperation;
	}

	/**
	 * 私有构造方法
	 */
	private ExcelUtil() {

	}

	/**
	 * 获取对象实例 单例模式
	 * 
	 * @return
	 */
	public static ExcelUtil getInstance() {
		if (instance == null) {
			instance = (ExcelUtil) DAOBeanFactory.getBean("excelUtil");
		}
		return instance;
	}

	/**
	 * ExcelUtil/src/mainUtil/ExcelUtil.java 初始化方法
	 */
	public void init() {
		this.excelOperation = (ExcelOperation) DAOBeanFactory.getBean("excelOperation");
		this.maxLine = DAOBeanFactory.getConfig("excel_maxLine");
		this.outputPath = DAOBeanFactory.getConfig("excel_outputPath");
		this.sql = DAOBeanFactory.getConfig("excel_excelSql");
		this.csvPath = DAOBeanFactory.getConfig("excel_csvPath");
	}

	public static void main(String[] args) {
//		ExcelUtil util = ExcelUtil.getInstance();
//		util.init();
//		System.out.println("开始执行方法，计时开始！！！");
//		long start = System.currentTimeMillis();
//		util.getExcelOperation().exportExcel(util);
//		// util.getExcelOperation().importCsv(util);
//		long end = System.currentTimeMillis();
//		System.out.println("方法执行完成！用时" + (end - start) * 0.001 + "秒");
		TradePrivilege privilege = new TradePrivilege();
		privilege.setID(122);
		privilege.setKind(1);
		privilege.setKindID("1080");
		privilege.setPrivilegeCode_B(104);
		privilege.setPrivilegeCode_S(203);
		privilege.setType(1);
		privilege.setTypeID("0002");
		FnBigTradeDao dao = new FnBigTradeDao();
		String str = dao.batchAddPrivilege("1", "0002", "ting01", privilege);
		System.out.println(str);
	}
}
