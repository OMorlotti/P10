package xyz.morlotti.virtualbookcase.webapi.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import xyz.morlotti.virtualbookcase.webapi.beans.Book;
import xyz.morlotti.virtualbookcase.webapi.services.interfaces.BookService;

import java.net.URI;
import java.util.Optional;

@RestController
public class BookController
{
	@Autowired
	BookService bookService;

	@RequestMapping(value="/books", method = RequestMethod.GET)
	public Iterable<Book> listBooks()
	{
		return bookService.listBooks();
	}

	@RequestMapping(value="/book/{id}", method = RequestMethod.GET)
	public Optional<Book> getBook(@PathVariable int id)
	{
		return bookService.getBook(id);
	}

	@RequestMapping(value = "/book", method = RequestMethod.POST)
	public ResponseEntity<Void> addBook(@RequestBody Book book)
	{
		Book newBook = bookService.addBook(book);

		URI location = ServletUriComponentsBuilder
			               .fromCurrentRequest()
			               .path("/{id}")
			               .buildAndExpand(newBook.getId())
			               .toUri()
			;

		return ResponseEntity.created(location).build();
	}

	@RequestMapping(value="/book/{id}", method = RequestMethod.PUT)
	public ResponseEntity<Void> updateBook(@PathVariable int id, @RequestBody Book book)
	{
		Book newBook = bookService.updateBook(id, book);

		URI location = ServletUriComponentsBuilder
			               .fromCurrentRequest()
			               .path("/{id}")
			               .buildAndExpand(newBook.getId())
			               .toUri()
			;

		return ResponseEntity.created(location).build();
	}

	@RequestMapping(value="/book/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<Void> deleteBook(@PathVariable int id)
	{
		bookService.deleteBook(id);

		return ResponseEntity.status(HttpStatus.OK).build();
	}
}
