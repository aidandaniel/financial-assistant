package com.finsight.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

/**
 * Answer options for quiz questions
 */
@Entity
@Table(name = "answer_options")
public class AnswerOption {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank
    private String text;
    
    @Column(name = "is_correct")
    private Boolean isCorrect = false;
    
    @Column(name = "explanation_text")
    private String explanationText;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id")
    private Question question;
    
    // Constructors
    public AnswerOption() {}
    
    public AnswerOption(String text, Boolean isCorrect, Question question) {
        this.text = text;
        this.isCorrect = isCorrect;
        this.question = question;
    }
    
    public AnswerOption(String text, Boolean isCorrect, String explanationText, Question question) {
        this.text = text;
        this.isCorrect = isCorrect;
        this.explanationText = explanationText;
        this.question = question;
    }
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getText() { return text; }
    public void setText(String text) { this.text = text; }
    
    public Boolean getIsCorrect() { return isCorrect; }
    public void setIsCorrect(Boolean isCorrect) { this.isCorrect = isCorrect; }
    
    public String getExplanationText() { return explanationText; }
    public void setExplanationText(String explanationText) { this.explanationText = explanationText; }
    
    public Question getQuestion() { return question; }
    public void setQuestion(Question question) { this.question = question; }
}
