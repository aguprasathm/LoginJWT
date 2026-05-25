package com.example.JwtRegister.Config;

import java.util.Properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

@Configuration
public class EmailConfig {
	@Value("${spring.mail.host}")
	private String mailhost;
	@Value("${spring.mail.port}")
	private String mailport;
	@Value("${spring.mail.username}")
	private String mailUsername;
	@Value("${spring.mail.password}")
	private String mailPassword;
	
	@Bean
	public JavaMailSender getJavaMailSender() {
		JavaMailSenderImpl javaMailSender=new JavaMailSenderImpl();
		javaMailSender.setHost(mailhost);
		javaMailSender.setPort(Integer.parseInt(mailport));
		javaMailSender.setUsername(mailUsername);
		javaMailSender.setPassword(mailPassword);
		
		Properties  props=javaMailSender.getJavaMailProperties();
		props.put("mail.smtp.starttls.enable", "true");
		return javaMailSender;
		
	}
}
