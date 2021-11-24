package xyz.morlotti.virtualbookcase.webapi.services.interfaces;

import xyz.morlotti.virtualbookcase.webapi.models.PreLoan;

import java.util.Optional;

public interface PreLoanService
{
	Iterable<PreLoan> listPreLoans();

	Optional<PreLoan> getLoan(int id);

	PreLoan addPreLoan(PreLoan preLoan);

	PreLoan updateLoan(int id, PreLoan loan);

	void deleteLoan(int id);
}
