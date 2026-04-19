package com.finsight.service;

import com.finsight.model.*;
import com.finsight.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Service for generating personalized dashboard cards based on user goals and knowledge level
 */
@Service
@Transactional
public class DashboardService {
    
    @Autowired
    private DashboardCardRepository dashboardCardRepository;
    
    @Autowired
    private UserGoalRepository userGoalRepository;
    
    @Autowired
    private CardInteractionRepository cardInteractionRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    /**
     * Generates personalized dashboard cards for a user
     */
    public List<PersonalizedCard> generatePersonalizedDashboard(Long userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("User not found"));
        
        // Get user's goals
        Set<UserGoal> userGoals = userGoalRepository.findByUserId(userId);
        Set<Long> goalIds = userGoals.stream()
            .map(ug -> ug.getGoal() != null ? ug.getGoal().getId() : null)
            .filter(Objects::nonNull)
            .collect(Collectors.toSet());
        
        // Get user's knowledge level
        KnowledgeLevel userLevel = user.getKnowledgeLevel();
        
        // Get relevant cards
        List<DashboardCard> relevantCards = getRelevantCards(goalIds, userLevel);
        
        // Personalize and rank cards
        List<PersonalizedCard> personalizedCards = personalizeCards(relevantCards, user, userGoals);
        
        // Sort by priority and relevance
        personalizedCards.sort((a, b) -> {
            int priorityCompare = Integer.compare(a.getPriority(), b.getPriority());
            if (priorityCompare != 0) return priorityCompare;
            return Integer.compare(b.getRelevanceScore(), a.getRelevanceScore());
        });
        
