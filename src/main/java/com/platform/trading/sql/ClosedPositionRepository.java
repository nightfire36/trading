package com.platform.trading.sql;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClosedPositionRepository extends JpaRepository<ClosedPosition, Long> {
	
	public List<ClosedPosition> findByTid(Long tid);
	public List<ClosedPosition> findByUid(Long uid);
	
}
