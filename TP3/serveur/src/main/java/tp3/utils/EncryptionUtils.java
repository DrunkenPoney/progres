package tp3.utils;

import org.apache.commons.lang3.RandomUtils;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Arrays;
import java.util.Base64;
import java.util.Random;

/**
 * Classe utilitaire pour l'encryption et la validation des mots de passe.
 * Source: {@link "http://www.appsdeveloperblog.com/encrypt-user-password-example-java/"}
 */
public final class EncryptionUtils {
	private static final Random RANDOM = new SecureRandom();
	private static final String SALT_CHARACTERS = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
	private static final String SECRET_FACTORY_ALGORITHM = "PBKDF2WithHmacSHA1";
	private static final int ITERATIONS = 10_000;
	private static final int KEY_LENGTH = 256;
	private static final int SALT_MIN_LENGTH = 64;
	private static final int SALT_MAX_LENGTH = 512;
	
	public static String salt() {
		final int length = RandomUtils.nextInt(SALT_MIN_LENGTH, SALT_MAX_LENGTH);
		StringBuilder salt = new StringBuilder(length);
		
		for (int i=0; i < length; i++)
			salt.append(SALT_CHARACTERS.charAt(RANDOM.nextInt(SALT_CHARACTERS.length())));
		
		return salt.toString();
	}
	
	public static byte[] hash(char[] chars, byte[] salt) {
		PBEKeySpec spec = new PBEKeySpec(chars, salt, ITERATIONS, KEY_LENGTH);
		Arrays.fill(chars, Character.MIN_VALUE);
		try {
			return SecretKeyFactory.getInstance(SECRET_FACTORY_ALGORITHM)
					.generateSecret(spec)
					.getEncoded();
		} catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
			throw new RuntimeException(e);
		} finally {
			spec.clearPassword();
		}
	}
	
	public static String encrypt(String toEncrypt, String salt) {
		return Base64.getEncoder().encodeToString(hash(toEncrypt.toCharArray(), salt.getBytes()));
	}
}
