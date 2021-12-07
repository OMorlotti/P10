package xyz.morlotti.virtualbookcase.webapi.services.impl;

import java.util.Optional;
import java.time.LocalDate;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;

import xyz.morlotti.virtualbookcase.webapi.models.User;
import xyz.morlotti.virtualbookcase.webapi.models.PreLoan;
import xyz.morlotti.virtualbookcase.webapi.models.BookDescription;
import xyz.morlotti.virtualbookcase.webapi.daos.UserDAO;
import xyz.morlotti.virtualbookcase.webapi.daos.PreLoanDAO;
import xyz.morlotti.virtualbookcase.webapi.daos.BookDescriptionDAO;
import xyz.morlotti.virtualbookcase.webapi.exceptions.APINotFoundException;
import xyz.morlotti.virtualbookcase.webapi.exceptions.APINotDeletedException;
import xyz.morlotti.virtualbookcase.webapi.exceptions.APIInvalidValueException;
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
		Iterable<PreLoan> preLoans = preLoanDAO.findByOrderByPreLoanStartDateAsc();

		return preLoans;
	}

	public Optional<PreLoan> getPreLoan(int id)
	{
		Optional<PreLoan> optional = preLoanDAO.findById(id);

		return optional;
	}

	public PreLoan addPreLoan(int bookDescriptionId, int userId)
	{
		/*------------------------------------------------------------------------------------------------------------*/

		Optional<BookDescription> optional1 = bookDescriptionDAO.findById(bookDescriptionId);

		if(!optional1.isPresent())
		{
			throw new APINotFoundException("cannot find book description id '" + bookDescriptionId + "'");
		}

		BookDescription bookDescription = optional1.get();

		/*------------------------------------------------------------------------------------------------------------*/

		Optional<User> optional2 = userDAO.findById(userId);

		if(!optional2.isPresent())
		{
			throw new APINotFoundException("cannot find user id '" + userId + "'");
		}

		User user = optional2.get();

		/*------------------------------------------------------------------------------------------------------------*/

		long nbOfAvailableBooks = bookDescription.getBooks().stream().filter(x -> x.isAvailable()).count();

		if(nbOfAvailableBooks != 0)
		{
			throw new APIInvalidValueException("the book description id '" + bookDescriptionId + "' is available for loaning");
		}

		if(bookDescription.getNumberOfPreLoans() >= 2 * bookDescription.getNumberOfBooks())
		{
			throw new APIInvalidValueException("the book description id '" + bookDescriptionId + "' is no longer available for pre-loaning");
		}

		/*------------------------------------------------------------------------------------------------------------*/

		PreLoan preLoan = new PreLoan();

		preLoan.setUser(user);
		preLoan.setBookDescription(bookDescription);
		preLoan.setPreLoanStartDate(LocalDate.now());

		/*------------------------------------------------------------------------------------------------------------*/

		PreLoan newPreLoan = preLoanDAO.save(preLoan);

		/*------------------------------------------------------------------------------------------------------------*/

		return newPreLoan;
	}

	public void deletePreLoan(int id)
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
