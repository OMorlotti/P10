package xyz.morlotti.virtualbookcase.webapi.services.interfaces;

import java.util.Optional;

import xyz.morlotti.virtualbookcase.webapi.models.Book;

public interface BookService
{
	Iterable<Book> listBooks();

	Optional<Book> getBook(int id);

	Book addBook(Book genre);

	Book updateBook(int id, Book genre);

	void deleteBook(int id);
}
