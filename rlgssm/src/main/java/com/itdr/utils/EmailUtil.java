package com.itdr.utils;

import javax.mail.Address;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

/**
 * Class: EmailUtil
 * version: JDK 1.8
 * create: 2019-09-11 13:13:46
 *
 * @author: Heyuu
 */
public class EmailUtil {
    public static boolean sendEmail(String email,String vCode){
        String from = "hy_0003@163.com";
        String to = email;
        final String username = "hy_0003@163.com";
        final String password = "heyu2019";

        // 设置环境信息
        Properties properties = System.getProperties();
        // 设置邮件服务器地址
        // 指定smtp服务器
        properties.setProperty("mail.smtp.host","smtp.163.com");
        // 是否进行权限验证
        properties.setProperty("mail.smtp.auth","true");
        // 设置邮件发送协议
        properties.setProperty("mail.transport.protocol","smtp");
        // 设置整个邮件环境信息
        Session session = Session.getInstance(properties);
        // 输出调试信息
        session.setDebug(true);

        try {
            // 实例化一封邮件：message
            MimeMessage message = new MimeMessage(session);
            // 设置发件人：from
            message.setFrom(new InternetAddress(from,"睿乐购商城","UTF-8"));
            // 设置标题
            message.setSubject("激活用户");
            // 设置内容
            String content = "<a href=\"http://localhost:8080/user/activate.do/"+vCode+"\">点此激活</a>";
            message.setContent(content,"text/html;charset=utf-8");

            // 设置附件
            // message.setDataHandler(dh);

            // 从session中获取发邮件的对象
            Transport transport = session.getTransport();
            // 连接服务器，端口号：25
            transport.connect("smtp.163.com",25,username,password);
            // 设置收件人，发送消息
            transport.sendMessage(message,new Address[]{new InternetAddress(to)});
            // 关闭连接
            transport.close();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

//    public static void main(String[] args) {
//        sendEmail("931976205@qq.com","ceshi",Tools.getVCode());
//    }
}
