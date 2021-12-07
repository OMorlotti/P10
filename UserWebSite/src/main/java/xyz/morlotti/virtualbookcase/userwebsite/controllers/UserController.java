package xyz.morlotti.virtualbookcase.userwebsite.controllers;

import java.util.Map;
import java.util.HashMap;

import org.springframework.ui.Model;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.beans.factory.annotation.Autowired;

import xyz.morlotti.virtualbookcase.userwebsite.beans.User;
import xyz.morlotti.virtualbookcase.userwebsite.MyFeignProxy;
import xyz.morlotti.virtualbookcase.userwebsite.beans.PreLoan;
import xyz.morlotti.virtualbookcase.userwebsite.security.TokenUtils;
import xyz.morlotti.virtualbookcase.userwebsite.beans.forms.FullUserInfo;

@Controller
public class UserController
{
	@Autowired
	MyFeignProxy feignProxy;

	@Autowired
	TokenUtils tokenUtils;

	Map<Integer, Integer> getLoanRanks(User user, Iterable<PreLoan> preLoans)
	{
		Map<Integer, Integer> result = new HashMap<>();

		for(PreLoan myPreLoan: user.getPreLoans())
		{
			/*--------------------------------------------------------------------------------------------------------*/

			int position = 1;

			for(PreLoan otherPreLoan: preLoans)
			{
				if(otherPreLoan.getBookDescription().getId() == myPreLoan.getBookDescription().getId())
				{
					if(!otherPreLoan.getLogin().equals(user.getLogin()))
					{
						position++;
					}
					else
					{
						break;
					}
				}
			}

			/*--------------------------------------------------------------------------------------------------------*/

			result.put(myPreLoan.getId(), position);

			/*--------------------------------------------------------------------------------------------------------*/
		}

		return result;
	}

	@RequestMapping(value = "/user", method = RequestMethod.GET)
	public String showUser(@CookieValue(TokenUtils.TOKEN_COOKIE_NAME) String token, Model model)
	{
		TokenUtils.UserInfo userInfo = tokenUtils.getUserInfoFromJwtToken(token);

		model.addAttribute("userInfo", userInfo);

		try
		{
			User user = feignProxy.getUser(token);

			Map<Integer, Integer> loanRanks = getLoanRanks(user, feignProxy.listPreLoans(token));

			model.addAttribute("user", user);
			model.addAttribute("loanRanks", loanRanks);
			model.addAttribute("show", "xxxx"); // "show" pour déplier l'utilisateur, "xxxx" (choisi arbitrairement) pour le cacher

			return "user";
		}
		catch(Exception e)
		{
			model.addAttribute("messageType", "danger");
			model.addAttribute("message", "Impossible de récupérer les informations de l'utilisateur. " + e.getMessage());

			return "error";
		}
	}

	@RequestMapping(value = "/loan/{id}/extend", method = RequestMethod.GET)
	public String extendLoan(@CookieValue(TokenUtils.TOKEN_COOKIE_NAME) String token, @PathVariable("id") int id, Model model)
	{
		TokenUtils.UserInfo userInfo = tokenUtils.getUserInfoFromJwtToken(token);

		model.addAttribute("userInfo", userInfo);

		try
		{
			try
			{
				feignProxy.extendLoan(token, id);

				model.addAttribute("messageType", "success");
				model.addAttribute("message", "Emprunt prolongé avec succès.");
			}
			catch(Exception e)
			{
				model.addAttribute("messageType", "danger");
				model.addAttribute("message", "Impossible de prolonger l'emprunt.");
			}

			User user = feignProxy.getUser(token);

			Map<Integer, Integer> loanRanks = getLoanRanks(user, feignProxy.listPreLoans(token));

			model.addAttribute("user", user);
			model.addAttribute("loanRanks", loanRanks);
			model.addAttribute("show", "xxxx"); // "show" pour déplier l'utilisateur, "xxxx" (choisi arbitrairement) pour le cacher

			return "user";
		}
		catch(Exception e)
		{
			model.addAttribute("messageType", "danger");
			model.addAttribute("message", "Impossible de récupérer les informations de l'utilisateur. " + e.getMessage());

			return "error";
		}
	}

	@RequestMapping(value = "/user", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE})
	public String updateUser(@CookieValue(TokenUtils.TOKEN_COOKIE_NAME) String token, FullUserInfo fullUserInfo, Model model)
	{
		TokenUtils.UserInfo userInfo = tokenUtils.getUserInfoFromJwtToken(token);

		model.addAttribute("userInfo", userInfo);

		try
		{
			User user = feignProxy.getUser(token);

			user.setPassword(fullUserInfo.getPassword());
			user.setFirstname(fullUserInfo.getFirstname());
			user.setLastname(fullUserInfo.getLastname());
			user.setStreetNb(fullUserInfo.getStreetNb());
			user.setStreetName(fullUserInfo.getStreetName());
			user.setPostalCode(fullUserInfo.getPostalCode());
			user.setCity(fullUserInfo.getCity());
			user.setCountry(fullUserInfo.getCountry());
			user.setEmail(fullUserInfo.getEmail());
			user.setSex(fullUserInfo.getSex());

			Map<Integer, Integer> loanRanks = getLoanRanks(user, feignProxy.listPreLoans(token));

			model.addAttribute("user", user);
			model.addAttribute("loanRanks", loanRanks);
			model.addAttribute("show", "show"); // "show" pour déplier l'utilisateur, "xxxx" (choisi arbitrairement) pour le cacher

			try
			{
				feignProxy.updateUser(token, user);

				model.addAttribute("messageType", "success");
				model.addAttribute("message", "Utilisateur mis à jour avec succès.");
			}
			catch(Exception e)
			{
				model.addAttribute("messageType", "danger");
				model.addAttribute("message", "Impossible de mettre à jour l'utilisateur.");
			}

			return "user";
		}
		catch(Exception e)
		{
			model.addAttribute("messageType", "danger");
			model.addAttribute("message", "Impossible de récupérer les informations de l'utilisateur. " + e.getMessage());

			return "error";
		}
	}
}
