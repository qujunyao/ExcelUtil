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
 * Excelҵ��ӿ�ʵ��
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
		// ÿ�����ʵ�����������
		int line = 0;
		// ����װ������
		int downloadRecordNum = 10000;
		SXSSFWorkbook wb = new SXSSFWorkbook(10000);
		SXSSFSheet sheet = (SXSSFSheet) wb.createSheet("sheet");
		SXSSFRow row = null;
		SXSSFCell cell = null;

		String sqlcount = "select count(1) from (" + excelUtil.getSql() + ") t";
		List<Map<Object, Object>> listcount = baseDao.getList(sqlcount);
		// sql��ѯ����������
		int count = 0;
		if (listcount != null && listcount.size() > 0) {
			count = Integer.parseInt(((Map<Object, Object>) listcount.get(0)).get("COUNT(1)").toString());
		}
		// �������װ�صĴ�����
		int num = count / downloadRecordNum;
		if (count % downloadRecordNum > 0) {
			num++;
		}
		int temp = -1;
		String sqlList;

		// ����sql����ѯ��ͷ
		List<Map<Object, Object>> listhead = baseDao.getList("select * from (" + excelUtil.getSql() + ") t where rownum = 1");
		// ������ת�ɶ��У�Ȼ��û�������Ӳ��������е�Ԫ�ػ�Խ��Խ�١�
		Queue<Map<Object, Object>> queue = new LinkedList<Map<Object, Object>>();
		// ������������ڱ����������������Ҫ��ҳ������ÿҳ���ձ�������������������һҳ����Ϊʣ�����ݵĸ�����
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
			System.out.println("�������������������������������������");
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
		System.out.println("����ִ�й�����" + num + "�ζ��С�");
		// ������ ���ѭ��������ĸ���������ѭ��һ��
		for (int k = 0; k < (count / maxline + 1); k++) {
			// �ڶ��Ź�������Ҫ���´���
			if (k > 0) {
				// ���һ��ѭ��ʱ����������Ϊ��sql��ѯ��������-�Ѿ�д�뵽����������
				if (k == count / maxline) {
					line = count - maxline * k;
				}
				sheet = (SXSSFSheet) wb.createSheet("sheet" + k);
			}
			// ������ͷ
			row = (SXSSFRow) sheet.createRow(0);
			// ȡ����ֵ���еļ�����Ϊ����
			Set<Object> set = listhead.get(0).keySet();
			Iterator<Object> it = set.iterator();
			String[] str = new String[set.size()];
			for (int j = 0; j < set.size(); j++) {
				name = (String) it.next();
				str[j] = name;
				cell = (SXSSFCell) row.createCell(j);
				cell.setCellValue(name);
			}
			// ѭ��д�뵽������
			for (int i = 1; i <= line; i++) {
				row = (SXSSFRow) sheet.createRow(i);
				if (queue.size() <= 0) {
					// װ�ض���
					if (num > 0) {
						num--;
						temp++;
						int numS = temp * downloadRecordNum + 1;
						int numE = (temp + 1) * downloadRecordNum;
						if (numE > count) {
							numE = count;
						}
						// ��ҳ��ѯ����װ�ض��С�
						sqlList = "select * from (select rownum n, t.* from  (" + excelUtil.getSql() + ") t) where n between " + numS + " and " + numE;
						queue.addAll(baseDao.getList(sqlList));
						System.out.println("-------->��" + (temp + 1) + "������װ�ض��У�" + (numE - numS + 1) + "�����ݣ����");
					}
				}
				map = queue.poll();
				// ��Ԫ��ֵ���ų�����һ����Ԫ����Ϊ������š�
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
		// ���ļ��浽ָ��λ��
		System.out.println("-------->���ݶ�ȡ��ϡ�");
		String outpath = excelUtil.getOutputPath();
		if (outpath == null || "".equals(outpath)) {
			outpath = "demo.xls";
			System.out.println("�ļ�·������Ϊ�գ�ʹ��Ĭ��·�����ļ���:demo.xls");
		}
		System.out.print("��ʼд���ļ�... ");
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
		System.out.println("����д���ļ���ϡ�");
	}

	@SuppressWarnings("unchecked")
	public void importCsv(ExcelUtil excelUtil) {
		String csvpath = excelUtil.getCsvPath();
		if (csvpath == null || "".equals(csvpath)) {
			System.out.println("�����·������Ϊ�գ���");
			return;
		}
		File file = new File(csvpath);
		// �ļ���
		String tableName = file.getName().replaceAll("[.][^.]+$", "");
		// ��׺��
		String prefix = file.getName().substring(file.getName().lastIndexOf(".") + 1);
		if (!"csv".equals(prefix)) {
			System.out.println("����csv�ļ���");
			return;
		}
		// ��ѯ�������������е���������
		String tableSql = "select column_name,data_type from user_tab_cols where table_name=upper('" + tableName + "') order by column_id";
		List<Map<Object, Object>> list = null;
		try {
			list = baseDao.getList(tableSql);
		} catch (Exception e) {
			System.out.println("�����ڣ�����������");
			return;
		}
		// ��������
		String[] tableHead = new String[list.size()];
		// �е�������������
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
		System.out.println("��ʼ�������ݡ�");
		try {
			reader = new BufferedReader(new FileReader(file));
			String tempString = null;
			// һ�ζ���һ�У�ֱ������nullΪ�ļ�����
			while ((tempString = reader.readLine()) != null) {
				if (tempString == null || "".equals(tempString)) {
					continue;
				}
				str = StringUtil.spileString(tempString, ",");
				boolean f2 = false;// �ٲ��-�׿���
				boolean f3 = false;// �ٲ��-β����
				List<StringBlock> listInt = new ArrayList<StringBlock>();// �ٲ���±꼯��
				int temp = 1;// β�����жϱ�ʾ
				int start = 0;// �׿����±�
				int end = 0;// β�����±�
				for (int i = 0; i < str.length; i++) {
					// �ж��ַ����Ƿ�ٲ��
					int x = StringUtil.count(str[i], "\"");
					if (!StringUtil.Parity(x)) {
						if (!StringUtil.Parity(temp)) {
							start = i;
						}
						temp++;
						f2 = true;// �׿��أ�������
					}
					if (f2 && !StringUtil.Parity(temp)) {
						f3 = true;// β���أ�������
						end = i;
					}
					// ��β���ض������������ַ���
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
					// �ַ���������
					str = StringUtil.StringReduction(str, listInt);
				}
				// ȥ˫����
				str = StringUtil.removeQuotes(str);
				sql = new StringBuffer("insert into " + tableName + " values( ");
				if (str.length != list.size()) {
					System.out.println("ִ��ʧ�ܣ���������ȣ��޷�����!");
					System.out.println("��ȡ����������" + str.length);
					System.out.println("��ѯ����������" + list.size());
					System.out.println("�ļ���ȡ���ַ�����" + tempString);
					System.out.println("���ݿ��ѯ�����У�" + list.toString());
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
						System.out.println("-------->ִ��ʧ�ܣ���ͷ���ݡ�");
					} else {
						System.out.println("-------->ִ��ʧ�ܣ�");
						System.out.println("-------->��ȡ�����ַ�����" + tempString);
						System.out.println("-------->ִ�е�sql��䣺" + sql.toString());
					}
				}
				if ((success + error) % 10000 == 0) {
					System.out.println("-------->�Ѿ�ִ����" + (success + error) + "�����ݣ������ɹ���ʧ�ܵ�������");
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
		System.out.println("���ݲ�����ϣ��ɹ�������" + success + "��ʧ��������" + error);
	}

	@SuppressWarnings("rawtypes")
	public List getListBysql(String sql) {
		List list = null;
		try {
			list = baseDao.getList(sql);
		} catch (Exception e) {
			System.out.println("��ѯʧ�ܣ�");
			e.printStackTrace();
		}
		return list;
	}

	@Override
	public void insertToDB(String sql) {
		try {
			baseDao.insertToDB(sql);
			System.out.println("ִ�гɹ���");
		} catch (Exception e) {
			System.out.println("ִ��ʧ�ܣ�");
			e.printStackTrace();
		}
	}
}
