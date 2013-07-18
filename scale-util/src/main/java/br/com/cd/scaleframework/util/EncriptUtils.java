/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.cd.scaleframework.util;

import java.math.BigInteger;
import java.security.MessageDigest;

/**
 *
 * @author ITaborda
 */
public class EncriptUtils {

    public static String md5(String valor) throws Exception {
        MessageDigest md = MessageDigest.getInstance("MD5");
        BigInteger hash = new BigInteger(1, md.digest(valor.getBytes()));
        String s = hash.toString(16);
        if (s.length() % 2 != 0) {
            s = "0" + s;
        }
        return s;
    }
}
