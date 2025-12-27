package com.portal.studymate.schoolmodule.repository;

import com.portal.studymate.schoolmodule.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface UserRepository extends MongoRepository<User, String> {
   Optional<User> findByEmail(String email);
}
