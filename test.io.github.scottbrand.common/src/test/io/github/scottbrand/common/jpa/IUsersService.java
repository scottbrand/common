package test.io.github.scottbrand.common.jpa;

import java.util.List;

import test.io.github.scottbrand.common.jpa.domain.User;

public interface IUsersService
{
	List<User> getUsers();
}
