package com.shop.portshop.service;

import com.shop.portshop.commons.MailHandler;
import com.shop.portshop.vo.MailDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

@Service
@Slf4j
@RequiredArgsConstructor
public class MailService {

    @Value("${spring.mail.username}")
    private String fromAddress;
    private final JavaMailSender javaMailSender;

    //암호 변경 이메일 보내기
    public boolean sendEmailForChangingPassword(MailDto mailDto){

        try {
            MailHandler mailHandler = new MailHandler(javaMailSender);
            // 받는 사람
            mailHandler.setTo(mailDto.getAddress());
            // 보내는 사람
            mailHandler.setFrom(fromAddress, "bookInside");
            // 제목
            mailHandler.setSubject(mailDto.getTitle());
            // HTML Layout
            String htmlContent = "<p>" + mailDto.getMessage() +"<p> <img src='cid:sample-img'>";
            mailHandler.setText(htmlContent);
            // 이미지 삽입
            mailHandler.setInline("sample-img", "static/img/logo3.png");
            //메일 보내기
            mailHandler.send();


        } catch (MessagingException | UnsupportedEncodingException e) {
           log.error("Mail서비스 에러", e);

        } catch (IOException e) {
            log.error("Mail서비스 파일 이미지 IO에러", e);

        } catch (Exception e) {
            log.error("MailError", e);
        }

        return true;
    }
}
