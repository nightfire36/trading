package com.platform.trading.rates;

import java.net.*;
import java.io.*;
import java.util.*;

import javax.net.ssl.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.platform.trading.resolver.TransactionResolver;

import javax.annotation.PostConstruct;
import javax.json.*;

import java.sql.Timestamp;

@Component
public class GetXchgRates {
	
	@Autowired
	private TransactionResolver resolver;
	
	private ArrayList<XchgRate> rates = null;
	
	private long lastUpdate = -1;
	
	public HttpsURLConnection connect(String url)
	{
		HttpsURLConnection connection = null;
		try
		{
			connection = (HttpsURLConnection)new URL(url).openConnection();
		}
		catch(MalformedURLException e)
		{
			e.printStackTrace();
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
		return connection;
	}
	
	public LinkedList<String> getHttpsContent(HttpsURLConnection connection)
	{
		LinkedList<String> content = new LinkedList<String>();
		BufferedReader reader;
		
		try
		{
			reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			String line;
			while((line = reader.readLine()) != null)
			{
				content.add(line);
			}
			reader.close();
			
			return content;
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
		return content;
	}
	
	public Timestamp stringToTimestamp(String dateTimestamp)
	{
		int begin = dateTimestamp.indexOf("(");
		int end = dateTimestamp.indexOf(")");
		Timestamp timestamp = new Timestamp(Long.parseLong(dateTimestamp.substring(begin + 1, end)));
		return timestamp;
	}
	
	public XchgRate jsonToXchgRate(String jsonString)
	{
		XchgRate rate = null;
		
		JsonReader jsonReader = Json.createReader(new StringReader(jsonString));
		JsonObject resultObj = jsonReader.readObject();
		jsonReader.close();
		
		switch(resultObj.getString("InstrumentName"))
		{
			case "AUDCAD":
				rate = new XchgRate("AUDCAD");
				rate.setAsk(resultObj.getJsonNumber("Ask").bigDecimalValue());
				rate.setBid(resultObj.getJsonNumber("Bid").bigDecimalValue());
				rate.setTimestamp(stringToTimestamp(resultObj.getString("Timestamp")));
				break;
			case "AUDCHF":
				rate = new XchgRate("AUDCHF");
				rate.setAsk(resultObj.getJsonNumber("Ask").bigDecimalValue());
				rate.setBid(resultObj.getJsonNumber("Bid").bigDecimalValue());
				rate.setTimestamp(stringToTimestamp(resultObj.getString("Timestamp")));
				break;
			case "AUDJPY":
				rate = new XchgRate("AUDJPY");
				rate.setAsk(resultObj.getJsonNumber("Ask").bigDecimalValue());
				rate.setBid(resultObj.getJsonNumber("Bid").bigDecimalValue());
				rate.setTimestamp(stringToTimestamp(resultObj.getString("Timestamp")));
				break;
			case "AUDUSD":
				rate = new XchgRate("AUDUSD");
				rate.setAsk(resultObj.getJsonNumber("Ask").bigDecimalValue());
				rate.setBid(resultObj.getJsonNumber("Bid").bigDecimalValue());
				rate.setTimestamp(stringToTimestamp(resultObj.getString("Timestamp")));
				break;
			case "CADJPY":
				rate = new XchgRate("CADJPY");
				rate.setAsk(resultObj.getJsonNumber("Ask").bigDecimalValue());
				rate.setBid(resultObj.getJsonNumber("Bid").bigDecimalValue());
				rate.setTimestamp(stringToTimestamp(resultObj.getString("Timestamp")));
				break;
			case "CHFPLN":
				rate = new XchgRate("CHFPLN");
				rate.setAsk(resultObj.getJsonNumber("Ask").bigDecimalValue());
				rate.setBid(resultObj.getJsonNumber("Bid").bigDecimalValue());
				rate.setTimestamp(stringToTimestamp(resultObj.getString("Timestamp")));
				break;
			case "EURAUD":
				rate = new XchgRate("EURAUD");
				rate.setAsk(resultObj.getJsonNumber("Ask").bigDecimalValue());
				rate.setBid(resultObj.getJsonNumber("Bid").bigDecimalValue());
				rate.setTimestamp(stringToTimestamp(resultObj.getString("Timestamp")));
				break;
			case "EURCAD":
				rate = new XchgRate("EURCAD");
				rate.setAsk(resultObj.getJsonNumber("Ask").bigDecimalValue());
				rate.setBid(resultObj.getJsonNumber("Bid").bigDecimalValue());
				rate.setTimestamp(stringToTimestamp(resultObj.getString("Timestamp")));
				break;
			case "EURCHF":
				rate = new XchgRate("EURCHF");
				rate.setAsk(resultObj.getJsonNumber("Ask").bigDecimalValue());
				rate.setBid(resultObj.getJsonNumber("Bid").bigDecimalValue());
				rate.setTimestamp(stringToTimestamp(resultObj.getString("Timestamp")));
				break;
			case "EURGBP":
				rate = new XchgRate("EURGBP");
				rate.setAsk(resultObj.getJsonNumber("Ask").bigDecimalValue());
				rate.setBid(resultObj.getJsonNumber("Bid").bigDecimalValue());
				rate.setTimestamp(stringToTimestamp(resultObj.getString("Timestamp")));
				break;
			case "EURJPY":
				rate = new XchgRate("EURJPY");
				rate.setAsk(resultObj.getJsonNumber("Ask").bigDecimalValue());
				rate.setBid(resultObj.getJsonNumber("Bid").bigDecimalValue());
				rate.setTimestamp(stringToTimestamp(resultObj.getString("Timestamp")));
				break;
			case "EURPLN":
				rate = new XchgRate("EURPLN");
				rate.setAsk(resultObj.getJsonNumber("Ask").bigDecimalValue());
				rate.setBid(resultObj.getJsonNumber("Bid").bigDecimalValue());
				rate.setTimestamp(stringToTimestamp(resultObj.getString("Timestamp")));
				break;
			case "EURUSD":
				rate = new XchgRate("EURUSD");
				rate.setAsk(resultObj.getJsonNumber("Ask").bigDecimalValue());
				rate.setBid(resultObj.getJsonNumber("Bid").bigDecimalValue());
				rate.setTimestamp(stringToTimestamp(resultObj.getString("Timestamp")));
				break;
			case "GBPAUD":
				rate = new XchgRate("GBPAUD");
				rate.setAsk(resultObj.getJsonNumber("Ask").bigDecimalValue());
				rate.setBid(resultObj.getJsonNumber("Bid").bigDecimalValue());
				rate.setTimestamp(stringToTimestamp(resultObj.getString("Timestamp")));
				break;
			case "GBPCAD":
				rate = new XchgRate("GBPCAD");
				rate.setAsk(resultObj.getJsonNumber("Ask").bigDecimalValue());
				rate.setBid(resultObj.getJsonNumber("Bid").bigDecimalValue());
				rate.setTimestamp(stringToTimestamp(resultObj.getString("Timestamp")));
				break;
			case "GBPCHF":
				rate = new XchgRate("GBPCHF");
				rate.setAsk(resultObj.getJsonNumber("Ask").bigDecimalValue());
				rate.setBid(resultObj.getJsonNumber("Bid").bigDecimalValue());
				rate.setTimestamp(stringToTimestamp(resultObj.getString("Timestamp")));
				break;
			case "GBPJPY":
				rate = new XchgRate("GBPJPY");
				rate.setAsk(resultObj.getJsonNumber("Ask").bigDecimalValue());
				rate.setBid(resultObj.getJsonNumber("Bid").bigDecimalValue());
				rate.setTimestamp(stringToTimestamp(resultObj.getString("Timestamp")));
				break;
			case "GBPPLN":
				rate = new XchgRate("GBPPLN");
				rate.setAsk(resultObj.getJsonNumber("Ask").bigDecimalValue());
				rate.setBid(resultObj.getJsonNumber("Bid").bigDecimalValue());
				rate.setTimestamp(stringToTimestamp(resultObj.getString("Timestamp")));
				break;
			case "GBPUSD":
				rate = new XchgRate("GBPUSD");
				rate.setAsk(resultObj.getJsonNumber("Ask").bigDecimalValue());
				rate.setBid(resultObj.getJsonNumber("Bid").bigDecimalValue());
				rate.setTimestamp(stringToTimestamp(resultObj.getString("Timestamp")));
				break;
			case "USDCAD":
				rate = new XchgRate("USDCAD");
				rate.setAsk(resultObj.getJsonNumber("Ask").bigDecimalValue());
				rate.setBid(resultObj.getJsonNumber("Bid").bigDecimalValue());
				rate.setTimestamp(stringToTimestamp(resultObj.getString("Timestamp")));
				break;
			case "USDCHF":
				rate = new XchgRate("USDCHF");
				rate.setAsk(resultObj.getJsonNumber("Ask").bigDecimalValue());
				rate.setBid(resultObj.getJsonNumber("Bid").bigDecimalValue());
				rate.setTimestamp(stringToTimestamp(resultObj.getString("Timestamp")));
				break;
			case "USDJPY":
				rate = new XchgRate("USDJPY");
				rate.setAsk(resultObj.getJsonNumber("Ask").bigDecimalValue());
				rate.setBid(resultObj.getJsonNumber("Bid").bigDecimalValue());
				rate.setTimestamp(stringToTimestamp(resultObj.getString("Timestamp")));
				break;
			case "USDPLN":
				rate = new XchgRate("USDPLN");
				rate.setAsk(resultObj.getJsonNumber("Ask").bigDecimalValue());
				rate.setBid(resultObj.getJsonNumber("Bid").bigDecimalValue());
				rate.setTimestamp(stringToTimestamp(resultObj.getString("Timestamp")));
				break;
		}
		
		return rate;
	}
	
	public ArrayList<XchgRate> parseJsonToXchgRates(String jsonString)
	{
		ArrayList<XchgRate> rates = new ArrayList<XchgRate>();
		
		JsonReader jsonReader = Json.createReader(new StringReader(jsonString));
		JsonObject resultObj = jsonReader.readObject();
		jsonReader.close();
		
		if(resultObj.getString("Result").equals("OK"))
		{
			JsonArray jsonRecords = resultObj.getJsonArray("Records");
			
			for(JsonValue record : jsonRecords)
			{
				XchgRate iRate = jsonToXchgRate(record.toString());
				if(iRate != null)
				{
					rates.add(iRate);
				}
			}
		}
		return rates;
	}
	
	public ArrayList<XchgRate> updateRates()
	{
		// this is URL to exchange rates provider
		String url = "";
		
		HttpsURLConnection connection = connect(url);
		if(connection != null)
		{
			try
			{
				connection.setRequestMethod("POST");
				
				connection.setRequestProperty("Content-Length", "0");
				connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64)");
				connection.setRequestProperty("Accept-Language", "pl-PL,pl;q=0.9,en-US;q=0.8,en;q=0.7");
				
				connection.setDoOutput(true);
				
				DataOutputStream os = new DataOutputStream(connection.getOutputStream());

				// parameter of POST request appropriate for chosen exchange rates provider
				os.writeBytes("");
				os.flush();
				os.close();
			}
			catch(ProtocolException e)
			{
				System.out.println("Setting properties error");
				e.printStackTrace();
			}
			catch(IOException e)
			{
				System.out.println("Setting properties error");
				e.printStackTrace();
			}
			
			LinkedList<String> content = getHttpsContent(connection);
			return parseJsonToXchgRates(content.get(0));
		}
		return null;
	}
	
	@PostConstruct
	public synchronized void updateRatesIfNeeded()
	{
		if(this.lastUpdate == -1)
		{
			this.rates = updateRates();
			this.lastUpdate = new Date().getTime();
		}
		else if((new Date().getTime() - this.lastUpdate) > 40*1000)
		{
			this.rates = updateRates();
			this.lastUpdate = new Date().getTime();
		}
	}

	public ArrayList<XchgRate> getRates()
	{
		updateRatesIfNeeded();
		return this.rates;
	}
	
	public XchgRate getPairRateNotUpdated(String pair)
	{
		for(XchgRate rate : this.rates)
		{
			if(rate.getCurrencyPair().equals(pair))
			{
				return rate;
			}
		}
		return null;
	}
	
	public XchgRate getPairRate(String pair)
	{
		updateRatesIfNeeded();
		return getPairRateNotUpdated(pair);
	}
}
