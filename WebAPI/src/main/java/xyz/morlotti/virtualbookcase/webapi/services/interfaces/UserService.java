package xyz.morlotti.virtualbookcase.webapi.services.interfaces;

import java.util.Optional;

import xyz.morlotti.virtualbookcase.webapi.models.User;

public interface UserService
{
	Iterable<User> listUsers();

	Optional<User> getUser(int id);

	User addUser(User genre);

	User updateUser(int id, User user);

	void deleteUser(int id);
}
