package com.dubbo.shop.mail.inter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSender;

public class MailService{

    @Autowired
    private MailSender mailSender;
}
