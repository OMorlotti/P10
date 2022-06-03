package xyz.morlotti.virtualbookcase.webapi.services.impl;

import javax.transaction.Transactional;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import xyz.morlotti.virtualbookcase.webapi.daos.LoanDAO;
import xyz.morlotti.virtualbookcase.webapi.daos.PreLoanDAO;
import xyz.morlotti.virtualbookcase.webapi.exceptions.APINotDeletedException;
import xyz.morlotti.virtualbookcase.webapi.services.interfaces.ResetService;

@Service
@Transactional
public class ResetServiceImpl implements ResetService
{
	@Autowired
	private LoanDAO loanDAO;

	@Autowired
	private PreLoanDAO preLoanDAO;

	@Override
	public void resetUser(String login)
	{
		try
		{
			loanDAO.deleteForUser(login);
		}
		catch(EmptyResultDataAccessException e)
		{
			throw new APINotDeletedException("Loans for user `" + login + "` not deleted: " + e.getMessage());
		}

		try
		{
			preLoanDAO.deleteForUser(login);
		}
		catch(EmptyResultDataAccessException e)
		{
			throw new APINotDeletedException("Pre-loans for user `" + login + "` not deleted: " + e.getMessage());
		}
	}
}
