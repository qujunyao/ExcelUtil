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
		System.out.println("主线程开始进入并行任务提交");
		for (TaskItem taskItem : taskItems) {
			// 使用future存储子线程执行后返回结果，必须在所有子线程都完成后才可以使用get();
			// 如果在这里使用get(),会造成等待同步。
			Future<MyTaskResult> future = executor.submit(new MyTask(threadsSignal, taskItem));
			resultLazyItems.add(future);
		}
		System.out.println("主线程开始走出并行任务提交");
		System.out.println("主线程进入等待阶段（等待所有并行子线程任务完成）。。。。。");
		// 等待所有并行子线程任务完成。
		threadsSignal.await();
		// 并不是终止线程的运行，而是禁止在这个Executor中添加新的任务
		executor.shutdown();
		System.out.println("主线程走出等待阶段（等待所有并行子线程任务完成）。。。。。");

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