package xyz.morlotti.virtualbookcase.webapi.security.services;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import xyz.morlotti.virtualbookcase.webapi.models.User;
import xyz.morlotti.virtualbookcase.webapi.daos.UserDAO;

@Service
public class UserDetailsServiceImpl implements UserDetailsService
{
	@Autowired
	UserDAO userDAO;

	@Override
	public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException
	{
		Optional<User> optional = userDAO.findByLogin(login);

		if(!optional.isPresent())
		{
			throw new UsernameNotFoundException("User `" + login + "` not found");
		}

		return UserDetailsImpl.build(optional.get());
	}
}
