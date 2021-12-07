package xyz.morlotti.virtualbookcase.userwebsite.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

import xyz.morlotti.virtualbookcase.userwebsite.MyFeignProxy;
import xyz.morlotti.virtualbookcase.userwebsite.security.TokenUtils;

@RestController
public class PreLoanController
{
	@Autowired
	MyFeignProxy feignProxy;

	@RequestMapping(value = "/preloan", method = RequestMethod.POST)
	public ResponseEntity<Void> addPreLoan(@CookieValue(TokenUtils.TOKEN_COOKIE_NAME) String token, @PathVariable("bookDescriptionId") int bookDescriptionId)
	{
		return feignProxy.addPreloan(token, bookDescriptionId);
	}
}
