package com.finsight.model;

/**
 * Types of dashboard cards available for different educational purposes
 */
public enum CardType {
    EDUCATIONAL("Educational", "Teaches financial concepts and terms"),
    CALCULATOR("Calculator", "Interactive financial calculators"),
    PROGRESS_TRACKER("Progress Tracker", "Shows learning and goal progress"),
    RECOMMENDATION("Recommendation", "Personalized suggestions and tips"),
    QUIZ("Quiz", "Quick knowledge checks and assessments"),
    RESOURCE("Resource", "Links to learning materials and tools"),
    MILESTONE("Milestone", "Achievement and milestone tracking"),
    INSIGHT("Insight", "Data-driven insights and analytics"),
    ACTION_ITEM("Action Item", "Specific tasks and next steps"),
    COMPARISON("Comparison", "Side-by-side financial comparisons"),
    DEFINITION("Definition", "Key financial term explanations"),
    SCENARIO("Scenario", "Real-world financial situations"),
    TIP("Tip", "Quick financial tips and best practices"),
    WARNING("Warning", "Common financial pitfalls to avoid"),
    OPPORTUNITY("Opportunity", "Financial opportunities and strategies");
    
    private final String displayName;
    private final String description;
    
    CardType(String displayName, String description) {
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
