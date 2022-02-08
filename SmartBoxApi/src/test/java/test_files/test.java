package test_files;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class test {
	private static BCryptPasswordEncoder passEncoder = new BCryptPasswordEncoder();
	private static String password = "TestPass";
	
	public static void main (String[] args) {
		System.out.println(passEncoder.encode(password));
	}
}