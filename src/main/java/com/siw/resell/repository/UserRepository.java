package com.siw.resell.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.siw.resell.model.User;

public interface UserRepository extends CrudRepository<User, Long> {

	public List<User> findByOrderByCognomeAscNome();
}