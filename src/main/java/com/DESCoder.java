package com;

import org.apache.commons.codec.binary.Hex;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import java.nio.charset.Charset;
import java.security.Key;
import java.util.Random;


/**
 * DES Coder<br/>
 * secret key length:   56 bit, default:    56 bit<br/>
 * mode:    ECB/CBC/PCBC/CTR/CTS/CFB/CFB8 to CFB128/OFB/OBF8 to OFB128<br/>
 * padding: Nopadding/PKCS5Padding/ISO10126Padding/
 * @author Aub
 *
 */
public class DESCoder {

    private static String keyString="bumper_cash";
    /**
     * 密钥算法
     */
    private static final String KEY_ALGORITHM = "DES";

    private static final String DEFAULT_CIPHER_ALGORITHM = "DES/ECB/PKCS5Padding";
//  private static final String DEFAULT_CIPHER_ALGORITHM = "DES/ECB/ISO10126Padding";


    /**
     * 初始化密钥
     *
     * @return byte[] 密钥
     * @throws Exception
     */
    private static byte[] initSecretKey() throws Exception{
        //返回生成指定算法的秘密密钥的 KeyGenerator 对象
        KeyGenerator kg = KeyGenerator.getInstance(KEY_ALGORITHM);
        //初始化此密钥生成器，使其具有确定的密钥大小
        kg.init(56);
        //生成一个密钥
        SecretKey  secretKey = kg.generateKey();
        return secretKey.getEncoded();
    }




    /**
     * 转换密钥
     *
     * @param key   二进制密钥
     * @return Key  密钥
     * @throws Exception
     */
    private static Key toKey(byte[] key) throws Exception{
        //实例化DES密钥规则
        DESKeySpec dks = new DESKeySpec(key);
        //实例化密钥工厂
        SecretKeyFactory skf = SecretKeyFactory.getInstance(KEY_ALGORITHM);
        //生成密钥
        SecretKey  secretKey = skf.generateSecret(dks);
        return secretKey;
    }

    /**
     * 加密
     *
     * @param data  待加密数据
     * @param key   密钥
     * @return byte[]   加密数据
     * @throws Exception
     */
    private static byte[] encrypt(byte[] data,Key key) throws Exception{
        return encrypt(data, key,DEFAULT_CIPHER_ALGORITHM);
    }

    /**
     * 加密
     *
     * @param data  待加密数据
     * @param key   二进制密钥
     * @return byte[]   加密数据
     * @throws Exception
     */
    private static byte[] encrypt(byte[] data,byte[] key) throws Exception{
        return encrypt(data, key,DEFAULT_CIPHER_ALGORITHM);
    }


    /**
     * 加密
     *
     * @param data  待加密数据
     * @param key   二进制密钥
     * @param cipherAlgorithm   加密算法/工作模式/填充方式
     * @return byte[]   加密数据
     * @throws Exception
     */
    private static byte[] encrypt(byte[] data,byte[] key,String cipherAlgorithm) throws Exception{
        //还原密钥
        Key k = toKey(key);
        return encrypt(data, k, cipherAlgorithm);
    }

    /**
     * 加密
     *
     * @param data  待加密数据
     * @param key   密钥
     * @param cipherAlgorithm   加密算法/工作模式/填充方式
     * @return byte[]   加密数据
     * @throws Exception
     */
    private static byte[] encrypt(byte[] data,Key key,String cipherAlgorithm) throws Exception{
        //实例化
        Cipher cipher = Cipher.getInstance(cipherAlgorithm);
        //使用密钥初始化，设置为加密模式
        cipher.init(Cipher.ENCRYPT_MODE, key);
        //执行操作
        return cipher.doFinal(data);
    }



    /**
     * 解密
     *
     * @param data  待解密数据
     * @param key   二进制密钥
     * @return byte[]   解密数据
     * @throws Exception
     */
    private static byte[] decrypt(byte[] data,byte[] key) throws Exception{
        return decrypt(data, key,DEFAULT_CIPHER_ALGORITHM);
    }

    /**
     * 解密
     *
     * @param data  待解密数据
     * @param key   密钥
     * @return byte[]   解密数据
     * @throws Exception
     */
    private static byte[] decrypt(byte[] data,Key key) throws Exception{
        return decrypt(data, key,DEFAULT_CIPHER_ALGORITHM);
    }

    /**
     * 解密
     *
     * @param data  待解密数据
     * @param key   二进制密钥
     * @param cipherAlgorithm   加密算法/工作模式/填充方式
     * @return byte[]   解密数据
     * @throws Exception
     */
    private static byte[] decrypt(byte[] data,byte[] key,String cipherAlgorithm) throws Exception{
        //还原密钥
        Key k = toKey(key);
        return decrypt(data, k, cipherAlgorithm);
    }

    /**
     * 解密
     *
     * @param data  待解密数据
     * @param key   密钥
     * @param cipherAlgorithm   加密算法/工作模式/填充方式
     * @return byte[]   解密数据
     * @throws Exception
     */
    private static byte[] decrypt(byte[] data,Key key,String cipherAlgorithm) throws Exception{
        //实例化
        Cipher cipher = Cipher.getInstance(cipherAlgorithm);
        //使用密钥初始化，设置为解密模式
        cipher.init(Cipher.DECRYPT_MODE, key);
        //执行操作
        return cipher.doFinal(data);
    }

    private static String  showByteArray(byte[] data){
        if(null == data){
            return null;
        }
        StringBuilder sb = new StringBuilder("{");
        for(byte b:data){
            sb.append(b).append(",");
        }
        sb.deleteCharAt(sb.length()-1);
        sb.append("}");
        return sb.toString();
    }

    public static void main(String[] args) throws Exception {
//        byte[] key = initSecretKey();
        System.out.println(encryAccessKey("1")); ;
        System.out.println(decryptStr("c3feea1a46402a26"));

//        decryptStr("1","12345678");
//
    }


    public static String encryAccessKey(String origin) throws RuntimeException {
        try {
            return encryAccessKey(origin,keyString);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String encryAccessKey(String origin, String keyString) throws Exception {
        byte[] bytes = origin.getBytes(Charset.forName("UTF-8"));
        byte[] key = keyString.getBytes();
        byte[] encrypt = encrypt(bytes, toKey(key));
        return Hex.encodeHexString(encrypt);
    }
    //
    public static String decryptStr(String encrypt) throws RuntimeException {
        try {
            return decryptStr(encrypt,keyString);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    //
    public static String decryptStr(String encrypt, String keyString) throws Exception {
        byte[] decodeHexbytes = Hex.decodeHex(encrypt.toCharArray());
        byte[] key = keyString.getBytes();
        return new String(decrypt(decodeHexbytes, toKey(key)));
    }





    private static Random random=new Random();
}

