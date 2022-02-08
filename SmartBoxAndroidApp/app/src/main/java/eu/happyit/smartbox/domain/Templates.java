package eu.happyit.smartbox.domain;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashSet;
import java.util.Set;

public class Templates {
	private String templateName;

	private int R;
	private int G;
	private int B;

	private Set<PinSets> pinSets = new HashSet<>();

	public Templates() {}

	public Templates (JSONObject object){
		try {
			templateName = object.getString("templateName");
			R = object.getInt("r");
			G = object.getInt("g");
			B = object.getInt("b");
			JSONArray ja = object.getJSONArray("pinSets");
			for (int i = 0; i<ja.length(); i++){
				pinSets.add(new PinSets((JSONObject) ja.get(i)));
			}
		}catch(JSONException e){
			e.printStackTrace();
		}
	}

	public String getTemplateName() {
		return templateName;
	}

	public void setTemplateName(String templateName) {
		this.templateName = templateName;
	}

	public int getR() {
		return R;
	}

	public void setR(int r) {
		R = r;
	}

	public int getG() {
		return G;
	}

	public void setG(int g) {
		G = g;
	}

	public int getB() {
		return B;
	}

	public void setB(int b) {
		B = b;
	}

	public Set<PinSets> getPinSets() {
		return pinSets;
	}

	public void setPinSets(Set<PinSets> pinSets) {
		this.pinSets = pinSets;
	}

}
