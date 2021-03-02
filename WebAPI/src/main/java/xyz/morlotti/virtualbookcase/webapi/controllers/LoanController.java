package xyz.morlotti.virtualbookcase.webapi.controllers;

import java.net.URI;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import xyz.morlotti.virtualbookcase.webapi.models.Loan;
import xyz.morlotti.virtualbookcase.webapi.models.User;
import xyz.morlotti.virtualbookcase.webapi.services.interfaces.LoanService;
import xyz.morlotti.virtualbookcase.webapi.exceptions.APINotFoundException;
import xyz.morlotti.virtualbookcase.webapi.security.services.UserDetailsImpl;
import xyz.morlotti.virtualbookcase.webapi.exceptions.APiNotAuthorizedException;

@RestController
public class LoanController
{
	@Autowired
	LoanService loanService;

	@PreAuthorize("hasAuthority('ADMIN') or hasAuthority('EMPLOYEE')")
	@RequestMapping(value = "/loans", method = RequestMethod.GET)
	public Iterable<Loan> listLoans()
	{
		return loanService.listLoans();
	}

	@PreAuthorize("hasAuthority('ADMIN') or hasAuthority('EMPLOYEE')")
	@RequestMapping(value = "/loans/in-late", method = RequestMethod.GET)
	public Iterable<Loan> listLoansInLate()
	{
		return loanService.listLoansInLate();
	}

	@PreAuthorize("hasAuthority('ADMIN') or hasAuthority('EMPLOYEE') or hasAuthority('USER')")
	@RequestMapping(value = "/loan/{id}", method = RequestMethod.GET)
	public Loan getLoan(@PathVariable("id") int id, Authentication authentication)
	{
		Optional<Loan> optional = loanService.getLoan(id);

		if(!optional.isPresent())
		{
			throw new APINotFoundException("Loan " + id + " not found");
		}

		return checkRole(optional.get(), authentication);
	}

	@PreAuthorize("hasAuthority('ADMIN') or hasAuthority('EMPLOYEE')")
	@RequestMapping(value = "/loan", method = RequestMethod.POST)
	public ResponseEntity<Void> addLoan(@Valid @RequestBody Loan loan)
	{
		Loan newLoan = loanService.addLoan(loan);

		URI location = ServletUriComponentsBuilder
		   .fromCurrentRequest()
		   .path("/{id}")
		   .buildAndExpand(newLoan.getId())
		   .toUri()
		;

		return ResponseEntity.created(location).build();
	}

	@PreAuthorize("hasAuthority('ADMIN') or hasAuthority('EMPLOYEE')")
	@RequestMapping(value = "/loan/{id}", method = RequestMethod.PUT)
	public ResponseEntity<Void> updateLoan(@PathVariable("id") int id, @Valid @RequestBody Loan loan)
	{
		Loan newLoan = loanService.updateLoan(id, loan);

		URI location = ServletUriComponentsBuilder
		   .fromCurrentRequest()
		   .path("/{id}")
		   .buildAndExpand(newLoan.getId())
		   .toUri()
		;

		return ResponseEntity.created(location).build();
	}

	@PreAuthorize("hasAuthority('ADMIN') or hasAuthority('EMPLOYEE') or hasAuthority('USER')")
	@RequestMapping(value = "/loan/{id}/extend", method = RequestMethod.PUT)
	public ResponseEntity<Void> extendLoan(@PathVariable("id") int id, Authentication authentication)
	{
		Loan loan = getLoan(id, authentication);

		if(!"ASK_EXTENSION".equals(loan.getState()))
		{
			throw new APiNotAuthorizedException("This loan is not available for being extended");
		}

		loan.setExtensionAsked(true);

		Loan newLoan = loanService.updateLoan(id, loan);

		URI location = ServletUriComponentsBuilder
           .fromCurrentRequest()
           .path("/{id}")
           .buildAndExpand(newLoan.getId())
           .toUri()
		;

		return ResponseEntity.created(location).build();
	}

	@PreAuthorize("hasAuthority('ADMIN')")
	@RequestMapping(value = "/loan/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<Void> deleteLoan(@PathVariable("id") int id)
	{
		loanService.deleteLoan(id);

		return ResponseEntity.status(HttpStatus.OK).build();
	}

	////////

	private Loan checkRole(Loan loan, Authentication authentication)
	{
		/* Check if the operation is authorized */

		UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

		if(loan.getUser().getId() != userDetails.getId() && userDetails.getAuthority() == User.Role.USER)
		{
			throw new APiNotAuthorizedException("The current user is not the borrower");
		}

		return loan;
	}
}
