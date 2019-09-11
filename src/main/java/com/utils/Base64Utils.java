package com.utils;

import org.apache.commons.codec.binary.Base64;

import java.io.UnsupportedEncodingException;

public class Base64Utils {

    //todo: 自己编写Base64的代码。参照：（1）java源码；（2）E:\Study\Java\编码加密；

    public static void main(String[] args) throws UnsupportedEncodingException {

        // 1.早期在Java上做Base64的编码与解码，会使用到JDK里sun.misc套件下的BASE64Encoder和BASE64Decoder这两个类别

        // 2.Apache Commons Codec有提供Base64的编码与解码功能，会使用到org.apache.commons.codec.binary套件下的Base64类别
        final Base64 base64 = new Base64();
        final String text = "字串文字";
        final byte[] textByte = text.getBytes("UTF-8");
        //编码
        final String encodedText = base64.encodeToString(textByte);
        System.out.println(encodedText);
        //解码
        System.out.println(new String(base64.decode(encodedText), "UTF-8"));

        // 3.从Java 8开始的java.util套件中，新增了Base64的类别，可以用来处理Base64的编码与解码
        final java.util.Base64.Decoder decoder = java.util.Base64.getDecoder();
        final java.util.Base64.Encoder encoder = java.util.Base64.getEncoder();
        final String text2 = "字串文字";
        final byte[] textByte2 = text2.getBytes("UTF-8");
        //编码
        final String encodedText2 = encoder.encodeToString(textByte2);
        System.out.println(encodedText2);
        //解码
        System.out.println(new String(decoder.decode(encodedText2), "UTF-8"));

//        System.out.println(java.util.Base64.getEncoder().encodeToString("s13".getBytes()));
//        System.out.println(java.util.Base64.getUrlEncoder().encodeToString("s13".getBytes()));
//        System.out.println(java.util.Base64.getMimeEncoder().encodeToString("s13".getBytes()));

        byte[] bytes = new byte[]{43,27,41,54};
        System.out.println(new String(bytes,"UTF-8"));

        // 小结：与sun.mis c套件和Apache Commons Codec所提供的Base64编解码器来比较的话，Java 8提供的Base64拥有更好的效能。
        // 实际测试编码与解码速度的话，Java 8提供的Base64，要比sun.mis c套件提供的还要快至少11倍，
        // 比Apache Commons Codec提供的还要快至少3倍。
        // 因此在Java上若要使用Base64，这个Java 8底下的java .util套件所提供的Base64类别绝对是首选！
        // 对于URL安全或MIME的Base64，只需将上述getEncoder()getDecoder()更换为getUrlEncoder()、getUrlDecoder()
        // 或getMimeEncoder()、getMimeDecoder()即可。




    }

}
