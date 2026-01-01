package com.portal.studymate.auth.repository;

import com.portal.studymate.auth.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface UserRepository extends MongoRepository<User, String> {
   Optional<User> findByEmail(String email);
}
