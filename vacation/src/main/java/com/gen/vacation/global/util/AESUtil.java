package com.gen.vacation.global.util;

import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

/**
 * Created by dhwlsdn741@gmail.com
 * User: jinwoo
 * Date: 2020-12-01
 * Time: 오전 11:12
 */
public class AESUtil {

/*    public static String encryptAES256(String msg, String key) throws Exception {

        SecureRandom random = new SecureRandom();

        byte bytes[] = new byte[20];

        random.nextBytes(bytes);

        byte[] saltBytes = bytes;



        // Password-Based Key Derivation function 2

        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");

        // 70000번 해시하여 256 bit 길이의 키를 만든다.

        PBEKeySpec spec = new PBEKeySpec(key.toCharArray(), saltBytes, 70000, 256);



        SecretKey secretKey = factory.generateSecret(spec);

        SecretKeySpec secret = new SecretKeySpec(secretKey.getEncoded(), "AES");



        // 알고리즘/모드/패딩

        // CBC : Cipher Block Chaining Mode

        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");

        cipher.init(Cipher.ENCRYPT_MODE, secret);

        AlgorithmParameters params = cipher.getParameters();

        // Initial Vector(1단계 암호화 블록용)

        byte[] ivBytes = params.getParameterSpec(IvParameterSpec.class).getIV();



        byte[] encryptedTextBytes = cipher.doFinal(msg.getBytes("UTF-8"));



        byte[] buffer = new byte[saltBytes.length + ivBytes.length + encryptedTextBytes.length];

        System.arraycopy(saltBytes, 0, buffer, 0, saltBytes.length);

        System.arraycopy(ivBytes, 0, buffer, saltBytes.length, ivBytes.length);

        System.arraycopy(encryptedTextBytes, 0, buffer, saltBytes.length + ivBytes.length, encryptedTextBytes.length);



        return Base64.getEncoder().encodeToString(buffer);

    }


    public static String decryptAES256(String msg, String key) throws Exception {

        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");

        ByteBuffer buffer = ByteBuffer.wrap(Base64.getDecoder().decode(msg));



        byte[] saltBytes = new byte[20];

        buffer.get(saltBytes, 0, saltBytes.length);

        byte[] ivBytes = new byte[cipher.getBlockSize()];

        buffer.get(ivBytes, 0, ivBytes.length);

        byte[] encryoptedTextBytes = new byte[buffer.capacity() - saltBytes.length - ivBytes.length];

        buffer.get(encryoptedTextBytes);



        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");

        PBEKeySpec spec = new PBEKeySpec(key.toCharArray(), saltBytes, 70000, 256);



        SecretKey secretKey = factory.generateSecret(spec);

        SecretKeySpec secret = new SecretKeySpec(secretKey.getEncoded(), "AES");



        cipher.init(Cipher.DECRYPT_MODE, secret, new IvParameterSpec(ivBytes));



        byte[] decryptedTextBytes = cipher.doFinal(encryoptedTextBytes);

        return new String(decryptedTextBytes);

    }*/

    public static String decrypt(String ciphertext, String passphrase) {
        try {
            final int keySize = 256;
            final int ivSize = 128;

            // 텍스트를 BASE64 형식으로 디코드 한다.
            byte[] ctBytes = Base64.decodeBase64(ciphertext.getBytes("UTF-8"));

            // 솔트를 구한다. (생략된 8비트는 Salted__ 시작되는 문자열이다.)
            byte[] saltBytes = Arrays.copyOfRange(ctBytes, 8, 16);


            // 암호화된 테스트를 구한다.( 솔트값 이후가 암호화된 텍스트 값이다.)
            byte[] ciphertextBytes = Arrays.copyOfRange(ctBytes, 16, ctBytes.length);

            // 비밀번호와 솔트에서 키와 IV값을 가져온다.
            byte[] key = new byte[keySize / 8];
            byte[] iv = new byte[ivSize / 8];
            EvpKDF(passphrase.getBytes("UTF-8"), keySize, ivSize, saltBytes, key, iv);

            // 복호화
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(key, "AES"), new IvParameterSpec(iv));
            byte[] recoveredPlaintextBytes = cipher.doFinal(ciphertextBytes);

            return new String(recoveredPlaintextBytes);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    private static byte[] EvpKDF(byte[] password, int keySize, int ivSize, byte[] salt, byte[] resultKey, byte[] resultIv) throws NoSuchAlgorithmException {
        return EvpKDF(password, keySize, ivSize, salt, 1, "MD5", resultKey, resultIv);
    }

    private static byte[] EvpKDF(byte[] password, int keySize, int ivSize, byte[] salt, int iterations, String hashAlgorithm, byte[] resultKey, byte[] resultIv) throws NoSuchAlgorithmException {
        keySize = keySize / 32;
        ivSize = ivSize / 32;
        int targetKeySize = keySize + ivSize;
        byte[] derivedBytes = new byte[targetKeySize * 4];
        int numberOfDerivedWords = 0;
        byte[] block = null;
        MessageDigest hasher = MessageDigest.getInstance(hashAlgorithm);
        while (numberOfDerivedWords < targetKeySize) {
            if (block != null) {
                hasher.update(block);
            }
            hasher.update(password);
            // Salting
            block = hasher.digest(salt);
            hasher.reset();
            // Iterations : 키 스트레칭(key stretching)
            for (int i = 1; i < iterations; i++) {
                block = hasher.digest(block);
                hasher.reset();
            }
            System.arraycopy(block, 0, derivedBytes, numberOfDerivedWords * 4, Math.min(block.length, (targetKeySize - numberOfDerivedWords) * 4));
            numberOfDerivedWords += block.length / 4;
        }
        System.arraycopy(derivedBytes, 0, resultKey, 0, keySize * 4);
        System.arraycopy(derivedBytes, keySize * 4, resultIv, 0, ivSize * 4);
        return derivedBytes; // key + iv
    }

}
