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
		// ����װ������,���������
		int downloadRecordNum = 500;
		// Ҫ������sql���
		String sql = streamUtil.getSql();
		// �ļ����·��
		String outpath = streamUtil.getOutpath();
		if (outpath == null || "".equals(outpath)) {
			outpath = "pic/";
		}
		// �ļ���ʾ����
		String PK = streamUtil.getPK();
		// �����ʾ����Ϊ�գ��������ֱ�ʾ
		long PK_num = 0L;
		// �ļ���׺����������.��
		String suffix = streamUtil.getSuffix();

		// sql��ѯ����������
		String sqlcount = "select count(1) from (" + sql + ") t";
		List<Map<Object, Object>> listcount = baseDao.getList(sqlcount);
		int count = 0;
		if (listcount != null && listcount.size() > 0) {
			count = Integer.parseInt(((Map<Object, Object>) listcount.get(0)).get("COUNT(1)").toString());
		}

		// �������װ�ش���
		int num = count / downloadRecordNum;
		if (count % downloadRecordNum > 0) {
			num++;
		}
		int temp = 0;

		// ��ѯ����
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
		System.out.println("����ִ�й�����" + num + "�ζ��С�");

		// ��ʼ������
		for (int m = 0; m < num; m++) {
			int numS = temp * downloadRecordNum + 1;
			int numE = (temp + 1) * downloadRecordNum;
			if (numE > count) {
				numE = count;
			}
			// ��ҳ��ѯ����װ�ؼ��ϡ�
			sqlList = "select * from (select rownum n, t.* from  (" + sql + ") t) where n between " + numS + " and " + numE;
			list = baseDao.getList(sqlList);
			System.out.print("-------->��" + (m + 1) + "��װ�ض���(" + (numE - numS + 1) + "������)...");
			if (list == null || list.size() <= 0) {
				System.out.println("-------->û�в�ѯ�����ݣ�����");
				continue;
			}
			System.out.print("װ����ϣ���ʼд����...");
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
										// ����ļ������ڣ��򴴽�
										file.createNewFile();
									} catch (Exception e) {
										// ����쳣��������·���е��ļ��в����ڡ�
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
			System.out.println("д����ϣ�");
		}
		System.out.println("���");
	}

	@Override
	public void insertByStream(String sql, byte[] b) {
		baseDao.insertToDB(sql, b);
	}

}
