package dao;

import java.util.List;
import java.util.Map;

/**
 * Excel�����ӿ�.
 */
public interface BaseDao {

	/**
	 * ����sql����ѯ����
	 * 
	 * @param sql
	 * @return
	 */
	public List<Map<String, Object>> getList(String sql);

	/**
	 * ����sql�����뵽���ݿ�
	 * 
	 * @param sql
	 */
	public void insertToDB(String sql);

	/**
	 * ���ļ����뵽���ݿ�
	 * 
	 * @param sql
	 * @param b
	 */
	public void insertToDB(String sql, byte[] b);

}
