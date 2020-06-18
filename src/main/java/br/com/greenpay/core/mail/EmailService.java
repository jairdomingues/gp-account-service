package br.com.greenpay.core.mail;

import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import br.com.greenpay.core.system.exception.CustomGenericNotFoundException;

@Service
public class EmailService {

	@Autowired
	private JavaMailSender mailSender;

	@Async
	public String sendMail(String email, String nome, String order) {

		try {

			MimeMessage mail = mailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(mail);
			helper.setTo(email);
			helper.setSubject("Pedido");
			helper.setText("<p>Olá, </p>"+nome, true);
			helper.setText("<p>Seu pedido </p>"+order+" <p><b>foi aprovado.</b></>", true);
			mailSender.send(mail);
			return "OK";

		} catch (Exception e) {
			e.printStackTrace();
			throw new CustomGenericNotFoundException("Erro ao enviar e-mail");
		}

	}

}
