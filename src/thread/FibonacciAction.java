package thread;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;

/* 
 * 
 * ����1^2+2^2+3^2+4^2+5^2+6^2ͨ���������ֶμ���,�����ַ��ĻὫ����ֳ����ɶ�,Ȼ��ֱ���м��� 
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

	/* ���������±��[l,h)ƽ���� */
	double atLeaf(int l, int h) {
		double sum = 0;
		for (int i = l; i < h; ++i)
			// performleftmost base step
			sum += array[i] * array[i];
		return sum;
	}

	/* ����(�ݹ����) */
	protected void compute() {
		int l = lo;
		int h = hi;
		FibonacciAction right = null;
		/* ����,����Ҫ��SurplusQueuedTaskCountС��3 */
		while (h - l > 1 && getSurplusQueuedTaskCount() <= 3) {
			int mid = (l + h) >>> 1;
			/* ����һ�������� */
			right = new FibonacciAction(array, mid, h, right);
			right.fork();
			h = mid;
		}
		double sum = atLeaf(l, h);
		while (right != null) {
			/* ��ʣ��ʱ��ȥִ�ж����е������߳�����,���Ч��,ʹ���̴߳���æµ״̬ */
			if (right.tryUnfork()) // directlycalculate if not stolen
				sum += right.atLeaf(right.lo, right.hi);
			else {
				/* �ȴ�ִ�н�� */
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