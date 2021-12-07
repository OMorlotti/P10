package xyz.morlotti.virtualbookcase.webapi.services.interfaces;

import java.util.Optional;

import xyz.morlotti.virtualbookcase.webapi.models.PreLoan;

public interface PreLoanService
{
	Iterable<PreLoan> listPreLoans();

	Optional<PreLoan> getPreLoan(int id);

	PreLoan addPreLoan(int bookDescriptionId, int userId);

	void deletePreLoan(int id);
}
