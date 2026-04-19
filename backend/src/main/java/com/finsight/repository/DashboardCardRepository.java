package com.finsight.repository;

import com.finsight.model.DashboardCard;
import com.finsight.model.CardType;
import com.finsight.model.KnowledgeLevel;
import com.finsight.model.FinancialGoal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

/**
 * Repository for DashboardCard entity operations
 */
@Repository
public interface DashboardCardRepository extends JpaRepository<DashboardCard, Long> {
    
    List<DashboardCard> findByCardType(CardType cardType);
    
    List<DashboardCard> findByRequiredKnowledgeLevel(KnowledgeLevel level);
    
    List<DashboardCard> findByGoalId(Long goalId);
    
    List<DashboardCard> findByCardTypeAndKnowledgeLevel(CardType cardType, KnowledgeLevel level);
    
    List<DashboardCard> findByGoalAndKnowledgeLevel(FinancialGoal goal, KnowledgeLevel level);
    
    List<DashboardCard> findByGoalAndCardType(FinancialGoal goal, CardType cardType);
    
    @Query("SELECT dc FROM DashboardCard dc WHERE dc.goal.id IN :goalIds AND dc.requiredKnowledgeLevel = :level")
    List<DashboardCard> findByGoalsAndKnowledgeLevel(@Param("goalIds") Set<Long> goalIds, @Param("level") KnowledgeLevel level);
    
    @Query("SELECT dc FROM DashboardCard dc WHERE dc.isActive = true ORDER BY dc.priority ASC")
    List<DashboardCard> findActiveCardsOrderByPriority();
    
    @Query("SELECT dc FROM DashboardCard dc WHERE dc.isActive = true AND dc.cardType = :type ORDER BY dc.priority ASC")
    List<DashboardCard> findActiveCardsByTypeOrderByPriority(@Param("type") CardType type);
    
    @Query("SELECT dc FROM DashboardCard dc WHERE dc.isActive = true AND dc.requiredKnowledgeLevel = :level ORDER BY dc.priority ASC")
    List<DashboardCard> findActiveCardsByKnowledgeLevelOrderByPriority(@Param("level") KnowledgeLevel level);
    
    @Query("SELECT dc FROM DashboardCard dc WHERE dc.isActive = true AND dc.goal.id = :goalId ORDER BY dc.priority ASC")
    List<DashboardCard> findActiveCardsByGoalOrderByPriority(@Param("goalId") Long goalId);
    
    @Query("SELECT COUNT(dc) FROM DashboardCard dc WHERE dc.cardType = :type")
    long countByCardType(@Param("type") CardType type);
    
    @Query("SELECT COUNT(dc) FROM DashboardCard dc WHERE dc.goal.id = :goalId")
    long countByGoalId(@Param("goalId") Long goalId);
    
    @Query("SELECT DISTINCT dc.cardType FROM DashboardCard dc WHERE dc.isActive = true")
    List<CardType> findAllActiveCardTypes();
    
    @Query("SELECT dc FROM DashboardCard dc WHERE dc.title LIKE %:keyword% OR dc.description LIKE %:keyword%")
    List<DashboardCard> searchByKeyword(@Param("keyword") String keyword);
    
    @Query("SELECT dc FROM DashboardCard dc WHERE dc.difficultyLevel BETWEEN :minDifficulty AND :maxDifficulty AND dc.isActive = true")
    List<DashboardCard> findByDifficultyRange(@Param("minDifficulty") Integer minDifficulty, @Param("maxDifficulty") Integer maxDifficulty);
}
