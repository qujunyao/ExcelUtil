package operation;

import mainUtil.StreamUtil;

public interface StreamOperation {

	/**
	 * 导出流文件
	 * 
	 * @param streamUtil
	 */
	public void exportStream(StreamUtil streamUtil);

	public void insertByStream(String sql, byte[] b);

}