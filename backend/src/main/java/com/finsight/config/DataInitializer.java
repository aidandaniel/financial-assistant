package com.finsight.config;

import com.finsight.model.*;
import com.finsight.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

/**
 * Initializes the database with sample data for testing and development
 */
@Component
public class DataInitializer implements CommandLineRunner {
    
    @Autowired
    private FinancialGoalRepository financialGoalRepository;
    
    @Autowired
    private QuestionRepository questionRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    @Override
    public void run(String... args) throws Exception {
        initializeFinancialGoals();
        initializeQuestions();
        System.out.println("Database initialized with sample data");
    }
    
    private void initializeFinancialGoals() {
        // Check if data already exists
        if (financialGoalRepository.count() > 0) {
            return;
        }
        
        List<FinancialGoal> goals = Arrays.asList(
            // Savings Goals
            new FinancialGoal("saving", "Saving", "Building emergency funds and regular savings", GoalCategory.SAVING),
            new FinancialGoal("emergency-fund", "Emergency Fund", "Creating a safety net for unexpected expenses", GoalCategory.SAVING),
            
            // Investing Goals
            new FinancialGoal("investing", "Investing", "Growing wealth through various investment vehicles", GoalCategory.INVESTING),
            new FinancialGoal("growing", "Wealth Growth", "Accelerating wealth accumulation", GoalCategory.INVESTING),
            
            // Debt Management
            new FinancialGoal("debt-payoff", "Debt Payoff", "Managing and eliminating debt efficiently", GoalCategory.DEBT_MANAGEMENT),
            
            // Education
            new FinancialGoal("learning", "Financial Education", "Learning about personal finance concepts", GoalCategory.EDUCATION),
            
            // Retirement
            new FinancialGoal("retirement", "Retirement Planning", "Planning for long-term financial security", GoalCategory.RETIREMENT),
            
            // Home Ownership
            new FinancialGoal("home-ownership", "Home Ownership", "Buying and managing real estate", GoalCategory.HOME_OWNERSHIP),
            
            // Budgeting
            new FinancialGoal("budgeting", "Budget Management", "Creating and maintaining personal budgets", GoalCategory.BUDGETING)
        );
        
        // Set difficulty levels and timelines
        goals.get(0).setDifficultyLevel(2); // Saving - Easy
        goals.get(1).setDifficultyLevel(3); // Emergency Fund - Medium
        goals.get(2).setDifficultyLevel(4); // Investing - Hard
        goals.get(3).setDifficultyLevel(5); // Wealth Growth - Very Hard
        goals.get(4).setDifficultyLevel(3); // Debt Payoff - Medium
        goals.get(5).setDifficultyLevel(2); // Education - Easy
        goals.get(6).setDifficultyLevel(5); // Retirement - Very Hard
        goals.get(7).setDifficultyLevel(4); // Home Ownership - Hard
        goals.get(8).setDifficultyLevel(2); // Budgeting - Easy
        
        // Set estimated timelines (in months)
        goals.get(0).setEstimatedTimelineMonths(6);
        goals.get(1).setEstimatedTimelineMonths(12);
        goals.get(2).setEstimatedTimelineMonths(24);
        goals.get(3).setEstimatedTimelineMonths(36);
        goals.get(4).setEstimatedTimelineMonths(18);
        goals.get(5).setEstimatedTimelineMonths(3);
        goals.get(6).setEstimatedTimelineMonths(120);
        goals.get(7).setEstimatedTimelineMonths(60);
        goals.get(8).setEstimatedTimelineMonths(3);
        
        financialGoalRepository.saveAll(goals);
    }
    
    private void initializeQuestions() {
        // Check if questions already exist
        if (questionRepository.count() > 0) {
            return;
        }
        
        // Get goals for reference
        FinancialGoal savingGoal = financialGoalRepository.findByName("saving").orElse(null);
        FinancialGoal investingGoal = financialGoalRepository.findByName("investing").orElse(null);
        FinancialGoal debtGoal = financialGoalRepository.findByName("debt-payoff").orElse(null);
        FinancialGoal learningGoal = financialGoalRepository.findByName("learning").orElse(null);
        
        if (savingGoal != null) {
            createSavingQuestions(savingGoal);
        }
        if (investingGoal != null) {
            createInvestingQuestions(investingGoal);
        }
        if (debtGoal != null) {
            createDebtQuestions(debtGoal);
        }
        if (learningGoal != null) {
            createLearningQuestions(learningGoal);
        }
    }
    
