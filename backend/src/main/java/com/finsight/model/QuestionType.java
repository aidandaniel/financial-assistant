package com.finsight.model;

/**
 * Types of quiz questions available
 */
public enum QuestionType {
    MULTIPLE_CHOICE("Multiple Choice", "Select one correct answer from multiple options"),
    TRUE_FALSE("True/False", "Determine if statement is true or false"),
    SCENARIO("Scenario", "Analyze a financial situation and provide answer"),
    CALCULATION("Calculation", "Perform financial calculations"),
    DEFINITION("Definition", "Define financial terms and concepts"),
    COMPARISON("Comparison", "Compare different financial options"),
    RISK_ASSESSMENT("Risk Assessment", "Evaluate financial risk levels");
    
    private final String displayName;
    private final String description;
    
    QuestionType(String displayName, String description) {
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
