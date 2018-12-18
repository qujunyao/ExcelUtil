package thread;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Main {
	public static void main(String[] args) throws InterruptedException {
		List<TaskItem> taskItems = new ArrayList<TaskItem>();
		for (int i = 0; i < 20; i++) {
			taskItems.add(new TaskItem(i, "task " + i));
		}

		CountDownLatch threadsSignal = new CountDownLatch(taskItems.size());
		ExecutorService executor = Executors.newFixedThreadPool(taskItems.size());
		List<Future<MyTaskResult>> resultLazyItems = new ArrayList<Future<MyTaskResult>>();
		System.out.println("���߳̿�ʼ���벢�������ύ");
		for (TaskItem taskItem : taskItems) {
			// ʹ��future�洢���߳�ִ�к󷵻ؽ�����������������̶߳���ɺ�ſ���ʹ��get();
			// ���������ʹ��get(),����ɵȴ�ͬ����
			Future<MyTaskResult> future = executor.submit(new MyTask(threadsSignal, taskItem));
			resultLazyItems.add(future);
		}
		System.out.println("���߳̿�ʼ�߳����������ύ");
		System.out.println("���߳̽���ȴ��׶Σ��ȴ����в������߳�������ɣ�����������");
		// �ȴ����в������߳�������ɡ�
		threadsSignal.await();
		// ��������ֹ�̵߳����У����ǽ�ֹ�����Executor������µ�����
		executor.shutdown();
		System.out.println("���߳��߳��ȴ��׶Σ��ȴ����в������߳�������ɣ�����������");

		for (Future<MyTaskResult> future : resultLazyItems) {
			try {
				MyTaskResult result = future.get();
				System.out.println(result.getName());
			} catch (ExecutionException e) {
				e.printStackTrace();
			}
		}
	}

}