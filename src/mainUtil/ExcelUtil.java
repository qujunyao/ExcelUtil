package mainUtil;

import dao.jdbc.FnBigTradeDao;
import model.TradePrivilege;
import operation.ExcelOperation;

import sun.nio.cs.ext.TIS_620;
import util.DAOBeanFactory;

/**
 * Excel������
 * 
 * @author Administrator
 * 
 */
public class ExcelUtil {

	/** ����ʵ�� */
	private static ExcelUtil instance;

	/** Excel��񵼳���ÿҳ������ */
	private String maxLine;

	/** Excel��������·�� */
	private String outputPath;

	/** Excel������sql��� */
	private String sql;

	/** �������·�� */
	private String csvPath;

	/** Excelҵ��ӿ� */
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
	 * ˽�й��췽��
	 */
	private ExcelUtil() {

	}

	/**
	 * ��ȡ����ʵ�� ����ģʽ
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
	 * ExcelUtil/src/mainUtil/ExcelUtil.java ��ʼ������
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
//		System.out.println("��ʼִ�з�������ʱ��ʼ������");
//		long start = System.currentTimeMillis();
//		util.getExcelOperation().exportExcel(util);
//		// util.getExcelOperation().importCsv(util);
//		long end = System.currentTimeMillis();
//		System.out.println("����ִ����ɣ���ʱ" + (end - start) * 0.001 + "��");
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
