package com.cbp.app.helper;
import java.time.Duration;
import java.time.LocalDateTime;

public class TimeLimitedRepeater {
    private static Duration DEFAULT_TIME_LIMIT = Duration.ofMinutes(45L);

    public TimeLimitedRepeater() { }

    public static TimeLimitedRepeater.TimeLimitedRepeaterWithTicker repeat(Ticker ticker) {
        return new TimeLimitedRepeater.TimeLimitedRepeaterWithTicker(ticker);
    }

    public static class TimeLimitedRepeaterWithTicker {
        private final Ticker ticker;

        private TimeLimitedRepeaterWithTicker(Ticker ticker) {
            this.ticker = ticker;
        }

        public void repeatWithDefaultTimeLimit() {
            this.repeatFor(TimeLimitedRepeater.DEFAULT_TIME_LIMIT);
        }

        public void repeatFor(Duration duration) {
            LocalDateTime endTime = LocalDateTime.now().plus(duration);

            while(LocalDateTime.now().isBefore(endTime)) {
                Ticker.TickResult result = this.ticker.tick();
                if (result == Ticker.TickResult.BREAK) {
                    break;
                }
            }
        }
    }
}
