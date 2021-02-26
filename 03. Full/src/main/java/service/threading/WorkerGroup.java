package service.threading;

import java.util.ArrayList;
import java.util.List;

import service.extensibility.ObjectBooster;

public class WorkerGroup extends ObjectBooster {

	private WorkerGroupInfo workerGroupInfo;

	private List<Worker> _lstWorkerList = new ArrayList<Worker>();

	public WorkerGroup(WorkerGroupInfo workerGroupInfo) {
		this.workerGroupInfo = workerGroupInfo;
	}

	public WorkerGroupInfo getWorkerGroupInfo() {
		return workerGroupInfo;
	}

	public void setWorkerGroupInfo(WorkerGroupInfo workerGroupInfo) {
		this.workerGroupInfo = workerGroupInfo;
	}
	
	public boolean CreateWorkers() {

		try {
			Class<?> cls = Class.forName(workerGroupInfo.getType());

			for (int i = 0; i < workerGroupInfo.getCount(); i++) {
				WorkerContext ctx = new WorkerContext(this, i, workerGroupInfo.getDelay());
				Worker worker = (Worker) cls.getConstructor(WorkerContext.class).newInstance(ctx);
				_lstWorkerList.add(worker);
			}

			return true;
		} catch (Exception e) {
			logger.error(e.getMessage());
			return false;
		}
	}

	public boolean StartWorkers() {
		try {
			for (int i = 0; i < workerGroupInfo.getCount(); i++) {
				_lstWorkerList.get(i).Start();
			}
			return true;
		} catch (Exception e) {
			logger.error(e.getMessage());
			return false;
		}
	}

	public boolean StopWorkers() {

		try {
			for (int i = 0; i < workerGroupInfo.getCount(); i++) {
				_lstWorkerList.get(i).Terminate();
				_lstWorkerList.get(i).Interrupt();
			}
			return true;
		} catch (Exception e) {
			logger.error(e.getMessage());
			return false;
		}
	}

	public boolean WaitWorkers() {
		try {
			for (int i = 0; i < workerGroupInfo.getCount(); i++) {
				_lstWorkerList.get(i).Join();
			}
			return true;
		} catch (Exception e) {
			logger.error(e.getMessage());
			return false;
		}
	}

}
