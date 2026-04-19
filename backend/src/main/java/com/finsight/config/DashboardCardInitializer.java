package com.finsight.config;

import com.finsight.model.*;
import com.finsight.repository.DashboardCardRepository;
import com.finsight.repository.FinancialGoalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

/**
 * Initializes dashboard cards with educational content
 */
@Component
@Order(2) // Run after basic data initialization
public class DashboardCardInitializer implements CommandLineRunner {
    
    @Autowired
    private DashboardCardRepository dashboardCardRepository;
    
    @Autowired
    private FinancialGoalRepository financialGoalRepository;
    
    @Override
    public void run(String... args) throws Exception {
        initializeDashboardCards();
        System.out.println("Dashboard cards initialized with educational content");
    }
    
    private void initializeDashboardCards() {
        // Check if cards already exist
        if (dashboardCardRepository.count() > 0) {
            return;
        }
        
        // Get goals for reference
        FinancialGoal savingGoal = financialGoalRepository.findByName("saving").orElse(null);
        FinancialGoal emergencyFundGoal = financialGoalRepository.findByName("emergency-fund").orElse(null);
        FinancialGoal investingGoal = financialGoalRepository.findByName("investing").orElse(null);
        FinancialGoal debtGoal = financialGoalRepository.findByName("debt-payoff").orElse(null);
        FinancialGoal learningGoal = financialGoalRepository.findByName("learning").orElse(null);
        FinancialGoal budgetingGoal = financialGoalRepository.findByName("budgeting").orElse(null);
        
        // Create cards for each goal
        
        // SAVING GOAL CARDS
        if (savingGoal != null) {
            createSavingCards(savingGoal);
        }
        
        // EMERGENCY FUND CARDS
        if (emergencyFundGoal != null) {
            createEmergencyFundCards(emergencyFundGoal);
        }
        
        // INVESTING CARDS
        if (investingGoal != null) {
            createInvestingCards(investingGoal);
        }
        
        // DEBT PAYOFF CARDS
        if (debtGoal != null) {
            createDebtCards(debtGoal);
        }
        
        // LEARNING CARDS
        if (learningGoal != null) {
            createLearningCards(learningGoal);
        }
        
        // BUDGETING CARDS
        if (budgetingGoal != null) {
            createBudgetingCards(budgetingGoal);
        }
        
        // GENERAL EDUCATIONAL CARDS
        createGeneralEducationalCards();
    }
    
    private void createSavingCards(FinancialGoal goal) {
        // Beginner Level - What is Saving?
        DashboardCard card1 = new DashboardCard(
            "What is Financial Saving?",
            "Learn the basics of saving money and why it's important for your financial health.",
            CardType.EDUCATIONAL,
            KnowledgeLevel.BEGINNER,
            goal
        );
        card1.setContentTemplate(
            "Hi {userName}! Saving means setting aside money for future use. " +
            "Start with small amounts from {userBank} and build the habit. " +
            "Even saving $10/week adds up to $520 per year!"
        );
        card1.setPriority(1);
        card1.setDifficultyLevel(1);
        dashboardCardRepository.save(card1);
        
        // Savings Calculator
        DashboardCard card2 = new DashboardCard(
            "Savings Calculator",
            "Calculate how much you can save over time with different contribution amounts.",
            CardType.CALCULATOR,
            KnowledgeLevel.BEGINNER,
            goal
        );
        card2.setContentTemplate(
            "Enter your monthly savings amount to see how it grows over time. " +
            "Starting at {userAge}, you could build significant wealth by retirement!"
        );
        card2.setPriority(2);
        card2.setDifficultyLevel(2);
        dashboardCardRepository.save(card2);
        
        // Intermediate - High-Yield Savings
        DashboardCard card3 = new DashboardCard(
            "High-Yield Savings Accounts",
            "Discover how to earn more interest on your savings with high-yield accounts.",
            CardType.RECOMMENDATION,
            KnowledgeLevel.INTERMEDIATE,
            goal
        );
        card3.setContentTemplate(
            "{userBank} may offer high-yield savings options. " +
            "These accounts typically earn 10-20x more interest than traditional savings accounts."
        );
        card3.setLearningResourceUrl("/learn/high-yield-savings");
        card3.setPriority(3);
        card3.setDifficultyLevel(3);
        dashboardCardRepository.save(card3);
    }
    
