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

import xyz.morlotti.virtualbookcase.webapi.models.User;
import xyz.morlotti.virtualbookcase.webapi.models.PreLoan;
import xyz.morlotti.virtualbookcase.webapi.exceptions.APINotFoundException;
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
}
