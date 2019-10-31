package com.platform.trading.sql;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OpenedPositionRepository extends JpaRepository<OpenedPosition, Long> {
	
	public OpenedPosition findByTid(Long tid);
	public List<OpenedPosition> findByUid(Long uid);
	
	public Long deleteByTid(Long tid);
	
}
