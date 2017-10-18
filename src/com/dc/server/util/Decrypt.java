package com.dc.server.util;

import org.apache.commons.codec.binary.Base64;

public class Decrypt {

	public static String encode(String encode) {

		byte[] encodeBytes = Base64.encodeBase64(encode.getBytes());
		System.out.println("commonsBase64>>加密:   " + new String(encodeBytes));
		return new String(encodeBytes);
	}

	public static String decode(String decode) {

		byte[] decodeBytes = Base64.decodeBase64(decode.getBytes());
		System.out.println("commonsBase64>>解密:   " + new String(decodeBytes));
		return new String(decodeBytes);
	}

}
