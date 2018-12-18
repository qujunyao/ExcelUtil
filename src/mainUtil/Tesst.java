package mainUtil;

public class Tesst {

	public static void main(String[] args) {
		MyThread thread = new MyThread();
		new Thread(thread).start();
	}
}
