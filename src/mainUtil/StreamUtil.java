package mainUtil;


import operation.StreamOperation;
import util.DAOBeanFactory;

/**
 * ���ļ�������
 * 
 * @author Administrator
 * 
 */
public class StreamUtil {

	/** ����ʵ�� */
	private static StreamUtil instance;
	private String sql;
	private String outpath;
	private String PK;
	private String suffix;
	private StreamOperation streamOperation;

	public String getSql() {
		return sql;
	}

	public String getOutpath() {
		return outpath;
	}

	public String getPK() {
		return PK;
	}

	public String getSuffix() {
		return suffix;
	}

	public StreamOperation getStreamOperation() {
		return streamOperation;
	}

	/**
	 * ˽�й��췽��
	 */
	private StreamUtil() {

	}

	/**
	 * ��ȡ����ʵ�� ����ģʽ
	 * 
	 * @return
	 */
	public static StreamUtil getInstance() {
		if (instance == null) {
			instance = (StreamUtil) DAOBeanFactory.getBean("streamUtil");
		}
		return instance;
	}

	public void init() {
		this.sql = DAOBeanFactory.getConfig("stream_sql");
		this.outpath = DAOBeanFactory.getConfig("stream_path");
		this.PK = DAOBeanFactory.getConfig("stream_PK");
		this.suffix = DAOBeanFactory.getConfig("stream_suffix");
		this.streamOperation = (StreamOperation) DAOBeanFactory.getBean("streamOperation");
	}

	public static void main(String[] args) {
		StreamUtil util = StreamUtil.getInstance();
		util.init();
		System.out.println("��ʼִ�з�������ʱ��ʼ������");
		long start = System.currentTimeMillis();
		util.getStreamOperation().exportStream(util);
		long end = System.currentTimeMillis();
		System.out.println("����ִ����ɣ���ʱ" + (end - start) * 0.001 + "��");
		
		
//		StreamUtil util = StreamUtil.getInstance();
//		util.init();
//		String path = "pic";
//		LinkedList<File> files = FileUtils.traverseFolder2(path);
//		byte[] bs = null;
//		for (File file : files) {
//			try {
//				bs = FileUtils.toByteArray3(file.getAbsolutePath());
//				String sql = "insert into k_firmpicture values(2,'abcde',?,sysdate)";
//				util.getStreamOperation().insertByStream(sql, bs);
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//		}
	}
}
