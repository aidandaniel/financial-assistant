package com.finsight.repository;

import com.finsight.model.User;
import com.finsight.model.KnowledgeLevel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository for User entity operations
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
    Optional<User> findByEmail(String email);
    
    boolean existsByEmail(String email);
    
    @Query("SELECT u FROM User u WHERE u.knowledgeLevel = :level")
    List<User> findByKnowledgeLevel(@Param("level") KnowledgeLevel level);
    
    @Query("SELECT COUNT(u) FROM User u WHERE u.knowledgeLevel = :level")
    long countByKnowledgeLevel(@Param("level") KnowledgeLevel level);
    
    @Query("SELECT u FROM User u WHERE u.primaryBank = :bank")
    List<User> findByPrimaryBank(@Param("bank") String bank);
    
    @Query("SELECT COUNT(u) FROM User u WHERE u.primaryBank = :bank")
    long countByPrimaryBank(@Param("bank") String bank);
    
    @Query("SELECT u FROM User u WHERE u.age BETWEEN :minAge AND :maxAge")
    List<User> findByAgeRange(@Param("minAge") Integer minAge, @Param("maxAge") Integer maxAge);
    
    @Query("SELECT COUNT(u) FROM User u WHERE u.createdAt >= :date")
    long countUsersSince(@Param("date") java.time.LocalDateTime date);
    
    @Query("SELECT u FROM User u JOIN u.goals ug WHERE ug.goal.name = :goalName")
    List<User> findByGoal(@Param("goalName") String goalName);
}
