package xyz.morlotti.virtualbookcase.webapi.services.impl;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import xyz.morlotti.virtualbookcase.webapi.daos.beans.Search;
import xyz.morlotti.virtualbookcase.webapi.daos.beans.SearchResult;
import xyz.morlotti.virtualbookcase.webapi.daos.BookDescriptionDAO;
import xyz.morlotti.virtualbookcase.webapi.services.interfaces.SearchService;

@Service
@Transactional
public class SearchImpl implements SearchService
{
	@Autowired
	private BookDescriptionDAO bookDescriptionDAO;

	public Iterable<SearchResult> search(Search search)
	{
		Iterable<SearchResult> searchResults = bookDescriptionDAO.search(search);

		return searchResults;
	}
}
