package com.danhy989.tradingstrategybot.DTO;

public class HeikinAshiCandle extends CandleStickData{

    private HeikinAshiCandle previousHeikinAshiCandle;

    private String HAClose;

    private String HAOpen;

    private String HAHigh;

    private String HALow;

    public HeikinAshiCandle(String coinPairName, String eventType,
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
                            Boolean isBarFinal,Boolean isFirstCandle, HeikinAshiCandle previousHeikinAshiCandle) {
        super(coinPairName, eventType,
                eventTime,
                symbol,
                openTime,
                open,
                high,
                low,
                close,
                volume,
                closeTime,
                intervalId,
                firstTradeId,
                lastTradeId,
                quoteAssetVolume,
                numberOfTrades,
                takerBuyBaseAssetVolume,
                takerBuyQuoteAssetVolume,
                isBarFinal,isFirstCandle);
        this.previousHeikinAshiCandle = previousHeikinAshiCandle;
        this.setupHeikinAshiFormula();
    }



    private String getHAClose(){
        return  String.valueOf(0.25*(Double.valueOf(open)+Double.valueOf(high)+Double.valueOf(low)+Double.valueOf(close)));
    }

    private String getHAOpen(){
        if(this.isFirstCandle){
            return String.valueOf(0.5*(Double.valueOf(this.open) + Double.valueOf(this.close)));
        }
        return String.valueOf(0.5*(Double.valueOf(previousHeikinAshiCandle.getOpen())+Double.valueOf(previousHeikinAshiCandle.getClose())));
    }

    private String getHAHigh(){
        if(this.isFirstCandle){
            return high;
        }
        return String.valueOf(Math.max(Double.valueOf(high), Math.max(Double.valueOf(this.HAClose), Double.valueOf(this.HAOpen))));
    }

    private String getHALow(){
        if(this.isFirstCandle){
            return low;
        }
        return String.valueOf(Math.min(Double.valueOf(low),Math.min(Double.valueOf(this.HAClose), Double.valueOf(this.HAOpen))));
    }

    private void setupHeikinAshiFormula(){
        this.HAClose = this.getHAClose();
        this.HAOpen = this.getHAOpen();
        this.HAHigh = this.getHAHigh();
        this.HALow = this.getHALow();
        this.close = HAClose;
        this.open = HAOpen;
        this.high = HAHigh;
        this.low = HALow;
    }
}
