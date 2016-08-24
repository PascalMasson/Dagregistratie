package com.naugrim.zeecontainer.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.security.InvalidKeyException;
import java.util.HashMap;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.SealedObject;
import javax.crypto.SecretKey;

public class Encryption {
	static String filePath;
	static Cipher cipher;
	static SecretKey key;

	public Encryption(SecretKey enckey) throws Exception {
		key = enckey;
		cipher = Cipher.getInstance("Blowfish");
	}

	public void Write(String path, Object[] objects) throws Exception {
		filePath = path;
		cipher = Cipher.getInstance("Blowfish");
		cipher.init(Cipher.ENCRYPT_MODE, key);

		SealedObject so = new SealedObject(objects, cipher);

		CipherOutputStream cipherOutputStream = new CipherOutputStream(new BufferedOutputStream(new FileOutputStream(path)), cipher);
		ObjectOutputStream objectOutputStream = new ObjectOutputStream(cipherOutputStream);
		objectOutputStream.writeObject(so);
		objectOutputStream.close();
		System.out.println("Data has been saved in " + filePath);

	}

	@SuppressWarnings("unchecked")
	public HashMap<String, String> Read(String path) throws Exception {
		if (key == null) {
			System.out.println("Encryption.Read()");
			System.err.println("KEY IS NULL");
		}
		cipher.init(Cipher.DECRYPT_MODE, key);
		CipherInputStream cipherInputStream = new CipherInputStream(new BufferedInputStream(new FileInputStream(path)), cipher);
		ObjectInputStream inputStream = new ObjectInputStream(cipherInputStream);
		SealedObject so = (SealedObject) inputStream.readObject();
		// Object[] arr = (Object[]) so.getObject( cipher );
		// return arr;
		inputStream.close();
		cipherInputStream.close();
		return (HashMap<String, String>) so.getObject(cipher);
	}

	@SuppressWarnings("unchecked")
	public String ReadPW(String path) throws Exception {
		if (key == null) {
			System.out.println("Encryption.Read()");
			System.err.println("KEY IS NULL");
		}
		cipher.init(Cipher.DECRYPT_MODE, key);
		CipherInputStream cipherInputStream = new CipherInputStream(new BufferedInputStream(new FileInputStream(path)), cipher);
		ObjectInputStream inputStream = new ObjectInputStream(cipherInputStream);
		SealedObject so = (SealedObject) inputStream.readObject();
		// Object[] arr = (Object[]) so.getObject( cipher );
		// return arr;
		inputStream.close();
		cipherInputStream.close();
		return (String) so.getObject(cipher);
	}

	public void Write(String path, Object s) throws Exception {
		filePath = path;
		cipher = Cipher.getInstance("Blowfish");
		cipher.init(Cipher.ENCRYPT_MODE, key);

		SealedObject so = new SealedObject((Serializable) s, cipher);

		CipherOutputStream cipherOutputStream = new CipherOutputStream(new BufferedOutputStream(new FileOutputStream(path)), cipher);
		ObjectOutputStream objectOutputStream = new ObjectOutputStream(cipherOutputStream);
		objectOutputStream.writeObject(so);
		objectOutputStream.close();
		System.out.println("Data has been saved in " + filePath);

	}

}
