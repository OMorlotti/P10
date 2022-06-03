package xyz.morlotti.virtualbookcase.webapi.services.impl;

import java.time.LocalDate;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.beans.factory.annotation.Autowired;

import xyz.morlotti.virtualbookcase.webapi.controllers.beans.APILoan;
import xyz.morlotti.virtualbookcase.webapi.daos.UserDAO;
import xyz.morlotti.virtualbookcase.webapi.exceptions.APINotAuthorizedException;
import xyz.morlotti.virtualbookcase.webapi.exceptions.APINotFoundException;
import xyz.morlotti.virtualbookcase.webapi.models.Book;
import xyz.morlotti.virtualbookcase.webapi.models.Loan;
import xyz.morlotti.virtualbookcase.webapi.daos.BookDAO;
import xyz.morlotti.virtualbookcase.webapi.daos.LoanDAO;
import xyz.morlotti.virtualbookcase.webapi.models.User;
import xyz.morlotti.virtualbookcase.webapi.services.interfaces.LoanService;
import xyz.morlotti.virtualbookcase.webapi.exceptions.APINotModifiedException;
import xyz.morlotti.virtualbookcase.webapi.exceptions.APINotDeletedException;

@Service
@Transactional
public class LoanServiceImpl implements LoanService
{
	@Autowired
	private LoanDAO loanDAO;

	@Autowired
	private BookDAO bookDAO;

	@Autowired
	private UserDAO userDAO;

	public Iterable<Loan> listLoans()
	{
		Iterable<Loan> loans = loanDAO.findAll();

		return loans;
	}

	public Iterable<Loan> listLoansInLate()
	{
		Iterable<Loan> loans = loanDAO.findInLate();

		return loans;
	}

	public Optional<Loan> getLoan(int id)
	{
		Optional<Loan> optional = loanDAO.findById(id);

		return optional;
	}

	public Loan addLoan(APILoan apiLoan)
	{
		User user = userDAO.findById(apiLoan.getUserId()).orElseThrow(() -> new APINotFoundException("Invalid user Id"));
		Book book = bookDAO.findById(apiLoan.getBookId()).orElseThrow(() -> new APINotFoundException("Invalid book Id"));

		Loan loan = new Loan(null, user, book, LocalDate.now(), null, false, null);

		return saveLoan(loan);
	}

	public Loan updateLoan(int id, APILoan apiLoan)
	{
		Optional<Loan> optional = getLoan(id);

		if(optional.isPresent())
		{
			Loan existingLoan = getLoan(id).get();

			existingLoan.setLoanStartDate(apiLoan.getLoanStartDate());

			existingLoan.setLoanEndDate(apiLoan.getLoanEndDate());

			existingLoan.setExtensionAsked(apiLoan.getExtensionAsked());

			existingLoan.setComment(apiLoan.getComment());

			return saveLoan(existingLoan);
		}
		else
		{
			throw new APINotFoundException("Loan " + id + "not found");
		}
	}

	public Loan extendLoan(Loan existingLoan)
	{
		if("ASK_EXTENSION".equals(existingLoan.getState()))
		{
			existingLoan.setExtensionAsked(true);

			return saveLoan(existingLoan);
		}
		else
		{
			throw new APINotAuthorizedException("This loan is not available for being extended");
		}
	}

	private Loan saveLoan(Loan loan)
	{
		/*------------------------------------------------------------------------------------------------------------*/

		Loan newLoan = loanDAO.save(loan);

		if(newLoan == null)
		{
			throw new APINotModifiedException("Loan not inserted");
		}

		/*------------------------------------------------------------------------------------------------------------*/

		Book book = loan.getBook();

		if(book.isAvailable() != (newLoan.getLoanEndDate() != null))
		{
			book.setAvailable(newLoan.getLoanEndDate() != null);

			Book newBook = bookDAO.save(book);

			if(newBook == null)
			{
				throw new APINotModifiedException("Book not updated");
			}
		}

		/*------------------------------------------------------------------------------------------------------------*/

		return newLoan;
	}

	public void deleteLoan(int id)
	{
		try
		{
			loanDAO.deleteById(id);
		}
		catch(EmptyResultDataAccessException e)
		{
			throw new APINotDeletedException("Loan " + id + " not deleted: " + e.getMessage());
		}
	}
}