    private void createEmergencyFundCards(FinancialGoal goal) {
        // Emergency Fund Basics
        DashboardCard card1 = new DashboardCard(
            "Emergency Fund 101",
            "Why you need an emergency fund and how to start building one.",
            CardType.EDUCATIONAL,
            KnowledgeLevel.BEGINNER,
            goal
        );
        card1.setContentTemplate(
            "An emergency fund is your financial safety net. " +
            "Aim for 3-6 months of expenses. Start with $500 and build from there!"
        );
        card1.setPriority(1);
        card1.setDifficultyLevel(1);
        dashboardCardRepository.save(card1);
        
        // Emergency Fund Calculator
        DashboardCard card2 = new DashboardCard(
            "Emergency Fund Calculator",
            "Calculate your ideal emergency fund amount based on monthly expenses.",
            CardType.CALCULATOR,
            KnowledgeLevel.BEGINNER,
            goal
        );
        card2.setContentTemplate(
            "Calculate your target emergency fund: " +
            "Monthly expenses × 3-6 months = Your safety net goal"
        );
        card2.setPriority(2);
        card2.setDifficultyLevel(2);
        dashboardCardRepository.save(card2);
        
        // Where to Keep Emergency Fund
        DashboardCard card3 = new DashboardCard(
            "Best Places for Emergency Funds",
            "Where to keep your emergency fund for easy access and good returns.",
            CardType.RECOMMENDATION,
            KnowledgeLevel.INTERMEDIATE,
            goal
        );
        card3.setContentTemplate(
            "Consider high-yield savings accounts or money market funds. " +
            "Keep it separate from daily spending but accessible when needed."
        );
        card3.setLearningResourceUrl("/learn/emergency-fund-placement");
        card3.setPriority(3);
        card3.setDifficultyLevel(3);
        dashboardCardRepository.save(card3);
    }
    
    private void createInvestingCards(FinancialGoal goal) {
        // Investing Basics
        DashboardCard card1 = new DashboardCard(
            "Investing Fundamentals",
            "Learn the basics of investing and how to get started.",
            CardType.EDUCATIONAL,
            KnowledgeLevel.BEGINNER,
            goal
        );
        card1.setContentTemplate(
            "Investing means putting money to work to generate returns. " +
            "Start with index funds and learn as you go. Time is your biggest advantage!"
        );
        card1.setPriority(1);
        card1.setDifficultyLevel(2);
        dashboardCardRepository.save(card1);
        
        // Risk Assessment
        DashboardCard card2 = new DashboardCard(
            "Understanding Investment Risk",
            "Learn about different types of investment risk and how to manage them.",
            CardType.EDUCATIONAL,
            KnowledgeLevel.INTERMEDIATE,
            goal
        );
        card2.setContentTemplate(
            "All investments have risk. The key is understanding your risk tolerance " +
            "and diversifying appropriately for your age and goals."
        );
        card2.setPriority(2);
        card2.setDifficultyLevel(3);
        dashboardCardRepository.save(card2);
        
        // TFSA vs RRSP
        DashboardCard card3 = new DashboardCard(
            "TFSA vs RRSP: Which is Better?",
            "Compare these popular Canadian investment accounts.",
            CardType.COMPARISON,
            KnowledgeLevel.ADVANCED,
            goal
        );
        card3.setContentTemplate(
            "TFSA: Tax-free growth and withdrawals. RRSP: Tax-deductible contributions but taxed withdrawals. " +
            "Many Canadians use both strategically."
        );
        card3.setLearningResourceUrl("/learn/tfsa-vs-rrsp");
        card3.setPriority(3);
        card3.setDifficultyLevel(4);
        dashboardCardRepository.save(card3);
    }
    
    private void createDebtCards(FinancialGoal goal) {
        // Understanding Debt
        DashboardCard card1 = new DashboardCard(
            "Good vs Bad Debt",
            "Learn the difference between productive and harmful debt.",
            CardType.EDUCATIONAL,
            KnowledgeLevel.BEGINNER,
            goal
        );
        card1.setContentTemplate(
            "Good debt builds wealth (mortgage, student loans). " +
            "Bad debt costs more than it's worth (high-interest credit cards)."
        );
        card1.setPriority(1);
        card1.setDifficultyLevel(1);
        dashboardCardRepository.save(card1);
        
        // Debt Payoff Strategies
        DashboardCard card2 = new DashboardCard(
            "Debt Payoff Methods",
            "Compare avalanche vs snowball debt payoff strategies.",
            CardType.COMPARISON,
            KnowledgeLevel.INTERMEDIATE,
            goal
        );
        card2.setContentTemplate(
            "Avalanche: Pay highest interest first (saves most money). " +
            "Snowball: Pay smallest balance first (psychological wins)."
        );
        card2.setLearningResourceUrl("/learn/debt-payoff-strategies");
        card2.setPriority(2);
        card2.setDifficultyLevel(3);
        dashboardCardRepository.save(card2);
        
        // Debt Consolidation
        DashboardCard card3 = new DashboardCard(
            "Debt Consolidation Options",
            "Explore ways to combine and simplify your debt payments.",
            CardType.RECOMMENDATION,
            KnowledgeLevel.INTERMEDIATE,
            goal
        );
        card3.setContentTemplate(
            "{userBank} may offer consolidation loans or lines of credit. " +
            "This can lower interest rates and simplify payments."
        );
        card3.setPriority(3);
        card3.setDifficultyLevel(3);
        dashboardCardRepository.save(card3);
    }
    
