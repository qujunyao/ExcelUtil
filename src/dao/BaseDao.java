package dao;

import java.util.List;

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
	public List getList(String sql);

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
