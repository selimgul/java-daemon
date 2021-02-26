package Daemon.Test1;

// http://www.apache.org/dist/commons/daemon/binaries/windows/

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class TestService implements Runnable {

	private static final long MS_PER_SEC = 1000L;

	private static Thread loggerThread;

	private static final Random random = new Random();

	private static final DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss ");

	private static void log(String message) {
		System.out.println(df.format(new Date()) + message);
	}

	public void run() {
		log("Thread started");

		while ( true ) {
			long sleepTime = random.nextInt(4) + 1;

			try {
				log("pausing " + sleepTime + " seconds");
				Thread.sleep(sleepTime * MS_PER_SEC);
			} catch ( InterruptedException e ) {
				log("Exiting");
				break;
			}
		}
	}

	private static void startThread() {
		log("Starting the thread");
		loggerThread = new Thread(new TestService());
		loggerThread.start();
	}

	public static void start(String[] args) {
		startThread();
		synchronized ( loggerThread ) {
			try {
				loggerThread.wait();
			} catch ( InterruptedException e ) {
				log("'Wait' interrupted: " + e.getMessage());
			}
		}
	}

	public static void stop(String[] args) {
		if ( loggerThread != null ) {
			log("Stopping the thread");
			loggerThread.interrupt();
			synchronized ( loggerThread ) {
				loggerThread.notify();
			}
		} else {
			log("No thread to interrupt");
		}
	}

	public static void main(String[] args) {

		Runtime.getRuntime().addShutdownHook(new Thread() {
			public void run() {
				TestService.stop(new String[] {});
			}
		});

		start(args);
	}
}
