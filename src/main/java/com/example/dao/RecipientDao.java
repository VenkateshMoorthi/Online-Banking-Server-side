package com.example.dao;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.example.domain.Recipient;

public interface RecipientDao extends CrudRepository<Recipient, Long> {
	List<Recipient> findAll();
	
    Recipient findById(Long Id);

    void deleteById(Long Id);
}
