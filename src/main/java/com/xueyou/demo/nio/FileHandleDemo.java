package com.xueyou.demo.nio;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.event.TreeWillExpandListener;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by wuxueyou on 2018/9/23.
 */
public class FileHandleDemo {
    private static final Logger LOGGER = LoggerFactory.getLogger(FileHandleDemo.class.getName());

    private static final String fileName1 = "/Users/wuxueyou/Project/netty/nettystudy/src/main/resources/tmpfile/123.txt";
    private static final String fileName2 = "/Users/wuxueyou/Project/netty/nettystudy/src/main/resources/tmpfile/222.txt";
    private static final String fileName3 = "http://csdnimg.cn/cdn/content-toolbar/zq-logo.jpg";
    private static final String fileName4 = "/Users/wuxueyou/Project/netty/nettystudy/src/main/resources/tmpfile/222.jpg";
    private static final String fileName5 = "/Users/wuxueyou/Project/netty/nettystudy/src/main/resources/tmpfile/333.txt";

    private static final int BYTEBUFFERCAPACITY = 100;
    private static final Set<OpenOption> OPEN_OPTION = new HashSet<>();

    static {
        OPEN_OPTION.add(StandardOpenOption.CREATE);
        OPEN_OPTION.add(StandardOpenOption.WRITE);
        OPEN_OPTION.add(StandardOpenOption.READ);
    }

    public static void main(String[] args) {
        LOGGER.info("BEGIN...");
//        copyFile();
//        copyFromNetFileFun1();
//        copyFromNetFileFun2();
        copyFromLocalFileNioFun();
        LOGGER.info("END...");
    }

    /**
     * 文件copy操作
     */
    private static void copyFile() {
        Path path1 = Paths.get(fileName1);
        Path path2 = Paths.get(fileName2);
        try {
            Files.copy(path1, path2);
        } catch (IOException e) {
            LOGGER.error("Copy error:{}", e);
        }
    }

    /**
     * Files.copy 方式
     */
    private static void copyFromNetFileFun1() {
        Path target = Paths.get(fileName4);
        try {
            URL url = new URL(fileName3);
            InputStream in = url.openStream();
            Files.copy(in, target);
        } catch (MalformedURLException e) {
            LOGGER.error("Url error:{}", e);
        } catch (IOException e) {
            LOGGER.error("io error:{}", e);
        }

    }

    /**
     * 手动写入outputStream方式
     */
    private static void copyFromNetFileFun2() {
        File target = new File(fileName4);
        InputStream in = null;
        OutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream(target);
            URL url = new URL(fileName3);
            in = url.openStream();
            byte[] buffers = new byte[1024];
            int len = 0;
            while ((len = in.read(buffers)) != -1) {
                outputStream.write(buffers, 0, len);
            }
        } catch (MalformedURLException e) {
            LOGGER.error("Url error:{}", e);
        } catch (IOException e) {
            LOGGER.error("io error:{}", e);
        } finally {
            try {
                in.close();
            } catch (IOException e) {
                LOGGER.error("inputStream close error:{}", e);
            }
            try {
                outputStream.close();
            } catch (IOException e) {
                LOGGER.error("outputStream close error:{}", e);
            }
        }
    }

    /**
     * 采用nio方式
     */
    private static void copyFromLocalFileNioFun() {
        FileChannel srcChannel = null;
        FileChannel targetChannel = null;
        try {
            Path srcPath = Paths.get(fileName1);
            Path dstPath = Paths.get(fileName5);
            targetChannel = FileChannel.open(dstPath, OPEN_OPTION);
            srcChannel = FileChannel.open(srcPath, OPEN_OPTION);
            ByteBuffer byteBuffer = ByteBuffer.allocate(BYTEBUFFERCAPACITY);
            int length = -1;
            while ((length = srcChannel.read(byteBuffer)) != -1) {
                byteBuffer.flip();
                targetChannel.write(byteBuffer);
                LOGGER.info("buffer length:{}", length);
                byteBuffer.clear();
            }
        } catch (MalformedURLException e) {
            LOGGER.error("Url error:{}", e);
        } catch (IOException e) {
            LOGGER.error("io error:{}", e);
        } finally {
            if (targetChannel != null) {
                try {
                    targetChannel.close();
                } catch (IOException e) {
                    LOGGER.error("targetchannel close channel:{}", e);
                }
            }
            if (srcChannel != null) {
                try {
                    srcChannel.close();
                } catch (IOException e) {
                    LOGGER.error("filechannel close channel:{}", e);
                }
            }
        }

    }
}

