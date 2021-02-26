package service.threading;

public class WorkerContext {

	private WorkerGroup workerGroup;
	private int workerID;
	private boolean finishJob = false;
	private int delay = 100;

	public WorkerContext(WorkerGroup workerGroup, int workerID, int delay) {
		this.workerGroup = workerGroup;
		this.workerID = workerID;
		this.delay = delay;
	}
	
	public static WorkerContext getDefault() {
		return new WorkerContext(null, 1, 100);
	}

	public WorkerGroup getWorkerGroup() {
		return workerGroup;
	}

	public void setWorkerGroup(WorkerGroup workerGroup) {
		this.workerGroup = workerGroup;
	}

	public int getWorkerID() {
		return workerID;
	}

	public void setWorkerID(int workerID) {
		this.workerID = workerID;
	}

	public boolean isFinishJob() {
		return finishJob;
	}

	public void setFinishJob(boolean finishJob) {
		this.finishJob = finishJob;
	}

	public int getDelay() {
		return delay;
	}

	public void setDelay(int delay) {
		this.delay = delay;
	}

}
