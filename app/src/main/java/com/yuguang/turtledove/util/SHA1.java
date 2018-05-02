package com.codido.nymeria.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Title: SHA1<br/>
 * Description: sha1工具类<br/>
 * Company: cloudfn<br/>
 *
 * @author lai_jj
 * @date 2015年1月27日下午5:01:47
 */
public class SHA1 {
    /**
     * Title: SHA_1<br/>
     * Description: 对decript进行SHA-1加密<br/>
     *
     * @param decript
     *
     * @return
     *
     * @author lai_jj
     * @date 2015年1月27日下午5:00:43
     */
    public static String SHA_1(String decript) {
        try {
            MessageDigest digest = MessageDigest
                    .getInstance("SHA-1");
            digest.update(decript.getBytes());
            byte messageDigest[] = digest.digest();
            // Create Hex String
            StringBuffer hexString = new StringBuffer();
            // 字节数组转换为 十六进制 数
            for (int i = 0; i < messageDigest.length; i++) {
                String shaHex = Integer.toHexString(messageDigest[i] & 0xFF);
                if (shaHex.length() < 2) {
                    hexString.append(0);
                }
                hexString.append(shaHex);
            }
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 进行byte加密
     *
     * @param data
     *
     * @return
     */
    public static byte[] SHA_2(byte[] data) {
        byte[] bytes = (byte[]) null;
        MessageDigest sha;
        try {
            sha = MessageDigest.getInstance("sha-1");
            sha.update(data);
            bytes = sha.digest();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return bytes;
    }
}
