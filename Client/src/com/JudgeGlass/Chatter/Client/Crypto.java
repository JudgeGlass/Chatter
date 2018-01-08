package com.JudgeGlass.Chatter.Client;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

public class Crypto {
	public static String encrypt(String text, String key){
		Key aesKey = new SecretKeySpec(key.getBytes(), "AES");
		Cipher cipher = null;
		try {
			cipher = Cipher.getInstance("AES");
		} catch (NoSuchAlgorithmException e1) {
			e1.printStackTrace();
		} catch (NoSuchPaddingException e1) {
			e1.printStackTrace();
		}
		try {
			cipher.init(Cipher.ENCRYPT_MODE, aesKey);
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		}
		byte[] encrypted = null;
		try {
			encrypted = cipher.doFinal(text.getBytes("UTF-8"));
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
		} catch (BadPaddingException e) {
			e.printStackTrace();
		}catch (UnsupportedEncodingException e){
			e.printStackTrace();
		}
		Base64.Encoder encoder = Base64.getEncoder();
		String encryptedString = encoder.encodeToString(encrypted);
		return encryptedString;
	}
	
	public static String decrypt(String eString, String key){
		Cipher cipher = null;
		try {
			cipher = Cipher.getInstance("AES");
		} catch (NoSuchAlgorithmException e1) {
			e1.printStackTrace();
		} catch (NoSuchPaddingException e1) {
			e1.printStackTrace();
		}
		Base64.Decoder decoder = Base64.getDecoder();
		try {
			cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(key.getBytes(), "AES"));
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		}
		String decrypted = null;
		try {
			decrypted = new String(cipher.doFinal(decoder.decode(eString)));
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
		} catch (BadPaddingException e) {
			e.printStackTrace();
		}
		return decrypted;
	}
}
