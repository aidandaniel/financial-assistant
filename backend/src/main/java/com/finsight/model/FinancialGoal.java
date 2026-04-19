package com.finsight.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import java.util.List;
import java.util.Set;

/**
 * Financial goal types available in the platform
 */
@Entity
@Table(name = "financial_goals")
public class FinancialGoal {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank
    @Column(unique = true, nullable = false)
    private String name;
    
    @Column(name = "display_name")
    private String displayName;
    
    @Column(columnDefinition = "TEXT")
    private String description;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "goal_category")
    private GoalCategory category;
    
    @Column(name = "difficulty_level")
    private Integer difficultyLevel; // 1-5 scale
    
    @Column(name = "estimated_timeline_months")
    private Integer estimatedTimelineMonths;
    
    @OneToMany(mappedBy = "goal", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Question> relatedQuestions;
    
    @OneToMany(mappedBy = "goal", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<UserGoal> userGoals;
    
    // Constructors
    public FinancialGoal() {}
    
    public FinancialGoal(String name, String displayName, String description, GoalCategory category) {
        this.name = name;
        this.displayName = displayName;
        this.description = description;
        this.category = category;
        this.difficultyLevel = 3;
        this.estimatedTimelineMonths = 12;
    }
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getDisplayName() { return displayName; }
    public void setDisplayName(String displayName) { this.displayName = displayName; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public GoalCategory getCategory() { return category; }
    public void setCategory(GoalCategory category) { this.category = category; }
    
    public Integer getDifficultyLevel() { return difficultyLevel; }
    public void setDifficultyLevel(Integer difficultyLevel) { this.difficultyLevel = difficultyLevel; }
    
    public Integer getEstimatedTimelineMonths() { return estimatedTimelineMonths; }
    public void setEstimatedTimelineMonths(Integer estimatedTimelineMonths) { this.estimatedTimelineMonths = estimatedTimelineMonths; }
    
    public List<Question> getRelatedQuestions() { return relatedQuestions; }
    public void setRelatedQuestions(List<Question> relatedQuestions) { this.relatedQuestions = relatedQuestions; }
    
    public Set<UserGoal> getUserGoals() { return userGoals; }
    public void setUserGoals(Set<UserGoal> userGoals) { this.userGoals = userGoals; }
}
