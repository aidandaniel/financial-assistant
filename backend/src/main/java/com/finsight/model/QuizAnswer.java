package com.finsight.model;

import jakarta.persistence.*;

/**
 * Individual answers within a quiz attempt
 */
@Entity
@Table(name = "quiz_answers")
public class QuizAnswer {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "quiz_attempt_id", nullable = false)
    private QuizAttempt quizAttempt;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id", nullable = false)
    private Question question;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "selected_answer_id")
    private AnswerOption selectedAnswer;
    
    @Column(name = "is_correct")
    private Boolean isCorrect;
    
    @Column(name = "time_to_answer_seconds")
    private Integer timeToAnswerSeconds;
    
    // Constructors
    public QuizAnswer() {}
    
    public QuizAnswer(QuizAttempt quizAttempt, Question question, AnswerOption selectedAnswer) {
        this.quizAttempt = quizAttempt;
        this.question = question;
        this.selectedAnswer = selectedAnswer;
        this.isCorrect = selectedAnswer != null && selectedAnswer.getIsCorrect();
    }
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public QuizAttempt getQuizAttempt() { return quizAttempt; }
    public void setQuizAttempt(QuizAttempt quizAttempt) { this.quizAttempt = quizAttempt; }
    
    public Question getQuestion() { return question; }
    public void setQuestion(Question question) { this.question = question; }
    
    public AnswerOption getSelectedAnswer() { return selectedAnswer; }
    public void setSelectedAnswer(AnswerOption selectedAnswer) { this.selectedAnswer = selectedAnswer; }
    
    public Boolean getIsCorrect() { return isCorrect; }
    public void setIsCorrect(Boolean isCorrect) { this.isCorrect = isCorrect; }
    
    public Integer getTimeToAnswerSeconds() { return timeToAnswerSeconds; }
    public void setTimeToAnswerSeconds(Integer timeToAnswerSeconds) { this.timeToAnswerSeconds = timeToAnswerSeconds; }
    
    @PrePersist
    public void prePersist() {
        if (this.isCorrect == null && this.selectedAnswer != null) {
            this.isCorrect = this.selectedAnswer.getIsCorrect();
        }
    }
}
