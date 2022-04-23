package lab9;

/**
 *
 * COMP 3021
 *
This is a class that prints the maximum value of a given array of 90 elements

This is a single threaded version.

Create a multi-thread version with 3 threads:

one thread finds the max among the cells [0,29]
another thread the max among the cells [30,59]
another thread the max among the cells [60,89]

Compare the results of the three threads and print at console the max value.

 *
 * @author valerio
 *
 */
public class FindMax {
	// this is an array of 90 elements
	// the max value of this array is 9999
	static int[] array = { 1, 34, 5, 6, 343, 5, 63, 5, 34, 2, 78, 2, 3, 4, 5, 234, 678, 543, 45, 67, 43, 2, 3, 4543,
			234, 3, 454, 1, 2, 3, 1, 9999, 34, 5, 6, 343, 5, 63, 5, 34, 2, 78, 2, 3, 4, 5, 234, 678, 543, 45, 67, 43, 2,
			3, 4543, 234, 3, 454, 1, 2, 3, 1, 34, 5, 6, 5, 63, 5, 34, 2, 78, 2, 3, 4, 5, 234, 678, 543, 45, 67, 43, 2,
			3, 4543, 234, 3, 454, 1, 2, 3 };

	public static void main(String[] args) {
		FindMax haha = new FindMax();
		haha.printMax();
		try {
			haha.printMaxMultiThread();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void printMax() {
		// this is a single threaded version
		int max = findMax(0, array.length - 1);
		System.out.println("the max value is " + max);
	}

	class findMaxThread implements Runnable {
		private int begin;
		private int end;
		private int returnValue;

		public findMaxThread(int i, int j) {
			// TODO Auto-generated constructor stub
			begin = i;
			end = j;
		}

		@Override
		public void run() {
			// TODO Auto-generated method stub
			returnValue = findMax(begin, end);
		}

		public int result() {
			return returnValue;
		}
	}

	public void printMaxMultiThread() throws InterruptedException {
		findMaxThread task1 = new findMaxThread(0, 29);
		findMaxThread task2 = new findMaxThread(30, 59);
		findMaxThread task3 = new findMaxThread(60, 89);

		Thread thread1 = new Thread(task1);
		Thread thread2 = new Thread(task2);
		Thread thread3 = new Thread(task3);

		thread1.start();
		thread2.start();
		thread3.start();

		thread1.join();
		thread2.join();
		thread3.join();

		int max1 = task1.result();
		int max2 = task2.result();
		int max3 = task3.result();

		System.out.println();
		System.out.println("Max from thread 1 = " + max1);
		System.out.println("Max from thread 2 = " + max2);
		System.out.println("Max from thread 3 = " + max3);

		int max = Integer.max(max1, Integer.max(max2, max3) );
		System.out.println("the max value out of the threed threads is " + max);
	}

	/**
	 * returns the max value in the array within a give range [begin,range]
	 *
	 * @param begin
	 * @param end
	 * @return
	 */
	private int findMax(int begin, int end) {
		// you should NOT change this function
		int max = array[begin];
		for (int i = begin + 1; i <= end; i++) {
			if (array[i] > max) {
				max = array[i];
			}
		}
		return max;
	}
}


