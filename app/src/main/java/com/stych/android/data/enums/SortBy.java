package com.stych.android.data.enums;

//'sort_by', 'in_array=/title/min_price/max_price/likes/comments/views/ratings/id'
public enum SortBy {
    ratings, views, min_price, max_price, id, title, likes, comments;

    public static CharSequence[] createOptions(boolean toString) {
        int index = 0;
        CharSequence[] items = new CharSequence[SortBy.values().length];
        for (SortBy sortBy : SortBy.values()) {
            if (index < items.length) {
                items[index] = toString ? sortBy.toString() : sortBy.toPrettyString();
                index++;
            }
        }
        return items;
    }

    @Override
    public String toString() {
        switch (this) {
            case id:
                return "id";
            case title:
                return "title";
            case min_price:
                return "min_price";
            case max_price:
                return "max_price";
            case likes:
                return "likes";
            case comments:
                return "comments";
            case views:
                return "views";
            case ratings:
                return "ratings";
            default:
                return "title";
        }
    }

    public String toPrettyString() {
        switch (this) {
            case id:
                return "Newest First";
            case title:
                return "Title";
            case min_price:
                return "Price - Low to High";
            case max_price:
                return "Price - High to Low";
            case likes:
                return "Likes";
            case comments:
                return "Comments";
            case views:
                return "Popularity";
            case ratings:
                return "Relevance";
            default:
                return "Title";
        }
    }
}