    private void createSavingQuestions(FinancialGoal goal) {
        // Beginner level questions
        Question q1 = new Question(
            "What is the recommended size of an emergency fund?",
            QuestionType.MULTIPLE_CHOICE,
            KnowledgeLevel.BEGINNER,
            goal
        );
        q1.setDifficultyLevel(1);
        q1.setExplanationText("Financial experts recommend having 3-6 months of living expenses saved as an emergency fund.");
        
        AnswerOption a1 = new AnswerOption("1 month of expenses", false, q1);
        AnswerOption a2 = new AnswerOption("3-6 months of expenses", true, q1);
        AnswerOption a3 = new AnswerOption("12 months of expenses", false, q1);
        AnswerOption a4 = new AnswerOption("2 weeks of expenses", false, q1);
        
        q1.setAnswerOptions(Arrays.asList(a1, a2, a3, a4));
        questionRepository.save(q1);
        
        // Intermediate level question
        Question q2 = new Question(
            "Which type of savings account typically offers the highest interest rate?",
            QuestionType.MULTIPLE_CHOICE,
            KnowledgeLevel.INTERMEDIATE,
            goal
        );
        q2.setDifficultyLevel(3);
        q2.setExplanationText("High-yield savings accounts typically offer higher interest rates than traditional savings accounts.");
        
        AnswerOption b1 = new AnswerOption("Traditional savings account", false, q2);
        AnswerOption b2 = new AnswerOption("High-yield savings account", true, q2);
        AnswerOption b3 = new AnswerOption("Checking account", false, q2);
        AnswerOption b4 = new AnswerOption("Money market account", false, q2);
        
        q2.setAnswerOptions(Arrays.asList(b1, b2, b3, b4));
        questionRepository.save(q2);
    }
    
    private void createInvestingQuestions(FinancialGoal goal) {
        // Beginner level question
        Question q1 = new Question(
            "What is diversification in investing?",
            QuestionType.DEFINITION,
            KnowledgeLevel.BEGINNER,
            goal
        );
        q1.setDifficultyLevel(2);
        q1.setExplanationText("Diversification means spreading your investments across different assets to reduce risk.");
        
        AnswerOption a1 = new AnswerOption("Putting all money in one stock", false, q1);
        AnswerOption a2 = new AnswerOption("Spreading investments across different assets", true, q1);
        AnswerOption a3 = new AnswerOption("Only investing in safe assets", false, q1);
        AnswerOption a4 = new AnswerOption("Timing the market perfectly", false, q1);
        
        q1.setAnswerOptions(Arrays.asList(a1, a2, a3, a4));
        questionRepository.save(q1);
        
        // Advanced level question
        Question q2 = new Question(
            "What is the difference between a TFSA and RRSP?",
            QuestionType.COMPARISON,
            KnowledgeLevel.ADVANCED,
            goal
        );
        q2.setDifficultyLevel(4);
        q2.setExplanationText("TFSA contributions are made with after-tax dollars and withdrawals are tax-free. RRSP contributions are tax-deductible but withdrawals are taxed.");
        
        AnswerOption b1 = new AnswerOption("No difference - they're the same", false, q2);
        AnswerOption b2 = new AnswerOption("TFSA is for retirement only, RRSP is for any savings", false, q2);
        AnswerOption b3 = new AnswerOption("TFSA uses after-tax dollars and tax-free withdrawals, RRSP is tax-deductible but taxed on withdrawal", true, q2);
        AnswerOption b4 = new AnswerOption("RRSP is better for everyone", false, q2);
        
        q2.setAnswerOptions(Arrays.asList(b1, b2, b3, b4));
        questionRepository.save(q2);
    }
    
    private void createDebtQuestions(FinancialGoal goal) {
        Question q1 = new Question(
            "What is the avalanche method for debt repayment?",
            QuestionType.DEFINITION,
            KnowledgeLevel.INTERMEDIATE,
            goal
        );
        q1.setDifficultyLevel(3);
        q1.setExplanationText("The avalanche method involves paying off debts with the highest interest rates first to minimize total interest paid.");
        
        AnswerOption a1 = new AnswerOption("Paying off smallest debts first", false, q1);
        AnswerOption a2 = new AnswerOption("Paying off highest interest rate debts first", true, q1);
        AnswerOption a3 = new AnswerOption("Paying equal amounts to all debts", false, q1);
        AnswerOption a4 = new AnswerOption("Ignoring debt and hoping it goes away", false, q1);
        
        q1.setAnswerOptions(Arrays.asList(a1, a2, a3, a4));
        questionRepository.save(q1);
    }
    
    private void createLearningQuestions(FinancialGoal goal) {
        Question q1 = new Question(
            "True or False: You need to be an expert to start investing.",
            QuestionType.TRUE_FALSE,
            KnowledgeLevel.BEGINNER,
            goal
        );
        q1.setDifficultyLevel(1);
        q1.setExplanationText("False! You can start investing with small amounts and learn as you go. Many investment options are beginner-friendly.");
        
        AnswerOption a1 = new AnswerOption("True", false, q1);
        AnswerOption a2 = new AnswerOption("False", true, q1);
        
        q1.setAnswerOptions(Arrays.asList(a1, a2));
        questionRepository.save(q1);
    }
}
