package com.danhy989.tradingstrategybot.Simulator;

import com.danhy989.tradingstrategybot.DTO.HeikinAshiCandle;
import com.danhy989.tradingstrategybot.Service.LongShortStrategyServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LongShortSimulator {

    private static final Logger log = LogManager.getLogger(LongShortSimulator.class);

    private static float orderPrice = 0f;
    private static float closeOrderPrice = 0f;
    private static float total = 2000f;
    private static float fee = 0.00075f;
    private static float totalFee = 0f;
    private static int numOfLongTrade = 0;
    private static int numOfShortTrade = 0;

    public static void fakeDataLongOpenPosition(final HeikinAshiCandle candle){
        float feeCost = fee * total;
        totalFee = totalFee + feeCost;
        total = total - feeCost;
        orderPrice = Float.valueOf(candle.getClose());
        numOfLongTrade++;
        log.info("Handle open LONG : total = {}, order price = {}, fee cost = {}", total, orderPrice, feeCost);
    }

    public static void fakeDataLongClosePosition(final HeikinAshiCandle candle){
        closeOrderPrice = Float.valueOf(candle.getClose());
        total = total*(closeOrderPrice/orderPrice);
        float feeCost = fee * total;
        totalFee = totalFee + feeCost;
        total = total - feeCost;
        log.info("Handle close LONG : total = {}, close order price = {}, fee cost = {}", total, closeOrderPrice, feeCost);
    }

    public static void fakeDataShortOpenPosition(final HeikinAshiCandle candle){
        float feeCost = fee * total;
        totalFee = totalFee + feeCost;
        total = total - feeCost;
        orderPrice = Float.valueOf(candle.getClose());
        numOfShortTrade++;
        log.info("Handle open SHORT : total = {}, order price = {}, fee cost = {}", total, orderPrice, feeCost);
    }

    public static void fakeDataShortClosePosition(final HeikinAshiCandle candle){
        closeOrderPrice = Float.valueOf(candle.getClose());
        total = total*(orderPrice/closeOrderPrice);
        float feeCost = fee * total;
        totalFee = totalFee + feeCost;
        total = total - feeCost;
        log.info("Handle close SHORT : total = {}, close order price = {}, fee cost = {}", total, closeOrderPrice, feeCost);
    }

    public static float getTotal() {
        return total;
    }

    public static float getTotalFee() {
        return totalFee;
    }

    public static int getNumOfLongTrade() {
        return numOfLongTrade;
    }

    public static int getNumOfShortTrade() {
        return numOfShortTrade;
    }
}