        // Return top cards (limit to reasonable number)
        return personalizedCards.stream().limit(8).collect(Collectors.toList());
    }
    
    /**
     * Gets relevant cards based on user goals and knowledge level
     */
    private List<DashboardCard> getRelevantCards(Set<Long> goalIds, KnowledgeLevel userLevel) {
        List<DashboardCard> cards = new ArrayList<>();
        
        // Cards matching user's goals and knowledge level
        cards.addAll(dashboardCardRepository.findByGoalsAndKnowledgeLevel(goalIds, userLevel));
        
        // Some cards from next level (for learning)
        if (userLevel != KnowledgeLevel.EXPERT) {
            KnowledgeLevel nextLevel = getNextLevel(userLevel);
            cards.addAll(dashboardCardRepository.findByGoalsAndKnowledgeLevel(goalIds, nextLevel));
        }
        
        // Some general educational cards
        cards.addAll(dashboardCardRepository.findByCardTypeAndKnowledgeLevel(CardType.EDUCATIONAL, userLevel));
        
        // Remove duplicates and filter active cards
        return cards.stream()
            .filter(card -> card.getIsActive())
            .distinct()
            .collect(Collectors.toList());
    }
    
    /**
     * Personalizes cards with user-specific content
     */
    private List<PersonalizedCard> personalizeCards(List<DashboardCard> cards, User user, Set<UserGoal> userGoals) {
        List<PersonalizedCard> personalizedCards = new ArrayList<>();
        
        for (DashboardCard card : cards) {
            PersonalizedCard personalized = new PersonalizedCard(card, user);
            
            // Calculate relevance score
            int relevanceScore = calculateRelevanceScore(card, user, userGoals);
            personalized.setRelevanceScore(relevanceScore);
            
            // Generate personalized content
            String personalizedContent = generatePersonalizedContent(card, user, userGoals);
            personalized.setPersonalizedContent(personalizedContent);
            
            // Set priority
            personalized.setPriority(card.getPriority());
            
            personalizedCards.add(personalized);
        }
        
        return personalizedCards;
    }
    
    /**
     * Calculates relevance score for a card based on user profile
     */
    private int calculateRelevanceScore(DashboardCard card, User user, Set<UserGoal> userGoals) {
        int score = 0;
        
        // Base score from card priority
        score += (10 - card.getPriority());
        
        // Bonus for matching user's primary goal
        Optional<UserGoal> primaryGoal = userGoals.stream().filter(ug -> ug.getIsPrimary()).findFirst();
        if (primaryGoal.isPresent() && card.getGoal() != null && 
            card.getGoal().getId().equals(primaryGoal.get().getGoal().getId())) {
            score += 20;
        }
        
        // Bonus for matching any user goal
        if (card.getGoal() != null && userGoals.stream()
            .anyMatch(ug -> ug.getGoal() != null && ug.getGoal().getId().equals(card.getGoal().getId()))) {
            score += 10;
        }
        
        // Bonus for appropriate difficulty level
        if (card.getRequiredKnowledgeLevel() == user.getKnowledgeLevel()) {
            score += 5;
        }
        
        // Bonus for educational content
        if (card.getCardType() == CardType.EDUCATIONAL || card.getCardType() == CardType.TIP) {
            score += 3;
        }
        
        return score;
    }
    
    /**
     * Generates personalized content for cards
     */
    private String generatePersonalizedContent(DashboardCard card, User user, Set<UserGoal> userGoals) {
        String content = card.getContentTemplate();
        
        // Replace placeholders with user-specific data
        content = content.replace("{userName}", user.getName());
        content = content.replace("{userBank}", user.getPrimaryBank() != null ? user.getPrimaryBank() : "your bank");
        content = content.replace("{userAge}", user.getAge() != null ? user.getAge().toString() : "your age");
        
        // Add goal-specific content
        if (card.getGoal() != null) {
            Optional<UserGoal> matchingGoal = userGoals.stream()
                .filter(ug -> ug.getGoal() != null && ug.getGoal().getId().equals(card.getGoal().getId()))
                .findFirst();
            
            if (matchingGoal.isPresent()) {
                UserGoal userGoal = matchingGoal.get();
                content = content.replace("{goalTarget}", userGoal.getTargetAmount() != null ? 
                    String.format("$%.2f", userGoal.getTargetAmount()) : "your goal");
                content = content.replace("{goalTimeline}", userGoal.getTargetDate() != null ? 
                    userGoal.getTargetDate().toString() : "your timeline");
            }
        }
        
        return content;
    }
    
    /**
     * Records user interaction with a card
     */
    public void recordCardInteraction(Long userId, Long cardId, InteractionType interactionType, 
                                     Integer timeSpentSeconds, Integer userRating, String feedback) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("User not found"));
        
        DashboardCard card = dashboardCardRepository.findById(cardId)
            .orElseThrow(() -> new RuntimeException("Card not found"));
        
        CardInteraction interaction = new CardInteraction(user, card, interactionType);
        interaction.setTimeSpentSeconds(timeSpentSeconds);
        interaction.setUserRating(userRating);
        interaction.setFeedbackText(feedback);
        
        cardInteractionRepository.save(interaction);
    }
    
    /**
     * Gets recommended cards based on user's interaction history
     */
    public List<PersonalizedCard> getRecommendedCards(Long userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("User not found"));
        
        // Get user's interaction history
        List<CardInteraction> interactions = cardInteractionRepository
            .findByUserIdOrderByInteractionDateDesc(userId);
        
        // Analyze preferences
        Map<CardType, Integer> typePreferences = new HashMap<>();
        Map<Long, Integer> topicPreferences = new HashMap<>();
        
        for (CardInteraction interaction : interactions) {
            CardType type = interaction.getDashboardCard().getCardType();
            typePreferences.put(type, typePreferences.getOrDefault(type, 0) + 1);
            
            if (interaction.getDashboardCard().getGoal() != null) {
                Long goalId = interaction.getDashboardCard().getGoal().getId();
                topicPreferences.put(goalId, topicPreferences.getOrDefault(goalId, 0) + 1);
            }
        }
        
        // Get cards matching preferences
        List<DashboardCard> recommendedCards = new ArrayList<>();
        
        // Add cards based on preferred types
        for (CardType preferredType : typePreferences.keySet()) {
            recommendedCards.addAll(dashboardCardRepository.findByCardTypeAndKnowledgeLevel(
                preferredType, user.getKnowledgeLevel()));
        }
        
        // Add cards based on preferred topics
        for (Long preferredGoalId : topicPreferences.keySet()) {
            FinancialGoal goal = new FinancialGoal();
            goal.setId(preferredGoalId);
            recommendedCards.addAll(dashboardCardRepository.findByGoalAndKnowledgeLevel(
                goal, user.getKnowledgeLevel()));
        }
        
        // Personalize and return
        Set<UserGoal> userGoals = userGoalRepository.findByUserId(userId);
        return personalizeCards(recommendedCards, user, userGoals).stream()
            .limit(5)
            .collect(Collectors.toList());
    }
    
    /**
     * Gets learning path cards for a specific goal
     */
    public List<PersonalizedCard> getLearningPathForGoal(Long userId, Long goalId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("User not found"));
        
        FinancialGoal goal = new FinancialGoal();
        goal.setId(goalId);
        
        // Get educational cards for the goal
        List<DashboardCard> learningCards = dashboardCardRepository.findByGoalAndCardType(
            goal, CardType.EDUCATIONAL);
        
        // Add quiz cards
        learningCards.addAll(dashboardCardRepository.findByGoalAndCardType(
            goal, CardType.QUIZ));
        
        // Add resource cards
        learningCards.addAll(dashboardCardRepository.findByGoalAndCardType(
            goal, CardType.RESOURCE));
        
        // Filter by knowledge level and personalize
        List<DashboardCard> filteredCards = learningCards.stream()
            .filter(card -> card.getRequiredKnowledgeLevel() == user.getKnowledgeLevel() ||
                           card.getRequiredKnowledgeLevel() == KnowledgeLevel.BEGINNER)
            .collect(Collectors.toList());
        
        Set<UserGoal> userGoals = userGoalRepository.findByUserId(userId);
        return personalizeCards(filteredCards, user, userGoals);
    }
    
    private KnowledgeLevel getNextLevel(KnowledgeLevel level) {
        switch (level) {
            case BEGINNER: return KnowledgeLevel.INTERMEDIATE;
            case INTERMEDIATE: return KnowledgeLevel.ADVANCED;
            case ADVANCED: return KnowledgeLevel.EXPERT;
            case EXPERT: return KnowledgeLevel.EXPERT;
            default: return KnowledgeLevel.BEGINNER;
        }
    }
}

