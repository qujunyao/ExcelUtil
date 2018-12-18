package thread;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;

/* 
 * 
 * 计算1^2+2^2+3^2+4^2+5^2+6^2通过二分来分段计算,而二分法的会将数组分成若干段,然后分别进行计算 
 * */
public class FibonacciAction extends RecursiveAction {
	/**
	 * 
	 */
	private static final long serialVersionUID = -117894325566400187L;
	final double[] array;
	final int lo, hi;
	double result;
	FibonacciAction next; // keeps track ofright-hand-side tasks

	FibonacciAction(double[] array, int lo, int hi, FibonacciAction next) {
		this.array = array;
		this.lo = lo;
		this.hi = hi;
		this.next = next;
	}

	/* 计算数组下表从[l,h)平方和 */
	double atLeaf(int l, int h) {
		double sum = 0;
		for (int i = l; i < h; ++i)
			// performleftmost base step
			sum += array[i] * array[i];
		return sum;
	}

	/* 分治(递归过程) */
	protected void compute() {
		int l = lo;
		int h = hi;
		FibonacciAction right = null;
		/* 二分,并且要求SurplusQueuedTaskCount小于3 */
		while (h - l > 1 && getSurplusQueuedTaskCount() <= 3) {
			int mid = (l + h) >>> 1;
			/* 创建一个子任务 */
			right = new FibonacciAction(array, mid, h, right);
			right.fork();
			h = mid;
		}
		double sum = atLeaf(l, h);
		while (right != null) {
			/* 用剩余时间去执行队列中的其他线程任务,提高效率,使得线程处于忙碌状态 */
			if (right.tryUnfork()) // directlycalculate if not stolen
				sum += right.atLeaf(right.lo, right.hi);
			else {
				/* 等待执行结果 */
				right.join();
				sum += right.result;
			}
			right = right.next;
		}
		result = sum;
	}

	public static void main(String[] args) {
		System.out.println(sumOfSquares(new ForkJoinPool(), new double[] { 1, 2, 3, 4, 5, 6 }));
	}

	static double sumOfSquares(ForkJoinPool pool, double[] array) {
		int n = array.length;
		FibonacciAction a = new FibonacciAction(array, 0, n, null);
		pool.invoke(a);
		return a.result;
	}
}