package com.platform.trading.resolver;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.platform.trading.rates.GetXchgRates;
import com.platform.trading.rates.XchgRate;
import com.platform.trading.sql.ClosedPosition;
import com.platform.trading.sql.ClosedPositionRepository;
import com.platform.trading.sql.OpenedPosition;
import com.platform.trading.sql.OpenedPositionRepository;
import com.platform.trading.sql.PendingOrder;
import com.platform.trading.sql.PendingOrderRepository;
import com.platform.trading.sql.User;
import com.platform.trading.sql.UserRepository;

@Component
@Transactional
public class TransactionResolver {
	
	@Autowired
	private PendingOrderRepository pendingOrdersDao;
	
	@Autowired
	private OpenedPositionRepository openedPositionsDao;
	
	@Autowired
	private ClosedPositionRepository closedPositionsDao;
	
	@Autowired
	private UserRepository usersDao;
	
	@Autowired
	private GetXchgRates rates;
	
	public boolean closePosition(Long tid)
	{
		OpenedPosition opened = openedPositionsDao.findByTid(tid);
		
		if(opened != null)
		{
			BigDecimal amount = opened.getAmount();
			BigDecimal openingPrice = opened.getOpeningPrice();
			BigDecimal closingPrice;
			BigDecimal transactionProfit;
			
			if(opened.isLongPosition())
			{
				closingPrice = rates.getPairRate(opened.getCurrencyPair()).getBid();
				transactionProfit = amount.multiply(closingPrice.divide(openingPrice, 5, RoundingMode.HALF_UP));
			}
			else
			{
				closingPrice = rates.getPairRate(opened.getCurrencyPair()).getAsk();
				transactionProfit = amount.multiply(openingPrice.divide(closingPrice, 5, RoundingMode.HALF_UP));
			}
			
			ClosedPosition closed = new ClosedPosition();
			closed.setTid(opened.getTid());
			closed.setUid(opened.getUid());
			closed.setCurrencyPair(opened.getCurrencyPair());
			closed.setAmount(amount);
			closed.setOpeningPrice(openingPrice);
			closed.setClosingPrice(closingPrice);
			closed.setOpeningTimestamp(opened.getOpeningTimestamp());
			closed.setProfit(transactionProfit.subtract(amount));
			closed.setLongPosition(opened.isLongPosition());
			
			closedPositionsDao.save(closed);
			
			// update user amount of money
			User user = null;
			try
			{
				user = usersDao.findByUid(opened.getUid());
			}
			catch(Exception e)
			{
				e.printStackTrace();
				return false;
			}
			
			BigDecimal accountBalance = user.getAccountBalance().add(transactionProfit);
			user.setAccountBalance(accountBalance);
			usersDao.save(user);
			
			openedPositionsDao.deleteByTid(tid);
			
			return true;
		}
		else return false;
	}
	
	public boolean openPosition(PendingOrder order)
	{
		OpenedPosition opened = new OpenedPosition();
		String currencyPair = order.getCurrencyPair();
		User user = usersDao.findByUid(order.getUid());
		
		if(user.getAccountBalance().compareTo(order.getAmount()) >= 0)
		{
			opened.setUid(order.getUid());
			opened.setCurrencyPair(currencyPair);
			opened.setAmount(order.getAmount());
			opened.setLongPosition(order.isLongPosition());
			openedPositionsDao.save(opened);
			
			if(order.isLongPosition())
			{
				opened.setOpeningPrice(rates.getPairRate(currencyPair).getAsk());
			}
			else
			{
				opened.setOpeningPrice(rates.getPairRate(currencyPair).getBid());
			}
			
			// update user amount of money
			User userDao = null;
			try
			{
				userDao = usersDao.findByUid(order.getUid());
			}
			catch(Exception e)
			{
				e.printStackTrace();
				return false;
			}
			
			BigDecimal accountBalance = userDao.getAccountBalance().subtract(order.getAmount());
			userDao.setAccountBalance(accountBalance);
			
			try
			{
				usersDao.save(userDao);
				pendingOrdersDao.deleteByOid(order.getOid());
			}
			catch(Exception e)
			{
				e.printStackTrace();
				return false;
			}
			return true;
		}
		else return false;
	}
	
	private void executeOrders(List<PendingOrder> orders)
	{
		for(PendingOrder order : orders)
		{
			if(order.getTid() != null)
			{
				pendingOrdersDao.deleteByOid(order.getOid());
				closePosition(order.getTid());
			}
			else if(order.getAmount() != null)
			{
				pendingOrdersDao.deleteByOid(order.getOid());
				openPosition(order);
			}
			else
			{
				System.out.println("Bad order!");
			}
		}
	}
	
	public void resolve()
	{
		ArrayList<XchgRate> ratesList = rates.getRates();
		
		for(XchgRate rate : ratesList)
		{
			String currencyPair = rate.getCurrencyPair();
			BigDecimal bid = rate.getBid();
			BigDecimal ask = rate.getAsk();
			
			List<PendingOrder> positionsAboveLong = pendingOrdersDao
					.findByCurrencyPairAndOrderPriceLessThanAndTriggeredAboveAndLongPosition(
							currencyPair, ask, true, true);
			executeOrders(positionsAboveLong);
			
			List<PendingOrder> positionsAboveShort = pendingOrdersDao
					.findByCurrencyPairAndOrderPriceLessThanAndTriggeredAboveAndLongPosition(
							currencyPair, bid, true, false);
			executeOrders(positionsAboveShort);
			
			List<PendingOrder> positionsBelowLong = pendingOrdersDao
					.findByCurrencyPairAndOrderPriceGreaterThanAndTriggeredAboveAndLongPosition(
							currencyPair, ask, false, true);
			executeOrders(positionsBelowLong);
			
			List<PendingOrder> positionsBelowShort = pendingOrdersDao
					.findByCurrencyPairAndOrderPriceGreaterThanAndTriggeredAboveAndLongPosition(
							currencyPair, bid, false, false);
			executeOrders(positionsBelowShort);
		}
	}
}
