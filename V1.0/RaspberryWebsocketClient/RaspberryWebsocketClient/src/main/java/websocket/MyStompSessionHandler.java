package websocket;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.Properties;

import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;

import com.fazecast.jSerialComm.SerialPort;

import GpioController.Controller;
import websocket.websocketMessage.LightMessage;
import websocket.websocketMessage.LockDownMessage;
import websocket.websocketMessage.MusicMessage;
import websocket.websocketMessage.PasswordMessage;
import websocket.websocketMessage.PlayMessage;

public class MyStompSessionHandler extends StompSessionHandlerAdapter {
	private String subscribe;
	private String type;
	public SerialPort arduinoPort;
	private Controller ctrl;
		
	public MyStompSessionHandler(String subscribe, String type, Controller ctrl) {
		this.subscribe = subscribe;
		this.type = type;
		this.ctrl = ctrl;
	}

	@Override
	public void afterConnected(StompSession session, StompHeaders connectedHeaders) {
		System.out.println("New session established : " + session.getSessionId());

		session.subscribe(subscribe, this);
		System.out.println(String.format("Subscribed to '%s'", subscribe));
		session.setAutoReceipt(true);
	}

	@Override
	public void handleException(StompSession session, StompCommand command, StompHeaders headers, byte[] payload,
			Throwable exception) {
		System.out.println("Got an exception: " + exception.toString());
		exception.printStackTrace();
	}

	@Override
	public Type getPayloadType(StompHeaders headers) {
		if (type.equals("light")) {
			return LightMessage.class;
		} else if (type.equals("play")) {
			return PlayMessage.class;
		} else if (type.equals("lockDown")) {
			return LockDownMessage.class;
		} else if (type.equals("music")) {
			return MusicMessage.class;
		} else if (type.equals("password")) {
			return PasswordMessage.class;
		}
		else {
			return Object.class;
		}
	}

	@Override
	public void handleFrame(StompHeaders headers, Object payload) {

		if (type.equals("light")) {
			LightMessage msg = (LightMessage) payload;
			ctrl.setLights(msg, arduinoPort);
		}

		else if (type.equals("play")) {
			PlayMessage msg = (PlayMessage) payload;
			ctrl.setMusic(msg);
		}

		else if (type.equals("lockDown")) {
			LockDownMessage msg = (LockDownMessage) payload;
			ctrl.setLockdown(msg, arduinoPort);
		}

		else if (type.equals("music")) {
			MusicMessage msg = (MusicMessage) payload;
			System.out.println(msg.getSongTitle());
		}
		else if (type.equals("password")) {
			try {
			PasswordMessage msg = (PasswordMessage) payload;	
			
			// Reading file
			InputStream in = getClass().getResourceAsStream("/settings.properties");
			Properties prop = new Properties();
			prop.load(in);
			in.close();
			// Saving to file
			prop.put("password", msg.getNewPassword());
			FileOutputStream fos = new FileOutputStream(getClass().getResource("/settings.properties").getPath());
			prop.store(fos, null);
			fos.flush();
						
			}catch (IOException e) {
				e.printStackTrace();
			}
		}

		else {
			System.out.println(payload);
			System.out.println("Error: I don't know this type of Object");
		}
	}
}