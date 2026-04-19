package com.finsight.model;

/**
 * Enumeration representing user's financial knowledge level
 */
public enum KnowledgeLevel {
    BEGINNER("Beginner", "Just starting financial journey"),
    INTERMEDIATE("Intermediate", "Basic understanding of financial concepts"),
    ADVANCED("Advanced", "Good financial literacy"),
    EXPERT("Expert", "Deep financial knowledge and experience");
    
    private final String displayName;
    private final String description;
    
    KnowledgeLevel(String displayName, String description) {
        this.displayName = displayName;
        this.description = description;
    }
    
    public String getDisplayName() {
        return displayName;
    }
    
    public String getDescription() {
        return description;
    }
    
    public static KnowledgeLevel fromString(String level) {
        try {
            return KnowledgeLevel.valueOf(level.toUpperCase());
        } catch (IllegalArgumentException e) {
            return BEGINNER;
        }
    }
}
