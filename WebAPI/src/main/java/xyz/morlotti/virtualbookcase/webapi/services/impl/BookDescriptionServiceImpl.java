package xyz.morlotti.virtualbookcase.webapi.services.impl;

import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.beans.factory.annotation.Autowired;

import xyz.morlotti.virtualbookcase.webapi.models.BookDescription;
import xyz.morlotti.virtualbookcase.webapi.daos.BookDescriptionDAO;
import xyz.morlotti.virtualbookcase.webapi.exceptions.APINotDeletedException;
import xyz.morlotti.virtualbookcase.webapi.exceptions.APINotModifiedException;
import xyz.morlotti.virtualbookcase.webapi.services.interfaces.BookDescriptionService;

@Service
@Transactional
public class BookDescriptionServiceImpl implements BookDescriptionService
{
	@Autowired
	private BookDescriptionDAO bookDescriptionDAO;

	public Iterable<BookDescription> listBookDescriptions()
	{
		Iterable<BookDescription> bookDescriptions = bookDescriptionDAO.findAll();

		return bookDescriptions;
	}

	public Optional<BookDescription> getBookDescription(int id)
	{
		Optional<BookDescription> optional = bookDescriptionDAO.findById(id);

		return optional;
	}

	public BookDescription addBookDescription(BookDescription bookDescription)
	{
		BookDescription newBookDescription = bookDescriptionDAO.save(bookDescription);

		if(newBookDescription == null)
		{
			throw new APINotModifiedException("BookDescription not inserted");
		}

		return newBookDescription;
	}

	public BookDescription updateBookDescription(int id, BookDescription bookDescription)
	{
		BookDescription existingBookDescription = getBookDescription(id).get();

		existingBookDescription.setAuthorFirstname(bookDescription.getAuthorFirstname());

		existingBookDescription.setAuthorLastname(bookDescription.getAuthorLastname());

		existingBookDescription.setComment(bookDescription.getComment());

		existingBookDescription.setEditionNumber(bookDescription.getEditionNumber());

		existingBookDescription.setEditionYear(bookDescription.getEditionYear());

		existingBookDescription.setEditor(bookDescription.getEditor());

		existingBookDescription.setFormat(bookDescription.getFormat());

		existingBookDescription.setIsbn(bookDescription.getIsbn());

		existingBookDescription.setTitle(bookDescription.getTitle());

		return addBookDescription(existingBookDescription);
	}

	public void deleteBookDescription(int id)
	{
		try
		{
			bookDescriptionDAO.deleteById(id);
		}
		catch(EmptyResultDataAccessException e)
		{
			throw new APINotDeletedException("BookDescription " + id + " not deleted: " + e.getMessage());
		}
	}
}
