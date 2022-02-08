package logistics;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Properties;

import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.converter.MessageConverter;
import org.springframework.web.socket.WebSocketHttpHeaders;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.Transport;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;

import com.fazecast.jSerialComm.SerialPort;

import GpioController.Controller;
import websocket.MyStompSessionHandler;

public class OpenWebsocketConnections {
	private String URL;
	private SerialPort arduinoPort;
	private Controller ctrl;
	private String auth;
	private WebSocketHttpHeaders httpHeaders;
	private MyStompSessionHandler sessionHandler;

	private static WebSocketStompClient light;
	private static WebSocketStompClient lockDown;
	private static WebSocketStompClient play;
	private static WebSocketStompClient password;
	// private WebSocketStompClient music;

	public OpenWebsocketConnections(String URL, SerialPort arduinoPort, Controller ctrl) {
		this.URL = URL;
		this.arduinoPort = arduinoPort;
		this.ctrl = ctrl;

		this.auth = getAuth();

		light = newStompClient("light");
		lockDown = newStompClient("lockDown");
		play = newStompClient("play");
		password = newStompClient("password");
		// music = newStompClient("music");
	}

	public void reconnect() {
		light.stop();
		lockDown.stop();
		play.stop();
		password.stop();
		//music.stop();
		
		light.start();
		lockDown.start();
		play.start();
		password.start();
		//music.start();
		
		getAuth();
		
		httpHeaders = new WebSocketHttpHeaders();
		httpHeaders.add("Authorization", "Basic " + new String(Base64.getEncoder().encode(auth.getBytes())));
		
		light.connect(URL, httpHeaders, sessionHandler);
		lockDown.connect(URL, httpHeaders, sessionHandler);
		play.connect(URL, httpHeaders, sessionHandler);
		password.connect(URL, httpHeaders, sessionHandler);
		//music.connect(URL, httpHeaders, sessionHandler);
	}

	public String getAuth() {
		Properties prop = new Properties();
		InputStream is = getClass().getResourceAsStream("/settings.properties");
		try {
			if (is != null) {
				prop.load(is);
				this.auth = prop.getProperty("username")+":"+prop.getProperty("password");
				is.close();
			} else {
				throw new FileNotFoundException("File not Found in classpath");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return this.auth;
	}

	public WebSocketStompClient newStompClient(String topic) {
		MessageConverter msgConverter = new MappingJackson2MessageConverter();

		// Making the authorization headers
		httpHeaders = new WebSocketHttpHeaders();
		httpHeaders.add("Authorization", "Basic " + new String(Base64.getEncoder().encode(auth.getBytes())));

		// Setting up the SockJs client
		WebSocketClient simpleWebSocketClient = new StandardWebSocketClient();
		List<Transport> transports = new ArrayList<>(1);
		transports.add(new WebSocketTransport(simpleWebSocketClient));
		SockJsClient sockJsClient = new SockJsClient(transports);

		// Making the final stompClient
		WebSocketStompClient stompClient = new WebSocketStompClient(sockJsClient);
		stompClient.setMessageConverter(msgConverter);

		// Configuring the sessionHandler
		sessionHandler = new MyStompSessionHandler("/user/queue/" + topic, topic, ctrl);
		if (topic.equals("light") || topic.equals("lockDown")) {
			sessionHandler.arduinoPort = arduinoPort;
		}

		// Connecting the client and opening a new session
		stompClient.connect(URL, httpHeaders, sessionHandler);

		return stompClient;
	}
}