// Supporting classes
class PersonalizedCard {
    private Long id;
    private String title;
    private String description;
    private CardType cardType;
    private String personalizedContent;
    private String learningResourceUrl;
    private String iconUrl;
    private int priority;
    private int relevanceScore;
    private KnowledgeLevel requiredKnowledgeLevel;
    
    public PersonalizedCard(DashboardCard card, User user) {
        this.id = card.getId();
        this.title = card.getTitle();
        this.description = card.getDescription();
        this.cardType = card.getCardType();
        this.learningResourceUrl = card.getLearningResourceUrl();
        this.iconUrl = card.getIconUrl();
        this.requiredKnowledgeLevel = card.getRequiredKnowledgeLevel();
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
    
    public String getPersonalizedContent() { return personalizedContent; }
    public void setPersonalizedContent(String personalizedContent) { this.personalizedContent = personalizedContent; }
    
    public String getLearningResourceUrl() { return learningResourceUrl; }
    public void setLearningResourceUrl(String learningResourceUrl) { this.learningResourceUrl = learningResourceUrl; }
    
    public String getIconUrl() { return iconUrl; }
    public void setIconUrl(String iconUrl) { this.iconUrl = iconUrl; }
    
    public int getPriority() { return priority; }
    public void setPriority(int priority) { this.priority = priority; }
    
    public int getRelevanceScore() { return relevanceScore; }
    public void setRelevanceScore(int relevanceScore) { this.relevanceScore = relevanceScore; }
    
    public KnowledgeLevel getRequiredKnowledgeLevel() { return requiredKnowledgeLevel; }
    public void setRequiredKnowledgeLevel(KnowledgeLevel requiredKnowledgeLevel) { this.requiredKnowledgeLevel = requiredKnowledgeLevel; }
}
