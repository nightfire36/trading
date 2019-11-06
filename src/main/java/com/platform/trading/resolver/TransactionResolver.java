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
	
	public BigDecimal calculateProfit(OpenedPosition opened, XchgRate rate)
	{
		BigDecimal openingPrice = opened.getOpeningPrice();
		BigDecimal amount = opened.getAmount();
		
		if(opened.isLongPosition())
		{
			BigDecimal currentPrice = rate.getBid();
			BigDecimal currentProfit = amount.multiply(currentPrice.divide(
					openingPrice, 16, RoundingMode.HALF_UP)).setScale(5, RoundingMode.HALF_UP);
			
			return currentProfit.subtract(amount);
		}
		else
		{
			BigDecimal currentPrice = rate.getAsk();
			BigDecimal currentProfit = amount.multiply(openingPrice.divide(
					currentPrice, 16, RoundingMode.HALF_UP)).setScale(5, RoundingMode.HALF_UP);
			
			return currentProfit.subtract(amount);
		}
	}
	
	public boolean updateAccountBalance(Long uid, BigDecimal updateAmount)
	{
		User user = null;
		try
		{
			user = usersDao.findByUid(uid);
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return false;
		}
		
		BigDecimal accountBalance = user.getAccountBalance().add(updateAmount);
		user.setAccountBalance(accountBalance);
		usersDao.save(user);
		
		return true;
	}
	
	public boolean closePosition(Long tid)
	{
		OpenedPosition opened = openedPositionsDao.findByTid(tid);
		
		if(opened != null)
		{
			BigDecimal closingPrice;
			
			BigDecimal transactionProfit = calculateProfit(opened, 
					rates.getPairRate(opened.getCurrencyPair()));
			
			if(opened.isLongPosition())
			{
				closingPrice = rates.getPairRate(opened.getCurrencyPair()).getBid();
			}
			else
			{
				closingPrice = rates.getPairRate(opened.getCurrencyPair()).getAsk();
			}
			
			ClosedPosition closed = new ClosedPosition();
			closed.setTid(tid);
			closed.setUid(opened.getUid());
			closed.setCurrencyPair(opened.getCurrencyPair());
			closed.setAmount(opened.getAmount());
			closed.setOpeningPrice(opened.getOpeningPrice());
			closed.setClosingPrice(closingPrice);
			closed.setOpeningTimestamp(opened.getOpeningTimestamp());
			closed.setProfit(transactionProfit);
			closed.setLongPosition(opened.isLongPosition());
			
			closedPositionsDao.save(closed);
			
			// update user account balance
			updateAccountBalance(opened.getUid(), opened.getAmount().add(transactionProfit));
			
			openedPositionsDao.deleteByTid(tid);
			
			return true;
		}
		else return false;
	}
	
	public boolean openPosition(Long uid, String currencyPair, BigDecimal amount, 
			boolean longPosition)
	{
		OpenedPosition opened = new OpenedPosition();
		User user = usersDao.findByUid(uid);
		
		if(user.getAccountBalance().compareTo(amount) >= 0)
		{
			opened.setUid(uid);
			opened.setCurrencyPair(currencyPair);
			opened.setAmount(amount);
			opened.setLongPosition(longPosition);
			
			if(longPosition)
			{
				opened.setOpeningPrice(rates.getPairRate(currencyPair).getAsk());
			}
			else
			{
				opened.setOpeningPrice(rates.getPairRate(currencyPair).getBid());
			}
			
			openedPositionsDao.save(opened);
			
			// update user amount of money
			updateAccountBalance(uid, amount.negate());
			
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
				try
				{
					pendingOrdersDao.deleteByOid(order.getOid());
					closePosition(order.getTid());
				}
				catch(Exception e)
				{
					e.printStackTrace();
				}
			}
			else if(order.getAmount() != null)
			{
				try
				{
					pendingOrdersDao.deleteByOid(order.getOid());
					openPosition(order.getUid(), order.getCurrencyPair(), order.getAmount(),
							order.isLongPosition());
				}
				catch(Exception e)
				{
					e.printStackTrace();
				}
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
