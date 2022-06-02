package xyz.morlotti.virtualbookcase.webapi.daos;

import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import xyz.morlotti.virtualbookcase.webapi.models.Loan;

@Repository
public interface LoanDAO extends JpaRepository<Loan, Integer>
{
	@Query("SELECT l FROM LOAN l WHERE l.loanEndDate IS NULL AND ((l.extensionAsked = 0 AND DATEDIFF(CURRENT_TIMESTAMP, l.loanStartDate) > 30) OR (l.extensionAsked = 1 AND DATEDIFF(CURRENT_TIMESTAMP, l.loanStartDate) > 60))")
	public List<Loan> findInLate();

	@Modifying
	@Query("DELETE FROM LOAN l WHERE l.user.id = (SELECT u.id FROM USER u WHERE u.login = :login)")
	public void deleteForUser(@Param("login") String login);
}
