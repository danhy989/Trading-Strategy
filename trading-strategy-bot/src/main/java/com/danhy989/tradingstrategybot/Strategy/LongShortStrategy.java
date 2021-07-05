package com.danhy989.tradingstrategybot.Strategy;

import com.danhy989.tradingstrategybot.Enums.PositionValueEnum;
import com.danhy989.tradingstrategybot.Enums.SignalEnum;
import com.danhy989.tradingstrategybot.Simulator.LongShortSimulator;
import com.danhy989.tradingstrategybot.Utils.TradingStrategyConstant;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.ta4j.core.BarSeries;
import org.ta4j.core.indicators.EMAIndicator;
import org.ta4j.core.indicators.MACDIndicator;
import org.ta4j.core.indicators.RSIIndicator;
import org.ta4j.core.indicators.StochasticOscillatorKIndicator;
import org.ta4j.core.indicators.helpers.ClosePriceIndicator;
import org.ta4j.core.num.DecimalNum;

@Component
public class LongShortStrategy extends StrategyBase implements InitializingBean {

    private static final Logger log = LogManager.getLogger(LongShortStrategy.class);

    @Value("${strategy.longShort.rsiPeriod}")
    private Integer rsiPeriod;

    @Value("${strategy.longShort.fastMAlen}")
    private Integer fastMAlen;

    @Value("${strategy.longShort.slowMAlen}")
    private Integer slowMAlen ;

    @Value("${strategy.longShort.signalMACDlen}")
    private Integer signalMACDlen;

    @Value("${strategy.longShort.stochLength}")
    private Integer stochLength;

    @Override
    public Pair<Enum, Enum> checkSignal(BarSeries series) {

        Pair<Enum,Enum> result = Pair.of(SignalEnum.NO_SIGNAL, PositionValueEnum.DEFAULT_POSITION_VALUE);

        // Validate bar size adopt to strategy
        if(!this.validateMinimumBarSize(series.getBarCount())){
            result = Pair.of(SignalEnum.NO_SIGNAL,PositionValueEnum.WAITING_BAR_COUNT_POSITION_VALUE);
            return result;
        }

        final ClosePriceIndicator closePriceIndi = new ClosePriceIndicator(series);

        MACDIndicator macd = null;
        EMAIndicator signalMACD = null;
        RSIIndicator xRSI = null;
        StochasticOscillatorKIndicator stochastic = null;

        //RSI
        xRSI = new RSIIndicator(closePriceIndi, rsiPeriod);

        //MACD
        macd = new MACDIndicator(closePriceIndi, fastMAlen, slowMAlen);
        signalMACD = new EMAIndicator(macd, signalMACDlen);

        //Stochastic
        stochastic = new StochasticOscillatorKIndicator(series, stochLength);

        //Condition Open Long position
        if(this.signalToOpenLongPosition(series,macd,signalMACD,xRSI,stochastic)){
            result = Pair.of(SignalEnum.HAVE_SIGNAL,PositionValueEnum.OPEN_LONG_POSITION_VALUE);
        }
        //Condition Close Long position
        if(this.signalToCloseLongPosition(series,xRSI)){
            result = Pair.of(SignalEnum.HAVE_SIGNAL,PositionValueEnum.CLOSE_LONG_POSITION_VALUE);
        }

        //Condition Open Short position
        if(this.signalToOpenShortPosition(series,macd,signalMACD,xRSI,stochastic)){
            result = Pair.of(SignalEnum.HAVE_SIGNAL,PositionValueEnum.OPEN_SHORT_POSITION_VALUE);
        }
        //Condition Close Short position
        if(this.signalToCloseShortPosition(series,xRSI)){
            result = Pair.of(SignalEnum.HAVE_SIGNAL,PositionValueEnum.CLOSE_SHORT_POSITION_VALUE);
        }

        log.info("````````````````````````````````````````START````````````````````````````````````````````````");
        log.info("RSI = {}, Stochastic = {}, MACD = {}, SignalMACD = {}",
                xRSI.getValue(series.getEndIndex()),
                stochastic.getValue(series.getEndIndex()),
                macd.getValue(series.getEndIndex()),
                signalMACD.getValue(series.getEndIndex()));
        log.info("RSI = {}, Stochastic = {}, MACD = {}",
                xRSI.getValue(series.getEndIndex()).isGreaterThan(DecimalNum.valueOf(TradingStrategyConstant.MID_RATE)),
                stochastic.getValue(series.getEndIndex()).isGreaterThan(DecimalNum.valueOf(TradingStrategyConstant.MID_RATE)),
                macd.getValue(series.getEndIndex()).isGreaterThan(signalMACD.getValue(series.getEndIndex())));
        log.info("`````````````````````````````````````````END`````````````````````````````````````````````````");
        return result;
    }

    private boolean signalToOpenLongPosition(final BarSeries series,final MACDIndicator macd,
                                             final EMAIndicator signalMACD,final RSIIndicator xRSI,
                                             final StochasticOscillatorKIndicator stochastic){
        if(macd.getValue(series.getEndIndex()).isGreaterThan(signalMACD.getValue(series.getEndIndex()))
                && xRSI.getValue(series.getEndIndex()).isGreaterThan(DecimalNum.valueOf(TradingStrategyConstant.MID_RATE))
                && stochastic.getValue(series.getEndIndex()).isGreaterThan(DecimalNum.valueOf(TradingStrategyConstant.MID_RATE))){
            return true;
        }
        return false;
    }

    private boolean signalToCloseLongPosition(final BarSeries series,
                                              final RSIIndicator xRSI){
        if(xRSI.getValue(series.getEndIndex()).isLessThan(DecimalNum.valueOf(TradingStrategyConstant.MID_RATE))){
            return true;
        }
        return false;
    }

    private boolean signalToOpenShortPosition(final BarSeries series,final MACDIndicator macd,
                                              final EMAIndicator signalMACD,final RSIIndicator xRSI,
                                              final StochasticOscillatorKIndicator stochastic){
        if(macd.getValue(series.getEndIndex()).isLessThan(signalMACD.getValue(series.getEndIndex()))
                && xRSI.getValue(series.getEndIndex()).isLessThan(DecimalNum.valueOf(TradingStrategyConstant.MID_RATE))
                && stochastic.getValue(series.getEndIndex()).isLessThan(DecimalNum.valueOf(TradingStrategyConstant.MID_RATE))){
            return true;
        }
        return false;
    }

    private boolean signalToCloseShortPosition(final BarSeries series,
                                               final RSIIndicator xRSI){
        if(xRSI.getValue(series.getEndIndex()).isGreaterThan(DecimalNum.valueOf(TradingStrategyConstant.MID_RATE))){
            return true;
        }
        return false;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        this.setMinimunBarSize(rsiPeriod,fastMAlen,slowMAlen,signalMACDlen,stochLength);
    }
}
