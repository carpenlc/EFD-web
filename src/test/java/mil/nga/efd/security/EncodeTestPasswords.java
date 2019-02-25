package mil.nga.efd.security;

import org.junit.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class EncodeTestPasswords {

	private static String PASSWORD = "password";
	
	@Test
	public void encodeTestPasswords() {
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		System.out.println("Encoded version of [ "
				+ PASSWORD 
				+ " ] is [ "
				+ encoder.encode(PASSWORD)
				+ " ].");
	}
}
