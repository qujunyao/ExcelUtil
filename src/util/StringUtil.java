package util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import model.StringBlock;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * �ַ���ʵ����
 */
public class StringUtil {

	/**
	 * �����ַ�ת�弯��
	 */
	private static Map<String, String> mapIndex = new HashMap<String, String>();

	static {
		mapIndex.put("$", "\\$");
		mapIndex.put("^", "\\^");
		mapIndex.put("*", "\\*");
		mapIndex.put("(", "\\(");
		mapIndex.put(")", "\\)");
		mapIndex.put("+", "\\+");
		mapIndex.put("|", "\\|");
		mapIndex.put("\\", "\\\\");
		mapIndex.put("\\\"", "\\\"");
		mapIndex.put("?", "\\?");
		mapIndex.put(".", "\\.");
	}

	/**
	 * �ַ�������ָ���ַ��ָ�
	 * 
	 * @param str
	 * @param index
	 * @return
	 */
	public static String[] spileString(String str, String index) {
		StringBuffer s1 = new StringBuffer();
		if (index.length() >= 2) {
			char[] cs = index.toCharArray();
			for (char a : cs) {
				String s = mapIndex.get(String.valueOf(a));
				if (s == null) {
					s1.append(String.valueOf(a));
				} else {
					s1.append(s);
				}
			}
		} else {
			s1.append(mapIndex.get(index));
		}
		if (!"null".equals(s1.toString())) {
			index = s1.toString();
		}
		return str.split(index, -5);
	}

	/**
	 * �༭����
	 * 
	 * @param ����
	 * @param ����
	 * @return
	 */
	public static Object EditCondition(String date, String type) {
		if (date == null || "".equals(date)) {
			return "null";
		}
		if ("VARCHAR2".equals(type) || "VARCHAR".equals(type) || "CHAR".equals(type)) {
			if (date != null && date.contains("'")) {
				return "'" + date.replace("'", "''") + "'";
			} else {
				return "'" + date + "'";
			}
		} else if ("NUMBER".equals(type)) {
			return "".equals(date) ? "null" : date;
		} else if ("DATE".equals(type)) {
			return "to_date('" + date + "','yyyy-mm-dd HH24:mi:ss')";
		} else {
			return "'" + date + "'";
		}
	}

	/**
	 * �ַ���ȥ˫����
	 * 
	 * @param str
	 * @return
	 */
	public static String[] removeQuotes(String[] str) {
		String[] temp = str;
		boolean f1 = false;
		boolean f2 = false;
		for (int i = 0; i < str.length; i++) {
			if (str[i].length() > 2) {
				if (str[i].startsWith("\"")) {
					f1 = true;
				}
				if (str[i].endsWith("\"")) {
					f2 = true;
				}
				if (f1 && f2) {
					temp[i] = str[i].substring(1, str[i].length() - 1);
					f1 = f2 = false;
					int count = CountStr.countStr(temp[i], "\"");
					if (count > 0) {
						temp[i] = temp[i].replace("\"\"", "\"");
					}
				}

			} else if (str[i].length() == 2) {
				if (str[i].startsWith("\"")) {
					if (str[i].endsWith("\"")) {
						temp[i] = "";
					}
				}
			}
		}
		return temp;
	}

	/**
	 * �ж�һ���ַ�����ĳ���ַ�������
	 * 
	 * @param text
	 *            �ַ���
	 * @param sub
	 *            �ַ�
	 * @return ���ֵĴ���
	 */
	public static int count(String text, String sub) {
		int count = 0, start = 0;
		while ((start = text.indexOf(sub, start)) >= 0) {
			start += sub.length();
			count++;
		}
		return count;
	}

	/**
	 * ��żУ��
	 * 
	 * @param t
	 * @return true ż����false ����
	 */
	public static boolean Parity(int t) {
		return t % 2 == 0 ? true : false;
	}

	/**
	 * �ַ���������
	 * 
	 * @param str
	 *            �ַ�����
	 * @param list
	 *            �ַ��鼯��
	 * @return
	 */
	public static String[] StringReduction(String[] str, List<StringBlock> list) {
		List<String> userList = new ArrayList<String>();
		Collections.addAll(userList, str);
		for (int i = list.size() - 1; i >= 0; i--) {
			int start = list.get(i).getStart();
			int end = list.get(i).getEnd();
			String content = list.get(i).getContent();
			userList.set(start, content);
			int t = start + 1;
			for (int j = start + 1; j < end + 1; j++) {
				userList.remove(t);
			}
		}
		String[] array = new String[userList.size()];
		String[] s = userList.toArray(array);
		return s;
	}

	/**
	 * ��ȡ���������
	 * 
	 * @param o
	 * @return
	 */
	public static String getType(Object o) {
		return o == null ? "" : o.getClass().toString();
	}

}
