package xyz.morlotti.virtualbookcase.webapi.services.interfaces;

import java.util.Optional;

import xyz.morlotti.virtualbookcase.webapi.models.Loan;

public interface LoanService
{
	Iterable<Loan> listLoans();

	Iterable<Loan> listLoansInLate();

	Optional<Loan> getLoan(int id);

	Loan addLoan(Loan genre);

<<<<<<< HEAD
	Loan updateLoan(int id, Loan genre);
=======
	public Loan updateLoan(int id, Loan loan);

	public Loan extendLoan(Loan loan);
>>>>>>> feature/ticket2

	void deleteLoan(int id);
}
