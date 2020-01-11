package help;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Random;

public class SecurityUtil {
	
	public static String hashPassword(String password) {
		
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-256");
			md.reset();
			md.update(password.getBytes());
			byte[] mdResult = md.digest();
			StringBuilder sb = new StringBuilder(mdResult.length * 2);
			for(byte b : mdResult) {
				int v = b & 0xff; // to make string that produced from string builder is fixed size (64)
				if(v < 16) {
					sb.append("0");
				}
				sb.append(Integer.toHexString(v));
			}
			
			return sb.toString();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	public static String getSalt() {
		Random r = new SecureRandom();
		byte [] salt = new byte[32];
		r.nextBytes(salt);
		
		return Base64.getEncoder().encodeToString(salt);
	}
}
