package xyz.morlotti.virtualbookcase.webapi.services.impl;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;

import xyz.morlotti.virtualbookcase.webapi.models.PreLoan;
import xyz.morlotti.virtualbookcase.webapi.daos.UserDAO;
import xyz.morlotti.virtualbookcase.webapi.daos.PreLoanDAO;
import xyz.morlotti.virtualbookcase.webapi.daos.BookDescriptionDAO;
import xyz.morlotti.virtualbookcase.webapi.exceptions.APINotDeletedException;
import xyz.morlotti.virtualbookcase.webapi.services.interfaces.PreLoanService;

@Service
public class PreLoanServiceImpl implements PreLoanService
{
	@Autowired
	PreLoanDAO preLoanDAO;

	@Autowired
	BookDescriptionDAO bookDescriptionDAO;

	@Autowired
	UserDAO userDAO;

	public Iterable<PreLoan> listPreLoans()
	{
		Iterable<PreLoan> preLoans = preLoanDAO.findAll();

		return preLoans;
	}

	public Optional<PreLoan> getLoan(int id)
	{
		Optional<PreLoan> optional = preLoanDAO.findById(id);

		return optional;
	}

	public PreLoan addPreLoan(PreLoan preLoan)
	{
		PreLoan newPreLoan = preLoanDAO.save(preLoan);

		return newPreLoan;
	}

	public PreLoan updateLoan(int id, PreLoan loan)
	{
		PreLoan existingLoan = getLoan(id).get();

		/* TODO */

		return addPreLoan(existingLoan);
	}

	public void deleteLoan(int id)
	{
		try
		{
			preLoanDAO.deleteById(id);
		}
		catch(EmptyResultDataAccessException e)
		{
			throw new APINotDeletedException("PreLoan " + id + " not deleted: " + e.getMessage());
		}
	}
}
