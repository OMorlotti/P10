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
@Table(name = "PRELOAN", uniqueConstraints = {
	@UniqueConstraint(columnNames = {"userFK", "bookDescriptionFK"})
})
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

	public LocalDate getPreLoanExpiryDate()
	{
		DayOfWeek dayOfWeek = preLoanStartDate.getDayOfWeek();

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

	public long getRemainingDays()
	{
		return DAYS.between(getPreLoanExpiryDate(), LocalDate.now());
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
