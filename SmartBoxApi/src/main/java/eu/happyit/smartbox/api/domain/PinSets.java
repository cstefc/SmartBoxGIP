package eu.happyit.smartbox.api.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
public class PinSets {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	private String pinSetName;
	private int pinRed;
	private int pinGreen;
	private int pinBlue;
	private boolean active;

	@JsonBackReference
	@ManyToOne
	private Templates template;

	public PinSets() {
	}

	public PinSets(int pinRed, int pinGreen, int pinBlue, String pinSetName, Templates template) {
		this.pinRed = pinRed;
		this.pinGreen = pinGreen;
		this.pinBlue = pinBlue;
		this.pinSetName = pinSetName;
		this.template = template;
		this.active = false;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getPinSetName() {
		return pinSetName;
	}

	public void setPinSetName(String pinSetName) {
		this.pinSetName = pinSetName;
	}

	public int getPinRed() {
		return pinRed;
	}

	public void setPinRed(int pinRed) {
		this.pinRed = pinRed;
	}

	public int getPinGreen() {
		return pinGreen;
	}

	public void setPinGreen(int pinGreen) {
		this.pinGreen = pinGreen;
	}

	public int getPinBlue() {
		return pinBlue;
	}

	public void setPinBlue(int pinBlue) {
		this.pinBlue = pinBlue;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public Templates getTemplate() {
		return template;
	}

	public void setTemplate(Templates template) {
		this.template = template;
	}

}
