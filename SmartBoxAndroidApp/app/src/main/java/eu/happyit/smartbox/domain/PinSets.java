package eu.happyit.smartbox.domain;

import org.json.JSONException;
import org.json.JSONObject;

public class PinSets {
	private long id;

	private String pinSetName;
	private int pinRed;
	private int pinGreen;
	private int pinBlue;
	private boolean active;

	private Templates template;

	public PinSets() {
	}

	public PinSets(JSONObject object) {
		try {
			pinRed = object.getInt("pinRed");
			pinGreen = object.getInt("pinGreen");
			pinBlue = object.getInt("pinBlue");
			pinSetName = object.getString("pinSetName");
			active = object.getBoolean("active");
			if (!object.has("template")){
				template = null;
			}else{
				template = new Templates(object.getJSONObject("template"));
			}
		}catch (JSONException e){
			e.printStackTrace();
		}
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
