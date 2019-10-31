package com.platform.trading.sql;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PendingOrderRepository extends JpaRepository<PendingOrder, Long> {
	
	public PendingOrder findByOid(Long oid);
	public List<PendingOrder> findByUid(Long uid);
	public List<PendingOrder> 
		findByCurrencyPairAndOrderPriceGreaterThanAndTriggeredAboveAndLongPosition
		(String currencyPair, BigDecimal orderPrice,boolean triggeredAbove, boolean longPosition);
	public List<PendingOrder> 
		findByCurrencyPairAndOrderPriceLessThanAndTriggeredAboveAndLongPosition
		(String currencyPair, BigDecimal orderPrice, boolean triggeredAbove, boolean longPosition);
	
	public Long deleteByTid(Long tid);
	public Long deleteByOid(Long oid);
	
}
