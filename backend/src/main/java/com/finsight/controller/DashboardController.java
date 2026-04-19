package com.finsight.controller;

import com.finsight.service.DashboardService;
import com.finsight.model.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller for personalized dashboard operations
 */
@RestController
@RequestMapping("/api/dashboard")
@Tag(name = "Dashboard Management", description = "Operations for personalized dashboard cards")
public class DashboardController {
    
    @Autowired
    private DashboardService dashboardService;
    
    @GetMapping("/personalized/{userId}")
    @Operation(summary = "Get personalized dashboard", description = "Generates personalized dashboard cards based on user goals and knowledge level")
    public ResponseEntity<?> getPersonalizedDashboard(@PathVariable Long userId) {
        try {
            List<PersonalizedCard> cards = dashboardService.generatePersonalizedDashboard(userId);
            return ResponseEntity.ok(new DashboardResponse(cards));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error generating dashboard: " + e.getMessage());
        }
    }
    
    @GetMapping("/recommended/{userId}")
    @Operation(summary = "Get recommended cards", description = "Provides card recommendations based on user interaction history")
    public ResponseEntity<?> getRecommendedCards(@PathVariable Long userId) {
        try {
            List<PersonalizedCard> cards = dashboardService.getRecommendedCards(userId);
            return ResponseEntity.ok(new DashboardResponse(cards));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error getting recommendations: " + e.getMessage());
        }
    }
    
    @GetMapping("/learning-path/{userId}/{goalId}")
    @Operation(summary = "Get learning path for goal", description = "Provides structured learning path cards for a specific financial goal")
    public ResponseEntity<?> getLearningPath(@PathVariable Long userId, @PathVariable Long goalId) {
        try {
            List<PersonalizedCard> cards = dashboardService.getLearningPathForGoal(userId, goalId);
            return ResponseEntity.ok(new DashboardResponse(cards));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error getting learning path: " + e.getMessage());
        }
    }
    
    @PostMapping("/interaction")
    @Operation(summary = "Record card interaction", description = "Records user interaction with dashboard cards for personalization")
    public ResponseEntity<?> recordInteraction(@RequestBody CardInteractionDTO interactionDTO) {
        try {
            dashboardService.recordCardInteraction(
                interactionDTO.getUserId(),
                interactionDTO.getCardId(),
                interactionDTO.getInteractionType(),
                interactionDTO.getTimeSpentSeconds(),
                interactionDTO.getUserRating(),
                interactionDTO.getFeedback()
            );
            return ResponseEntity.ok("Interaction recorded successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error recording interaction: " + e.getMessage());
        }
    }
    
    @GetMapping("/stats/{userId}")
    @Operation(summary = "Get dashboard statistics", description = "Provides user's dashboard interaction statistics")
    public ResponseEntity<?> getDashboardStats(@PathVariable Long userId) {
        try {
            // This would be implemented with additional statistics service
            DashboardStats stats = new DashboardStats();
            stats.setTotalCardsViewed(0); // Would be calculated from interactions
            stats.setAverageTimePerCard(0); // Would be calculated from interactions
            stats.setFavoriteCardType("Educational"); // Would be calculated from preferences
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error getting dashboard stats: " + e.getMessage());
        }
    }
}

// DTOs
class DashboardResponse {
    private List<PersonalizedCard> cards;
    private String message;
    
    public DashboardResponse(List<PersonalizedCard> cards) {
        this.cards = cards;
        this.message = "Dashboard generated successfully";
    }
    
    // Getters
    public List<PersonalizedCard> getCards() { return cards; }
    public void setCards(List<PersonalizedCard> cards) { this.cards = cards; }
    
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
}

class CardInteractionDTO {
    private Long userId;
    private Long cardId;
    private InteractionType interactionType;
    private Integer timeSpentSeconds;
    private Integer userRating;
    private String feedback;
    
    // Getters and Setters
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    
    public Long getCardId() { return cardId; }
    public void setCardId(Long cardId) { this.cardId = cardId; }
    
    public InteractionType getInteractionType() { return interactionType; }
    public void setInteractionType(InteractionType interactionType) { this.interactionType = interactionType; }
    
    public Integer getTimeSpentSeconds() { return timeSpentSeconds; }
    public void setTimeSpentSeconds(Integer timeSpentSeconds) { this.timeSpentSeconds = timeSpentSeconds; }
    
    public Integer getUserRating() { return userRating; }
    public void setUserRating(Integer userRating) { this.userRating = userRating; }
    
    public String getFeedback() { return feedback; }
    public void setFeedback(String feedback) { this.feedback = feedback; }
}

class DashboardStats {
    private int totalCardsViewed;
    private double averageTimePerCard;
    private String favoriteCardType;
    private int totalInteractions;
    private List<String> topTopics;
    
    // Getters and Setters
    public int getTotalCardsViewed() { return totalCardsViewed; }
    public void setTotalCardsViewed(int totalCardsViewed) { this.totalCardsViewed = totalCardsViewed; }
    
    public double getAverageTimePerCard() { return averageTimePerCard; }
    public void setAverageTimePerCard(double averageTimePerCard) { this.averageTimePerCard = averageTimePerCard; }
    
    public String getFavoriteCardType() { return favoriteCardType; }
    public void setFavoriteCardType(String favoriteCardType) { this.favoriteCardType = favoriteCardType; }
    
    public int getTotalInteractions() { return totalInteractions; }
    public void setTotalInteractions(int totalInteractions) { this.totalInteractions = totalInteractions; }
    
    public List<String> getTopTopics() { return topTopics; }
    public void setTopTopics(List<String> topTopics) { this.topTopics = topTopics; }
}