    private void createLearningCards(FinancialGoal goal) {
        // Financial Literacy Assessment
        DashboardCard card1 = new DashboardCard(
            "Financial Literacy Quiz",
            "Test your financial knowledge and get personalized learning recommendations.",
            CardType.QUIZ,
            KnowledgeLevel.BEGINNER,
            goal
        );
        card1.setContentTemplate(
            "Take this quick quiz to assess your financial knowledge level. " +
            "We'll recommend learning materials based on your results!"
        );
        card1.setPriority(1);
        card1.setDifficultyLevel(1);
        dashboardCardRepository.save(card1);
        
        // Learning Resources
        DashboardCard card2 = new DashboardCard(
            "Recommended Learning Resources",
            "Curated resources to improve your financial knowledge.",
            CardType.RESOURCE,
            KnowledgeLevel.BEGINNER,
            goal
        );
        card2.setContentTemplate(
            "Based on your interests, we recommend these resources: " +
            "Books, podcasts, and websites for financial education."
        );
        card2.setLearningResourceUrl("/resources/learning-materials");
        card2.setPriority(2);
        card2.setDifficultyLevel(1);
        dashboardCardRepository.save(card2);
        
        // Common Financial Terms
        DashboardCard card3 = new DashboardCard(
            "Financial Terms Dictionary",
            "Learn key financial terms and concepts.",
            CardType.DEFINITION,
            KnowledgeLevel.BEGINNER,
            goal
        );
        card3.setContentTemplate(
            "Understanding financial terms is key to making informed decisions. " +
            "Learn about interest, inflation, diversification, and more."
        );
        card3.setLearningResourceUrl("/learn/financial-dictionary");
        card3.setPriority(3);
        card3.setDifficultyLevel(1);
        dashboardCardRepository.save(card3);
    }
    
    private void createBudgetingCards(FinancialGoal goal) {
        // Budget Basics
        DashboardCard card1 = new DashboardCard(
            "Creating Your First Budget",
            "Learn how to create a budget that works for your lifestyle.",
            CardType.EDUCATIONAL,
            KnowledgeLevel.BEGINNER,
            goal
        );
        card1.setContentTemplate(
            "A budget is your spending plan. Track income and expenses, " +
            "then allocate money to your goals. Start simple and adjust as needed."
        );
        card1.setPriority(1);
        card1.setDifficultyLevel(1);
        dashboardCardRepository.save(card1);
        
        // Budget Calculator
        DashboardCard card2 = new DashboardCard(
            "Monthly Budget Calculator",
            "Create a personalized monthly budget based on your income and expenses.",
            CardType.CALCULATOR,
            KnowledgeLevel.BEGINNER,
            goal
        );
        card2.setContentTemplate(
            "Enter your monthly income and expenses to see where your money goes. " +
            "Identify opportunities to save and invest more!"
        );
        card2.setPriority(2);
        card2.setDifficultyLevel(2);
        dashboardCardRepository.save(card2);
        
        // Budgeting Apps
        DashboardCard card3 = new DashboardCard(
            "Best Budgeting Apps",
            "Discover tools to help you stick to your budget.",
            CardType.RECOMMENDATION,
            KnowledgeLevel.INTERMEDIATE,
            goal
        );
        card3.setContentTemplate(
            "Many apps can help you track spending automatically. " +
            "Look for features like expense categorization and goal tracking."
        );
        card3.setLearningResourceUrl("/learn/budgeting-apps");
        card3.setPriority(3);
        card3.setDifficultyLevel(2);
        dashboardCardRepository.save(card3);
    }
    
    private void createGeneralEducationalCards() {
        // Financial Health Check
        DashboardCard card1 = new DashboardCard(
            "Financial Health Checkup",
            "Assess your overall financial health with this quick checkup.",
            CardType.QUIZ,
            KnowledgeLevel.BEGINNER,
            null
        );
        card1.setContentTemplate(
            "Take this comprehensive financial health assessment. " +
            "We'll evaluate savings, debt, investments, and give you a score!"
        );
        card1.setPriority(1);
        card1.setDifficultyLevel(1);
        dashboardCardRepository.save(card1);
        
        // Common Financial Mistakes
        DashboardCard card2 = new DashboardCard(
            "Common Financial Mistakes",
            "Learn about and avoid these common financial pitfalls.",
            CardType.WARNING,
            KnowledgeLevel.BEGINNER,
            null
        );
        card2.setContentTemplate(
            "Avoid these common mistakes: not having an emergency fund, " +
            "ignoring retirement savings, carrying high-interest debt, and lifestyle inflation."
        );
        card2.setLearningResourceUrl("/learn/financial-mistakes");
        card2.setPriority(2);
        card2.setDifficultyLevel(1);
        dashboardCardRepository.save(card2);
        
        // Setting Financial Goals
        DashboardCard card3 = new DashboardCard(
            "SMART Financial Goals",
            "Learn to set effective financial goals using the SMART framework.",
            CardType.EDUCATIONAL,
            KnowledgeLevel.BEGINNER,
            null
        );
        card3.setContentTemplate(
            "SMART goals: Specific, Measurable, Achievable, Relevant, Time-bound. " +
            "Apply this framework to your financial objectives for better success!"
        );
        card3.setLearningResourceUrl("/learn/smart-goals");
        card3.setPriority(3);
        card3.setDifficultyLevel(2);
        dashboardCardRepository.save(card3);
    }
}
