package xyz.morlotti.virtualbookcase.webapi.services.impl;

import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.beans.factory.annotation.Autowired;

import xyz.morlotti.virtualbookcase.webapi.models.Book;
import xyz.morlotti.virtualbookcase.webapi.daos.BookDAO;
import xyz.morlotti.virtualbookcase.webapi.services.interfaces.BookService;
import xyz.morlotti.virtualbookcase.webapi.exceptions.APINotModifiedException;
import xyz.morlotti.virtualbookcase.webapi.exceptions.APINotDeletedException;

@Service
@Transactional
public class BookServiceImpl implements BookService
{
	@Autowired
	private BookDAO bookDAO;

	public Iterable<Book> listBooks()
	{
		Iterable<Book> books = bookDAO.findAll();

		return books;
	}

	public Optional<Book> getBook(int id)
	{
		Optional<Book> optional = bookDAO.findById(id);

		return optional;
	}

	public Book addBook(Book book)
	{
		Book newBook = bookDAO.save(book);

		if(newBook == null)
		{
			throw new APINotModifiedException("Book not inserted");
		}

		return newBook;
	}

	public Book updateBook(int id, Book book)
	{
		Book existingBook = getBook(id).get();

		existingBook.setLocalId(book.getLocalId());

		existingBook.setCondition(book.getCondition());

		return addBook(existingBook);
	}

	public void deleteBook(int id)
	{
		try
		{
			bookDAO.deleteById(id);
		}
		catch(EmptyResultDataAccessException e)
		{
			throw new APINotDeletedException("Book " + id + " not deleted: " + e.getMessage());
		}
	}
}
