package xyz.morlotti.virtualbookcase.webapi.test;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.test.context.TestPropertySource;
import xyz.morlotti.virtualbookcase.webapi.MyApplication;
import xyz.morlotti.virtualbookcase.webapi.controllers.beans.APILoan;
import xyz.morlotti.virtualbookcase.webapi.daos.BookDAO;
import xyz.morlotti.virtualbookcase.webapi.models.*;
import xyz.morlotti.virtualbookcase.webapi.security.jwt.JwtUtils;
import xyz.morlotti.virtualbookcase.webapi.services.interfaces.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@SpringBootTest(classes = MyApplication.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestPropertySource(locations = "/DaoTest.properties")
public class APITest
{
	@Autowired
	private BookDAO bookDAO;

	@Autowired
	private ResetService reset;

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

	private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

	@BeforeAll
	public void init() // Effectué avant les tests
	{
		try // Retire tous les Loans et PreLoans de tous les utilisateurs
		{
			for(User user: userService.listUsers())
			{
				reset.resetUser(user.getLogin());
			}
		}
		catch(Exception e)
		{
			System.err.println(e.getMessage());

			System.exit(1);
		}

		try // Rend tous les livres disponibles
		{
			for(Book book: bookService.listBooks())
			{
				book.setAvailable(true);

				bookDAO.save(book);
			}
		}
		catch(Exception e)
		{
			System.err.println(e.getMessage());

			System.exit(1);
		}
	}

	/*--------------------*/
	/* DAO TESTS          */
	/*--------------------*/

	@Test
	public void servicesNotNull()
	{
		Assertions.assertNotNull(bookDescriptionService);
		Assertions.assertNotNull(bookService);
		Assertions.assertNotNull(loanService);
		Assertions.assertNotNull(preLoanService);
		Assertions.assertNotNull(userService);
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

	@Test
	public void userNotEmptyTest()
	{
		List<User> list = (List<User>) userService.listUsers();

		Assertions.assertFalse(list.isEmpty());
	}

	/*--------------------*/
	/* TOKEN TESTS        */
	/*--------------------*/

	@Autowired
	private JwtUtils jwtUtils;

	@Autowired
	private AuthenticationManager authenticationManager;

	@Test
	public void createAndValidateToken()
	{
		String username = "harry";
		String password = "00000";

		try
		{
			Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(username, password)
			);

			String token = jwtUtils.generateJwtToken(authentication);

			if(token != null && !token.isEmpty())
			{
				jwtUtils.validateJwtToken(token);
			}
			else
			{
				Assertions.fail("Token is empty");
			}
		}
		catch(Exception e)
		{
			Assertions.fail(e.getMessage());
		}
	}

	@Test
	public void invalidToken()
	{
		try
		{
			jwtUtils.validateJwtToken("BAD TOKEN");
		}
		catch(Exception e)
		{
			/* OK */
			Assertions.assertFalse(false);
			/* OK */
		}
	}

	/*--------------------*/
	/* USER TESTS         */
	/*--------------------*/

	@Test
	public void createUser() /* l'auto-incrément augmente à chaque test */
	{
		User resultingUser = null;

		try
		{
			User user = new User();

			user.setLogin("ron");
			user.setPassword("00000");
			user.setFirstname("Ronald");
			user.setLastname("Weasley");
			user.setStreetNb("28");
			user.setStreetName("route de Poudlard");
			user.setPostalCode(38000);
			user.setCity("Londres");
			user.setCountry("UK");
			user.setEmail("ronald.weasley@poudlard.uk");
			user.setBirthdate(LocalDate.parse("01/03/1980", formatter));
			user.setSex(User.Sex.H);
			user.setRole(User.Role.USER);
			user.setMembership(LocalDate.parse("05/02/2010", formatter));

			resultingUser = userService.addUser(user);

			Assertions.assertTrue(resultingUser.getId() != null);
		}
		catch(Exception e)
		{
			Assertions.fail(e.getMessage());
		}

		if(resultingUser != null)
		{
			try
			{
				userService.deleteUser(resultingUser.getId());
			}
			catch(Exception e)
			{
				Assertions.fail(e.getMessage());
			}
		}
	}

	@Test
	public void deleteNotExistingUser()
	{
		try
		{
			userService.deleteUser(999999999);

			Assertions.fail("should not occur");
		}
		catch(Exception e)
		{
			Assertions.assertTrue(true);
		}
	}

	/*--------------------*/
	/* BOOKDESCR TESTS    */
	/*--------------------*/

	@Test
	public void createBookDescription() /* l'auto-incrément augmente à chaque test */
	{
		BookDescription resultingBookDescription = null;

		try
		{
			BookDescription bookDescription = new BookDescription();

			bookDescription.setIsbn("9781484469064");
			bookDescription.setTitle("Tintin et l'Affaire Tournesol");
			bookDescription.setAuthorFirstname("Hergé");
			bookDescription.setAuthorLastname("Hergé");
			bookDescription.setEditor("Casterman");
			bookDescription.setEditionNumber(1);
			bookDescription.setEditionYear(2012);
			bookDescription.setGenre("BD-enfant");
			bookDescription.setFormat("24x32");
			bookDescription.setComment("Blabla");

			resultingBookDescription = bookDescriptionService.addBookDescription(bookDescription);

			Assertions.assertTrue(resultingBookDescription.getId() != null);
		}
		catch(Exception e)
		{
			Assertions.fail(e.getMessage());
		}

		if(resultingBookDescription != null)
		{
			try
			{
				bookDescriptionService.deleteBookDescription(resultingBookDescription.getId());
			}
			catch(Exception e)
			{
				Assertions.fail(e.getMessage());
			}
		}
	}

	@Test
	public void deleteNotExistingeBookDescription()
	{
		try
		{
			bookDescriptionService.deleteBookDescription(999999999);

			Assertions.fail("should not occur");
		}
		catch(Exception e)
		{
			Assertions.assertTrue(true);
		}
	}

	/*--------------------*/
	/* BOOK TESTS         */
	/*--------------------*/

	@Test
	public void createBook() /* l'auto-incrément augmente à chaque test */
	{
		Book resultingBook = null;

		try
		{
			BookDescription bookDescription = bookDescriptionService.getBookDescription(1).orElseThrow(() -> new Exception("Book Description 1 not found"));

			Book book = new Book();

			book.setLocalId("123ABC");
			book.setCondition(Book.Condition.parseString("GOOD"));
			book.setAvailable(true);
			book.setBookDescription(bookDescription);

			resultingBook = bookService.addBook(book);

			Assertions.assertTrue(resultingBook.getId() != null);
		}
		catch(Exception e)
		{
			Assertions.fail(e.getMessage());
		}

		if(resultingBook != null)
		{
			try
			{
				bookService.deleteBook(resultingBook.getId());
			}
			catch(Exception e)
			{
				Assertions.fail(e.getMessage());
			}
		}
	}

	@Test
	public void deleteNotExistingeBook()
	{
		try
		{
			bookService.deleteBook(999999999);

			Assertions.fail("should not occur");
		}
		catch(Exception e)
		{
			Assertions.assertTrue(true);
		}
	}

	/*--------------------*/
	/* LOAN TESTS         */
	/*--------------------*/

	@Test
	public void createLoan() /* l'auto-incrément augmente à chaque test */
	{
		Loan resultingLoan = null;

		try
		{
			APILoan loan = new APILoan();

			loan.setUserId(1);
			loan.setBookId(1);

			resultingLoan = loanService.addLoan(loan);

			Assertions.assertTrue(resultingLoan.getId() != null);
		}
		catch(Exception e)
		{
			Assertions.fail(e.getMessage());
		}

		if(resultingLoan != null)
		{
			try
			{
				loanService.deleteLoan(resultingLoan.getId());
			}
			catch(Exception e)
			{
				Assertions.fail(e.getMessage());
			}
		}
	}

	@Test
	public void createLoanAndCheckState()
	{
		Loan resultingLoan = null;

		try
		{
			APILoan loan = new APILoan();

			loan.setUserId(1);
			loan.setBookId(1);

			resultingLoan = loanService.addLoan(loan);

			Assertions.assertTrue(resultingLoan.getId() != null);

			Assertions.assertTrue(resultingLoan.getState() == "NO_EXTENSION");
		}
		catch(Exception e)
		{
			Assertions.fail(e.getMessage());
		}

		if(resultingLoan != null)
		{
			try
			{
				loanService.deleteLoan(resultingLoan.getId());
			}
			catch(Exception e)
			{
				Assertions.fail(e.getMessage());
			}
		}
	}

	@Test
	public void newLoanExtensionMustFail()
	{
		Loan resultingLoan = null;

		try
		{
			APILoan loan = new APILoan();

			loan.setUserId(1);
			loan.setBookId(1);

			resultingLoan = loanService.addLoan(loan);

			Assertions.assertTrue(resultingLoan.getId() != null);
		}
		catch(Exception e)
		{
			Assertions.fail(e.getMessage());
		}

		if(resultingLoan != null)
		{
			try
			{
				loanService.extendLoan(resultingLoan);

				Assertions.fail("should not occur");
			}
			catch(Exception e)
			{
				/* YES ! */
			}
		}

		if(resultingLoan != null)
		{
			try
			{
				loanService.deleteLoan(resultingLoan.getId());
			}
			catch(Exception e)
			{
				Assertions.fail(e.getMessage());
			}
		}
	}

	@Test
	public void deleteNotExistingLoan()
	{
		try
		{
			loanService.deleteLoan(999999999);

			Assertions.fail("should not occur");
		}
		catch(Exception e)
		{
			Assertions.assertTrue(true);
		}
	}

	/*--------------------*/
	/* PRELOAN TESTS      */
	/*--------------------*/

	@Test
	public void preLoanForUser1OnBook1MustFail()
	{
		try
		{
			preLoanService.addPreLoan(1, 1);

			Assertions.fail("should not occur");
		}
		catch(Exception e)
		{
			Assertions.assertTrue(true);
		}
	}

	@Test
	public void deleteNotExistingPreLoan()
	{
		try
		{
			preLoanService.deletePreLoan(999999999);

			Assertions.fail("should not occur");
		}
		catch(Exception e)
		{
			Assertions.assertTrue(true);
		}
	}

	/*--------------------*/
}
