package service.threading;

import java.util.ArrayList;
import java.util.List;

import service.config.Config;

public class WorkerGroupManager extends Worker {

	private List<WorkerGroup> lstWorkerGroupList = new ArrayList<WorkerGroup>();
	private boolean isRunning = false;

	public WorkerGroupManager() {
		super(WorkerContext.getDefault());
	}

	private boolean createWorkerGroups() {
		try {

			int iWorkerGroupCount = Integer.parseInt(Config.getProperty("workergroup.groups.count"));

			for (int i = 0; i < iWorkerGroupCount; i++) {

				String strGroupPrefix = String.format("workergroup.groups[%s]", i);

				WorkerGroupInfo wgi = new WorkerGroupInfo();
				wgi.setName(Config.getProperty(String.format("%s.%s", strGroupPrefix, "name")));
				wgi.setType(Config.getProperty(String.format("%s.%s", strGroupPrefix, "type")));
				wgi.setCount(Config.getProperty(String.format("%s.%s", strGroupPrefix, "count"), Integer.class));
				wgi.setDelay(Config.getProperty(String.format("%s.%s", strGroupPrefix, "delay"), Integer.class));

				WorkerGroup wg = new WorkerGroup(wgi);
				wg.CreateWorkers();
				lstWorkerGroupList.add(wg);

				logger.info(wgi.getName() + " group created.");
			}

			return true;
		} catch (Exception e) {
			logger.error(e.getMessage());
			return false;
		}
	}

	private boolean startWorkerGroups() {

		try {
			for (WorkerGroup workerGroup : lstWorkerGroupList) {
				workerGroup.StartWorkers();
			}

			isRunning = true;

			return true;
		} catch (Exception e) {
			logger.error(e.getMessage());
			return false;
		}

	}

	private boolean sendTerminationSignal() {
		try {
			for (WorkerGroup workerGroup : lstWorkerGroupList) {
				workerGroup.StopWorkers();
			}
			return true;

		} catch (Exception e) {
			logger.error(e.getMessage());
			return false;
		}
	}

	private boolean waitForExit() {

		try {
			for (WorkerGroup workerGroup : lstWorkerGroupList) {
				workerGroup.WaitWorkers();
			}
			
			isRunning = false;
			
			return true;
		} catch (Exception e) {
			logger.error(e.getMessage());
			return false;
		}
	}

	public boolean Stop() {

		try {
			if (!sendTerminationSignal()) {
				return false;
			}

			return waitForExit();
		} catch (Exception e) {
			logger.error(e.getMessage());
			return false;
		}
	}

	@Override
	public void run() {
		createWorkerGroups();
		startWorkerGroups();
		waitForExit();
	}

	public boolean isRunning() {
		return isRunning;
	}

}
