package xyz.morlotti.virtualbookcase.webapi.daos;

import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import xyz.morlotti.virtualbookcase.webapi.models.PreLoan;

@Repository
public interface PreLoanDAO extends JpaRepository<PreLoan, Integer>
{
	List<PreLoan> findByOrderByPreLoanStartDateAsc();

	@Modifying
	@Query("DELETE FROM PRELOAN p WHERE p.user.id = (SELECT u.id FROM USER u WHERE u.login = :login)")
	public void deleteForUser(@Param("login") String login);
}
