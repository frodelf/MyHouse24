package com.avada.myHouse24.repo;

import com.avada.myHouse24.entity.AccountTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.sql.Date;
import java.util.List;

public interface AccountTransactionRepository extends JpaRepository<AccountTransaction, Long> {
    @Query(value = "SELECT MAX(id) FROM AccountTransaction")
    Long findMaxId();
    @Query(value = "SELECT SUM(a.sum) FROM AccountTransaction a WHERE a.isIncome = true")
    Long sumWhereIsIncomeIsTrue();
    @Query(value = "SELECT SUM(a.sum) FROM AccountTransaction a WHERE a.isIncome = false")
    Long sumWhereIsIncomeIsFalse();
    @Query(value = "SELECT SUM(a.sum) FROM AccountTransaction a")
    Double findAllSum();
    @Query(value = "SELECT SUM(sum) as sum FROM account_transaction GROUP BY from_date LIMIT 10", nativeQuery = true)
    List<Double> findValueForStats();

    @Query(value = "SELECT from_date FROM account_transaction GROUP BY from_date LIMIT 10", nativeQuery = true)
    List<Date> findMonthForStats();
}
