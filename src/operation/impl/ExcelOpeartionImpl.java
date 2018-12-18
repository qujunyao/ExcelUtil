package operation.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

import mainUtil.ExcelUtil;
import model.StringBlock;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.streaming.SXSSFCell;
import org.apache.poi.xssf.streaming.SXSSFRow;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import dao.BaseDao;
import operation.ExcelOperation;
import util.StringUtil;

/**
 * Excel业务接口实现
 * 
 * @author Administrator
 * 
 */
public class ExcelOpeartionImpl implements ExcelOperation {

	private BaseDao baseDao;

	public void setBaseDao(BaseDao baseDao) {
		this.baseDao = baseDao;
	}

	@SuppressWarnings("unchecked")
	public void exportExcel(ExcelUtil excelUtil) {
		// 每个表的实际最大行数。
		int line = 0;
		// 队列装载容量
		int downloadRecordNum = 10000;
		SXSSFWorkbook wb = new SXSSFWorkbook(10000);
		SXSSFSheet sheet = (SXSSFSheet) wb.createSheet("sheet");
		SXSSFRow row = null;
		SXSSFCell cell = null;

		String sqlcount = "select count(1) from (" + excelUtil.getSql() + ") t";
		List<Map<Object, Object>> listcount = baseDao.getList(sqlcount);
		// sql查询的总条数。
		int count = 0;
		if (listcount != null && listcount.size() > 0) {
			count = Integer.parseInt(((Map<Object, Object>) listcount.get(0)).get("COUNT(1)").toString());
		}
		// 计算队列装载的次数。
		int num = count / downloadRecordNum;
		if (count % downloadRecordNum > 0) {
			num++;
		}
		int temp = -1;
		String sqlList;

		// 根据sql语句查询表头
		List<Map<Object, Object>> listhead = baseDao.getList("select * from (" + excelUtil.getSql() + ") t where rownum = 1");
		// 将集合转成队列，然后没次做出队操作，队列的元素会越来越少。
		Queue<Map<Object, Object>> queue = new LinkedList<Map<Object, Object>>();
		// 如果总条数大于表格的最大行数，则需要分页导出，每页按照表格的最大行数导出，最后一页行数为剩余数据的个数。
		int maxline = 0;
		if (excelUtil.getMaxLine() == null || "".equals(excelUtil.getMaxLine())) {
			maxline = 65535;
		} else {
			try {
				maxline = Integer.valueOf(excelUtil.getMaxLine());
			} catch (Exception e) {
				maxline = 65535;
			}
		}
		if (maxline > 1048575) {
			System.out.println("超过单个表格最大行数，按照最大行数导出！");
			maxline = 1048575;
		}
		if (count > maxline) {
			line = maxline;
		} else {
			line = count;
		}
		String name = null;
		Map<Object, Object> map = null;
		Object obj = null;
		System.out.println("本次执行共加载" + num + "次队列。");
		// 主程序 外层循环工作表的个数，至少循环一次
		for (int k = 0; k < (count / maxline + 1); k++) {
			// 第二张工作表，需要重新创建
			if (k > 0) {
				// 最后一次循环时，表格的行数为：sql查询的总条数-已经写入到表格的条数。
				if (k == count / maxline) {
					line = count - maxline * k;
				}
				sheet = (SXSSFSheet) wb.createSheet("sheet" + k);
			}
			// 创建表头
			row = (SXSSFRow) sheet.createRow(0);
			// 取出键值对中的键，即为列名
			Set<Object> set = listhead.get(0).keySet();
			Iterator<Object> it = set.iterator();
			String[] str = new String[set.size()];
			for (int j = 0; j < set.size(); j++) {
				name = (String) it.next();
				str[j] = name;
				cell = (SXSSFCell) row.createCell(j);
				cell.setCellValue(name);
			}
			// 循环写入到工作表。
			for (int i = 1; i <= line; i++) {
				row = (SXSSFRow) sheet.createRow(i);
				if (queue.size() <= 0) {
					// 装载队列
					if (num > 0) {
						num--;
						temp++;
						int numS = temp * downloadRecordNum + 1;
						int numE = (temp + 1) * downloadRecordNum;
						if (numE > count) {
							numE = count;
						}
						// 分页查询，并装载队列。
						sqlList = "select * from (select rownum n, t.* from  (" + excelUtil.getSql() + ") t) where n between " + numS + " and " + numE;
						queue.addAll(baseDao.getList(sqlList));
						System.out.println("-------->第" + (temp + 1) + "次重新装载队列（" + (numE - numS + 1) + "条数据）完毕");
					}
				}
				map = queue.poll();
				// 单元格赋值，排除掉第一个单元格，因为他是序号。
				for (int j = 1; j < map.size(); j++) {
					cell = (SXSSFCell) row.createCell(j - 1);
					obj = map.get(str[j - 1]);
					if (obj == null) {
						obj = "";
					}
					cell.setCellValue(obj.toString());
				}
			}
		}
		// 将文件存到指定位置
		System.out.println("-------->数据读取完毕。");
		String outpath = excelUtil.getOutputPath();
		if (outpath == null || "".equals(outpath)) {
			outpath = "demo.xls";
			System.out.println("文件路径不能为空，使用默认路径和文件名:demo.xls");
		}
		System.out.print("开始写入文件... ");
		FileOutputStream fout = null;
		try {
			fout = new FileOutputStream(outpath);
			wb.write(fout);
			fout.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (fout != null) {
				try {
					fout.close();
				} catch (Exception e2) {
				}
			}
		}
		System.out.println("数据写入文件完毕。");
	}

