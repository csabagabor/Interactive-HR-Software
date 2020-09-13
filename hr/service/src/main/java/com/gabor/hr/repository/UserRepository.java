package com.gabor.hr.repository;

import com.gabor.hr.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);

    User findByFirstName(String firstName);

    User findByLastName(String lastName);
}
