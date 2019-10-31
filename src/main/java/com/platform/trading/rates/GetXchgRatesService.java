package com.platform.trading.rates;

import java.util.concurrent.*;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GetXchgRatesService {
	
	private ScheduledExecutorService thread = null;
	
	@Autowired
	private GetXchgRates rates;
	
	@PostConstruct
	public void setup()
	{
		thread = Executors.newScheduledThreadPool(1);
		thread.scheduleAtFixedRate(rates, 0, 60, TimeUnit.SECONDS);
	}
	
	public boolean isRunning()
	{
		return !thread.isTerminated();
	}
	
	public void stop()
	{
		thread.shutdown();
		
		while(isRunning())
		{
			try
			{
				Thread.sleep(200);
			}
			catch(InterruptedException e)
			{
				e.printStackTrace();
			}
		}
	}
	
}
