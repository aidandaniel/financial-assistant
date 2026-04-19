package com.finsight.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * Tracks user interactions with dashboard cards for personalization
 */
@Entity
@Table(name = "card_interactions", indexes = {
    @Index(name = "idx_card_interaction_user", columnList = "user_id"),
    @Index(name = "idx_card_interaction_card", columnList = "dashboard_card_id"),
    @Index(name = "idx_card_interaction_date", columnList = "interaction_date")
})
public class CardInteraction {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dashboard_card_id", nullable = false)
    private DashboardCard dashboardCard;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "interaction_type")
    private InteractionType interactionType;
    
    @Column(name = "interaction_date")
    private LocalDateTime interactionDate;
    
    @Column(name = "time_spent_seconds")
    private Integer timeSpentSeconds;
    
    @Column(name = "user_rating")
    private Integer userRating; // 1-5 stars
    
    @Column(name = "feedback_text")
    private String feedbackText;
    
    // Constructors
    public CardInteraction() {}
    
    public CardInteraction(User user, DashboardCard card, InteractionType type) {
        this.user = user;
        this.dashboardCard = card;
        this.interactionType = type;
        this.interactionDate = LocalDateTime.now();
    }
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    
    public DashboardCard getDashboardCard() { return dashboardCard; }
    public void setDashboardCard(DashboardCard dashboardCard) { this.dashboardCard = dashboardCard; }
    
    public InteractionType getInteractionType() { return interactionType; }
    public void setInteractionType(InteractionType interactionType) { this.interactionType = interactionType; }
    
    public LocalDateTime getInteractionDate() { return interactionDate; }
    public void setInteractionDate(LocalDateTime interactionDate) { this.interactionDate = interactionDate; }
    
    public Integer getTimeSpentSeconds() { return timeSpentSeconds; }
    public void setTimeSpentSeconds(Integer timeSpentSeconds) { this.timeSpentSeconds = timeSpentSeconds; }
    
    public Integer getUserRating() { return userRating; }
    public void setUserRating(Integer userRating) { this.userRating = userRating; }
    
    public String getFeedbackText() { return feedbackText; }
    public void setFeedbackText(String feedbackText) { this.feedbackText = feedbackText; }
    
    @PrePersist
    public void prePersist() {
        if (this.interactionDate == null) {
            this.interactionDate = LocalDateTime.now();
        }
    }
}
