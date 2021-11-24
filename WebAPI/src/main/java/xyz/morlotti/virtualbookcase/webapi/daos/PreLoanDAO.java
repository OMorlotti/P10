package xyz.morlotti.virtualbookcase.webapi.daos;

import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

import xyz.morlotti.virtualbookcase.webapi.models.PreLoan;

@Repository
public interface PreLoanDAO extends JpaRepository<PreLoan, Integer>
{
	List<PreLoan> findByOrderByPreLoanStartDateAsc();
}
