package thread;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

public class TestMain {
	public static void main(String[] args) throws InterruptedException, ExecutionException {
		new TestMain().exec();
	}

	void exec() throws InterruptedException, ExecutionException {
		// �����첽�����б�
		List<FutureTask<Integer>> futureTasks = new ArrayList<FutureTask<Integer>>();
		// �̳߳� ��ʼ��ʮ���߳� ��JDBC���ӳ���һ����˼ ʵ������
		ExecutorService executorService = Executors.newFixedThreadPool(220);
		long start = System.currentTimeMillis();
		// ������run������ʵ�� Callable��һ���ӿڣ���call����д�߼�����
		Callable<Integer> callable = new Callable<Integer>() {
			@Override
			public Integer call() throws Exception {
				Integer res = new Random().nextInt(100);
				Thread.sleep(1000);
				System.out.println("����ִ��:��ȡ����� :" + res);
				return res;
			}
		};

		for (int i = 0; i < 10; i++) {
			// ����һ���첽����
			FutureTask<Integer> futureTask = new FutureTask<Integer>(callable);
			futureTasks.add(futureTask);
			// �ύ�첽�����̳߳أ����̳߳ع������� ��ˬ�ѡ�
			// �������첽���������������ﲢ��������
			executorService.submit(futureTask);
		}

		int count = 0;
		for (FutureTask<Integer> futureTask : futureTasks) {
			// futureTask.get() �õ�������Ҫ�Ľ��
			// �÷�����һ������get(long timeout, TimeUnit unit) ��һ������Ϊ���ȴ�ʱ�䣬�ڶ���Ϊʱ��ĵ�λ
			count += futureTask.get();
		}
		long end = System.currentTimeMillis();
		System.out.println("�̳߳ص�����ȫ�����:���Ϊ:" + count + "��main�̹߳رգ������̵߳�����");
		System.out.println("ʹ��ʱ�䣺" + (end - start) + "ms");
		// �����̳߳�
		executorService.shutdown();

	}
}
