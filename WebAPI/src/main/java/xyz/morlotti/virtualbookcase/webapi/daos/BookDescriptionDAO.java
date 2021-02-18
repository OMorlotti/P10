package xyz.morlotti.virtualbookcase.webapi.daos;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

import xyz.morlotti.virtualbookcase.webapi.models.BookDescription;

@Repository
public interface BookDescriptionDAO extends JpaRepository<BookDescription, Integer>
{

}
