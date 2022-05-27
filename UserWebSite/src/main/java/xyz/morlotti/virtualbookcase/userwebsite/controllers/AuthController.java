package xyz.morlotti.virtualbookcase.userwebsite.controllers;

import org.springframework.ui.Model;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.beans.factory.annotation.Autowired;

import xyz.morlotti.virtualbookcase.userwebsite.MyFeignProxy;
import xyz.morlotti.virtualbookcase.userwebsite.beans.forms.Email;
import xyz.morlotti.virtualbookcase.userwebsite.security.TokenUtils;
import xyz.morlotti.virtualbookcase.userwebsite.beans.forms.Credentials;

import javax.servlet.http.HttpServletResponse;

@Controller
public class AuthController
{
	@Autowired
	MyFeignProxy feignProxy;

	@Autowired
	TokenUtils tokenUtils;

	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String login()
	{
		return "login";
	}

	@RequestMapping(value = "/login", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE})
	public String login(Credentials credentials, Model model, HttpServletResponse httpServletResponse)
	{
		try
		{
			String token = feignProxy.login(credentials.getLogin(), credentials.getPassword());

			TokenUtils.UserInfo userInfo = tokenUtils.getUserInfoFromJwtToken(token);

			TokenUtils.createTokenCookie(httpServletResponse, token);

			model.addAttribute("userInfo", userInfo);

			return "home";
		}
		catch(Exception e)
		{
			model.addAttribute("messageType", "danger");
			model.addAttribute("message", "Login ou mot de passe invalide ou erreur interne (" + e.getMessage() + ")");

			return "login";
		}
	}

	@RequestMapping(value = "/logout", method = RequestMethod.GET)
	public String logout(HttpServletResponse httpServletResponse)
	{
		TokenUtils.deleteTokenCookie(httpServletResponse);

		return "home";
	}

	@RequestMapping(value="/remind-password", method = RequestMethod.GET)
	public String remindPasswordStep1()
	{
		return "remind-password";
	}

	@RequestMapping(value="/remind-password", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE})
	public String remindPasswordStep2(Email email, Model model)
	{
		try
		{
			feignProxy.remindPassword(email.getEmail());

			model.addAttribute("messageType", "success");
			model.addAttribute("message", "Mot de passe envoyé.");

			return "home";
		}
		catch(Exception e)
		{
			model.addAttribute("messageType", "danger");
			model.addAttribute("message", "Mot de passe non envoyé.");

			return "remind-password";
		}
	}
}
