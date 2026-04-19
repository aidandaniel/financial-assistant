package com.finsight.model;

/**
 * Types of interactions users can have with dashboard cards
 */
public enum InteractionType {
    VIEW("View", "User viewed the card"),
    CLICK("Click", "User clicked on the card"),
    EXPAND("Expand", "User expanded the card to see more details"),
    COMPLETE("Complete", "User completed the card's action"),
    DISMISS("Dismiss", "User dismissed or removed the card"),
    SHARE("Share", "User shared the card content"),
    BOOKMARK("Bookmark", "User bookmarked the card for later"),
    RATE("Rate", "User rated the card"),
    FEEDBACK("Feedback", "User provided feedback on the card"),
    LEARN_MORE("Learn More", "User clicked to learn more about the topic"),
    CALCULATE("Calculate", "User used a calculator card"),
    QUIZ_ATTEMPT("Quiz Attempt", "User attempted a quiz card"),
    MILESTONE_ACHIEVED("Milestone Achieved", "User achieved a milestone");
    
    private final String displayName;
    private final String description;
    
    InteractionType(String displayName, String description) {
        this.displayName = displayName;
        this.description = description;
    }
    
    public String getDisplayName() {
        return displayName;
    }
    
    public String getDescription() {
        return description;
    }
}
