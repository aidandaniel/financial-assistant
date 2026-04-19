// ============================================
// FRONTSIDE API INTEGRATION
// Connects frontend to Java backend
// ============================================

class FinSightAPI {
    constructor() {
        this.baseURL = 'http://localhost:8080/api';
        this.token = localStorage.getItem('finsight_token');
    }

    // Authentication
    async register(userData) {
        const response = await fetch(`${this.baseURL}/users/register`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(userData)
        });
        return await response.json();
    }

    async login(email, password) {
        const response = await fetch(`${this.baseURL}/users/login`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ email, password })
        });
        const data = await response.json();
        if (data.token) {
            this.token = data.token;
            localStorage.setItem('finsight_token', data.token);
        }
        return data;
    }

    // User Management
    async getUserProfile(userId) {
        const response = await fetch(`${this.baseURL}/users/${userId}`, {
            headers: { 'Authorization': `Bearer ${this.token}` }
        });
        return await response.json();
    }

    async updateUserProfile(userId, profileData) {
        const response = await fetch(`${this.baseURL}/users/${userId}`, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${this.token}`
            },
            body: JSON.stringify(profileData)
        });
        return await response.json();
    }

    async addUserGoal(userId, goalData) {
        const response = await fetch(`${this.baseURL}/users/${userId}/goals`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${this.token}`
            },
            body: JSON.stringify(goalData)
        });
        return await response.json();
    }

    async getUserGoals(userId) {
        const response = await fetch(`${this.baseURL}/users/${userId}/goals`, {
            headers: { 'Authorization': `Bearer ${this.token}` }
        });
        return await response.json();
    }

    // Dashboard
    async getPersonalizedDashboard(userId) {
        const response = await fetch(`${this.baseURL}/dashboard/personalized/${userId}`, {
            headers: { 'Authorization': `Bearer ${this.token}` }
        });
        return await response.json();
    }

    async getRecommendedCards(userId) {
        const response = await fetch(`${this.baseURL}/dashboard/recommended/${userId}`, {
            headers: { 'Authorization': `Bearer ${this.token}` }
        });
        return await response.json();
    }

    async getLearningPath(userId, goalId) {
        const response = await fetch(`${this.baseURL}/dashboard/learning-path/${userId}/${goalId}`, {
            headers: { 'Authorization': `Bearer ${this.token}` }
        });
        return await response.json();
    }

    async recordCardInteraction(interactionData) {
        const response = await fetch(`${this.baseURL}/dashboard/interaction`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${this.token}`
            },
            body: JSON.stringify(interactionData)
        });
        return await response.json();
    }

    // Quiz System
    async generateQuiz(userId, questionCount = 10) {
        const response = await fetch(`${this.baseURL}/quiz/generate/${userId}?questionCount=${questionCount}`, {
            headers: { 'Authorization': `Bearer ${this.token}` }
        });
        return await response.json();
    }

    async submitQuiz(userId, answers) {
        const response = await fetch(`${this.baseURL}/quiz/submit/${userId}`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${this.token}`
            },
            body: JSON.stringify(answers)
        });
        return await response.json();
    }

    async getQuizHistory(userId) {
        const response = await fetch(`${this.baseURL}/quiz/history/${userId}`, {
            headers: { 'Authorization': `Bearer ${this.token}` }
        });
        return await response.json();
    }

    async getQuizRecommendations(userId) {
        const response = await fetch(`${this.baseURL}/quiz/recommendations/${userId}`, {
            headers: { 'Authorization': `Bearer ${this.token}` }
        });
        return await response.json();
    }

    // Utility methods
    isAuthenticated() {
        return !!this.token;
    }

    logout() {
        this.token = null;
        localStorage.removeItem('finsight_token');
    }
}

// Global API instance
const finsightAPI = new FinSightAPI();
