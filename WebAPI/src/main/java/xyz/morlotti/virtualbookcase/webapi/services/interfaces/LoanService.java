package xyz.morlotti.virtualbookcase.webapi.services.interfaces;

import java.util.Optional;

import xyz.morlotti.virtualbookcase.webapi.controllers.beans.APILoan;
import xyz.morlotti.virtualbookcase.webapi.models.Loan;

public interface LoanService
{
	public Iterable<Loan> listLoans();

	public Iterable<Loan> listLoansInLate();

	public Optional<Loan> getLoan(int id);

	public Loan addLoan(APILoan apiLoan);

<<<<<<< HEAD
<<<<<<< HEAD
	Loan updateLoan(int id, Loan genre);
=======
	public Loan updateLoan(int id, Loan loan);

	public Loan extendLoan(Loan loan);
>>>>>>> feature/ticket2
=======
	public Loan updateLoan(int id, APILoan apiLoan);

	public Loan extendLoan(Loan loan);
>>>>>>> feature/ticket3

	public void deleteLoan(int id);
}
