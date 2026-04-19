package com.finsight.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * Tracks user progress through learning materials and goals
 */
@Entity
@Table(name = "learning_progress", indexes = {
    @Index(name = "idx_learning_progress_user", columnList = "user_id"),
    @Index(name = "idx_learning_progress_goal", columnList = "goal_id")
})
public class LearningProgress {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "goal_id")
    private FinancialGoal goal;
    
    @Column(name = "learning_module")
    private String learningModule;
    
    @Column(name = "completion_percentage")
    private Double completionPercentage = 0.0;
    
    @Column(name = "time_spent_minutes")
    private Integer timeSpentMinutes = 0;
    
    @Column(name = "last_accessed")
    private LocalDateTime lastAccessed;
    
    @Column(name = "completed_at")
    private LocalDateTime completedAt;
    
    @Column(name = "current_level")
    private Integer currentLevel = 1;
    
    @Column(name = "total_levels")
    private Integer totalLevels = 10;
    
    @Column(name = "achievements")
    private String achievements; // JSON string of achievements
    
    // Constructors
    public LearningProgress() {}
    
    public LearningProgress(User user, FinancialGoal goal, String learningModule) {
        this.user = user;
        this.goal = goal;
        this.learningModule = learningModule;
        this.lastAccessed = LocalDateTime.now();
    }
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    
    public FinancialGoal getGoal() { return goal; }
    public void setGoal(FinancialGoal goal) { this.goal = goal; }
    
    public String getLearningModule() { return learningModule; }
    public void setLearningModule(String learningModule) { this.learningModule = learningModule; }
    
    public Double getCompletionPercentage() { return completionPercentage; }
    public void setCompletionPercentage(Double completionPercentage) { this.completionPercentage = completionPercentage; }
    
    public Integer getTimeSpentMinutes() { return timeSpentMinutes; }
    public void setTimeSpentMinutes(Integer timeSpentMinutes) { this.timeSpentMinutes = timeSpentMinutes; }
    
    public LocalDateTime getLastAccessed() { return lastAccessed; }
    public void setLastAccessed(LocalDateTime lastAccessed) { this.lastAccessed = lastAccessed; }
    
    public LocalDateTime getCompletedAt() { return completedAt; }
    public void setCompletedAt(LocalDateTime completedAt) { this.completedAt = completedAt; }
    
    public Integer getCurrentLevel() { return currentLevel; }
    public void setCurrentLevel(Integer currentLevel) { this.currentLevel = currentLevel; }
    
    public Integer getTotalLevels() { return totalLevels; }
    public void setTotalLevels(Integer totalLevels) { this.totalLevels = totalLevels; }
    
    public String getAchievements() { return achievements; }
    public void setAchievements(String achievements) { this.achievements = achievements; }
    
    @PrePersist
    public void prePersist() {
        if (this.lastAccessed == null) {
            this.lastAccessed = LocalDateTime.now();
        }
    }
    
    @PreUpdate
    public void preUpdate() {
        this.lastAccessed = LocalDateTime.now();
        if (this.completionPercentage != null && this.completionPercentage >= 100.0 && this.completedAt == null) {
            this.completedAt = LocalDateTime.now();
        }
    }
}
