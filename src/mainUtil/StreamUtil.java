package mainUtil;


import operation.StreamOperation;
import util.DAOBeanFactory;

/**
 * 流文件操作类
 * 
 * @author Administrator
 * 
 */
public class StreamUtil {

	/** 对象实例 */
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
	 * 私有构造方法
	 */
	private StreamUtil() {

	}

	/**
	 * 获取对象实例 单例模式
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
		System.out.println("开始执行方法，计时开始！！！");
		long start = System.currentTimeMillis();
		util.getStreamOperation().exportStream(util);
		long end = System.currentTimeMillis();
		System.out.println("方法执行完成！用时" + (end - start) * 0.001 + "秒");
		
		
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
