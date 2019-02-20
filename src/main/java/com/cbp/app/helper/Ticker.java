package com.cbp.app.helper;

@FunctionalInterface
public interface Ticker {
    Ticker.TickResult tick();

    public static enum TickResult {
        BREAK,
        CONTINUE;

        private TickResult() {
        }
    }
}
