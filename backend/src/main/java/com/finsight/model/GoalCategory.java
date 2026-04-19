package com.finsight.model;

/**
 * Categories of financial goals
 */
public enum GoalCategory {
    SAVING("Saving", "Building emergency funds and savings"),
    INVESTING("Investing", "Growing wealth through investments"),
    DEBT_MANAGEMENT("Debt Management", "Managing and reducing debt"),
    RETIREMENT("Retirement", "Planning for retirement"),
    HOME_OWNERSHIP("Home Ownership", "Buying and managing property"),
    EDUCATION("Education", "Educational expenses and planning"),
    BUDGETING("Budgeting", "Managing personal finances"),
    TAX_PLANNING("Tax Planning", "Optimizing tax strategies"),
    INSURANCE("Insurance", "Risk management and coverage"),
    ESTATE_PLANNING("Estate Planning", "Legacy and inheritance planning"),
    CUSTOM("Custom", "User-defined financial goals");
    
    private final String displayName;
    private final String description;
    
    GoalCategory(String displayName, String description) {
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
