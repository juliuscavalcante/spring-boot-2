package com.spring.springboot2.repository;

import com.spring.springboot2.domain.SpringUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringUserRepository extends JpaRepository<SpringUser, Long>  {

    SpringUser findByUsername(String username);

}
