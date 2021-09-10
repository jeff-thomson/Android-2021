package com.stych.android.data.enums;

//'image_for', 'in_array=ephotos/iphotos'
public enum ImageFor {
    ephotos, iphotos, pphotos;

    public static ImageFor fromString(String imageFor) {
        if ("ephotos".equals(imageFor)) {
            return ephotos;
        } else if ("pphotos".equals(imageFor)) {
            return pphotos;
        }
        return iphotos;
    }

    @Override
    public String toString() {
        switch (this) {
            case ephotos:
                return "ephotos";
            case iphotos:
                return "iphotos";
            case pphotos:
                return "pphotos";
            default:
                return "ephotos";
        }
    }
}
