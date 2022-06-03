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

	public Loan updateLoan(int id, APILoan apiLoan);

	public Loan extendLoan(Loan loan);

	public void deleteLoan(int id);
}
