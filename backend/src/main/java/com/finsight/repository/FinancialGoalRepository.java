package com.finsight.repository;

import com.finsight.model.FinancialGoal;
import com.finsight.model.GoalCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

/**
 * Repository for FinancialGoal entity operations
 */
@Repository
public interface FinancialGoalRepository extends JpaRepository<FinancialGoal, Long> {
    
    FinancialGoal findByName(String name);
    
    List<FinancialGoal> findByCategory(GoalCategory category);
    
    List<FinancialGoal> findByDifficultyLevel(Integer difficultyLevel);
    
    @Query("SELECT g FROM FinancialGoal g WHERE g.difficultyLevel BETWEEN :minDifficulty AND :maxDifficulty")
    List<FinancialGoal> findByDifficultyRange(
        @Param("minDifficulty") Integer minDifficulty,
        @Param("maxDifficulty") Integer maxDifficulty
    );
    
    @Query("SELECT g FROM FinancialGoal g WHERE g.estimatedTimelineMonths <= :maxMonths")
    List<FinancialGoal> findByMaxTimeline(@Param("maxMonths") Integer maxMonths);
    
    @Query("SELECT g FROM FinancialGoal g WHERE g.name IN :names")
    List<FinancialGoal> findByNameIn(@Param("names") Set<String> names);
    
    @Query("SELECT DISTINCT g.category FROM FinancialGoal g")
    List<GoalCategory> findAllCategories();
    
    boolean existsByName(String name);
    
    @Query("SELECT COUNT(g) FROM FinancialGoal g WHERE g.category = :category")
    long countByCategory(@Param("category") GoalCategory category);
}
