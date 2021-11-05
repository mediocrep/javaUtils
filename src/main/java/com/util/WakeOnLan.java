package com.util;

import java.io.IOException;
import java.net.*;
import java.util.HashMap;
import java.util.Map;

public class WakeOnLan {

    /**
     * main方法，发送UDP广播，实现远程开机
     */
    public static void main(String[] args) {
        System.out.println("网络唤醒程序开始执行...");
        /*main静态方法中不能使用 this
	    报错：'run.halo.app.WakeOnLan.this' cannot be referenced from a static context
        注：Java获取当前类名的两种方法:
        1.适用于非静态方法：this.getClass().getName()
        2.适用于静态方法：Thread.currentThread().getStackTrace()[1].getClassName()
        */
        if(args.length < 2
                || (!"lan".equals(args[0]) && !"wan".equals(args[0]))
                || (!"asus".equals(args[1]) && !"lenovo".equals(args[1]))){
            System.out.println("Usage: java "
                    + Thread.currentThread().getStackTrace()[1].getClassName()
                    + " (lan | wan) (asus | lenovo)");
            return;
        }

        String ip = "";
        int port = 0;
        //转换为2进制的魔术包数据
        byte[] magicPacketArr;
        //广播魔术包
        MulticastSocket socket = null;
//        DatagramSocket socket = null;
        Map<String,String> macMap = new HashMap<>();
        StringBuilder sb = new StringBuilder("唤醒对象信息：\n");
        try {
            macMap.put("asus","9C5C8E35DB67"); //华硕笔记本电脑的有线mac地址
            macMap.put("lenovo","4487FCA0CCE2"); //联想台式电脑的有线mac地址

            String prefix = "0xFFFFFFFFFFFF";
            String targetMAC = macMap.get(args[0]);
            //魔术包数据
            StringBuilder magicPacketStr = new StringBuilder(prefix);
            for(int i=0;i<16;i++){
                magicPacketStr.append(targetMAC);
            }

            magicPacketArr = hexToBinary(magicPacketStr.toString());

            if("lan".equals(args[0])){
                ip = "255.255.255.255"; //广播IP地址
                port = 9;//端口号
                sb.append("局域网唤醒：");
            }else if("wan".equals(args[0])){
                sb.append("广域网唤醒：");
                ip = "mediocrepeople.tpddns.cn"; //IP地址
                if("asus".equals(args[1])){
                    port = 9609;//端口号
                }else if("lenovo".equals(args[1])){
                    port = 11609;//端口号
                }
            }

            if("asus".equals(args[1])){
                sb.append("华硕笔记本电脑").append("\n");
            }else if("lenovo".equals(args[1])){
                sb.append("联想台式电脑").append("\n");
            }
            sb.append("MAC：").append(targetMAC).append("\n")
                    .append("IP：").append(ip).append("\n")
                    .append("PORT：").append(port).append("\n");
            System.out.println(sb.toString());

            //1.获取ip地址
            InetAddress address = InetAddress.getByName(ip);
            //2.获取广播socket
            socket = new MulticastSocket(port);
//            socket = new DatagramSocket(port);
            //3.封装数据包
            /*public DatagramPacket(byte[] buf,int length
             *      ,InetAddress address
             *      ,int port)
             * buf：缓存的命令
             * length：每次发送的数据字节数，该值必须小于等于buf的大小
             * address：广播地址
             * port：广播端口
             */
            DatagramPacket packet = new DatagramPacket(magicPacketArr, magicPacketArr.length, address, port);
            //4.发送数据
            socket.send(packet);
            System.out.println("网络唤醒程序执行结束...");
        } catch (UnknownHostException e) {
            //Ip地址错误时候抛出的异常
            e.printStackTrace();
        } catch (IOException e) {
            //获取socket失败时候抛出的异常
            e.printStackTrace();
        } finally {
            //5.关闭socket
            try{
                if(socket != null)
                    socket.close();
            }catch(Exception e){
                e.printStackTrace();
            }

        }
    }

    /**
     * 将16进制字符串转换为用byte数组表示的二进制形式
     * @param hexString：16进制字符串
     * @return：用byte数组表示的十六进制数
     */
    private static byte[] hexToBinary(String hexString) {
        //1.去除字符串中的16进制标识"0X"并将所有字母转换为大写
        hexString = hexString.toUpperCase().replace("0X", "");
        //2.定义变量：用于存储转换结果的数组
        int len = hexString.length() / 2;
        byte[] result = new byte[len];

        //3.开始转换
        //	3.1.定义两个临时存储数据的变量（貌似没用）
//        char tmp1 = '0';
//        char tmp2 = '0';
        //	3.2.开始转换，将每两个十六进制数放进一个byte变量中
        for(int i = 0; i < len; i++){
            result[i] = (byte)((hexToDec(hexString.charAt(i*2))<<4)|(hexToDec(hexString.charAt(i*2+1))));
            //System.out.print(result[i] + ", ");
        }
        System.out.println();
        return result;
    }

    /**
     * 用于将16进制的单个字符映射到10进制的方法
     * @param c：16进制数的一个字符
     * @return：对应的十进制数
     */
    private static int hexToDec(char c) {
        return (byte)"0123456789ABCDEF".indexOf(c);
    }
}
