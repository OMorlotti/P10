package xyz.morlotti.virtualbookcase.webapi.services.interfaces;

import java.util.Optional;

import xyz.morlotti.virtualbookcase.webapi.models.BookDescription;

public interface BookDescriptionService
{
	Iterable<BookDescription> listBookDescriptions();

	Optional<BookDescription> getBookDescription(int id);

	BookDescription addBookDescription(BookDescription genre);

	BookDescription updateBookDescription(int id, BookDescription genre);

	void deleteBookDescription(int id);
}
