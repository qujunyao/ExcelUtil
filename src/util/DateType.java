package util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DateType {
	public static class DateSelect {
		public boolean isDate(String date) {
			/**
			 * �ж����ڸ�ʽ�ͷ�Χ
			 */
			String rexp = "^((\\d{2}(([02468][048])|([13579][26]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])))))|(\\d{2}(([02468][1235679])|([13579][01345789]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|(1[0-9])|(2[0-8]))))))";

			Pattern pat = Pattern.compile(rexp);

			Matcher mat = pat.matcher(date);

			boolean dateType = mat.matches();

			return dateType;
		}
	}

	public static void main(String[] args) {
		/**
		 * ���ڸ�ʽ��ȷ
		 */
		String date1 = "2014/01/03";
		/**
		 * ���ڷ�Χ����ȷ---ƽ�����û��29��
		 */
		String date2 = "2014-02-29";
		/**
		 * �����·ݷ�Χ����ȷ---�·�û��13��
		 */
		String date3 = "2014-13-03";
		/**
		 * ���ڷ�Χ����ȷ---����û��31��
		 */
		String date4 = "2014-06-31";
		/**
		 * ���ڷ�Χ����ȷ ----1�³���31��
		 */
		String date5 = "2014-01-32";
		/**
		 * ����������
		 */
		String date6 = "0014-01-03";

		DateSelect date = new DateSelect();

		/**
		 * ��ӡ��ȷ���ڸ�ʽ
		 */
		System.out.println(date.isDate(date1));
		/**
		 * ��ӡdate1
		 */
		System.out.println(date.isDate(date2));
		/**
		 * ��ӡdate3
		 */
		System.out.println(date.isDate(date3));
		/**
		 * ��ӡdate4
		 */
		System.out.println(date.isDate(date4));
		/**
		 * ��ӡdate5
		 */
		System.out.println(date.isDate(date5));
		/**
		 * ��ӡdate6
		 */
		System.out.println(date.isDate(date6));
	}

}