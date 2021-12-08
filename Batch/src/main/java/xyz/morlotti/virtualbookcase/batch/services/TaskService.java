package xyz.morlotti.virtualbookcase.batch.services;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;

import org.springframework.web.bind.annotation.RequestHeader;
import xyz.morlotti.virtualbookcase.batch.beans.Loan;
import xyz.morlotti.virtualbookcase.batch.EmailSender;
import xyz.morlotti.virtualbookcase.batch.MyFeignProxy;
import xyz.morlotti.virtualbookcase.batch.beans.PreLoan;

@Service
@EnableAsync
@EnableScheduling
public class TaskService
{
	private static final Logger logger = LoggerFactory.getLogger(TaskService.class);

	@Value("${virtualbookcase.app.batch.login}")
	private String login;

	@Value("${virtualbookcase.app.batch.password}")
	private String password;

	@Autowired
	private MyFeignProxy myFeignProxy;

	@Autowired
	private EmailSender emailSender;

	// See: https://riptutorial.com/spring/example/21209/cron-expression
	@Scheduled(cron = "0 10 * * * *") // Tous les jours à 10h
	public void inLateTask()
	{
		String token = myFeignProxy.login(login, password);

		List<Loan> loans = myFeignProxy.listLoansInLate(token);

		for(Loan loan: loans)
		{
			try
			{
				emailSender.sendMessage(
					loan.getEmail(),
					loan.getEmail(),
					"",
					"Retour de livre en retard de " + (-loan.getRemainingDays()) + " jour(s)",
					"Bonjour " + loan.getLogin() + "\nLe retour du livre " + loan.getBook().getBookDescription().getTitle() + " est hors délais de "+ (-loan.getRemainingDays()) + " jour(s).\n" +
						"Veuillez effectuer le retour au plus vite !\n" +
						"Cordialement,\n" +
						"Votre bibliothèque"
				);
			}
			catch(Exception e)
			{
				logger.error("Email " + loan.getEmail() + " for user " + loan.getLogin() + " was not successfully sent (book #" + loan.getBook().getId() + "): {}", e);
			}
		}
	}

	@Scheduled(cron = "0 1 * * * *") // Tous les jours à 1h00
	public void cleanPreLoanTask()
	{
		String token = myFeignProxy.login(login, password);

		Iterable<PreLoan> preLoans = myFeignProxy.listPreLoans(token);

		List<PreLoan> expiredPreLoans = StreamSupport.stream(preLoans.spliterator(), false).filter(x -> x.getRemainingDays() <= 0).collect(Collectors.toList());

		for(PreLoan preLoan: expiredPreLoans)
		{
			try
			{
				myFeignProxy.deletePreLoan(token, preLoan.getId());

				emailSender.sendMessage(
						preLoan.getEmail(),
						preLoan.getEmail(),
						"",
						"Pré-réservation du livre \"" + preLoan.getBookDescription().getTitle() + "\" expirée",
						"Bonjour " + preLoan.getLogin() + "\nPré-réservation du livre \"" + preLoan.getBookDescription().getTitle() + "\" le " + preLoan.getPreLoanExpiryDate() + " .\n" +
						"Vous pourrez le réserver à nouveau ultérieurement.\n" +
						"Cordialement,\n" +
						"Votre bibliothèque"
				);
			}
			catch(Exception e)
			{
				logger.error("Email " + preLoan.getEmail() + " for user " + preLoan.getLogin() + " was not successfully sent (preloan #" + preLoan.getId() + "): {}", e);
			}
		}
	}
}
