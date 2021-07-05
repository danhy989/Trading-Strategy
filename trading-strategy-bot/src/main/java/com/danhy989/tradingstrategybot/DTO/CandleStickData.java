package com.danhy989.tradingstrategybot.DTO;


public class CandleStickData {

    protected long id;
    protected String coinPairName;

    protected boolean isFirstCandle;

    /* Properties from Binance pojo */
    protected String eventType;
    protected long eventTime;
    protected String symbol;
    protected Long openTime;
    protected String open;
    protected String high;
    protected String low;
    protected String close;
    protected String volume;
    protected Long closeTime;
    protected String intervalId;
    protected Long firstTradeId;
    protected Long lastTradeId;
    protected String quoteAssetVolume;
    protected Long numberOfTrades;
    protected String takerBuyBaseAssetVolume;
    protected String takerBuyQuoteAssetVolume;
    protected Boolean isBarFinal;

    public CandleStickData(final String coinPairName, String eventType,
                            long eventTime,
                            String symbol,
                            Long openTime,
                            String open,
                            String high,
                            String low,
                            String close,
                            String volume,
                            Long closeTime,
                            String intervalId,
                            Long firstTradeId,
                            Long lastTradeId,
                            String quoteAssetVolume,
                            Long numberOfTrades,
                            String takerBuyBaseAssetVolume,
                            String takerBuyQuoteAssetVolume,
                            Boolean isBarFinal,Boolean isFirstCandle) {
        this.coinPairName = coinPairName;
        this.eventType = eventType;
        this.eventTime = eventTime;
        this.symbol = symbol;
        this.openTime = openTime;
        this.open = open;
        this.high = high;
        this.low = low;
        this.close = close;
        this.volume = volume;
        this.closeTime = closeTime;
        this.intervalId = intervalId;
        this.firstTradeId = firstTradeId;
        this.lastTradeId = lastTradeId;
        this.quoteAssetVolume = quoteAssetVolume;
        this.numberOfTrades = numberOfTrades;
        this.takerBuyBaseAssetVolume = takerBuyBaseAssetVolume;
        this.takerBuyQuoteAssetVolume = takerBuyQuoteAssetVolume;
        this.isBarFinal = isBarFinal;
        this.isFirstCandle = isFirstCandle;
    }

    public long getId() {
        return id;
    }

    public String getCoinPairName() {
        return coinPairName;
    }

    public String getEventType() {
        return eventType;
    }

    public long getEventTime() {
        return eventTime;
    }

    public String getSymbol() {
        return symbol;
    }

    public Long getOpenTime() {
        return openTime;
    }

    public String getOpen() {
        return open;
    }

    public String getHigh() {
        return high;
    }

    public String getLow() {
        return low;
    }

    public String getClose() {
        return close;
    }

    public String getVolume() {
        return volume;
    }

    public Long getCloseTime() {
        return closeTime;
    }

    public String getIntervalId() {
        return intervalId;
    }

    public Long getFirstTradeId() {
        return firstTradeId;
    }

    public Long getLastTradeId() {
        return lastTradeId;
    }

    public String getQuoteAssetVolume() {
        return quoteAssetVolume;
    }

    public Long getNumberOfTrades() {
        return numberOfTrades;
    }

    public String getTakerBuyBaseAssetVolume() {
        return takerBuyBaseAssetVolume;
    }

    public String getTakerBuyQuoteAssetVolume() {
        return takerBuyQuoteAssetVolume;
    }

    public Boolean getBarFinal() {
        return isBarFinal;
    }

    @Override
    public String toString() {
        return "CandleStickData{" +
                "id=" + id +
                ", coinPairName='" + coinPairName + '\'' +
                ", isFirstCandle=" + isFirstCandle +
                ", eventType='" + eventType + '\'' +
                ", eventTime=" + eventTime +
                ", symbol='" + symbol + '\'' +
                ", openTime=" + openTime +
                ", open='" + open + '\'' +
                ", high='" + high + '\'' +
                ", low='" + low + '\'' +
                ", close='" + close + '\'' +
                ", volume='" + volume + '\'' +
                ", closeTime=" + closeTime +
                ", intervalId='" + intervalId + '\'' +
                ", firstTradeId=" + firstTradeId +
                ", lastTradeId=" + lastTradeId +
                ", quoteAssetVolume='" + quoteAssetVolume + '\'' +
                ", numberOfTrades=" + numberOfTrades +
                ", takerBuyBaseAssetVolume='" + takerBuyBaseAssetVolume + '\'' +
                ", takerBuyQuoteAssetVolume='" + takerBuyQuoteAssetVolume + '\'' +
                ", isBarFinal=" + isBarFinal +
                '}';
    }
}
