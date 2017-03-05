package com.example.dao;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.example.domain.PrimaryTransaction;

public interface PrimaryTransactionDao extends CrudRepository<PrimaryTransaction, Long> {
	List<PrimaryTransaction> findAll();
}
