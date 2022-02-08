package GpioController;

import com.fazecast.jSerialComm.SerialPort;

import logistics.MusicPlayer;
import logistics.SendToArduino;
import websocket.websocketMessage.LightMessage;
import websocket.websocketMessage.LockDownMessage;
import websocket.websocketMessage.PlayMessage;

public class Controller {
	private MusicPlayer mp3;

	public Controller(SerialPort arduinoPort) {
		this.mp3 = new MusicPlayer();
	}

	public void setLights(LightMessage msg, SerialPort arduinoPort) {
		try {
			int pinR = msg.getPinR();
			int pinG = msg.getPinG();
			int pinB = msg.getPinB();

			int valueR = 0;
			int valueG = 0;
			int valueB = 0;

			if (msg.isActive()) {
				valueR = msg.getValueR();
				valueG = msg.getValueG();
				valueB = msg.getValueB();
			}

			String pinSetMessage = String.format(
					"{'type':'light','pinR':%s,'pinG':%s,'pinB':%s,'valueR':%s,'valueG':%s,'valueB':%s}", pinR, pinG,
					pinB, valueR, valueG, valueB);

			new SendToArduino(arduinoPort, pinSetMessage);
			System.out.println("Lights on: " + msg.isActive());

		} catch (InterruptedException e) {
			e.printStackTrace();
			System.out.println("There's a problem with the light.");
		}

	}

	public void setMusic(PlayMessage msg) {
		System.out.println("Playing music: " + msg.isPlay());

		if (msg.isPlay()) {
			mp3.play();
		} else {
			mp3.pause();
		}
	}

	public void setLockdown(LockDownMessage msg, SerialPort arduinoPort) {
		try {

			String lockDownMessage;
			if (msg.isLockDown()) {
				lockDownMessage = "{'type':'lock','status':1}";
			} else {
				lockDownMessage = "{'type':'lock','status':0}";
			}

			new SendToArduino(arduinoPort, lockDownMessage);
			System.out.println("Door is locked: " + msg.isLockDown());

		} catch (InterruptedException e) {
			e.printStackTrace();
			System.out.println("There's a problem with locking the door");
		}
	}

}
