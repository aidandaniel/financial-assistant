package com.finsight.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Records of user quiz attempts for progress tracking
 */
@Entity
@Table(name = "quiz_attempts", indexes = {
    @Index(name = "idx_quiz_attempt_user", columnList = "user_id"),
    @Index(name = "idx_quiz_attempt_date", columnList = "attempt_date")
})
public class QuizAttempt {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    
    @Column(name = "score")
    private Integer score;
    
    @Column(name = "total_questions")
    private Integer totalQuestions;
    
    @Column(name = "percentage")
    private Double percentage;
    
    @Column(name = "attempt_date")
    private LocalDateTime attemptDate;
    
    @Column(name = "duration_minutes")
    private Integer durationMinutes;
    
    @Column(name = "knowledge_level_before")
    @Enumerated(EnumType.STRING)
    private KnowledgeLevel knowledgeLevelBefore;
    
    @Column(name = "knowledge_level_after")
    @Enumerated(EnumType.STRING)
    private KnowledgeLevel knowledgeLevelAfter;
    
    @OneToMany(mappedBy = "quizAttempt", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<QuizAnswer> answers;
    
    // Constructors
    public QuizAttempt() {}
    
    public QuizAttempt(User user, Integer score, Integer totalQuestions, Integer durationMinutes) {
        this.user = user;
        this.score = score;
        this.totalQuestions = totalQuestions;
        this.durationMinutes = durationMinutes;
        this.percentage = totalQuestions > 0 ? (double) score / totalQuestions * 100 : 0.0;
        this.attemptDate = LocalDateTime.now();
    }
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    
    public Integer getScore() { return score; }
    public void setScore(Integer score) { this.score = score; }
    
    public Integer getTotalQuestions() { return totalQuestions; }
    public void setTotalQuestions(Integer totalQuestions) { this.totalQuestions = totalQuestions; }
    
    public Double getPercentage() { return percentage; }
    public void setPercentage(Double percentage) { this.percentage = percentage; }
    
    public LocalDateTime getAttemptDate() { return attemptDate; }
    public void setAttemptDate(LocalDateTime attemptDate) { this.attemptDate = attemptDate; }
    
    public Integer getDurationMinutes() { return durationMinutes; }
    public void setDurationMinutes(Integer durationMinutes) { this.durationMinutes = durationMinutes; }
    
    public KnowledgeLevel getKnowledgeLevelBefore() { return knowledgeLevelBefore; }
    public void setKnowledgeLevelBefore(KnowledgeLevel knowledgeLevelBefore) { this.knowledgeLevelBefore = knowledgeLevelBefore; }
    
    public KnowledgeLevel getKnowledgeLevelAfter() { return knowledgeLevelAfter; }
    public void setKnowledgeLevelAfter(KnowledgeLevel knowledgeLevelAfter) { this.knowledgeLevelAfter = knowledgeLevelAfter; }
    
    public List<QuizAnswer> getAnswers() { return answers; }
    public void setAnswers(List<QuizAnswer> answers) { this.answers = answers; }
    
    @PrePersist
    public void prePersist() {
        if (this.attemptDate == null) {
            this.attemptDate = LocalDateTime.now();
        }
        if (this.percentage == null && this.totalQuestions != null && this.totalQuestions > 0) {
            this.percentage = (double) this.score / this.totalQuestions * 100;
        }
    }
}
