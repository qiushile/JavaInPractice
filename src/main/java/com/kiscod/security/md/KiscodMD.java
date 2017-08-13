package com.kiscod.security.md;

import org.apache.commons.codec.binary.Hex;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by lance on 2017/3/29.
 */
public class KiscodMD {

    private static String src = "kiscod security md";

    public static void main(String[] args) {

        jdkMD2();

        jdkMD5();

    }

    public static void jdkMD2() {

        try {
            MessageDigest md = MessageDigest.getInstance("MD2");
            byte[] md5Bytes = md.digest(src.getBytes());
            System.out.println("JDK MD2: " + Hex.encodeHexString(md5Bytes));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

    }

    public static void jdkMD5() {

        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] md5Bytes = md.digest(src.getBytes());
            System.out.println("JDK MD5: " + Hex.encodeHexString(md5Bytes));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

    }



}
