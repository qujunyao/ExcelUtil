package mainUtil;

public class MyThreadPrinter2 implements Runnable {

	private String name;
	private Object prev;
	private Object self;

	private MyThreadPrinter2(String name, Object prev, Object self) {
		this.name = name;
		this.prev = prev;
		this.self = self;
	}

	@Override
	public void run() {
		int count = 5;
		while (count > 0) {
			synchronized (prev) {

				synchronized (self) {
					System.out.println(name + "*****" + name + "的线程");
					count--;

					self.notify();
					System.out.println("释放" + name + "锁--------" + name + "的线程");
				}
				try {
					System.out.println("+++" + prev + "对象释放并等待" + name + "的线程");
					prev.wait();
					System.out.println("---" + name + "的线程");
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}

		}
	}

	public static void main(String[] args) throws Exception {
		Object a = new Object();
		Object b = new Object();
		Object c = new Object();
		System.out.println("a->" + a);
		System.out.println("b->" + b);
		System.out.println("c->" + c);
		MyThreadPrinter2 pa = new MyThreadPrinter2("A", c, a);
		MyThreadPrinter2 pb = new MyThreadPrinter2("B", a, b);
		MyThreadPrinter2 pc = new MyThreadPrinter2("C", b, c);

		new Thread(pa).start();
		Thread.sleep(100); // 确保按顺序A、B、C执行
		new Thread(pb).start();
		Thread.sleep(100);
		new Thread(pc).start();
		Thread.sleep(100);
	}
}