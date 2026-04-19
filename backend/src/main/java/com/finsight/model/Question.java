package com.finsight.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.List;

/**
 * Quiz question entity for financial knowledge assessment
 */
@Entity
@Table(name = "questions", indexes = {
    @Index(name = "idx_question_goal", columnList = "goal_id"),
    @Index(name = "idx_question_difficulty", columnList = "difficulty_level"),
    @Index(name = "idx_question_knowledge_level", columnList = "required_knowledge_level")
})
public class Question {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank
    @Column(columnDefinition = "TEXT")
    private String questionText;
    
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "question_type")
    private QuestionType questionType;
    
    @Column(name = "difficulty_level")
    private Integer difficultyLevel; // 1-5 scale
    
    @Enumerated(EnumType.STRING)
    @Column(name = "required_knowledge_level")
    private KnowledgeLevel requiredKnowledgeLevel;
    
    @Column(name = "explanation_text", columnDefinition = "TEXT")
    private String explanationText;
    
    @Column(name = "learning_resource_url")
    private String learningResourceUrl;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "goal_id")
    private FinancialGoal goal;
    
    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<AnswerOption> answerOptions;
    
    @Column(name = "created_at")
    private java.time.LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private java.time.LocalDateTime updatedAt;
    
    // Constructors
    public Question() {}
    
    public Question(String questionText, QuestionType questionType, 
                   KnowledgeLevel requiredKnowledgeLevel, FinancialGoal goal) {
        this.questionText = questionText;
        this.questionType = questionType;
        this.requiredKnowledgeLevel = requiredKnowledgeLevel;
        this.goal = goal;
        this.difficultyLevel = 3;
        this.createdAt = java.time.LocalDateTime.now();
        this.updatedAt = java.time.LocalDateTime.now();
    }
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getQuestionText() { return questionText; }
    public void setQuestionText(String questionText) { this.questionText = questionText; }
    
    public QuestionType getQuestionType() { return questionType; }
    public void setQuestionType(QuestionType questionType) { this.questionType = questionType; }
    
    public Integer getDifficultyLevel() { return difficultyLevel; }
    public void setDifficultyLevel(Integer difficultyLevel) { this.difficultyLevel = difficultyLevel; }
    
    public KnowledgeLevel getRequiredKnowledgeLevel() { return requiredKnowledgeLevel; }
    public void setRequiredKnowledgeLevel(KnowledgeLevel requiredKnowledgeLevel) { this.requiredKnowledgeLevel = requiredKnowledgeLevel; }
    
    public String getExplanationText() { return explanationText; }
    public void setExplanationText(String explanationText) { this.explanationText = explanationText; }
    
    public String getLearningResourceUrl() { return learningResourceUrl; }
    public void setLearningResourceUrl(String learningResourceUrl) { this.learningResourceUrl = learningResourceUrl; }
    
    public FinancialGoal getGoal() { return goal; }
    public void setGoal(FinancialGoal goal) { this.goal = goal; }
    
    public List<AnswerOption> getAnswerOptions() { return answerOptions; }
    public void setAnswerOptions(List<AnswerOption> answerOptions) { this.answerOptions = answerOptions; }
    
    public java.time.LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(java.time.LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    public java.time.LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(java.time.LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
    
    @PreUpdate
    public void preUpdate() {
        this.updatedAt = java.time.LocalDateTime.now();
    }
    
    @PrePersist
    public void prePersist() {
        java.time.LocalDateTime now = java.time.LocalDateTime.now();
        if (this.createdAt == null) {
            this.createdAt = now;
        }
        this.updatedAt = now;
    }
}
