package xyz.morlotti.virtualbookcase.webapi.test;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.beans.factory.annotation.Autowired;

import xyz.morlotti.virtualbookcase.webapi.models.*;
import xyz.morlotti.virtualbookcase.webapi.MyApplication;
import xyz.morlotti.virtualbookcase.webapi.services.interfaces.*;

@SpringBootTest(classes = MyApplication.class)
public class DaoTest
{
	@Autowired
	private BookDescriptionService bookDescriptionService;

	@Autowired
	private BookService bookService;

	@Autowired
	private LoanService loanService;

	@Autowired
	private PreLoanService preLoanService;

	@Autowired
	private UserService userService;


	/*
	TESTS DES OPÉRATIONS GET ALL POUR CHAQUE ENTITÉ
	 */

	@Test
	public void bookDescriptionServiceNotNull()
	{
		Assertions.assertNotNull(bookDescriptionService);
	}

	@Test
	public void bookDescriptionNotEmptyTest()
	{
		List<BookDescription> list = (List<BookDescription>) bookDescriptionService.listBookDescriptions();

		Assertions.assertFalse(list.isEmpty());
	}

	@Test
	public void bookNotEmptyTest()
	{
		List<Book> list = (List<Book>) bookService.listBooks();

		Assertions.assertFalse(list.isEmpty());
	}
}
