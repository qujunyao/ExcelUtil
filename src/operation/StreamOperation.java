package operation;

import mainUtil.StreamUtil;

public interface StreamOperation {

	/**
	 * �������ļ�
	 * 
	 * @param streamUtil
	 */
	public void exportStream(StreamUtil streamUtil);

	public void insertByStream(String sql, byte[] b);

}