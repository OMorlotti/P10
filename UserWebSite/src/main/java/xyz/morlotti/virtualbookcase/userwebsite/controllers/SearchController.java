package xyz.morlotti.virtualbookcase.userwebsite.controllers;

import org.springframework.ui.Model;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.beans.factory.annotation.Autowired;

import xyz.morlotti.virtualbookcase.userwebsite.MyFeignProxy;
import xyz.morlotti.virtualbookcase.userwebsite.beans.BookDescription;
import xyz.morlotti.virtualbookcase.userwebsite.beans.Loan;
import xyz.morlotti.virtualbookcase.userwebsite.beans.User;
import xyz.morlotti.virtualbookcase.userwebsite.beans.forms.Search;
import xyz.morlotti.virtualbookcase.userwebsite.security.TokenUtils;
import xyz.morlotti.virtualbookcase.userwebsite.beans.forms.SearchResult;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
public class SearchController
{
	@Autowired
	MyFeignProxy feignProxy;

	@Autowired
	TokenUtils tokenUtils;

	@RequestMapping(value = "/search", method = RequestMethod.GET)
	public String search1(@CookieValue(TokenUtils.TOKEN_COOKIE_NAME) String token, Model model)
	{
		TokenUtils.UserInfo userInfo = tokenUtils.getUserInfoFromJwtToken(token);

		model.addAttribute("userInfo", userInfo);
		model.addAttribute("showResult", false);

		return "search";
	}

	@RequestMapping(value = "/search", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE})
	public String search2(@CookieValue(TokenUtils.TOKEN_COOKIE_NAME) String token, Search search, Model model)
	{
		TokenUtils.UserInfo userInfo = tokenUtils.getUserInfoFromJwtToken(token);

		model.addAttribute("userInfo", userInfo);
		model.addAttribute("showResult", true);

		try
		{
			Iterable<SearchResult> searchResults = feignProxy.searchBook(search);

			Set<Integer> bookDescriptionIdSet = new HashSet<>();

			if(userInfo != null)
			{
				feignProxy.getUser(token).getLoans().stream().forEach(x -> {

					bookDescriptionIdSet.add(x.getBook().getBookDescription().getId());
				});

				feignProxy.getUser(token).getPreLoans().stream().forEach(x -> {

					bookDescriptionIdSet.add(x.getBookDescription().getId());
				});
			}

			model.addAttribute("searchResults", searchResults);
			model.addAttribute("bookDescriptionIdSet", bookDescriptionIdSet);

		}
		catch(Exception e)
		{
			model.addAttribute("messageType", "danger");
			model.addAttribute("message", "Erreur interne : " + e.getMessage() + ".");
		}

		return "search";
	}
}
