package br.com.greenpay.core.partner;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import br.com.greenpay.core.order.PaymentService;

@Component
public class ScheduleService {

	private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
	private static final String zoneBR = "America/Sao_Paulo";
	private static final String everyTenSeconds = "*/30 * * * * *";

	@Autowired
	PaymentService paymentService;

	//@Scheduled(cron = everyTenSeconds, zone = zoneBR)
	public void scheduleFutureTaskOne() {
		System.out.println("Task one scheduled to now: " + dateFormat.format(new Date()));
		paymentService.listenerPayment();
	}

}
