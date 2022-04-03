package com.example.scadanli;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class SCADANLI_Socket {
    Socket socket=null;
    InputStream is;
    BufferedReader br;
    /**
     *
     * @param ip
     *          服务器的ip
     * @param port
     *          端口号
     * @throws IOException
     */
    public SCADANLI_Socket(String ip, int port) throws IOException {//创建连接
        socket=new Socket(ip,port);
    }

    /**
     *
     * @return
     *          返回服务器的数据
     * @throws IOException
     */
    public String GetData() throws IOException {
        if(socket!=null) {
            // 步骤1：创建输入流对象InputStream
            is = socket.getInputStream();
            // 步骤2：创建输入流读取器对象 并传入输入流对象
            // 该对象作用：获取服务器返回的数据
            InputStreamReader isr = new InputStreamReader(is);
            br = new BufferedReader(isr);
            // 步骤3：通过输入流读取器对象 接收服务器发送过来的数据
             return br.readLine();
        }
        else{
            return null;
        }
    }

    /**
     *
     * @param str
     *              发送到服务器的数据
     * @throws IOException
     */
    public void SentData(String str) throws IOException {
        // 步骤1：从Socket 获得输出流对象OutputStream
        // 该对象作用：发送数据
        OutputStream outputStream = socket.getOutputStream();
        // 步骤2：写入需要发送的数据到输出流对象中
        outputStream.write((str+"\n").getBytes(StandardCharsets.UTF_8));
        // 特别注意：数据的结尾加上换行符才可让服务器端的readline()停止阻塞
        // 步骤3：发送数据到服务端
        outputStream.flush();
    }

    /**
     *          断开服务器的连接
     * @throws IOException
     */
    public void DisConnect() throws IOException {
        // 断开 客户端发送到服务器 的连接，即关闭输出流对象OutputStream
        is.close();
        // 断开 服务器发送到客户端 的连接，即关闭输入流读取器对象BufferedReader
        br.close();
        // 最终关闭整个Socket连接
        socket.close();

    }
}
