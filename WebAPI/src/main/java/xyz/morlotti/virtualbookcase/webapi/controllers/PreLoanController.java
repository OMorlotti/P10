package xyz.morlotti.virtualbookcase.webapi.controllers;

import java.net.URI;
import java.util.Collection;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import xyz.morlotti.virtualbookcase.webapi.models.PreLoan;
import xyz.morlotti.virtualbookcase.webapi.exceptions.APINotFoundException;
import xyz.morlotti.virtualbookcase.webapi.models.User;
import xyz.morlotti.virtualbookcase.webapi.security.services.UserDetailsImpl;
import xyz.morlotti.virtualbookcase.webapi.services.interfaces.PreLoanService;
import xyz.morlotti.virtualbookcase.webapi.exceptions.APINotAuthorizedException;

@RestController
public class PreLoanController
{
	@Autowired
	PreLoanService preLoanService;

	@PreAuthorize("hasAuthority('ADMIN') or hasAuthority('EMPLOYEE') or hasAuthority('USER')")
	@RequestMapping(value = "/preloans", method = RequestMethod.GET)
	public Iterable<PreLoan> listLoans()
	{
		return preLoanService.listPreLoans();
	}

	@PreAuthorize("hasAuthority('ADMIN') or hasAuthority('EMPLOYEE') or hasAuthority('USER')")
	@RequestMapping(value = "/preloan/bookDescription/{bookDescriptionId}", method = RequestMethod.POST)
	public ResponseEntity<Void> addPreLoanCurrentUser(@PathVariable("bookDescriptionId") int bookDescriptionId, Authentication authentication)
	{
		UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

		return addPreLoan(bookDescriptionId, userDetails.getId());
	}

	@PreAuthorize("hasAuthority('ADMIN') or hasAuthority('EMPLOYEE')")
	@RequestMapping(value = "/preloan/bookDescription/{bookDescriptionId}/user/{userId}", method = RequestMethod.POST)
	public ResponseEntity<Void> addPreLoan(@PathVariable("bookDescriptionId") int bookDescriptionId, @PathVariable("userId") int userId)
	{
		PreLoan newPreLoan = preLoanService.addPreLoan(bookDescriptionId, userId);

		URI location = ServletUriComponentsBuilder
			.fromCurrentRequest()
			.path("/{id}")
			.buildAndExpand(newPreLoan.getId())
			.toUri()
		;

		return ResponseEntity.created(location).build();
	}

	@PreAuthorize("hasAuthority('ADMIN') or hasAuthority('EMPLOYEE') or hasAuthority('USER')")
	@RequestMapping(value = "/preloan/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<Void> deletePreLoan(@PathVariable("id") int id, Authentication authentication)
	{
		UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

		if(userDetails.getAuthority() == User.Role.USER)
		{
			Optional<PreLoan> optional = preLoanService.getPreLoan(id);

			if(!optional.isPresent())
			{
				throw new APINotFoundException("cannot find preloan '" + id + "'");
			}

			if(optional.get().getUser().getId() != userDetails.getId())
			{
				throw new APINotAuthorizedException("not authorized to cancel preloan '" + id + "'");
			}
		}

		preLoanService.deletePreLoan(id);

		return ResponseEntity.status(HttpStatus.OK).build();
	}
}
