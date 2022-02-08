package logistics;

import com.fazecast.jSerialComm.SerialPort;

public class OpenArduinoPort {
	public SerialPort arduinoPort;
	
	public OpenArduinoPort(String port) throws InterruptedException {
		SerialPort[] ports = SerialPort.getCommPorts();
		for (SerialPort arduinoPort : ports) {
			if (port.equals("")) {
				System.out.println(arduinoPort.getSystemPortName());
			}
			if (arduinoPort.getSystemPortName().equals(port)) {
				arduinoPort.setComPortParameters(9600, 8, 1, 0);
				arduinoPort.setComPortTimeouts(SerialPort.TIMEOUT_SCANNER, 0, 0);
				arduinoPort.openPort();
				
				this.arduinoPort = arduinoPort;
				Thread.sleep(4000);
				System.out.println(String.format("Port: %s is open.", arduinoPort.getSystemPortName()));
				break;
			}
		}
	}
}
