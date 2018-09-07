package com.xueyou.demo.ioandnio;

import java.io.*;

public class IoReadWriterDemo {
    private static final String FILE_NAME = "src/main/resources/abc.txt";
    private static final String FILE_NAME2 = "src/main/resources/abc-copy.txt";
    //    private static final String FILE_NAME = "src/main/resources/aa.gif";
//    private static final String FILE_NAME2 = "src/main/resources/aa-copy.gif";
    private static final int BUFFER_SIZE = 20;

    public static void main(String[] args) throws IOException {
        File f = new File(FILE_NAME);
        File f2 = new File(FILE_NAME2);
        FileReader fileReader = null;
        FileWriter fileWriter = null;
        char[] chars = new char[BUFFER_SIZE];
        try {
            fileReader = new FileReader(f);
            fileWriter = new FileWriter(f2);
            int len = 0;
            StringBuffer sb = new StringBuffer();
            while ((len = fileReader.read(chars)) != -1) {
                fileWriter.write(chars, 0, len);
                sb.append(chars, 0, len);
            }
            System.out.println(sb.toString());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (fileReader != null) {
                fileReader.close();
            }
            if (fileWriter != null) {
                fileWriter.close();
            }
        }
    }
}
