package operation;

import java.util.List;
import java.util.Map;

import mainUtil.ExcelUtil;

/**
 * Excelҵ��ӿ�
 * 
 * @author Administrator
 * 
 */
public interface ExcelOperation {

	/**
	 * ����sql��䵼��Excel������ 10�������ݣ�132���ֶΣ�ƽ����ʱ100�����ҡ�
	 */
	public void exportExcel(ExcelUtil excelUtil);

	/**
	 * ����csv�����ݿ⣻ 10�������ݣ�132���ֶΣ�ƽ����ʱ130�����ҡ�
	 */
	public void importCsv(ExcelUtil excelUtil);

	/**
	 * ����sql�����뵽���ݿ�
	 * 
	 * @param sql
	 */
	public void insertToDB(String sql);

	/**
	 * ����sql����ѯ���ݿ�
	 * 
	 * @param sql
	 * @return
	 */
	public List<Map<String, Object>> getListBysql(String sql);

}
