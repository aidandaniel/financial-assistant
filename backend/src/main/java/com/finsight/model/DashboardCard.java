package com.finsight.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import java.util.List;

/**
 * Dashboard card templates that can be dynamically selected based on user goals
 */
@Entity
@Table(name = "dashboard_cards", indexes = {
    @Index(name = "idx_card_goal", columnList = "goal_id"),
    @Index(name = "idx_card_type", columnList = "card_type"),
    @Index(name = "idx_card_difficulty", columnList = "difficulty_level")
})
public class DashboardCard {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank
    private String title;
    
    @Column(columnDefinition = "TEXT")
    private String description;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "card_type")
    private CardType cardType;
    
    @Column(name = "difficulty_level")
    private Integer difficultyLevel; // 1-5 scale
    
    @Enumerated(EnumType.STRING)
    @Column(name = "required_knowledge_level")
    private KnowledgeLevel requiredKnowledgeLevel;
    
    @Column(name = "content_template", columnDefinition = "TEXT")
    private String contentTemplate;
    
    @Column(name = "learning_resource_url")
    private String learningResourceUrl;
    
    @Column(name = "icon_url")
    private String iconUrl;
    
    @Column(name = "priority")
    private Integer priority = 5; // Lower number = higher priority
    
    @Column(name = "is_active")
    private Boolean isActive = true;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "goal_id")
    private FinancialGoal goal;
    
    @OneToMany(mappedBy = "dashboardCard", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<CardInteraction> interactions;
    
    // Constructors
    public DashboardCard() {}
    
    public DashboardCard(String title, String description, CardType cardType, 
                         KnowledgeLevel requiredLevel, FinancialGoal goal) {
        this.title = title;
        this.description = description;
        this.cardType = cardType;
        this.requiredKnowledgeLevel = requiredLevel;
        this.goal = goal;
        this.difficultyLevel = 3;
        this.isActive = true;
    }
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public CardType getCardType() { return cardType; }
    public void setCardType(CardType cardType) { this.cardType = cardType; }
    
    public Integer getDifficultyLevel() { return difficultyLevel; }
    public void setDifficultyLevel(Integer difficultyLevel) { this.difficultyLevel = difficultyLevel; }
    
    public KnowledgeLevel getRequiredKnowledgeLevel() { return requiredKnowledgeLevel; }
    public void setRequiredKnowledgeLevel(KnowledgeLevel requiredKnowledgeLevel) { this.requiredKnowledgeLevel = requiredKnowledgeLevel; }
    
    public String getContentTemplate() { return contentTemplate; }
    public void setContentTemplate(String contentTemplate) { this.contentTemplate = contentTemplate; }
    
    public String getLearningResourceUrl() { return learningResourceUrl; }
    public void setLearningResourceUrl(String learningResourceUrl) { this.learningResourceUrl = learningResourceUrl; }
    
    public String getIconUrl() { return iconUrl; }
    public void setIconUrl(String iconUrl) { this.iconUrl = iconUrl; }
    
    public Integer getPriority() { return priority; }
    public void setPriority(Integer priority) { this.priority = priority; }
    
    public Boolean getIsActive() { return isActive; }
    public void setIsActive(Boolean isActive) { this.isActive = isActive; }
    
    public FinancialGoal getGoal() { return goal; }
    public void setGoal(FinancialGoal goal) { this.goal = goal; }
    
    public List<CardInteraction> getInteractions() { return interactions; }
    public void setInteractions(List<CardInteraction> interactions) { this.interactions = interactions; }
}
