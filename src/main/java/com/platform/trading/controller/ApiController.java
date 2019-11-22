package com.platform.trading.controller;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.platform.trading.rates.GetXchgRates;
import com.platform.trading.rates.XchgRate;
import com.platform.trading.resolver.TransactionResolver;
import com.platform.trading.sql.ClosedPositionRepository;
import com.platform.trading.sql.OpenedPosition;
import com.platform.trading.sql.OpenedPositionRepository;
import com.platform.trading.sql.PendingOrder;
import com.platform.trading.sql.PendingOrderRepository;
import com.platform.trading.sql.User;
import com.platform.trading.sql.UserRepository;

@Transactional
@RestController
@RequestMapping("/api")
public class ApiController {
	
	@Autowired
	private PendingOrderRepository pendingOrdersDao;
	
	@Autowired
	private OpenedPositionRepository openedPositionsDao;
	
	@Autowired
	private MvcController mvcController;
	
	@Autowired
	private TransactionResolver resolver;
	
	@Autowired
	private GetXchgRates rates;
	
	@GetMapping("/rates")
	public ArrayList<XchgRate> getRates()
	{
		return rates.getRates();
	}
	
	@GetMapping("/rate/{pair}")
	public XchgRate getPairRate(@PathVariable("pair") String pair)
	{
		return rates.getPairRate(pair);
	}
	
	@GetMapping("/opened_positions")
	public List<OpenedPosition> openedPositions(Authentication auth)
	{
		User user = mvcController.initUser(auth.getName());
		
		List<OpenedPosition> openedList = openedPositionsDao.findByUid(user.getUid());
		if(openedList != null)
		{
			for(OpenedPosition opened : openedList)
			{
				BigDecimal profit = resolver.calculateProfit(opened, 
						rates.getPairRate(opened.getCurrencyPair()));
				opened.setCurrentProfit(profit);
			}
			return openedList;
		}
		else return null;
	}
	
	@GetMapping("/pending_orders")
	public List<PendingOrder> pendingOrders(Authentication auth)
	{
		User user = mvcController.initUser(auth.getName());
		
		return pendingOrdersDao.findByUid(user.getUid());
	}
	
	@PostMapping("/long/{pair}/{amount}")
	public String longPosition(@PathVariable("pair") String pair, 
			@PathVariable("amount") BigDecimal amount, Authentication auth)
	{
		User user = mvcController.initUser(auth.getName());
		
		if(resolver.openPosition(user.getUid(), pair, amount, true))
		{
			return "Success";
		}
		else return "Error: not enough money";
	}
	
	@PostMapping("/short/{pair}/{amount}")
	public String shortPosition(@PathVariable("pair") String pair, 
			@PathVariable("amount") BigDecimal amount, Authentication auth)
	{
		User user = mvcController.initUser(auth.getName());
		
		if(resolver.openPosition(user.getUid(), pair, amount, false))
		{
			return "Success";
		}
		else return "Error: not enough money";
	}
	
	@PostMapping("/order_long/{pair}/{amount}/{trigger}/{price}")
	public String orderLong(@PathVariable("pair") String pair, 
			@PathVariable("amount") BigDecimal amount, @PathVariable("trigger") boolean trigger,
			@PathVariable("price") BigDecimal price, Authentication auth)
	{
		PendingOrder order = new PendingOrder();
		
		User user = mvcController.initUser(auth.getName());
		
		order.setUid(user.getUid());
		order.setCurrencyPair(pair);
		order.setAmount(amount);
		order.setOrderPrice(price);
		order.setLongPosition(true);
		order.setTriggeredAbove(trigger);
		
		pendingOrdersDao.save(order);
		
		resolver.resolve();
		
		return "success";
	}
	
	@PostMapping("/order_short/{pair}/{amount}/{trigger}/{price}")
	public String orderShort(@PathVariable("pair") String pair, 
			@PathVariable("amount") BigDecimal amount, @PathVariable("trigger") boolean trigger, 
			@PathVariable("price") BigDecimal price, Authentication auth)
	{
		PendingOrder order = new PendingOrder();
		
		User user = mvcController.initUser(auth.getName());
		
		order.setUid(user.getUid());
		order.setCurrencyPair(pair);
		order.setAmount(amount);
		order.setOrderPrice(price);
		order.setLongPosition(false);
		order.setTriggeredAbove(trigger);
		
		pendingOrdersDao.save(order);
		
		resolver.resolve();
		
		return "success";
	}
	
	@PostMapping("/close/{tid}")
	public String closePosition(@PathVariable("tid") Long tid, Authentication auth)
	{
		User user = mvcController.initUser(auth.getName());
		
		OpenedPosition opened = openedPositionsDao.findByTid(tid);
		
		if(opened != null)
		{
			if(user.getUid() == opened.getUid())
			{
				pendingOrdersDao.deleteByTid(tid);
				
				resolver.closePosition(tid);
				
				return "Position closed successfully";
			}
			else return "Position belongs to another user";
		}
		else return "Position closing failure";
	}
	
	@PostMapping("/order_closure/{tid}/{trigger}/{price}")
	public String orderClosure(@PathVariable("tid") Long tid, @PathVariable("trigger") boolean trigger, 
			@PathVariable("price") BigDecimal price, Authentication auth)
	{
		PendingOrder order = new PendingOrder();
		
		User user = mvcController.initUser(auth.getName());
		
		OpenedPosition opened = openedPositionsDao.findByTid(tid);
		if(opened != null)
		{
			if(user.getUid() == opened.getUid())
			{
				order.setUid(opened.getUid());
				order.setTid(tid);
				order.setCurrencyPair(opened.getCurrencyPair());
				order.setAmount(null);
				order.setOrderPrice(price);
				order.setLongPosition(opened.isLongPosition());
				order.setTriggeredAbove(trigger);
				
				pendingOrdersDao.save(order);
				
				resolver.resolve();
				
				return "Order placed successfully";
			}
			else return "Position belongs to another user";
		}
		else return "Order placing failure";
	}
	
	@PostMapping("/cancel/{oid}")
	public String cancelOrder(@PathVariable("oid") Long oid, Authentication auth)
	{
		User user = mvcController.initUser(auth.getName());
		
		PendingOrder pending = pendingOrdersDao.findByOid(oid);
		
		if(pending != null)
		{
			if(user.getUid() == pending.getUid())
			{
				pendingOrdersDao.deleteByOid(oid);
				return "Order cancelled successfully";
			}
			else return "Order belongs to another user";
		}
		else return "Order closing failure";
	}
}
