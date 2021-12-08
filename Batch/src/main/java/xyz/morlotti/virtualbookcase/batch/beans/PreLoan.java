package xyz.morlotti.virtualbookcase.batch.beans;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class PreLoan
{
	private Integer id;

	private BookDescription bookDescription;

	private LocalDate preLoanStartDate;

	private LocalDate preLoanExpiryDate;

	private Integer remainingDays;

	private String login;

	private String email;
}
