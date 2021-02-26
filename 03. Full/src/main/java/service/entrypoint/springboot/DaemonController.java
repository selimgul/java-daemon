package service.entrypoint.springboot;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import service.entrypoint.daemon.DaemonApp;

@RestController
@RequestMapping("/daemon")
public class DaemonController {

	@RequestMapping(method = RequestMethod.GET, value = "start")
	ResponseEntity<?> startDaemon() throws Exception {
		DaemonApp.DaemonInstance.start();
		return ResponseEntity.ok("Daemon started.");
	}

	@RequestMapping(method = RequestMethod.GET, value = "stop")
	ResponseEntity<?> stopDaemon() throws Exception {
		DaemonApp.DaemonInstance.stop();
		return ResponseEntity.ok("Daemon stopped.");
	}
}