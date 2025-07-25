package com.email_test.EmailCheck.Repository;

import com.email_test.EmailCheck.Modal.Coin;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CoinRepository extends JpaRepository<Coin, String> {
}
