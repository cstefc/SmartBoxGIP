package eu.happyit.smartbox.api.websocket.messageTemplates;

import eu.happyit.smartbox.api.domain.PinSets;
import eu.happyit.smartbox.api.domain.Templates;

public class LightMessage {
	int valueR;
	
	int valueG;
	
	int valueB;
	
	int pinR;
	
	int pinG;
	
	int pinB;
	
	boolean active;
	
	public LightMessage () {}
	
	public LightMessage (PinSets pinSet) {
		Templates template = pinSet.getTemplate();
		
		this.valueR = template.getR();
		this.valueG = template.getG();
		this.valueB = template.getB();
		
		this.pinR = pinSet.getPinRed();
		this.pinG = pinSet.getPinGreen();
 		this.pinB = pinSet.getPinBlue();

 		this.active = pinSet.isActive();
	}

	public int getValueR() {
		return valueR;
	}

	public void setValueR(int valueR) {
		this.valueR = valueR;
	}

	public int getValueG() {
		return valueG;
	}

	public void setValueG(int valueG) {
		this.valueG = valueG;
	}

	public int getValueB() {
		return valueB;
	}

	public void setValueB(int valueB) {
		this.valueB = valueB;
	}

	public int getPinR() {
		return pinR;
	}

	public void setPinR(int pinR) {
		this.pinR = pinR;
	}

	public int getPinG() {
		return pinG;
	}

	public void setPinG(int pinG) {
		this.pinG = pinG;
	}

	public int getPinB() {
		return pinB;
	}

	public void setPinB(int pinB) {
		this.pinB = pinB;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}
	
	
}
