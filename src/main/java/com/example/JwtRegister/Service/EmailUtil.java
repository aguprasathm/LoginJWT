package com.example.JwtRegister.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailUtil {
	@Autowired
	private JavaMailSender javaMailSender;
	
	public void sendOtpEmail(String email, String otp) throws MessagingException {
	    MimeMessage mimeMessage = javaMailSender.createMimeMessage();
	    MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
	    mimeMessageHelper.setTo(email);
	    mimeMessageHelper.setSubject(" OTP Verification");

	    // Construct the HTML content for the email
//	    String emailBody = """
//	        
//	                        <p>Dear User,</p>
//	                        <p>Thank you for choosing our platform. To verify your account, please click the button below:</p>
//	                        <a href="http://localhost:8081/auth/verify-account?email=%s&otp=%s" class="button"><h4>Verify your account</h4></a>
//	                        <p>This link will expire in 5 minutes for security reasons.</p>
//	                        <p>If you did not request this verification, please ignore this email.</p>
//	                        <div class="footer">
//	                            <p>Best regards,<br>Your Company Name</p>
//	              
//	            """.formatted(email, otp);
	    
	    String format = String.format("your otp for email verification is: %s", otp);

	    mimeMessageHelper.setText(format, true);
	    javaMailSender.send(mimeMessage);
	}

	
	public void sendsetpasswordEmail(String email) throws MessagingException {
		MimeMessage mimeMessage=javaMailSender.createMimeMessage();
		MimeMessageHelper mimeMessageHelper=new MimeMessageHelper(mimeMessage,true);
		mimeMessageHelper.setTo(email);
		mimeMessageHelper.setSubject("Set Password");
		mimeMessageHelper.setText("""
				<div>
				<a href="http://localhost:8081/auth/set-password?email=%s" target="_blank">click link to set password</a>
				</div>
				
				""".formatted(email),true);
		javaMailSender.send(mimeMessage);
	}
}
