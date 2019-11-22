package com.cognition.bit.common.until;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;

import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 * @author Worry
 * @version 2019/8/3
 */
public class MailUtil {

    /**
     * QQ发送邮件
     * @param recipient 单人邮箱
     * @param recipients 多人邮箱
     * @param title 标题
     * @param text
     * @throws MessagingException
     */
    public static void sendQQMail(String recipient,List<String> recipients,String title,String text) throws MessagingException {
        Properties properties = new Properties();
        //连接协议
        properties.put("mail.transport.protocol", "smtp");
        //主机名
        properties.put("mail.smtp.host", "smtp.qq.com");
        // 端口号
        properties.put("mail.smtp.port", 465);
        properties.put("mail.smtp.auth", "true");
        // 设置是否使用ssl安全连接 ---一般都使用
        properties.put("mail.smtp.ssl.enable", "true");
        // 设置是否显示debug信息 true 会在控制台显示相关信息
        properties.put("mail.debug", "true");
        // 得到回话对象
        Session session = Session.getInstance(properties);
        // 获取邮件对象
        Message message = new MimeMessage(session);
        // 设置发件人邮箱地址
        message.setFrom(new InternetAddress("954932995@qq.com"));
        // 设置收件人邮箱地址
        List<InternetAddress> internetAddresses = new ArrayList<>();
        if (recipients.size()>1) {
            for (String rec : recipients) {
                internetAddresses.add(new InternetAddress(rec));
            }
            InternetAddress[] internetAddressesStr = new InternetAddress[internetAddresses.size()];
            internetAddresses.toArray(internetAddressesStr);
            //多人发送放入邮箱地址数组
            message.setRecipients(Message.RecipientType.TO, internetAddressesStr);
        }else {
            //一个收件人
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(recipient));
        }

        // 设置邮件标题
        message.setSubject(title);
        // 设置邮件内容
        message.setText(text);
        // 得到邮差对象
        Transport transport = session.getTransport();
        // 连接自己的邮箱账户
        // 密码为QQ邮箱开通的stmp服务后得到的客户端授权码
        transport.connect("954932995@qq.com", "gtpcrnleaazobdgi");
        // 发送邮件
        transport.sendMessage(message, message.getAllRecipients());
        transport.close();
    }
}
