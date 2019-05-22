package dao;

import java.util.List;
import java.util.Map;

/**
 * Excel操作接口.
 */
public interface BaseDao {

	/**
	 * 根据sql语句查询数据
	 * 
	 * @param sql
	 * @return
	 */
	public List<Map<String, Object>> getList(String sql);

	/**
	 * 根据sql语句插入到数据库
	 * 
	 * @param sql
	 */
	public void insertToDB(String sql);

	/**
	 * 流文件插入到数据库
	 * 
	 * @param sql
	 * @param b
	 */
	public void insertToDB(String sql, byte[] b);

}
