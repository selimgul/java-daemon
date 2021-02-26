package service.entrypoint.daemon;

import java.time.LocalDate;
import org.apache.commons.daemon.Daemon;
import org.apache.commons.daemon.DaemonContext;
import service.entrypoint.springboot.SpringBootApp;
import service.extensibility.ObjectBooster;
import service.threading.WorkerGroupManager;

public class DaemonApp extends ObjectBooster implements Daemon {

  /*
   * Daemon uygulamasının merkezi DaemonApp sınıfıdır. Uygulama 3 farklı şekilde
   * başlatılabilir.
   *
   * 1) Console application olarak; Uygulama console app olarak (public static
   * void main) başlatılırsa DaemonApp sınıfının static Run metodu çağrılır ve
   * yine static DaemonApp instance'ı üzerinden işlemler yürütülür.
   *
   * 2) Service Controller üzerinden; Uygulama linux/unix üzerinde terminalden
   * jsvc ile başlatılırsa Daemon interface'ini implemente eden DaemonApp
   * instance'ı otomatik olarak yaratılır ve service controller tarafından
   * interface metotları çağrılır. Bu durumda yaratılmış olan mevcut instance
   * sınıf içindeki Daemon instance'a atanır.
   *
   * 1 ve 2 numaralı kullanımlarda SpringBoot özelliklerinin kullanılabilmesi için
   * SpringBootApp sınıfının Run metodu çağrılır ve SpringBoot'un yüklenmesi
   * sağlanır. Her iki durumda da SpringBoot devreye girdiği için standart rest
   * controller'lar kullanılabilir durumdadır ve gerekirse daemon instance'ına da
   * erişilebilir.
   *
   * 3) Uygulama herhangi bir web container'a (tomcat gibi) yüklenerek de
   * çalıştırılabilir. Bu durumda SpringBoot otomatik olarak devreye gireceğinden
   * 1 ve 2 numaralı çalışmalarda olduğu gibi DaemonApp instance'ı üzerinde işlem
   * gerçekleştirilebilir.
   *
   *
   * WorkerGroupManager
   *         !
   *         !
   *         !---WorkerGroup[0]
   *         !
   *         !
   *         !---WorkerGroup[1]
   *         ! . .
   *         !---WorkerGroup[N]::WorkerGroupInfo
   *         				!
   *         				!
   *         				!---Worker[0]
   *         				!
   *         				!
   *         				!---Worker[1]
   *         				! . .
   *         				!---Worker[M]::WorkerContext->Worker
   */

  public static DaemonApp DaemonInstance = new DaemonApp();
  private WorkerGroupManager workerGroupManager = new WorkerGroupManager();

  public static void Run(String[] args) {

    DaemonInstance.logger
        .info("================================================================================");
    DaemonInstance.logger
        .info(String.format("Daemon Starter (Selim Gul, 2004-%s)", LocalDate.now().getYear()));
    DaemonInstance.logger.info("Application started in console mode. Press any key to shutdown.");
    DaemonInstance.logger
        .info("================================================================================");

    try {
      SpringBootApp.Run(args);

      DaemonInstance.start();

      System.in.read();

      DaemonInstance.stop();

      System.exit(0);
    } catch (Exception e) {
      DaemonInstance.logger.info(e.getMessage());
    }

  }

  @Override
  public void init(DaemonContext context) {
    try {
      SpringBootApp.Run(context.getArguments());
      DaemonInstance = this;
      logger.info("Daemon initialized with arguments {}." + context.getArguments());
    } catch (Exception e) {
      logger.error(e.getMessage());
    }
  }

  @Override
  public void start() {
    try {
      if (!workerGroupManager.isRunning()) {
        logger.info("Starting up daemon.");
        workerGroupManager.Start();
        logger.info("Daemon started.");
      } else {
        logger.info("Daemon is already running.");
      }
    } catch (Exception e) {
      logger.error(e.getMessage());
    }
  }

  @Override
  public void stop() {
    try {
      if (workerGroupManager.isRunning()) {
        logger.info("Stopping daemon.");
        workerGroupManager.Stop();
        logger.info("Daemon stopped.");
      } else {
        logger.info("Daemon is already stopped.");
      }
    } catch (Exception e) {
      logger.error(e.getMessage());
    }
  }

  @Override
  public void destroy() {
    logger.info("Daemon destroyed.");
  }
}
