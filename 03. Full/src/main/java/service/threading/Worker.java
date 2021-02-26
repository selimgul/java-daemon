package service.threading;

import service.extensibility.ObjectBooster;

public class Worker extends ObjectBooster implements Runnable {

	private Thread thread;
	protected WorkerContext ctx;

	public Worker(WorkerContext ctx) {
		this.ctx = ctx;
	}

	public void Start() {
		thread = new Thread(this);
		thread.start();
	}

	public void Interrupt() {
		thread.interrupt();
	}

	public void Terminate() {
		ctx.setFinishJob(true);
	}

	public void Execute() {
	}

	public void Join() {
		try {
			thread.join();
		} catch ( InterruptedException e ) {
		}
	}

	@Override
	public void run() {

		try {
			while ( !ctx.isFinishJob()) {
				Execute();
				Thread.sleep(ctx.getDelay());
			}
		} catch ( InterruptedException e ) {
		} catch ( Exception e ) {
			logger.error(e.getMessage());
		}

	}
}
