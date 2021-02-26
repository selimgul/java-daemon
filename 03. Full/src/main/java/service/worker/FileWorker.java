package service.worker;

import service.threading.Worker;
import service.threading.WorkerContext;

public class FileWorker extends Worker {

  public FileWorker(WorkerContext ctx) {
    super(ctx);
  }

  @Override
  public void Execute() {
    logger.info(String.format("%s :: %s", ctx.getWorkerGroup().getWorkerGroupInfo().getName(),
        ctx.getWorkerID()));
  }

}