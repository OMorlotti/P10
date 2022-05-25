package xyz.morlotti.virtualbookcase.webapi.test;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import xyz.morlotti.virtualbookcase.webapi.MyApplication;
import xyz.morlotti.virtualbookcase.webapi.models.Book;
import xyz.morlotti.virtualbookcase.webapi.models.BookDescription;
import xyz.morlotti.virtualbookcase.webapi.models.User;
import xyz.morlotti.virtualbookcase.webapi.services.interfaces.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@SpringBootTest(classes = MyApplication.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestPropertySource(locations = "/DaoTest.properties")
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

	private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

	@BeforeAll
	public void init()
	{
		User user1 = new User();

		user1.setLogin("Hermione");
		user1.setPassword("00000");
		user1.setFirstname("Hermione");
		user1.setLastname("Granger");
		user1.setStreetNb("28");
		user1.setStreetName("route de Poudlard");
		user1.setPostalCode(38000);
		user1.setCity("Londres");
		user1.setCountry("UK");
		user1.setEmail("hermione.granger@poudlard.uk");
		user1.setBirthdate(LocalDate.parse("18/05/1995", formatter));
		user1.setSex(User.Sex.H);
		user1.setRole(User.Role.USER);
		user1.setMembership(LocalDate.parse("05/02/2010", formatter));

		userService.addUser(user1);

		/**/

		User user2 = new User();

		user2.setLogin("Albus");
		user2.setPassword("00000");
		user2.setFirstname("Albus");
		user2.setLastname("Dumbledor");
		user2.setStreetNb("28");
		user2.setStreetName("route de Poudlard");
		user2.setPostalCode(38000);
		user2.setCity("Londres");
		user2.setCountry("UK");
		user2.setEmail("albus.dumbledor@poudlard.uk");
		user2.setBirthdate(LocalDate.parse("28/08/1953", formatter));
		user2.setSex(User.Sex.H);
		user2.setRole(User.Role.USER);
		user2.setMembership(LocalDate.parse("05/02/2001", formatter));

		userService.addUser(user2);

		/* INIT BOOKDESCRIPTIONS */

		BookDescription bookDescription1 = new BookDescription();

		bookDescription1.setIsbn("9781484469064");
		bookDescription1.setTitle("Tintin et le Lotus bleu");
		bookDescription1.setAuthorFirstname("Hergé");
		bookDescription1.setAuthorLastname("Hergé");
		bookDescription1.setEditor("Casterman");
		bookDescription1.setEditionNumber(1);
		bookDescription1.setEditionYear(2012);
		bookDescription1.setGenre("BD-enfant");
		bookDescription1.setFormat("24x32");
		bookDescription1.setComment("Blabla");

		bookDescriptionService.addBookDescription(bookDescription1);

		/**/

		BookDescription bookDescription2 = new BookDescription();

		bookDescription2.setIsbn("9782800132884");
		bookDescription2.setTitle("Les Schtroumpfs Olympiques");
		bookDescription2.setAuthorFirstname("Peyo");
		bookDescription2.setAuthorLastname("Peyo");
		bookDescription2.setEditor("Dupuis");
		bookDescription2.setEditionNumber(2);
		bookDescription2.setEditionYear(2020);
		bookDescription2.setGenre("BD-enfant");
		bookDescription2.setFormat("24x32");
		bookDescription2.setComment("Blabla");

		bookDescriptionService.addBookDescription(bookDescription2);

		/* INIT BOOKS */

		Book book1 = new Book();

		book1.setLocalId("123ABC");
		book1.setCondition(Book.Condition.parseString("GOOD"));
		book1.setAvailable(book1.isAvailable());
		book1.setBookDescription(bookDescription1);

		bookService.addBook(book1);

		/**/

		Book book2 = new Book();

		book2.setLocalId("123ABC");
		book2.setCondition(Book.Condition.parseString("NEW"));
		book2.setAvailable(book2.isAvailable());
		book2.setBookDescription(bookDescription2);

		bookService.addBook(book2);
	}

	@Test
	public void bookDescriptionServiceNotNull()
	{
		Assertions.assertNotNull(bookDescriptionService);
	}

	@Test
	public void userNotEmptyTest()
	{
		List<User> list = (List<User>) userService.listUsers();

		Assertions.assertFalse(list.isEmpty());
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
