package com.infy.visitormanagement.enums;

public enum LogAction {
    VISITOR_REGISTERED("Visitor Registered", "success", "fa-user-plus"),
    VISIT_ADDED("Visit Added", "info", "fa-calendar-plus"),
    VISITOR_EDITED("Visitor Edited", "info", "fa-user-edit"),
    VISITOR_DELETED("Visitor Deleted", "danger", "fa-user-minus"),
    CHECKED_IN("Checked In", "success", "fa-sign-in-alt"),
    CHECKED_OUT("Checked Out", "info", "fa-sign-out-alt"),
    VISITOR_REJECTED("Visitor Rejected", "danger", "fa-user-times"),
    GATEPASS_GENERATED("Gatepass Generated", "success", "fa-id-card");
    private final String action;
    private final String type;
    private final String icon;

    LogAction(String action, String type, String icon) {
        this.action = action;
        this.type = type;
        this.icon = icon;
    }

    public String getAction() {
        return action;
    }

    public String getType() {
        return type;
    }

    public String getIcon() {
        return icon;
    }
}
