package com.platform.trading.sql;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
	
	public User findByUid(Long uid);
	public User findByEmail(String email);
	
}
