package pl.platform.trading.rates;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ExchangeRatesRunner implements Runnable {

    @Autowired
    private ExchnageRatesProvider rates;

    private long lastTimestamp;

    @PostConstruct
    public void setup() {
        Thread thread = new Thread(this);
        thread.start();
    }

    public void run() {
        while(true) {
            // dont get rates more frequent than every 40 s
            // even if interrupted
            if(this.lastTimestamp + 40 * 1000 < System.currentTimeMillis()) {
                Thread ratesProviderThread = new Thread(this.rates);
                ratesProviderThread.start();
                this.lastTimestamp = System.currentTimeMillis();
            }
            try {
                Thread.sleep(60 * 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
