package application.logistics;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.cert.X509Certificate;
import java.util.Base64;
import java.util.prefs.Preferences;

public class Request {

	private String response;
	private int responseCode;
	private String auth;
	private URL url;

	public Request(String path) {
		try {
			url = new URL("http://localhost:8080" + path);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("GET");
			
			if (getAuth()) {
				connection.setRequestProperty("Authorization", "Basic " + auth);
				connection.connect();
				responseCode = connection.getResponseCode();
				if (responseCode == 200) {
					response = new BufferedReader(new InputStreamReader(connection.getInputStream())).readLine();
				}
			}else {
				responseCode = 401;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private boolean getAuth() {
		Preferences pref = Preferences.userRoot().node("userInformation");
		String username = pref.get("username", "");
		String password = pref.get("password", "");
		if (username.equals("") || password.equals("")) {
			return false;
		}
		auth = Base64.getEncoder().encodeToString((username + ":" + password).getBytes(StandardCharsets.UTF_8));
		return true;
	}

	public int getResponseCode() {
		return responseCode;
	}

	public String getResponse() {
		return response;
	}
}
