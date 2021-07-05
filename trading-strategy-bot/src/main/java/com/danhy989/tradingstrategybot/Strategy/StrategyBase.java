package com.danhy989.tradingstrategybot.Strategy;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;
import org.ta4j.core.BarSeries;

import java.util.Arrays;

public abstract class StrategyBase {

    private static final Logger log = LogManager.getLogger(StrategyBase.class);

    protected Integer minimumBarSize = 0;

    protected abstract Pair<Enum,Enum> checkSignal(BarSeries series);

    protected void setMinimunBarSize(final Integer ...integers){
        this.minimumBarSize = Arrays.stream(integers).max(Integer::compare).get();
    }
    protected boolean validateMinimumBarSize(final Integer barSize){
        if(barSize < minimumBarSize){
            log.info("Current barsize = {}, Minimum barsize = {} . Please wait!",barSize,minimumBarSize);
            return false;
        }
        return true;
    }
}
