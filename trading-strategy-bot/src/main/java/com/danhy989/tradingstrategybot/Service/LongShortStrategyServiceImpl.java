package com.danhy989.tradingstrategybot.Service;

import com.danhy989.tradingstrategybot.DTO.HeikinAshiCandle;
import com.danhy989.tradingstrategybot.Enums.PositionValueEnum;
import com.danhy989.tradingstrategybot.Enums.SignalEnum;
import com.danhy989.tradingstrategybot.Simulator.LongShortSimulator;
import com.danhy989.tradingstrategybot.Strategy.LongShortStrategy;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.ta4j.core.BarSeries;

@Service
public class LongShortStrategyServiceImpl implements LongShortStrategyService{

    private static final Logger log = LogManager.getLogger(LongShortStrategyServiceImpl.class);

    @Value("#{new Boolean('${mode.simulator:false}')}")
    private boolean isSimulatorMode;

    private static boolean isBoughtLong = false;
    private static boolean isSoldLong = false;
    private static boolean isBoughtShort = false;
    private static boolean isSoldShort = false;

    @Autowired
    private LongShortStrategy longShortStrategy;

    @Override
    public void runStrategy(final BarSeries series,final HeikinAshiCandle candle) {
        try {
            final Pair<Enum, Enum> signal = longShortStrategy.checkSignal(series);

            //NO SIGNAL
            if(signal.getKey() == SignalEnum.NO_SIGNAL){
                if (PositionValueEnum.WAITING_BAR_COUNT_POSITION_VALUE.equals(signal.getValue())) {
                    //Leave to StrategyBase handle
                } else if (PositionValueEnum.DEFAULT_POSITION_VALUE.equals(signal.getValue())) {
                    log.info("NO SIGNAL");
                } else {
                    log.warn("Unknown no signal");
                }
            }

            //HAVE SIGNAL
            if(signal.getKey() == SignalEnum.HAVE_SIGNAL){
                if (PositionValueEnum.OPEN_LONG_POSITION_VALUE.equals(signal.getValue())) {
                    this.handleLongOpenPosition(candle);
                } else if (PositionValueEnum.CLOSE_LONG_POSITION_VALUE.equals(signal.getValue())) {
                    this.handleLongClosePosition(candle);
                } else if (PositionValueEnum.OPEN_SHORT_POSITION_VALUE.equals(signal.getValue())){
                    this.handleShortOpenPosition(candle);
                } else if(PositionValueEnum.CLOSE_SHORT_POSITION_VALUE.equals(signal.getValue())){
                    this.handleShortClosePosition(candle);
                } else {
                    log.warn("Unknown have signal");
                }
                log.info("Pair = {}, Interval = {}, NumOfTrade = {}, NumOfLongTrade = {}, " +
                                "NumOfShortTrade = {}, Total = {}, TotalFeeCost = {}",
                        candle.getCoinPairName(),candle.getIntervalId(),
                        LongShortSimulator.getNumOfLongTrade()+LongShortSimulator.getNumOfShortTrade(),
                        LongShortSimulator.getNumOfLongTrade(), LongShortSimulator.getNumOfShortTrade(),
                        LongShortSimulator.getTotal(),LongShortSimulator.getTotalFee());
                log.info("\n");
            }

        } catch (Exception e) {
            log.error("Error : {}",e);
        }
    }

    private void handleLongOpenPosition(final HeikinAshiCandle candle){
        if(isBoughtShort) {
            this.handleShortClosePosition(candle);
        }
        if(!isBoughtLong){
            log.info("OPEN LONG POSITION");
            if (isSimulatorMode) {
                LongShortSimulator.fakeDataLongOpenPosition(candle);
            }else{
                this.orderLongOpenPosition();
            }
            isBoughtLong = true;
            isSoldLong = false;
        }
    }

    private void orderLongOpenPosition(){
        //Handle code to order long position to binance
    }

    private void handleLongClosePosition(final HeikinAshiCandle candle){
        if(!isSoldLong && isBoughtLong){
            log.info("CLOSE LONG POSITION");
            if (isSimulatorMode) {
                LongShortSimulator.fakeDataLongClosePosition(candle);
            }else{
                this.orderLongClosePosition();
            }
            isSoldLong = true;
            isBoughtLong = false;
        }
    }

    private void orderLongClosePosition(){
        //Handle code to order long position to binance
    }

    private void handleShortOpenPosition(final HeikinAshiCandle candle){
        if(isBoughtLong) {
            this.handleLongClosePosition(candle);
        }
        if(!isBoughtShort){
            log.info("OPEN SHORT POSITION");
            if (isSimulatorMode) {
                LongShortSimulator.fakeDataShortOpenPosition(candle);
            }else{
                this.orderShortOpenPosition();
            }
            isBoughtShort = true;
            isSoldShort = false;
        }
    }

    private void orderShortOpenPosition(){
        //Handle code to order short position to binance
    }

    public void handleShortClosePosition(final HeikinAshiCandle candle){
        if(!isSoldShort && isBoughtShort){
            log.info("CLOSE SHORT POSITION");
            if (isSimulatorMode) {
                LongShortSimulator.fakeDataShortClosePosition(candle);
            }else{
                this.orderShortClosePosition();
            }
            isSoldShort = true;
            isBoughtShort = false;
        }
    }

    private void orderShortClosePosition(){
        //Handle code to order short position to binance
    }

}
