package dev.totaltax.particle.event.impl;

import dev.totaltax.particle.event.Event;

public class EventKey extends Event {
    int key;
    int direction;

    public EventKey(int key, int direction) {
        this.key = key;
        this.direction = direction;
    }

    public int getKey() {
        return key;
    }

    public Type getDirection() {
        switch (direction) {
            case 0 -> {
                return Type.RELEASE;
            }
            case 1 -> {
                return Type.DOWN;
            }
            case 2 -> {
                return Type.HOLD;
            }
        }
        return Type.RELEASE;
    }

    public enum Type {
        DOWN, HOLD, RELEASE

    }
}
