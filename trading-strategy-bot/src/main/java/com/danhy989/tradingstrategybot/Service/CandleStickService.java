package com.danhy989.tradingstrategybot.Service;

import com.danhy989.tradingstrategybot.DTO.HeikinAshiCandle;
import com.binance.api.client.BinanceApiClientFactory;
import com.binance.api.client.BinanceApiWebSocketClient;
import com.binance.api.client.domain.market.CandlestickInterval;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import org.ta4j.core.BarSeries;
import org.ta4j.core.BaseBarSeriesBuilder;

import java.time.ZonedDateTime;
import java.util.LinkedList;



@Component
public class CandleStickService implements
        ApplicationListener<ContextRefreshedEvent> {

    private static final Logger log = LogManager.getLogger(CandleStickService.class);

    @Value("${binance.apiKey}")
    private String apiKey;

    @Value("${binance.apiSecret}")
    private String apiSecret;

    @Value("${coin.pairName}")
    private String coinPairName;

    @Value("${coin.candlestickInterval}")
    private String candlestickInterval;

    @Autowired
    private LongShortStrategyService longShortStrategyService;

    private static BarSeries series = new BaseBarSeriesBuilder().withName("long-short-strategy").build();

    private LinkedList<HeikinAshiCandle> closePrices = new LinkedList<>();

    public void subscribeCandleStickData() {
        BinanceApiWebSocketClient client = BinanceApiClientFactory.newInstance(apiKey, apiSecret).newWebSocketClient();
        client.onCandlestickEvent(coinPairName, CandlestickInterval.valueOf(candlestickInterval), response -> {

            if (response.getBarFinal()) {
                //Convert Regular Candle data to Heikin Ashi Candle data
                final boolean isFirstCandle = this.closePrices.isEmpty();
                final HeikinAshiCandle previousHeikinAshiCandle = isFirstCandle ? null : this.closePrices.getLast();
                HeikinAshiCandle candle = new HeikinAshiCandle(coinPairName, response.getEventType(),
                        response.getEventTime(), response.getSymbol(), response.getOpenTime(), response.getOpen(),
                        response.getHigh(), response.getLow(), response.getClose(), response.getVolume(),
                        response.getCloseTime(), response.getIntervalId(), response.getFirstTradeId(),
                        response.getLastTradeId(), response.getQuoteAssetVolume(), response.getNumberOfTrades(),
                        response.getTakerBuyBaseAssetVolume(), response.getTakerBuyQuoteAssetVolume(),
                        response.getBarFinal(), isFirstCandle, previousHeikinAshiCandle);

                log.info("Heikin Ashi Candle: Pair = {}, Close Price = {}",candle.getCoinPairName(),candle.getClose());

                //Add bar to series bar
                this.closePrices.add(candle);
                series.addBar(ZonedDateTime.now(), candle.getOpen(), candle.getHigh(), candle.getLow(), candle.getClose(), candle.getVolume());

                longShortStrategyService.runStrategy(series,candle);
            } else {
                log.info("kline is not final open_time={}, close price={}", response.getOpenTime(), response.getClose());
            }
        });

    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        log.info("starting service");
        this.subscribeCandleStickData(); //Start reading Binance data whenever the application loads
    }
}
