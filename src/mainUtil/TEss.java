package mainUtil;

import java.io.File;
import java.util.LinkedList;

import util.FileUtils;

public class TEss {
	public static void main(String[] args) {
		StreamUtil util = StreamUtil.getInstance();
		util.init();
		String path = "pic";
		LinkedList<File> files = FileUtils.traverseFolder2(path);
		byte[] bs = null;
		for (File file : files) {
			try {
				bs = FileUtils.toByteArray3(file.getAbsolutePath());
				String sql = "insert into k_firmpicture values(2,'abcde',?,sysdate)";
				util.getStreamOperation().insertByStream(sql, bs);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
