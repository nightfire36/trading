package pl.platform.trading.rates;

import java.net.*;
import java.io.*;
import java.util.*;

import javax.net.ssl.*;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import pl.platform.trading.resolver.TransactionResolverImpl;

@Component
public class ExchnageRatesProvider implements Runnable {

    @Autowired
    private TransactionResolverImpl resolver;

    private List<ExchangeRate> rates = null;

    private HttpsURLConnection connect(String url) {
        HttpsURLConnection connection = null;
        try {
            connection = (HttpsURLConnection) new URL(url).openConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return connection;
    }

    private String getHttpsContent(HttpsURLConnection connection) {
        String content = "";
        BufferedReader reader;

        try {
            reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                content += line;
            }
            reader.close();

            return content;

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<ExchangeRate> jsonToExchangeRates(String jsonString) {
        ObjectMapper objectMapper = new ObjectMapper();

        List<ExchangeRate> rates = null;
        try {
            rates = objectMapper.readValue(jsonString, new TypeReference<List<ExchangeRate>>() {});
        } catch (IOException e) {
            e.printStackTrace();
        }

        return rates;
    }

    private List<ExchangeRate> updateRates() {
	// url to exchange rates provider 
        String url = "";

        HttpsURLConnection connection = connect(url);
        if (connection != null) {
            try {
                connection.setRequestMethod("POST");

                connection.setRequestProperty("Content-Length", "0");
                connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64)");
                connection.setRequestProperty("Accept-Language", "pl-PL, pl;q=0.9, en;q=0.8");

                connection.setDoOutput(true);

                DataOutputStream os = new DataOutputStream(connection.getOutputStream());
                os.writeBytes("");
                os.flush();
                os.close();

                return jsonToExchangeRates(getHttpsContent(connection));

            } catch (IOException e) {
                System.out.println("Setting properties error");
                e.printStackTrace();
            }
        }
        return null;
    }

    public void run() {
        this.rates = updateRates();
        resolver.resolve();
    }

    public List<ExchangeRate> getRates() {
        return this.rates;
    }

    public ExchangeRate getPairRate(String pair) {
        return this.rates.stream()
                .filter(rate -> rate.getCurrencyPair().equals(pair))
                .findFirst()
                .orElse(null);
    }
}
