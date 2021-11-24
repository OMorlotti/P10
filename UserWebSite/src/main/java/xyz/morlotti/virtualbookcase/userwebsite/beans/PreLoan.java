package xyz.morlotti.virtualbookcase.userwebsite.beans;

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

	private User user;

	private BookDescription bookDescription;

	private LocalDate preLoanStartDate;

	private LocalDate preLoanExpiryDate;

	private Integer remainingDays;

	private String login;

	private String email;
}
