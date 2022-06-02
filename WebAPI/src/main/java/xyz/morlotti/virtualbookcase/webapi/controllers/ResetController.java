package xyz.morlotti.virtualbookcase.webapi.controllers;

import java.net.URI;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import xyz.morlotti.virtualbookcase.webapi.services.interfaces.ResetService;

@RestController
public class ResetController
{
	@Autowired
	ResetService resetService;

	@PreAuthorize("hasAuthority('ADMIN')")
	@RequestMapping(value = "/reset/user/{login}", method = RequestMethod.GET)
	public ResponseEntity<Object> listUsers(@PathVariable("login") String login)
	{
		resetService.resetUser(login);

		URI location = ServletUriComponentsBuilder
			.fromCurrentRequest()
			.path("/reset/user/{login}")
			.buildAndExpand(login)
			.toUri()
		;

		return ResponseEntity.created(location).build();
	}
}
