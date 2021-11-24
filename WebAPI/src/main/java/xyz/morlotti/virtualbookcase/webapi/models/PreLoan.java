package xyz.morlotti.virtualbookcase.webapi.models;

import java.time.DayOfWeek;
import java.time.LocalDate;

import javax.persistence.*;

import lombok.*;

import com.fasterxml.jackson.annotation.JsonIgnore;

import static java.time.temporal.ChronoUnit.DAYS;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity(name = "PRELOAN")
@Table(name = "PRELOAN", catalog = "virtualbookcase")
public class PreLoan implements java.io.Serializable
{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	private Integer id;

	@JsonIgnore
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "userFK", nullable = false)
	private User user;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "bookDescriptionFK", nullable = false)
	private BookDescription bookDescription;

	@Column(name = "preLoanStartDate", nullable = false)
	private LocalDate preLoanStartDate;

	public long getRemainingDays()
	{
		DayOfWeek dayOfWeek = LocalDate.now().getDayOfWeek();

		/**/ if(dayOfWeek == DayOfWeek.FRIDAY
		        ||
		        dayOfWeek == DayOfWeek.SATURDAY
		 ) {
			return DAYS.between(LocalDate.now(), preLoanStartDate.plusDays(2 + 2));
		}
		else if(dayOfWeek == DayOfWeek.SUNDAY)
		{
			return DAYS.between(LocalDate.now(), preLoanStartDate.plusDays(2 + 1));
		}
		else
		{
			return DAYS.between(LocalDate.now(), preLoanStartDate.plusDays(2 + 0));
		}
	}

	public LocalDate getPreLoanExpiryDate()
	{
		DayOfWeek dayOfWeek = LocalDate.now().getDayOfWeek();

		/**/ if(dayOfWeek == DayOfWeek.FRIDAY
		        ||
		        dayOfWeek == DayOfWeek.SATURDAY
		 ) {
			return preLoanStartDate.plusDays(2 + 2);
		}
		else if(dayOfWeek == DayOfWeek.SUNDAY)
		{
			return preLoanStartDate.plusDays(2 + 1);
		}
		else
		{
			return preLoanStartDate.plusDays(2 + 0);
		}
	}

	public String getLogin() // getUser() is not present in the JSON for avoiding recursivity error
	{
		return user.getLogin();
	}

	public String getEmail() // getUser() is not present in the JSON for avoiding recursivity error
	{
		return user.getEmail();
	}
}
