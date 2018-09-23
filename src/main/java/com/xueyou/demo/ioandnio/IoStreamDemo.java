package com.xueyou.demo.ioandnio;

import java.io.*;

public class IoStreamDemo {
    private static final String FILE_NAME = "src/main/resources/abc.txt";
    private static final String FILE_NAME2 = "src/main/resources/abc-copy.txt";
    //    private static final String FILE_NAME = "src/main/resources/aa.gif";
//    private static final String FILE_NAME2 = "src/main/resources/aa-copy.gif";
//    private static final String FILE_NAME = "http://static3.j.cn/img/dailySign/180907/2258/739558a6b2ae11e8.png";
//    private static final String FILE_NAME2 = "src/main/resources/net-copy.png";
    private static final int BUFFER_SIZE = 2048;

    public static void main(String[] args) throws IOException {
        File f = new File(FILE_NAME);
        File f2 = new File(FILE_NAME2);
        byte[] bytes = new byte[BUFFER_SIZE];
        FileInputStream fileInputStream = null;
        FileOutputStream fileOutputStream = null;
        try {
            fileInputStream = new FileInputStream(f);
            fileOutputStream = new FileOutputStream(f2);
            int len = 0;
            int a = 0;
            while ((len = fileInputStream.read(bytes)) != -1) {
                System.out.println(++a);
                fileOutputStream.write(bytes, 0, len);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (fileInputStream != null) {
                fileInputStream.close();
            }
            if (fileOutputStream != null) {
                fileOutputStream.close();
            }
        }
    }
}
