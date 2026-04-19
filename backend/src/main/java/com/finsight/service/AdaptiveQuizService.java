package com.finsight.service;

import com.finsight.model.*;
import com.finsight.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Service for adaptive quiz system that generates personalized questions
 * based on user's knowledge level, goals, and learning progress
 */
@Service
@Transactional
public class AdaptiveQuizService {
    
    @Autowired
    private QuestionRepository questionRepository;
    
    @Autowired
    private QuizAttemptRepository quizAttemptRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private UserGoalRepository userGoalRepository;
    
    /**
     * Generates a personalized quiz for the user
     */
    public QuizSession generatePersonalizedQuiz(Long userId, int questionCount) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("User not found"));
        
        Set<UserGoal> userGoals = userGoalRepository.findByUserId(userId);
        Set<Long> goalIds = userGoals.stream()
            .map(ug -> ug.getGoal() != null ? ug.getGoal().getId() : null)
            .filter(Objects::nonNull)
            .collect(Collectors.toSet());
        
        KnowledgeLevel currentLevel = user.getKnowledgeLevel();
        
        // Get questions based on user's level and goals
        List<Question> questions = selectAdaptiveQuestions(goalIds, currentLevel, questionCount);
        
        return new QuizSession(questions, user, currentLevel);
    }
    
    /**
     * Selects adaptive questions based on user's profile
     */
    private List<Question> selectAdaptiveQuestions(Set<Long> goalIds, KnowledgeLevel level, int count) {
        List<Question> allQuestions = new ArrayList<>();
        
        // 60% questions at current level
        List<Question> currentLevelQuestions = questionRepository.findByGoalsAndKnowledgeLevel(
            goalIds, level);
        allQuestions.addAll(currentLevelQuestions);
        
        // 30% questions slightly above current level (for learning)
        if (level != KnowledgeLevel.EXPERT) {
            KnowledgeLevel nextLevel = getNextLevel(level);
            List<Question> nextLevelQuestions = questionRepository.findByGoalsAndKnowledgeLevel(
                goalIds, nextLevel);
            allQuestions.addAll(nextLevelQuestions);
        }
        
        // 10% questions below current level (for reinforcement)
        if (level != KnowledgeLevel.BEGINNER) {
            KnowledgeLevel previousLevel = getPreviousLevel(level);
            List<Question> previousLevelQuestions = questionRepository.findByGoalsAndKnowledgeLevel(
                goalIds, previousLevel);
            allQuestions.addAll(previousLevelQuestions);
        }
        
        // Shuffle and select required number
        Collections.shuffle(allQuestions);
        return allQuestions.stream()
            .limit(count)
            .collect(Collectors.toList());
    }
    
    /**
     * Processes quiz submission and updates user's knowledge level
     */
    public QuizResult processQuizSubmission(Long userId, List<QuizAnswerDTO> answers) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("User not found"));
        
        KnowledgeLevel levelBefore = user.getKnowledgeLevel();
        
        // Create quiz attempt
        QuizAttempt attempt = new QuizAttempt();
        attempt.setUser(user);
        attempt.setKnowledgeLevelBefore(levelBefore);
        
        int correctCount = 0;
        List<QuizAnswer> quizAnswers = new ArrayList<>();
        
        for (QuizAnswerDTO answerDTO : answers) {
            Question question = questionRepository.findById(answerDTO.getQuestionId())
                .orElseThrow(() -> new RuntimeException("Question not found"));
            
            AnswerOption selectedAnswer = question.getAnswerOptions().stream()
                .filter(ao -> ao.getId().equals(answerDTO.getSelectedAnswerId()))
                .findFirst()
                .orElse(null);
            
            QuizAnswer quizAnswer = new QuizAnswer(attempt, question, selectedAnswer);
            quizAnswer.setTimeToAnswerSeconds(answerDTO.getTimeToAnswerSeconds());
            quizAnswers.add(quizAnswer);
            
            if (selectedAnswer != null && selectedAnswer.getIsCorrect()) {
                correctCount++;
            }
        }
        
        attempt.setAnswers(quizAnswers);
        attempt.setScore(correctCount);
        attempt.setTotalQuestions(answers.size());
        attempt.setPercentage((double) correctCount / answers.size() * 100);
        
        // Update knowledge level based on performance
        KnowledgeLevel newLevel = calculateNewKnowledgeLevel(levelBefore, attempt.getPercentage());
        attempt.setKnowledgeLevelAfter(newLevel);
        user.setKnowledgeLevel(newLevel);
        
        // Save results
        quizAttemptRepository.save(attempt);
        userRepository.save(user);
        
        return new QuizResult(attempt, levelBefore, newLevel);
    }
    
    /**
     * Calculates new knowledge level based on quiz performance
     */
    private KnowledgeLevel calculateNewKnowledgeLevel(KnowledgeLevel currentLevel, double percentage) {
        if (percentage >= 90 && currentLevel != KnowledgeLevel.EXPERT) {
            return getNextLevel(currentLevel);
        } else if (percentage < 60 && currentLevel != KnowledgeLevel.BEGINNER) {
            return getPreviousLevel(currentLevel);
        }
        return currentLevel;
    }
    
    /**
     * Gets next knowledge level
     */
    private KnowledgeLevel getNextLevel(KnowledgeLevel level) {
        switch (level) {
            case BEGINNER: return KnowledgeLevel.INTERMEDIATE;
            case INTERMEDIATE: return KnowledgeLevel.ADVANCED;
            case ADVANCED: return KnowledgeLevel.EXPERT;
            case EXPERT: return KnowledgeLevel.EXPERT;
            default: return KnowledgeLevel.BEGINNER;
        }
    }
    
    /**
     * Gets previous knowledge level
     */
    private KnowledgeLevel getPreviousLevel(KnowledgeLevel level) {
        switch (level) {
            case EXPERT: return KnowledgeLevel.ADVANCED;
            case ADVANCED: return KnowledgeLevel.INTERMEDIATE;
            case INTERMEDIATE: return KnowledgeLevel.BEGINNER;
            case BEGINNER: return KnowledgeLevel.BEGINNER;
            default: return KnowledgeLevel.BEGINNER;
        }
    }
    
    /**
     * Gets user's quiz history and progress
     */
    public List<QuizAttempt> getUserQuizHistory(Long userId) {
        return quizAttemptRepository.findByUserIdOrderByAttemptDateDesc(userId);
    }
    
    /**
     * Gets learning recommendations based on quiz performance
     */
    public List<LearningRecommendation> getLearningRecommendations(Long userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("User not found"));
        
        List<QuizAttempt> recentAttempts = quizAttemptRepository
            .findTop5ByUserIdOrderByAttemptDateDesc(userId);
        
        List<LearningRecommendation> recommendations = new ArrayList<>();
        
        // Analyze recent performance and generate recommendations
        for (QuizAttempt attempt : recentAttempts) {
            for (QuizAnswer answer : attempt.getAnswers()) {
                if (!answer.getIsCorrect()) {
                    Question question = answer.getQuestion();
                    LearningRecommendation recommendation = new LearningRecommendation(
                        question.getGoal(),
                        question.getLearningResourceUrl(),
                        question.getExplanationText(),
                        question.getRequiredKnowledgeLevel()
                    );
                    recommendations.add(recommendation);
                }
            }
        }
        
        return recommendations.stream()
            .distinct()
            .collect(Collectors.toList());
    }
}

