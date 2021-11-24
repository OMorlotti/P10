package xyz.morlotti.virtualbookcase.webapi.services.interfaces;

import java.util.Optional;

import xyz.morlotti.virtualbookcase.webapi.models.Loan;

public interface LoanService
{
	Iterable<Loan> listLoans();

	Iterable<Loan> listLoansInLate();

	Optional<Loan> getLoan(int id);

	Loan addLoan(Loan genre);

	Loan updateLoan(int id, Loan genre);

	void deleteLoan(int id);
}
