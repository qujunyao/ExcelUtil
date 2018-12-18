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

		// 核心处理逻辑处理
		Thread.sleep(2000);

		System.out.println("task id:" + taskItem.getId() + " >>>>等待結束");
		System.out.println("task id:" + taskItem.getId() + " >>>>线程名称:" + Thread.currentThread().getName() + "结束. 还有" + threadsSignal.getCount() + " 个线程");

		// 必须等核心处理逻辑处理完成后才可以减1
		this.threadsSignal.countDown();

		return result;
	}

}