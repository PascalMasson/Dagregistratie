package com.naugrim.zeecontainer.utils;

import java.util.Random;

public class RandomString {
	static final String UpperLowerCaseLetters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdeefghijklmnopqrstuvwqyz";
	static final String UpperCaseLetters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	static final String LowerCaseLetters = UpperCaseLetters.toLowerCase();
	static final String Numbers = "0123456789";

	static Random r = new Random();

	public static String generateRandomString(int len) {
		StringBuilder sb = new StringBuilder(len);
		for (int i = 0; i < len; i++) {
			sb.append(UpperLowerCaseLetters.charAt(r.nextInt(UpperLowerCaseLetters.length())));
		}
		return sb.toString();

	}

	// for template:
	// n = number
	// l = letter
	public static String generateRandomZipCode(String template) {
		template = template.toLowerCase();
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < template.length(); i++) {
			if (template.charAt(i) == 'n') {
				sb.append(Numbers.charAt(new Random().nextInt(Numbers.length())));
			}
			else if (template.charAt(i) == 'l') {
				sb.append(UpperCaseLetters.charAt(new Random().nextInt(UpperCaseLetters.length())));
			}
			else {
				sb.append(" ");
			}
		}
		return sb.toString();
	}
}