	@SuppressWarnings("unchecked")
	public void importCsv(ExcelUtil excelUtil) {
		String csvpath = excelUtil.getCsvPath();
		if (csvpath == null || "".equals(csvpath)) {
			System.out.println("传入的路径不能为空！！");
			return;
		}
		File file = new File(csvpath);
		// 文件名
		String tableName = file.getName().replaceAll("[.][^.]+$", "");
		// 后缀名
		String prefix = file.getName().substring(file.getName().lastIndexOf(".") + 1);
		if (!"csv".equals(prefix)) {
			System.out.println("不是csv文件！");
			return;
		}
		// 查询表名，列名，列的数据类型
		String tableSql = "select column_name,data_type from user_tab_cols where table_name=upper('" + tableName + "') order by column_id";
		List<Map<Object, Object>> list = null;
		try {
			list = baseDao.getList(tableSql);
		} catch (Exception e) {
			System.out.println("表不存在，或其他错误！");
			return;
		}
		// 列名数组
		String[] tableHead = new String[list.size()];
		// 列的数据类型数组
		String[] dataType = new String[list.size()];
		for (int j = 0; j < list.size(); j++) {
			tableHead[j] = (String) list.get(j).get("COLUMN_NAME");
			dataType[j] = (String) list.get(j).get("DATA_TYPE");
		}
		StringBuffer sql = null;
		BufferedReader reader = null;
		String[] str = null;
		int success = 0;
		int error = 0;
		System.out.println("开始插入数据。");
		try {
			reader = new BufferedReader(new FileReader(file));
			String tempString = null;
			// 一次读入一行，直到读入null为文件结束
			while ((tempString = reader.readLine()) != null) {
				if (tempString == null || "".equals(tempString)) {
					continue;
				}
				str = StringUtil.spileString(tempString, ",");
				boolean f2 = false;// 假拆分-首开关
				boolean f3 = false;// 假拆分-尾开关
				List<StringBlock> listInt = new ArrayList<StringBlock>();// 假拆分下标集合
				int temp = 1;// 尾开关判断标示
				int start = 0;// 首开关下标
				int end = 0;// 尾开关下标
				for (int i = 0; i < str.length; i++) {
					// 判断字符串是否假拆分
					int x = StringUtil.count(str[i], "\"");
					if (!StringUtil.Parity(x)) {
						if (!StringUtil.Parity(temp)) {
							start = i;
						}
						temp++;
						f2 = true;// 首开关，开启。
					}
					if (f2 && !StringUtil.Parity(temp)) {
						f3 = true;// 尾开关，开启。
						end = i;
					}
					// 收尾开关都开启，建立字符块
					if (f2 && f3) {
						StringBlock block = new StringBlock();
						StringBuffer buffer = new StringBuffer();
						block.setStart(start);
						block.setEnd(end);
						for (int j = start; j < end + 1; j++) {
							if (j == end) {
								buffer.append(str[j]);
							} else {
								buffer.append(str[j]).append(",");
							}
						}
						block.setContent(buffer.toString());
						listInt.add(block);
						f2 = false;
						f3 = false;
					}
				}
				if (temp > 1 && listInt != null && listInt.size() > 0) {
					// 字符数组缩容
					str = StringUtil.StringReduction(str, listInt);
				}
				// 去双引号
				str = StringUtil.removeQuotes(str);
				sql = new StringBuffer("insert into " + tableName + " values( ");
				if (str.length != list.size()) {
					System.out.println("执行失败，列数不相等，无法插入!");
					System.out.println("读取出的列数：" + str.length);
					System.out.println("查询出的列数：" + list.size());
					System.out.println("文件读取的字符串：" + tempString);
					System.out.println("数据库查询出的列：" + list.toString());
					continue;
				}
				for (int i = 0; i < str.length; i++) {
					if (i == str.length - 1) {
						sql.append(StringUtil.EditCondition(str[i], dataType[i]));
					} else {
						sql.append(StringUtil.EditCondition(str[i], dataType[i])).append(",");
					}
				}
				sql.append(")");
				try {
					baseDao.insertToDB(sql.toString());
					success++;
				} catch (Exception e) {
					error++;
					boolean f4 = false;
					for (int s = 0; s < str.length; s++) {
						if (!str[s].equals(list.get(s).get("COLUMN_NAME"))) {
							f4 = true;
							break;
						}
					}
					if (!f4) {
						System.out.println("-------->执行失败！表头数据。");
					} else {
						System.out.println("-------->执行失败！");
						System.out.println("-------->读取出的字符串：" + tempString);
						System.out.println("-------->执行的sql语句：" + sql.toString());
					}
				}
				if ((success + error) % 10000 == 0) {
					System.out.println("-------->已经执行了" + (success + error) + "条数据（包含成功和失败的条数）");
				}
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e1) {
				}
			}
		}
		System.out.println("数据插入完毕，成功条数：" + success + "；失败条数：" + error);
	}

	@SuppressWarnings("rawtypes")
	public List getListBysql(String sql) {
		List list = null;
		try {
			list = baseDao.getList(sql);
		} catch (Exception e) {
			System.out.println("查询失败！");
			e.printStackTrace();
		}
		return list;
	}

	@Override
	public void insertToDB(String sql) {
		try {
			baseDao.insertToDB(sql);
			System.out.println("执行成功！");
		} catch (Exception e) {
			System.out.println("执行失败！");
			e.printStackTrace();
		}
	}
}
