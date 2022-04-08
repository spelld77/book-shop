package com.shop.portshop.commons;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

@Slf4j
public class MailHandler {

    private JavaMailSender javaMailSender;
    private MimeMessage message;
    private MimeMessageHelper messageHelper;

    //생성자
    public MailHandler(JavaMailSender javaMailSender) throws MessagingException {
        this.javaMailSender = javaMailSender;
        message = javaMailSender.createMimeMessage();
        messageHelper = new MimeMessageHelper(message, true, "UTF-8");
    }

    //메일 전송
    public void send() throws Exception {
        try {
            javaMailSender.send(message);

        } catch(Exception e) {
            log.error("Sending mail failed", e);
        }
    }

    //발신자 이메일
    public void setFrom(String email,String name) throws MessagingException, UnsupportedEncodingException {
        messageHelper.setFrom(email, name);
    }

    //수신자 이메일
    public void setTo(String email) throws MessagingException {
        messageHelper.setTo(email);
    }

    //메일 제목
    public void setSubject(String subject) throws MessagingException {
        messageHelper.setSubject(subject);
    }

    //메일 내용
    public void setText(String text) throws MessagingException {
        messageHelper.setText(text, true);
    }

    public void setInline(String contentId, String pathToInline) throws IOException, MessagingException {
        File file = new ClassPathResource(pathToInline).getFile();
        FileSystemResource fsr = new FileSystemResource(file);
        messageHelper.addInline(contentId, fsr);
    }
}
