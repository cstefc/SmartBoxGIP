package eu.happyit.smartbox.api.domain;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
public class Templates {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	private String templateName;

	private int R;
	private int G;
	private int B;

	@JsonBackReference
	@ManyToOne
	private Users user;

	@JsonManagedReference
	@OneToMany(mappedBy = "template", orphanRemoval = true,  cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private Set<PinSets> pinSets = new HashSet<PinSets>();

	public Templates() {
	}

	public Templates(int R, int G, int B, String templateName, Users user) {
		this.R = R;
		this.G = G;
		this.B = B;
		this.templateName = templateName;
		this.user = user;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
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

	public Users getUser() {
		return user;
	}

	public void setUser(Users user) {
		this.user = user;
	}

	public Set<PinSets> getPinSets() {
		return pinSets;
	}

	public void setPinSets(Set<PinSets> pinSets) {
		this.pinSets = pinSets;
	}

}
