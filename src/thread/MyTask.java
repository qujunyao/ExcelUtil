package thread;

import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;

public class MyTask implements Callable<MyTaskResult> {
	private final TaskItem taskItem;
	private final CountDownLatch threadsSignal;

	public MyTask(CountDownLatch threadsSignal, TaskItem taskItem) {
		this.threadsSignal = threadsSignal;
		this.taskItem = taskItem;
	}

	@Override
	public MyTaskResult call() throws Exception {
		MyTaskResult result = new MyTaskResult(this.taskItem.getName());

		// ���Ĵ����߼�����
		Thread.sleep(2000);

		System.out.println("task id:" + taskItem.getId() + " >>>>�ȴ��Y��");
		System.out.println("task id:" + taskItem.getId() + " >>>>�߳�����:" + Thread.currentThread().getName() + "����. ����" + threadsSignal.getCount() + " ���߳�");

		// ����Ⱥ��Ĵ����߼�������ɺ�ſ��Լ�1
		this.threadsSignal.countDown();

		return result;
	}

}