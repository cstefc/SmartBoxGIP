package logistics;

import java.io.PrintWriter;

import com.fazecast.jSerialComm.SerialPort;

public class SendToArduino {
	public SendToArduino(SerialPort port, String pinSetString) throws InterruptedException {
		if (port.isOpen()) {
			PrintWriter output = new PrintWriter(port.getOutputStream(), true);
			output.println(pinSetString);
			output.flush();
		} else {
			System.out.println("Failed to open port: " + port.getSystemPortName());
		}
	}
}
