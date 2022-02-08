package application.logistics;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.cert.X509Certificate;
import java.util.Base64;
import java.util.prefs.Preferences;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class Request {

	private String response;
	private int responseCode;
	private String auth;
	private URL url;

	public Request(String path) {
		try {
			
			// Create a trust manager that does not validate certificate chains
			TrustManager[] trustAllCerts = new TrustManager[] { 
			    new X509TrustManager() {     
			        public java.security.cert.X509Certificate[] getAcceptedIssuers() { 
			            return new X509Certificate[0];
			        } 
			        public void checkClientTrusted( 
			            java.security.cert.X509Certificate[] certs, String authType) {
			            } 
			        public void checkServerTrusted( 
			            java.security.cert.X509Certificate[] certs, String authType) {
			        }
			    } 
			}; 
			
			// Install the all-trusting trust manager
			try {
			    SSLContext sc = SSLContext.getInstance("SSL"); 
			    sc.init(null, trustAllCerts, new java.security.SecureRandom()); 
			    HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
			} catch (GeneralSecurityException e) {
			} 
			
			url = new URL("https://smartbox.happyit.eu" + path);
			HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
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