// Supporting DTOs and classes
class QuizSession {
    private List<Question> questions;
    private User user;
    private KnowledgeLevel currentLevel;
    
    public QuizSession(List<Question> questions, User user, KnowledgeLevel currentLevel) {
        this.questions = questions;
        this.user = user;
        this.currentLevel = currentLevel;
    }
    
    // Getters
    public List<Question> getQuestions() { return questions; }
    public User getUser() { return user; }
    public KnowledgeLevel getCurrentLevel() { return currentLevel; }
}

class QuizAnswerDTO {
    private Long questionId;
    private Long selectedAnswerId;
    private Integer timeToAnswerSeconds;
    
    // Getters and Setters
    public Long getQuestionId() { return questionId; }
    public void setQuestionId(Long questionId) { this.questionId = questionId; }
    
    public Long getSelectedAnswerId() { return selectedAnswerId; }
    public void setSelectedAnswerId(Long selectedAnswerId) { this.selectedAnswerId = selectedAnswerId; }
    
    public Integer getTimeToAnswerSeconds() { return timeToAnswerSeconds; }
    public void setTimeToAnswerSeconds(Integer timeToAnswerSeconds) { this.timeToAnswerSeconds = timeToAnswerSeconds; }
}

class QuizResult {
    private QuizAttempt attempt;
    private KnowledgeLevel levelBefore;
    private KnowledgeLevel levelAfter;
    
    public QuizResult(QuizAttempt attempt, KnowledgeLevel levelBefore, KnowledgeLevel levelAfter) {
        this.attempt = attempt;
        this.levelBefore = levelBefore;
        this.levelAfter = levelAfter;
    }
    
    // Getters
    public QuizAttempt getAttempt() { return attempt; }
    public KnowledgeLevel getLevelBefore() { return levelBefore; }
    public KnowledgeLevel getLevelAfter() { return levelAfter; }
}

class LearningRecommendation {
    private FinancialGoal goal;
    private String resourceUrl;
    private String explanation;
    private KnowledgeLevel targetLevel;
    
    public LearningRecommendation(FinancialGoal goal, String resourceUrl, String explanation, KnowledgeLevel targetLevel) {
        this.goal = goal;
        this.resourceUrl = resourceUrl;
        this.explanation = explanation;
        this.targetLevel = targetLevel;
    }
    
    // Getters
    public FinancialGoal getGoal() { return goal; }
    public String getResourceUrl() { return resourceUrl; }
    public String getExplanation() { return explanation; }
    public KnowledgeLevel getTargetLevel() { return targetLevel; }
}
