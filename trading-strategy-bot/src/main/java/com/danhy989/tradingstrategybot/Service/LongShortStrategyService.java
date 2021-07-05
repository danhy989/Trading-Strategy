package com.danhy989.tradingstrategybot.Service;

import com.danhy989.tradingstrategybot.DTO.HeikinAshiCandle;
import org.ta4j.core.BarSeries;

public interface LongShortStrategyService {
    void runStrategy(final BarSeries barSeries, final HeikinAshiCandle candleStickData);
}
