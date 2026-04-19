package com.finsight.repository;

import com.finsight.model.CardInteraction;
import com.finsight.model.InteractionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Repository for CardInteraction entity operations
 */
@Repository
public interface CardInteractionRepository extends JpaRepository<CardInteraction, Long> {
    
    List<CardInteraction> findByUserIdOrderByInteractionDateDesc(Long userId);
    
    List<CardInteraction> findByUserIdAndInteractionTypeOrderByInteractionDateDesc(Long userId, InteractionType interactionType);
    
    List<CardInteraction> findByDashboardCardIdOrderByInteractionDateDesc(Long cardId);
    
    @Query("SELECT ci FROM CardInteraction ci WHERE ci.user.id = :userId AND ci.interactionDate >= :date")
    List<CardInteraction> findByUserIdSinceDate(@Param("userId") Long userId, @Param("date") LocalDateTime date);
    
    @Query("SELECT COUNT(ci) FROM CardInteraction ci WHERE ci.user.id = :userId AND ci.interactionType = :type")
    long countByUserIdAndInteractionType(@Param("userId") Long userId, @Param("type") InteractionType type);
    
    @Query("SELECT AVG(ci.userRating) FROM CardInteraction ci WHERE ci.dashboardCard.id = :cardId AND ci.userRating IS NOT NULL")
    Double getAverageRatingForCard(@Param("cardId") Long cardId);
    
    @Query("SELECT ci.dashboardCard.id, COUNT(ci) FROM CardInteraction ci WHERE ci.user.id = :userId GROUP BY ci.dashboardCard.id ORDER BY COUNT(ci) DESC")
    List<Object[]> getMostInteractedCardsByUser(@Param("userId") Long userId);
    
    @Query("SELECT ci.interactionType, COUNT(ci) FROM CardInteraction ci WHERE ci.user.id = :userId GROUP BY ci.interactionType")
    List<Object[]> getInteractionStatsByUser(@Param("userId") Long userId);
    
    @Query("SELECT AVG(ci.timeSpentSeconds) FROM CardInteraction ci WHERE ci.dashboardCard.id = :cardId AND ci.timeSpentSeconds IS NOT NULL")
    Double getAverageTimeSpentForCard(@Param("cardId") Long cardId);
}
