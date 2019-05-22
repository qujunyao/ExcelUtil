package operation;

import java.util.List;
import java.util.Map;

import mainUtil.ExcelUtil;

/**
 * Excel业务接口
 * 
 * @author Administrator
 * 
 */
public interface ExcelOperation {

	/**
	 * 根据sql语句导出Excel方法； 10万条数据，132个字段，平均用时100秒左右。
	 */
	public void exportExcel(ExcelUtil excelUtil);

	/**
	 * 导入csv到数据库； 10万条数据，132个字段，平均用时130秒左右。
	 */
	public void importCsv(ExcelUtil excelUtil);

	/**
	 * 根据sql语句插入到数据库
	 * 
	 * @param sql
	 */
	public void insertToDB(String sql);

	/**
	 * 根据sql语句查询数据库
	 * 
	 * @param sql
	 * @return
	 */
	public List<Map<String, Object>> getListBysql(String sql);

}
