package com.stych.android.data.enums;

//'view_type', 'in_array=item/establishment/offer/other'
public enum ViewType {
    item, establishment, offer, other;

    @Override
    public String toString() {
        switch (this) {
            case item:
                return "item";
            case establishment:
                return "establishment";
            case offer:
                return "offer";
            case other:
                return "other";
            default:
                return "other";
        }
    }
}
