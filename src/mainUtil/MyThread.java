package mainUtil;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.net.URLDecoder;
import java.security.PublicKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import javax.security.cert.X509Certificate;

public class MyThread implements Runnable {

	private volatile boolean stop = false;

	/**
	 * 停止线程
	 */
	public void pleaseStop() {
		this.stop = true;
	}

	@Override
	public void run() {
		while (!stop) {
			File file = new File("C:\\Users\\Administrator\\Desktop\\issue_core.jar");
			boolean flag = false;
			try {
				flag = verification(file);
				System.out.println(flag);
				Thread.sleep(6 * 1000);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 验证证书是否为我们公司自己的。
	 * 
	 * @param file
	 * @return true 成功，false 失败
	 * @throws Exception
	 */
	private static boolean verification(File file) throws Exception {
		byte[] content = readFile(file);
		if (content == null) {
			return false;
		}
		ByteArrayInputStream stream = new ByteArrayInputStream(content);
		X509Certificate cert = X509Certificate.getInstance(stream);
		PublicKey publicKey = cert.getPublicKey();
		String mi = "17973307399984012030777021951811475627184816862721829530627940712024631360405074455578249911676295932543939589272026313773180526758935931350546490907648161109387457265133163607197403126873787142124183862752686577590134160463071481677075297194939184080508128760173249649450896017814742976164481135003480409145940033596380660214366030394688432736631978384550509439103211594228445456277439449294641246708939906446105805404577628902972618078006276952735006098176018497175013566462971809780824530907419368595959653117805830496434191164576842490896037364047814797236087735962079220912684788385281094638861485208666815888079";
		String ming = decodeByFile(mi, publicKey);
		boolean flag = false;
		if (ming != null && !"".equals(ming)) {
			if ("\u9a8c\u8bc1\u5931\u8d25\uff01".equals(ming)) {
				flag = true;
			}
		}
		return flag;
	}

	/**
	 * 通过公钥文件对待解密的字符串进行解密
	 * 
	 * @param str
	 *            待解密的字符串
	 * @param publicKeyURI
	 *            公钥文件地址
	 * @return String 解密后的原字符串
	 */
	private static String decodeByFile(String str, PublicKey publicKey) {

		RSAPublicKey pk = (RSAPublicKey) publicKey;
		BigInteger modulus = pk.getModulus();
		BigInteger exponent = pk.getPublicExponent();

		BigInteger strBig = new BigInteger(str);

		byte[] strbyte = strBig.modPow(exponent, modulus).toByteArray();
		// System.out.println(strbyte.length);

		try {
			return URLDecoder.decode(new String(strbyte), "GBK");
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException("通过公钥进行解密异常", e);
		}
	}

	/**
	 * 数组缩容
	 * 
	 * @param ary
	 * @param start
	 * @param end
	 * @return
	 */
	private static byte[] arraySmall(byte[] ary, int start, int end) {
		List<Byte> userList = new LinkedList<Byte>();
		for (byte i : ary) {
			userList.add(i);
		}
		for (int i = 0; i < end - start + 1; i++) {
			userList.remove(start);
		}
		return listTobyte(userList);
	}

	/**
	 * list转数组
	 * 
	 * @param list
	 * @return
	 */
	private static byte[] listTobyte(List<Byte> list) {
		if (list == null || list.size() < 0) {
			return null;
		}
		byte[] bytes = new byte[list.size()];
		int i = 0;
		Iterator<Byte> iterator = list.iterator();
		while (iterator.hasNext()) {
			bytes[i] = iterator.next();
			i++;
		}
		return bytes;
	}

	/**
	 * 读文件到字节数组
	 * 
	 * @param file
	 * @return
	 * @throws Exception
	 */
	private static byte[] readFile(File file) throws Exception {
		if (file.exists() && file.isFile()) {
			JarFile jar = null;
			try {
				jar = new JarFile(file);
			} catch (Exception e) {
				return null;
			}
			if (!onlyOneFile(jar)) {
				jar.close();
				return null;
			}
			JarEntry entry1 = jar.getJarEntry("META-INF/JARSIGNE.RSA");
			long fileLength = entry1.getSize();
			if (fileLength > 0L) {
				InputStream fis = null;
				try {
					fis = jar.getInputStream(entry1);
				} catch (Exception e) {
				}
				byte[] b = new byte[(int) fileLength];
				fis.read(b);
				byte[] bs = arraySmall(b, 2, 57);
				fis.close();
				fis = null;
				return bs;
			}
		} else {
			return null;
		}
		return null;
	}

	private static boolean onlyOneFile(JarFile jar) {
		Enumeration enu = jar.entries();
		int count = 0;
		while (enu.hasMoreElements()) {
			JarEntry entry = (JarEntry) enu.nextElement();
			String name = entry.getName();
			String regex = "META-INF/[a-zA-Z0-9]+.RSA";
			if (name.matches(regex)) {
				count++;
			}
		}
		if (count == 1) {
			return true;
		} else {
			return false;
		}
	}

}
