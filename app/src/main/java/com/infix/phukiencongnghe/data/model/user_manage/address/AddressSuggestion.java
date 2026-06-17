package com.infix.phukiencongnghe.data.model.user_manage.address;

public class AddressSuggestion {
    private final String placeId;
    private final String title;
    private final String subtitle;

    public AddressSuggestion(
            String placeId,
            String title,
            String subtitle
    ) {
        this.placeId = placeId;
        this.title = title;
        this.subtitle = subtitle;
    }

    public String getPlaceId() {
        return placeId;
    }

    public String getTitle() {
        return title;
    }

    public String getSubtitle() {
        return subtitle;
    }
}
