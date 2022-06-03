package xyz.morlotti.virtualbookcase.webapi.controllers.beans;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class APILoan
{
	Integer userId;

	Integer bookId;

	LocalDate loanStartDate = null;

	LocalDate loanEndDate = null;

	Boolean extensionAsked = false;

	String comment = null;
}
