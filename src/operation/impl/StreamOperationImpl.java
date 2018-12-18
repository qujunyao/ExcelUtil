package operation.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import mainUtil.StreamUtil;

import dao.BaseDao;
import operation.StreamOperation;
import util.DAOBeanFactory;
import util.StringUtil;

public class StreamOperationImpl implements StreamOperation {

	private BaseDao baseDao;

	public void setBaseDao(BaseDao baseDao) {
		this.baseDao = baseDao;
	}

	@SuppressWarnings("unchecked")
	public void exportStream(StreamUtil streamUtil) {
		// 队列装载容量,及相关数据
		int downloadRecordNum = 500;
		// 要导出的sql语句
		String sql = streamUtil.getSql();
		// 文件输出路径
		String outpath = streamUtil.getOutpath();
		if (outpath == null || "".equals(outpath)) {
			outpath = "pic/";
		}
		// 文件标示主键
		String PK = streamUtil.getPK();
		// 如果标示主键为空，则按照数字标示
		long PK_num = 0L;
		// 文件后缀名，不带“.”
		String suffix = streamUtil.getSuffix();

		// sql查询的总条数。
		String sqlcount = "select count(1) from (" + sql + ") t";
		List<Map<Object, Object>> listcount = baseDao.getList(sqlcount);
		int count = 0;
		if (listcount != null && listcount.size() > 0) {
			count = Integer.parseInt(((Map<Object, Object>) listcount.get(0)).get("COUNT(1)").toString());
		}

		// 计算队列装载次数
		int num = count / downloadRecordNum;
		if (count % downloadRecordNum > 0) {
			num++;
		}
		int temp = 0;

		// 查询列名
		List<Map<Object, Object>> listhead = baseDao.getList("select * from (" + sql + ") t where rownum = 1");
		Set<Object> set = listhead.get(0).keySet();
		Iterator<Object> it = set.iterator();
		String[] str = new String[set.size()];
		for (int j = 0; j < set.size(); j++) {
			str[j] = (String) it.next();
		}
		List<Map<Object, Object>> list = null;
		String sqlList = null;
		FileOutputStream fos = null;
		File file = null;
		System.out.println("本次执行共加载" + num + "次队列。");

		// 开始主程序
		for (int m = 0; m < num; m++) {
			int numS = temp * downloadRecordNum + 1;
			int numE = (temp + 1) * downloadRecordNum;
			if (numE > count) {
				numE = count;
			}
			// 分页查询，并装载集合。
			sqlList = "select * from (select rownum n, t.* from  (" + sql + ") t) where n between " + numS + " and " + numE;
			list = baseDao.getList(sqlList);
			System.out.print("-------->第" + (m + 1) + "次装载队列(" + (numE - numS + 1) + "条数据)...");
			if (list == null || list.size() <= 0) {
				System.out.println("-------->没有查询出数据！！！");
				continue;
			}
			System.out.print("装载完毕！开始写数据...");
			for (int i = 0; i < list.size(); i++) {
				for (int j = 0; j < set.size(); j++) {
					try {
						if ("class [B".equals(StringUtil.getType(list.get(i).get(str[j])))) {
							byte[] object = (byte[]) list.get(i).get(str[j]);
							Object PK_id = null;
							if (PK == null || "".equals(PK) || list.get(i).get(PK) == null) {
								PK_id = ++PK_num;
							} else {
								PK_id = list.get(i).get(PK);
							}
							file = new File(outpath + PK_id + "_" + str[j] + "." + suffix);
							try {
								if (!file.exists()) {
									try {
										// 如果文件不存在，则创建
										file.createNewFile();
									} catch (Exception e) {
										// 如果异常，可能是路径中的文件夹不存在。
										file.getParentFile().mkdir();
										try {
											file.createNewFile();
										} catch (Exception e2) {
											System.out.println("3:" + e);
										}
									}
								}
								fos = new FileOutputStream(file);
								if (object.length > 0) {
									fos.write(object, 0, object.length);
								}
								fos.close();
							} catch (Exception e) {
								System.out.println("1:" + e);
							} finally {
								if (fos != null) {
									try {
										fos.close();
									} catch (IOException e) {
									}
								}
							}
						}
					} catch (Exception e) {
						// e.printStackTrace();
						System.out.println("2:" + e);
					}
				}
			}
			System.out.println("写入完毕！");
		}
		System.out.println("完成");
	}

	@Override
	public void insertByStream(String sql, byte[] b) {
		baseDao.insertToDB(sql, b);
	}

}
