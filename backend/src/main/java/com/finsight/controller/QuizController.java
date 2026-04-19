package com.finsight.controller;

import com.finsight.service.AdaptiveQuizService;
import com.finsight.model.*;
import com.finsight.repository.QuizAttemptRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller for quiz operations
 */
@RestController
@RequestMapping("/api/quiz")
@Tag(name = "Quiz Management", description = "Operations for adaptive quiz system")
public class QuizController {
    
    @Autowired
    private AdaptiveQuizService adaptiveQuizService;
    
    @Autowired
    private QuizAttemptRepository quizAttemptRepository;
    
    @PostMapping("/generate/{userId}")
    @Operation(summary = "Generate personalized quiz", description = "Creates a personalized quiz based on user's knowledge level and goals")
    public ResponseEntity<?> generateQuiz(@PathVariable Long userId, @RequestParam(defaultValue = "10") int questionCount) {
        try {
            var quizSession = adaptiveQuizService.generatePersonalizedQuiz(userId, questionCount);
            return ResponseEntity.ok(quizSession);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error generating quiz: " + e.getMessage());
        }
    }
    
    @PostMapping("/submit/{userId}")
    @Operation(summary = "Submit quiz answers", description = "Processes quiz submission and updates user's knowledge level")
    public ResponseEntity<?> submitQuiz(@PathVariable Long userId, @RequestBody List<QuizAnswerDTO> answers) {
        try {
            var result = adaptiveQuizService.processQuizSubmission(userId, answers);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error submitting quiz: " + e.getMessage());
        }
    }
    
    @GetMapping("/history/{userId}")
    @Operation(summary = "Get quiz history", description = "Retrieves user's quiz attempt history")
    public ResponseEntity<?> getQuizHistory(@PathVariable Long userId) {
        try {
            List<QuizAttempt> history = adaptiveQuizService.getUserQuizHistory(userId);
            return ResponseEntity.ok(history);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error retrieving quiz history: " + e.getMessage());
        }
    }
    
    @GetMapping("/recommendations/{userId}")
    @Operation(summary = "Get learning recommendations", description = "Provides personalized learning recommendations based on quiz performance")
    public ResponseEntity<?> getRecommendations(@PathVariable Long userId) {
        try {
            var recommendations = adaptiveQuizService.getLearningRecommendations(userId);
            return ResponseEntity.ok(recommendations);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error getting recommendations: " + e.getMessage());
        }
    }
    
    @GetMapping("/stats/{userId}")
    @Operation(summary = "Get quiz statistics", description = "Provides user's quiz performance statistics")
    public ResponseEntity<?> getQuizStats(@PathVariable Long userId) {
        try {
            List<QuizAttempt> attempts = quizAttemptRepository.findByUserIdOrderByAttemptDateDesc(userId);
            
            if (attempts.isEmpty()) {
                return ResponseEntity.ok(new QuizStats());
            }
            
            QuizStats stats = new QuizStats();
            stats.setTotalQuizzesTaken(attempts.size());
            stats.setAverageScore(attempts.stream()
                .mapToDouble(a -> a.getPercentage())
                .average()
                .orElse(0.0));
            stats.setHighestScore(attempts.stream()
                .mapToDouble(a -> a.getPercentage())
                .max()
                .orElse(0.0));
            stats.setLowestScore(attempts.stream()
                .mapToDouble(a -> a.getPercentage())
                .min()
                .orElse(0.0));
            stats.setMostRecentScore(attempts.get(0).getPercentage());
            stats.setKnowledgeLevelProgression(attempts.stream()
                .map(a -> a.getKnowledgeLevelAfter())
                .distinct()
                .count());
            
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error getting quiz stats: " + e.getMessage());
        }
    }
}

// Supporting DTOs
class QuizStats {
    private int totalQuizzesTaken;
    private double averageScore;
    private double highestScore;
    private double lowestScore;
    private double mostRecentScore;
    private int knowledgeLevelProgression;
    
    // Getters and Setters
    public int getTotalQuizzesTaken() { return totalQuizzesTaken; }
    public void setTotalQuizzesTaken(int totalQuizzesTaken) { this.totalQuizzesTaken = totalQuizzesTaken; }
    
    public double getAverageScore() { return averageScore; }
    public void setAverageScore(double averageScore) { this.averageScore = averageScore; }
    
    public double getHighestScore() { return highestScore; }
    public void setHighestScore(double highestScore) { this.highestScore = highestScore; }
    
    public double getLowestScore() { return lowestScore; }
    public void setLowestScore(double lowestScore) { this.lowestScore = lowestScore; }
    
    public double getMostRecentScore() { return mostRecentScore; }
    public void setMostRecentScore(double mostRecentScore) { this.mostRecentScore = mostRecentScore; }
    
    public int getKnowledgeLevelProgression() { return knowledgeLevelProgression; }
    public void setKnowledgeLevelProgression(int knowledgeLevelProgression) { this.knowledgeLevelProgression = knowledgeLevelProgression; }
}
