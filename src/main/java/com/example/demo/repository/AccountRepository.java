package com.example.demo.repository;

import com.example.demo.domain.Account;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Slf4j
@Repository
public class AccountRepository {

    @PersistenceContext
    EntityManager em;

    public Long save(Account account) {
        log.info("save 함수 호출 email={}", account.getEmail());
        em.persist(account);
        return account.getId();
    }

    public Optional<Account> findByEmail(String email) {
        log.info("findByEmail 호출 email={}", email);

        String query = "select m from Account m where m.email = :email";

        List<Account> result = em.createQuery(query, Account.class)
                .setParameter("email", email)
                .getResultList();

        return result.isEmpty() ? Optional.empty() : Optional.of(result.get(0));
    }
}
