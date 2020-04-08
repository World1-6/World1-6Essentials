package com.andrew121410.World16Elevators.Objects;

public enum ElevatorStatus {

    UP,
    DOWN,
    IDLE,
    DONT_KNOW;

    public ElevatorStatus upOrDown(boolean up) {
        if (up) return UP;
        return DOWN;
    }
}
