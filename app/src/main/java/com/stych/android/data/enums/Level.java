package com.stych.android.data.enums;

//'level', 'in_array=top/second/third'
public enum Level {
    top, second, third;

    @Override
    public String toString() {
        switch (this) {
            case top:
                return "top";
            case second:
                return "second";
            case third:
                return "third";
            default:
                return "top";
        }
    }

    public Level childLevel() {
        if (this == top) {
            return second;
        } else if (this == second) {
            return third;
        }
        return top;
    }
}
