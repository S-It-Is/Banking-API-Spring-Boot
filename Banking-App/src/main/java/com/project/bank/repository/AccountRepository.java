package com.project.bank.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.bank.entity.Account;

public interface AccountRepository extends JpaRepository<Account,Integer>{
	
	Optional<Account> findByEmail(String email);

}
