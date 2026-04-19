package com.finsight.repository;

import com.finsight.model.Question;
import com.finsight.model.QuestionType;
import com.finsight.model.KnowledgeLevel;
import com.finsight.model.FinancialGoal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

/**
 * Repository for Question entity operations
 */
@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {
    
    List<Question> findByRequiredKnowledgeLevel(KnowledgeLevel level);
    
    List<Question> findByQuestionType(QuestionType type);
    
    List<Question>ByGoalId(Long goalId);
    
    @Query("SELECT q FROM Question q WHERE q.requiredKnowledgeLevel = :level AND q.difficultyLevel BETWEEN :minDifficulty AND :maxDifficulty")
    List<Question> findByKnowledgeLevelAndDifficultyRange(
        @Param("level") KnowledgeLevel level,
        @Param("minDifficulty") Integer minDifficulty,
        @Param("maxDifficulty") Integer maxDifficulty
    );
    
    @Query("SELECT q FROM Question q WHERE q.goal.id IN :goalIds AND q.requiredKnowledgeLevel = :level")
    List<Question> findByGoalsAndKnowledgeLevel(
        @Param("goalIds") Set<Long> goalIds,
        @Param("level") KnowledgeLevel level
    );
    
    @Query("SELECT q FROM Question q WHERE q.goal.id IN :goalIds AND q.difficultyLevel BETWEEN :minDifficulty AND :maxDifficulty")
    List<Question> findByGoalsAndDifficultyRange(
        @Param("goalIds") Set<Long> goalIds,
        @Param("minDifficulty") Integer minDifficulty,
        @Param("maxDifficulty") Integer maxDifficulty
    );
    
    @Query("SELECT DISTINCT q FROM Question q JOIN q.answerOptions ao WHERE q.requiredKnowledgeLevel = :level AND q.difficultyLevel <= :maxDifficulty ORDER BY RANDOM()")
    List<Question> findRandomQuestionsByLevelAndMaxDifficulty(
        @Param("level") KnowledgeLevel level,
        @Param("maxDifficulty") Integer maxDifficulty
    );
    
    @Query("SELECT COUNT(q) FROM Question q WHERE q.goal.id = :goalId AND q.requiredKnowledgeLevel = :level")
    long countByGoalAndKnowledgeLevel(
        @Param("goalId") Long goalId,
        @Param("level") KnowledgeLevel level
    );
    
    @Query("SELECT q FROM Question q WHERE q.questionText LIKE %:keyword% OR q.explanationText LIKE %:keyword%")
    List<Question> searchByKeyword(@Param("keyword") String keyword);
    
    @Query("SELECT q FROM Question q WHERE q.goal.id IN :goalIds AND q.questionType = :type")
    List<Question> findByGoalsAndQuestionType(
        @Param("goalIds") Set<Long> goalIds,
        @Param("type") QuestionType type
    );
}
