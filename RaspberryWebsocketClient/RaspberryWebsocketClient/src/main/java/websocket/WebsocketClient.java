package websocket;

import java.util.concurrent.ExecutionException;

import org.springframework.messaging.simp.stomp.StompClientSupport;
import org.springframework.scheduling.annotation.EnableScheduling;

import GpioController.Controller;
import javazoom.jl.decoder.JavaLayerException;
import logistics.OpenArduinoPort;
import logistics.OpenWebsocketConnections;

@EnableScheduling
public class WebsocketClient extends StompClientSupport {
	private static String auth;

	private static String URL = "wss://smartbox.happyit.eu/raspberry";
	private static String port = "ttyUSB0";

	private static Controller ctrl;
	// pc port: cu.wchusbserial1420

	public static void main(String[] args) throws InterruptedException, ExecutionException, JavaLayerException {
		OpenArduinoPort openPort = new OpenArduinoPort(port);
		ctrl = new Controller(openPort.arduinoPort);
		OpenWebsocketConnections websocketConnections = new OpenWebsocketConnections(URL, openPort.arduinoPort, ctrl);
		auth = websocketConnections.getAuth();
		while (true) {
			if (!auth.equals(websocketConnections.getAuth())) {				
				Thread.sleep(200);
				new OpenWebsocketConnections(URL, openPort.arduinoPort, ctrl);
				auth = websocketConnections.getAuth();
			}
		}
	}
}