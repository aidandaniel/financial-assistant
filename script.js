// ============================================
// FINSIGHT AI — script.js
// Firebase Auth + app logic
// ============================================

// ============================================
// FINSIGHT AI - Mock Auth System (for testing)
// ============================================
// TEST CREDENTIALS:
// Email: test@example.com
// Password: password123
// 
// Replace this section with real Firebase imports when you have valid credentials

// Mock Auth System
class MockAuth {
  constructor() {
    this.currentUser = null;
    this.listeners = [];
  }
  
  onAuthStateChanged(callback) {
    this.listeners.push(callback);
    // Simulate initial auth state check
    setTimeout(() => callback(this.currentUser), 100);
  }
  
  async signInWithEmailAndPassword(email, password) {
    // Simulate API delay
    await new Promise(resolve => setTimeout(resolve, 1000));
    
    // Mock validation - accept any valid email + 6+ char password
    if (email && email.includes('@') && password && password.length >= 6) {
      this.currentUser = {
        email: email,
        displayName: email.split('@')[0],
        uid: 'mock-user-' + Date.now()
      };
      this.notifyListeners();
      return { user: this.currentUser };
    } else {
      if (!email || !email.includes('@')) {
        throw { code: 'auth/invalid-email' };
      } else if (!password || password.length < 6) {
        throw { code: 'auth/weak-password' };
      } else {
        throw { code: 'auth/user-not-found' };
      }
    }
  }
  
  async createUserWithEmailAndPassword(email, password) {
    await new Promise(resolve => setTimeout(resolve, 1000));
    
    if (email && password.length >= 6) {
      this.currentUser = {
        email: email,
        displayName: email.split('@')[0],
        uid: 'mock-user-' + Date.now()
      };
      this.notifyListeners();
      return { user: this.currentUser };
    } else {
      throw { code: 'auth/weak-password' };
    }
  }
  
  async signInWithPopup() {
    await new Promise(resolve => setTimeout(resolve, 1000));
    
    this.currentUser = {
      email: 'mockuser@gmail.com',
      displayName: 'Mock User',
      uid: 'mock-google-user-123'
    };
    this.notifyListeners();
    return { user: this.currentUser };
  }
  
  async signOut() {
    await new Promise(resolve => setTimeout(resolve, 500));
    this.currentUser = null;
    this.notifyListeners();
  }
  
  notifyListeners() {
    this.listeners.forEach(callback => callback(this.currentUser));
  }
}

const auth = new MockAuth();

// ============================================
// APP STATE
// ============================================
let userData = {
  email:         '',
  name:          '',
  age:           '',
  bank:          '',
  goals:         [],
  timeline:      5,
  literacyLevel: null,
  isNewUser:     true
};

let isRegistrationMode = false;

// ============================================
// SCREEN NAVIGATION
// ============================================
function showScreen(screenId) {
  document.querySelectorAll('.screen').forEach(s => s.classList.remove('active'));
  const target = document.getElementById(screenId);
  if (target) target.classList.add('active');
}

// ============================================
// FIREBASE AUTH STATE LISTENER
// Fires on page load. Returning users skip
// straight to the dashboard.
// ============================================
auth.onAuthStateChanged((user) => {
  console.log('Auth state changed:', user);
  if (user) {
    console.log('User is logged in:', user.email);
    userData.email     = user.email || '';
    userData.name      = user.displayName || user.email.split('@')[0];
    userData.isNewUser = false;
    showScreen('dashboard-screen');
    populateDashboard();
  } else {
    console.log('No user logged in');
  }
  // No user - stay on login screen (already active by default in HTML)
});

// ============================================
// INIT
// ============================================
document.addEventListener('DOMContentLoaded', function () {

  // Fix input type
  const usernameInput = document.getElementById('username');
  if (usernameInput) {
    usernameInput.type        = 'email';
    usernameInput.placeholder = 'Email';
  }

  // Inject sign-up / sign-in toggle below the form
  const loginForm = document.querySelector('.login-form');
  if (loginForm) {
    const toggleP     = document.createElement('p');
    toggleP.className = 'auth-toggle';
    toggleP.innerHTML = 'Don\'t have an account? <a href="#" id="toggle-auth">Sign up</a>';
    loginForm.appendChild(toggleP);

    document.getElementById('toggle-auth').addEventListener('click', function (e) {
      e.preventDefault();
      isRegistrationMode = !isRegistrationMode;
      updateAuthForm();
    });

    loginForm.addEventListener('submit', function (e) {
      e.preventDefault();
      removeFormError();
      if (isRegistrationMode) {
        handleRegistration();
      } else {
        handleLogin();
      }
    });
  }

  // Google OAuth button
  const googleBtn = document.querySelector('.google-btn');
  if (googleBtn) {
    googleBtn.addEventListener('click', handleGoogleSignIn);
  }

  // Welcome screen
  const welcomeForm = document.querySelector('#welcome-screen .onboarding-form');
  if (welcomeForm) {
    const nameInput = document.getElementById('name-input');
    // Removed live name update - name only appears in input field

    welcomeForm.addEventListener('submit', function (e) {
      e.preventDefault();
      const name = document.getElementById('name-input').value.trim();
      if (!name) { showInlineError(welcomeForm, 'Please enter your name.'); return; }
      userData.name = name;
      showScreen('details-screen');
    });
  }

  // Details screen
  const detailsForm = document.querySelector('#details-screen .onboarding-form');
  if (detailsForm) {
    detailsForm.addEventListener('submit', function (e) {
      e.preventDefault();

      const age  = parseInt(document.getElementById('age').value);
      const bank = document.getElementById('bank').value.trim();

      if (!age || age < 18) {
        showValidationPopup('Age Requirement', 'You must be 18 or older to use FinSight AI.');
        return;
      }

      const canadianBanks = [
        'RBC', 'Royal Bank', 'TD', 'Toronto-Dominion', 'Scotiabank',
        'BMO', 'Bank of Montreal', 'CIBC', 'National Bank', 'Desjardins',
        'Tangerine', 'Simplii', 'EQ Bank', 'HSBC', 'ATB Financial',
        'Meridian', 'Laurentian', 'Manulife Bank', 'PC Financial', 'Motusbank',
        'Bank of China (Canada)', 'Bank of Nova Scotia', 'Canadian Imperial Bank of Commerce',
        'Canadian Western Bank', 'Caisses Populaires Acadiennes', 'Central 1 Credit Union',
        'CIBC Mortgage Corp.', 'Coast Capital Savings', 'Conexus Credit Union',
        'Connect First Credit Union', 'Copperfin Technology', 'Desjardins',
        'DUCA Financial', 'Equitable Bank', 'First Nations Bank of Canada',
        'FirstOntario', 'Financial 15 Bank', 'Forward Bank',
        'G&F Financial Group', 'Habitations Côté Ouest', 'Home Bank',
        'Home Trust Company', 'ICICI Bank Canada', 'Industrial Alliance Credit Union',
        'Ing Bank', 'Jubilee Financial', 'KOHO Financial',
        'Laurentian Bank of Canada', 'Libro Credit Union', 'Luminus Financial',
        'Manulife Bank of Canada', 'Meridian Credit Union', 'Motive Financial',
        'Neo Financial', 'North Shore Credit Union', 'Northern Credit Bureaus',
        'Ontario Teachers Pension Plan', 'Pacific & Western Bank of Canada',
        'Peoples Trust Company', 'Public Savings Bank', 'RFA Financial',
        'Rogers Bank', 'Royal Bank of Canada', 'Royal Trust Company',
        'Servus Credit Union', 'Simplii Financial', 'Steinbach Credit Union',
        'Sun Life Financial', 'Tangerine Bank', 'TD Bank',
        'UNI Financial', 'VanCity Credit Union', 'Vancity',
        'VersaBank', 'Wealthsimple', 'Wise Bank', 'Zagbank'
      ];

      if (!canadianBanks.some(b => bank.toLowerCase().includes(b.toLowerCase()))) {
        showValidationPopup('Bank Not Recognized',
          'Please enter a recognized Canadian bank. We support all major banks including RBC, TD, Scotiabank, BMO, CIBC, and many others.');
        return;
      }

      userData.age  = age;
      userData.bank = bank;
      showScreen('goals-screen');
    });
  }

  // Timeline slider
  const slider  = document.getElementById('timeline');
  const sliderV = document.getElementById('timeline-value');
  if (slider && sliderV) {
    slider.addEventListener('input', function () {
      sliderV.textContent = this.value === '1' ? '1 year' : `${this.value} years`;
    });
  }

  // Goals screen
  const goalsForm = document.querySelector('#goals-screen .onboarding-form');
  if (goalsForm) {
    goalsForm.addEventListener('submit', function (e) {
      e.preventDefault();
      const checked = document.querySelectorAll('.goal-chip input[type="checkbox"]:checked');
      const customGoal = document.getElementById('custom-goal').value.trim();
      
      if (checked.length === 0 && !customGoal) { 
        showInlineError(goalsForm, 'Please select at least one goal or enter a custom goal.'); 
        return; 
      }
      
      // Allow custom goal without requiring predefined selections
      const goals = Array.from(checked).map(cb => cb.value);
      if (customGoal) {
        goals.push('custom');
        userData.customGoal = customGoal;
      }
      
      userData.goals = goals;
      
      userData.goals = goals;
      userData.timeline = document.getElementById('timeline').value;
      showScreen('loading-screen');
      startLoadingSequence();
    });
  }

  // Quick Actions — View Transactions
  const viewTxBtn = Array.from(document.querySelectorAll('.action-btn'))
    .find(b => b.textContent.includes('View Transactions'));
  if (viewTxBtn) {
    viewTxBtn.addEventListener('click', function () {
      const content  = this.parentElement;
      const existing = content.querySelector('.transactions-list');
      if (existing) { existing.remove(); this.textContent = 'View Transactions'; return; }

      const transactions = [
        { desc: 'Tim Hortons',    date: 'Apr 14', amount: -6.50 },
        { desc: 'Netflix',        date: 'Apr 13', amount: -15.99 },
        { desc: 'Loblaws',        date: 'Apr 12', amount: -87.32 },
        { desc: 'Gas Station',    date: 'Apr 10', amount: -52.18 },
        { desc: 'Salary Deposit', date: 'Apr 1',  amount: 3200.00 }
      ];

      const listDiv     = document.createElement('div');
      listDiv.className = 'transactions-list';
      listDiv.innerHTML = transactions.map(t => `
        <div class="transaction-row">
          <div class="transaction-desc">
            <div>${t.desc}</div>
            <div class="transaction-date">${t.date}</div>
          </div>
          <div class="transaction-amount ${t.amount < 0 ? 'debit' : 'credit'}">
            ${t.amount < 0 ? '-' : '+'}$${Math.abs(t.amount).toFixed(2)}
          </div>
        </div>`).join('');

      content.appendChild(listDiv);
      this.textContent = 'Hide Transactions';
    });
  }

  // Quick Actions — Set Budget
  const setBudgetBtn = Array.from(document.querySelectorAll('.action-btn'))
    .find(b => b.textContent.includes('Set Budget'));
  if (setBudgetBtn) {
    setBudgetBtn.addEventListener('click', function () {
      const content  = this.parentElement;
      const existing = content.querySelector('.budget-form');
      if (existing) { existing.remove(); return; }

      const formDiv     = document.createElement('div');
      formDiv.className = 'budget-form';
      formDiv.innerHTML = `
        <label>Monthly Budget ($)</label>
        <input type="number" id="budget-amount" placeholder="0.00">
        <div class="budget-form-actions">
          <button type="button" id="btn-save-budget" class="btn-save">Save</button>
          <button type="button" id="btn-cancel-budget" class="btn-cancel">Cancel</button>
        </div>
        <div id="budget-confirm" class="budget-confirm"></div>`;

      content.appendChild(formDiv);

      document.getElementById('btn-save-budget').addEventListener('click', function () {
        const val = parseFloat(document.getElementById('budget-amount').value);
        if (!val || val <= 0) return;
        document.getElementById('budget-confirm').textContent = `Budget set: $${val.toFixed(2)}`;
        setTimeout(() => formDiv.remove(), 1500);
      });
      document.getElementById('btn-cancel-budget').addEventListener('click', () => formDiv.remove());
    });
  }

  // Ask Anything
  const questionInput = document.querySelector('.question-input');
  if (questionInput) {
    questionInput.addEventListener('keypress', function (e) {
      if (e.key !== 'Enter') return;
      const q = this.value.trim();
      if (!q) return;
      const existing = this.parentElement.querySelector('.ai-bubble');
      if (existing) existing.remove();
      const bubble     = document.createElement('div');
      bubble.className = 'ai-bubble';
      bubble.innerHTML = `<div class="ai-bubble-label">FinSight AI</div><div>${getAIResponse(q)}</div>`;
      this.parentElement.appendChild(bubble);
      this.value = '';
    });
  }

  // Profile icon — customization menu
  const profileIcon = document.querySelector('.profile-icon');
  let isCustomizeMode = false;
  
  if (profileIcon) {
    profileIcon.addEventListener('click', function () {
      if (!isCustomizeMode) {
        showCustomizationMenu();
      } else {
        hideCustomizationMenu();
      }
      isCustomizeMode = !isCustomizeMode;
    });
  }
});

// ============================================
// FIREBASE AUTH HANDLERS
// ============================================

async function handleLogin() {
  console.log('Login attempt started');
  const email    = document.getElementById('username').value.trim();
  const password = document.getElementById('password').value;

  console.log('Email:', email, 'Password length:', password.length);

  if (!email || !email.includes('@')) { showFormError('Please enter a valid email address.'); return; }
  if (password.length < 6)           { showFormError('Password must be at least 6 characters.'); return; }

  setAuthLoading(true);
  try {
    console.log('Calling signInWithEmailAndPassword');
    const cred         = await signInWithEmailAndPassword(auth, email, password);
    console.log('Login successful:', cred);
    userData.email     = cred.user.email;
    userData.name      = cred.user.displayName || email.split('@')[0];
    userData.isNewUser = false;
    // onAuthStateChanged handles routing
  } catch (err) {
    console.error('Login error:', err);
    setAuthLoading(false);
    showFormError(friendlyAuthError(err.code));
  }
}

async function handleRegistration() {
  const email           = document.getElementById('username').value.trim();
  const password        = document.getElementById('password').value;
  const confirmInput    = document.getElementById('confirm-password');
  const confirmPassword = confirmInput ? confirmInput.value : '';

  if (!/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(email)) { showFormError('Please enter a valid email address.'); return; }
  if (password.length < 6)                         { showFormError('Password must be at least 6 characters.'); return; }
  if (password !== confirmPassword)                 { showFormError('Passwords do not match.'); return; }

  setAuthLoading(true);
  try {
    const cred         = await createUserWithEmailAndPassword(auth, email, password);
    userData.email     = cred.user.email;
    userData.name      = email.split('@')[0];
    userData.isNewUser = true;
    // New user — go through onboarding
    showScreen('welcome-screen');
    const nameInput = document.getElementById('name-input');
    if (nameInput) nameInput.value = userData.name;
  } catch (err) {
    setAuthLoading(false);
    showFormError(friendlyAuthError(err.code));
  }
}

async function handleGoogleSignIn() {
  setAuthLoading(true);
  try {
    const result   = await auth.signInWithPopup();
    const user     = result.user;
    userData.email = user.email;
    userData.name  = user.displayName || user.email.split('@')[0];

    // For mock system, treat all Google sign-ins as new users for demo
    userData.isNewUser = true;

    showScreen('welcome-screen');
    const nameInput = document.getElementById('name-input');
    if (nameInput) nameInput.value = userData.name.split(' ')[0];
  } catch (err) {
    setAuthLoading(false);
    if (err.code !== 'auth/popup-closed-by-user') {
      showFormError(friendlyAuthError(err.code));
    }
  }
}

function friendlyAuthError(code) {
  const map = {
    'auth/user-not-found':         'No account found with that email. Try signing up.',
    'auth/wrong-password':         'Incorrect password. Please try again.',
    'auth/email-already-in-use':   'An account with that email already exists. Try logging in.',
    'auth/weak-password':          'Password must be at least 6 characters.',
    'auth/invalid-email':          'Please enter a valid email address.',
    'auth/too-many-requests':      'Too many attempts. Please wait a moment and try again.',
    'auth/network-request-failed': 'Network error. Check your connection and try again.',
    'auth/popup-blocked':          'Popup was blocked. Please allow popups for this site.',
    'auth/invalid-credential':     'Incorrect email or password.',
  };
  return map[code] || 'Something went wrong. Please try again.';
}

function setAuthLoading(loading) {
  const btn = document.querySelector('.login-form .primary-btn');
  if (!btn) return;
  btn.disabled    = loading;
  btn.textContent = loading
    ? (isRegistrationMode ? 'Creating account...' : 'Signing in...')
    : (isRegistrationMode ? 'Create Account' : 'Continue');
}

// ============================================
// AUTH FORM TOGGLE
// ============================================
function updateAuthForm() {
  const form       = document.querySelector('.login-form');
  const submitBtn  = form.querySelector('button[type="submit"]');
  const toggleLink = document.getElementById('toggle-auth');
  const title      = document.querySelector('.app-title');

  const existing = form.querySelector('.confirm-group');
  if (existing) existing.remove();

  if (isRegistrationMode) {
    if (title)      title.textContent      = 'Sign Up';
    submitBtn.textContent                  = 'Create Account';
    if (toggleLink) toggleLink.textContent = 'Back to login';
    const confirmGroup     = document.createElement('div');
    confirmGroup.className = 'input-group confirm-group';
    confirmGroup.innerHTML = '<input type="password" id="confirm-password" placeholder="Confirm Password" required>';
    form.insertBefore(confirmGroup, submitBtn);
  } else {
    if (title)      title.textContent      = 'Login';
    submitBtn.textContent                  = 'Continue';
    if (toggleLink) toggleLink.textContent = 'Sign up';
  }
}

// ============================================
// LOADING SEQUENCE
// ============================================
function startLoadingSequence() {
  const h2       = document.querySelector('#loading-screen h2');
  const messages = ['Analyzing your goals...', 'Building your roadmap...', 'Personalizing your dashboard...'];
  let i = 0;
  const interval = setInterval(() => {
    if (h2) h2.textContent = messages[i];
    i++;
    if (i >= messages.length) {
      clearInterval(interval);
      setTimeout(() => { showScreen('dashboard-screen'); populateDashboard(); }, 800);
    }
  }, 800);
}

// ============================================
// DASHBOARD
// ============================================
function populateDashboard() {
  const greetingEl = document.getElementById('greeting-text');
  if (greetingEl && userData.name) {
    const h = new Date().getHours();
    const g = h < 12 ? 'Good morning' : h < 18 ? 'Good afternoon' : 'Good evening';
    greetingEl.textContent = `${g}, ${userData.name.split(' ')[0]}.`;
  }

  const profileIcon = document.querySelector('.profile-icon');
  if (profileIcon && userData.name) profileIcon.textContent = userData.name.charAt(0).toUpperCase();

  populateUserProfile();
  populateDashboardCards();
  addGoalBasedTimeline();
  addOptionalQuizSection();
}

function populateUserProfile() {
  const ageEl  = document.getElementById('profile-age');
  if (ageEl)   ageEl.textContent  = userData.age  || '--';
  const bankEl = document.getElementById('profile-bank');
  if (bankEl)  bankEl.textContent = userData.bank || '--';
  const goalsEl = document.getElementById('profile-goals');
  if (goalsEl) {
    goalsEl.innerHTML = userData.goals.length
      ? userData.goals.map(g => `<span class="tag">${g.replace(/-/g, ' ')}</span>`).join('')
      : '<span class="tag">None selected</span>';
  }
}

async function populateDashboardCards() {
  try {
    // Get personalized dashboard cards from backend
    const response = await finsightAPI.getPersonalizedDashboard(userData.id || 1);
    const cards = response.cards;
    
    // Clear existing cards
    const dashboardContent = document.querySelector('.dashboard-content');
    const existingCards = dashboardContent.querySelectorAll('.dashboard-card');
    existingCards.forEach(card => card.remove());
    
    // Create personalized cards
    cards.forEach((cardData, index) => {
      const card = createPersonalizedCard(cardData, index);
      dashboardContent.appendChild(card);
    });
    
    // Add edit buttons to all dashboard cards
    addCardEditButtons();
    
  } catch (error) {
    console.error('Error loading personalized dashboard:', error);
    // Fallback to original cards if API fails
    populateFallbackCards();
  }
}

function createPersonalizedCard(cardData, index) {
  const card = document.createElement('div');
  card.className = 'dashboard-card medium personalized-card';
  card.dataset.cardId = cardData.id;
  card.dataset.cardType = cardData.cardType;
  
  const cardContent = document.createElement('div');
  cardContent.className = 'card-content';
  
  // Different card layouts based on type
  switch (cardData.cardType) {
    case 'EDUCATIONAL':
      cardContent.innerHTML = `
        <div class="card-header">
          <h3>${cardData.title}</h3>
          <span class="card-type-badge educational">Educational</span>
        </div>
        <div class="card-body">
          <p>${cardData.description}</p>
          <div class="personalized-content">${cardData.personalizedContent}</div>
          ${cardData.learningResourceUrl ? 
            `<button class="learn-more-btn" onclick="openLearningResource('${cardData.learningResourceUrl}')">Learn More</button>` : ''}
        </div>
      `;
      break;
      
    case 'CALCULATOR':
      cardContent.innerHTML = `
        <div class="card-header">
          <h3>${cardData.title}</h3>
          <span class="card-type-badge calculator">Calculator</span>
        </div>
        <div class="card-body">
          <p>${cardData.description}</p>
          <div class="calculator-content">
            <div class="personalized-content">${cardData.personalizedContent}</div>
            <button class="calculate-btn" onclick="openCalculator('${cardData.id}')">Calculate</button>
          </div>
        </div>
      `;
      break;
      
    case 'QUIZ':
      cardContent.innerHTML = `
        <div class="card-header">
          <h3>${cardData.title}</h3>
          <span class="card-type-badge quiz">Quiz</span>
        </div>
        <div class="card-body">
          <p>${cardData.description}</p>
          <div class="personalized-content">${cardData.personalizedContent}</div>
          <button class="quiz-btn" onclick="startQuickQuiz('${cardData.id}')">Start Quiz</button>
        </div>
      `;
      break;
      
    case 'RECOMMENDATION':
      cardContent.innerHTML = `
        <div class="card-header">
          <h3>${cardData.title}</h3>
          <span class="card-type-badge recommendation">Recommendation</span>
        </div>
        <div class="card-body">
          <p>${cardData.description}</p>
          <div class="personalized-content">${cardData.personalizedContent}</div>
          ${cardData.learningResourceUrl ? 
            `<button class="explore-btn" onclick="openLearningResource('${cardData.learningResourceUrl}')">Explore</button>` : ''}
        </div>
      `;
      break;
      
    default:
      cardContent.innerHTML = `
        <div class="card-header">
          <h3>${cardData.title}</h3>
          <span class="card-type-badge general">${cardData.cardType}</span>
        </div>
        <div class="card-body">
          <p>${cardData.description}</p>
          <div class="personalized-content">${cardData.personalizedContent}</div>
        </div>
      `;
  }
  
  card.appendChild(cardContent);
  
  // Record card view interaction
  recordCardInteraction(cardData.id, 'VIEW');
  
  return card;
}

function populateFallbackCards() {
  // Original card implementation as fallback
  const accountCard = document.querySelector('.dashboard-card.medium .card-content');
  if (accountCard) {
    accountCard.innerHTML = `
      <div class="account-row"><span>Chequing</span><span>$2,400</span></div>
      <div class="account-row"><span>TFSA Savings</span><span>$8,150</span></div>
      <div class="account-row"><span>Monthly Change</span><span><span class="badge positive">+8.2%</span></span></div>
      <div class="account-row"><span>Primary Bank</span><span>${userData.bank || '---'}</span></div>
      <div class="account-row"><span>Total</span><span>$10,550</span></div>`;
  }

  const portfolioCard = document.querySelectorAll('.dashboard-card.medium .card-content')[1];
  if (portfolioCard) {
    const p = getGoalBasedPortfolio();
    portfolioCard.innerHTML = `
      <div style="margin-bottom:14px">
        <div style="font-size:16px;font-weight:600;color:var(--text-primary)">${p.title}</div>
        <div style="font-size:12px;color:var(--text-muted);margin-top:2px">${p.subtitle}</div>
      </div>
      ${p.holdings}
      <div class="account-row" style="margin-top:12px">
        <span>Timeline</span>
        <span>${userData.timeline} ${userData.timeline == 1 ? 'year' : 'years'}</span>
      </div>`;
  }
  
  // Add edit buttons to all dashboard cards
  addCardEditButtons();
}

function addCardEditButtons() {
  const cards = document.querySelectorAll('.dashboard-card');
  console.log('Found cards:', cards.length);
  
  cards.forEach((card, index) => {
    // Skip if card already has edit buttons
    if (card.querySelector('.card-edit-buttons')) {
      console.log(`Card ${index} already has edit buttons, skipping`);
      return;
    }
    
    console.log(`Adding edit buttons to card ${index}`);
    
    const editButtons = document.createElement('div');
    editButtons.className = 'card-edit-buttons';
    editButtons.innerHTML = `
      <button class="card-delete-btn" title="Delete card">✕</button>
      <button class="card-move-btn" title="Move card">☰</button>
    `;
    
    card.appendChild(editButtons);
    console.log(`Edit buttons added to card ${index}`);
    
    // Add event listeners
    const deleteBtn = editButtons.querySelector('.card-delete-btn');
    const moveBtn = editButtons.querySelector('.card-move-btn');
    
    if (deleteBtn) {
      deleteBtn.addEventListener('click', function(e) {
        e.stopPropagation();
        if (confirm('Delete this dashboard card?')) {
          card.remove();
          console.log(`Card ${index} deleted`);
          // Re-arrange remaining cards
          rearrangeCards();
        }
      });
    }
    
    if (moveBtn) {
      moveBtn.addEventListener('click', function(e) {
        e.stopPropagation();
        card.classList.add('dragging');
        card.draggable = true;
        console.log(`Card ${index} made draggable`);
      });
    }
  });
  
  return `Added edit buttons to ${cards.length} cards`;
}

function rearrangeCards() {
  const cards = document.querySelectorAll('.dashboard-card');
  cards.forEach((card, index) => {
    card.style.order = index;
  });
}

function enableCardEditMode() {
  const cards = document.querySelectorAll('.dashboard-card');
  cards.forEach(card => {
    card.classList.add('edit-mode');
  });
  addCardEditButtons();
}

function disableCardEditMode() {
  const cards = document.querySelectorAll('.dashboard-card');
  cards.forEach(card => {
    card.classList.remove('edit-mode');
    card.draggable = false;
  });
  
  // Remove edit buttons
  document.querySelectorAll('.card-edit-buttons').forEach(btn => btn.remove());
}

function getGoalBasedPortfolio() {
  switch (getPrimaryGoal()) {
    case 'saving': case 'emergency-fund':
      return { title:'Conservative Holdings', subtitle:'Safety-focused, capital-preserved', holdings:`
        <div class="holding-row"><span>EQ Bank HYSA</span><span class="badge positive">+4.5%</span></div>
        <div class="holding-row"><span>1-Yr GIC</span><span class="badge positive">+5.1%</span></div>
        <div class="holding-row"><span>Savings ETF (CASH.TO)</span><span class="badge positive">+4.8%</span></div>` };
    case 'investing': case 'growing':
      return { title:'Growth Holdings', subtitle:'Market-based, long-term focused', holdings:`
        <div class="holding-row"><span>S&P 500 ETF (VFV)</span><span class="badge positive">+12.4%</span></div>
        <div class="holding-row"><span>All-Equity ETF (XEQT)</span><span class="badge positive">+10.1%</span></div>
        <div class="holding-row"><span>Tech Fund (XIT)</span><span class="badge negative">−2.3%</span></div>` };
    case 'debt-payoff':
      return { title:'Debt Payoff Tracker', subtitle:'Progress toward debt freedom', holdings:`
        <div class="holding-row"><span>Credit Card</span><span class="badge negative">$3,200 left</span></div>
        <div class="holding-row"><span>Student Loan</span><span class="badge negative">$11,500 left</span></div>
        <div class="holding-row"><span>Monthly Target</span><span class="badge positive">$680</span></div>` };
    case 'learning':
      return { title:'Recommended Reads', subtitle:'Curated resources for you', holdings:`
        <div class="holding-row"><span>TFSA vs RRSP Guide</span><span style="font-size:12px;color:var(--text-muted)">MoneySense</span></div>
        <div class="holding-row"><span>Index Investing Basics</span><span style="font-size:12px;color:var(--text-muted)">CDN Couch Potato</span></div>
        <div class="holding-row"><span>Emergency Fund 101</span><span style="font-size:12px;color:var(--text-muted)">Financial Post</span></div>` };
    case 'custom':
      const customGoalText = userData.customGoal || 'Your Custom Goal';
      return { title:'Custom Goal', subtitle:`Personalized for: ${customGoalText}`, holdings:`
        <div class="holding-row"><span>Goal-Based Strategy</span><span class="badge positive">Tailored</span></div>
        <div class="holding-row"><span>Personalized Plan</span><span style="font-size:12px;color:var(--text-muted)">${customGoalText}</span></div>
        <div class="holding-row"><span>Custom Investments</span><span class="badge positive">Flexible</span></div>` };
    default:
      return { title:'Balanced Portfolio', subtitle:'Diversified approach', holdings:`
        <div class="holding-row"><span>S&P 500 ETF (VFV)</span><span class="badge positive">+12.4%</span></div>
        <div class="holding-row"><span>TFSA GIC</span><span class="badge positive">+4.1%</span></div>
        <div class="holding-row"><span>Tech Fund (XIT)</span><span class="badge negative">−2.3%</span></div>` };
  }
}

function getPrimaryGoal() {
  const priority = ['debt-payoff','saving','emergency-fund','investing','growing','learning'];
  for (const g of priority) { if (userData.goals.includes(g)) return g; }
  if (userData.goals.includes('custom')) return 'custom';
  return userData.goals[0] || 'saving';
}

// ============================================
// TIMELINE
// ============================================
function addGoalBasedTimeline() {
  const content = document.querySelector('.dashboard-content');
  if (!content) return;
  const existing = content.querySelector('.timeline-section');
  if (existing) existing.remove();

  const milestones = getTimeline(getPrimaryGoal(), parseInt(userData.timeline));
  const section     = document.createElement('div');
  section.className = 'timeline-section';
  section.innerHTML = `
    <h3>Your Plan-to-Action Timeline</h3>
    <div class="milestones-track">
      ${milestones.map((m, i) => `
        <div class="milestone">
          <div class="milestone-index">${i + 1}</div>
          <div class="milestone-text">${m}</div>
        </div>`).join('')}
    </div>`;
  content.appendChild(section);
}

function getTimeline(goal, years) {
  switch (goal) {
    case 'saving': case 'emergency-fund': return [
      '<strong>Month 1:</strong> Open TFSA and set up automatic contributions',
      '<strong>Month 3:</strong> First $1,000 milestone',
      `<strong>Year ${Math.max(1,Math.floor(years*0.4))}:</strong> $10,000 saved`,
      `<strong>Year ${Math.max(1,Math.floor(years*0.8))}:</strong> Emergency fund fully funded`,
      `<strong>Year ${years}:</strong> Goal reached — consider expanding into investing`];
    case 'debt-payoff': return [
      '<strong>Month 1:</strong> List all debts and interest rates — highest first',
      '<strong>Month 6:</strong> High-interest credit card cleared',
      `<strong>Year ${Math.max(1,Math.floor(years*0.5))}:</strong> Student loan balance halved`,
      `<strong>Year ${years}:</strong> Debt free — redirect payments to savings`];
    case 'investing': case 'growing': return [
      '<strong>Month 1:</strong> Open TFSA and fund an all-equity ETF (e.g. XEQT)',
      '<strong>Month 3:</strong> Set up automatic monthly contributions',
      `<strong>Year ${Math.max(1,Math.floor(years*0.3))}:</strong> Portfolio reaches $25,000`,
      `<strong>Year ${Math.max(1,Math.floor(years*0.7))}:</strong> Max out annual TFSA contributions`,
      `<strong>Year ${years}:</strong> Investment goal achieved`];
    case 'learning': return [
      '<strong>Month 1:</strong> Complete a basic personal finance course',
      '<strong>Month 3:</strong> Read 3 recommended books (start with MoneySense)',
      '<strong>Month 6:</strong> Join an investing community or subreddit',
      `<strong>Year ${years}:</strong> Confident enough to manage your own portfolio` ];
    case 'custom': 
      const customGoalText = userData.customGoal || 'Your Custom Goal';
      return [
        `<strong>Month 1:</strong> Create a personalized plan for: ${customGoalText}`,
        '<strong>Month 3:</strong> Set up specific actions and milestones',
        `<strong>Month 6:</strong> Review progress and adjust strategy`,
        `<strong>Year ${Math.max(1,Math.floor(years*0.5))}:</strong> Key milestone achieved`,
        `<strong>Year ${years}:</strong> Custom goal completed — celebrate your success!` ];
    default: return [
      '<strong>Month 1:</strong> Define your financial goals and set a monthly budget',
      '<strong>Month 3:</strong> Build a starter emergency fund ($1,000)',
      `<strong>Year ${Math.max(1,Math.floor(years*0.5))}:</strong> Halfway to your primary goal`,
      `<strong>Year ${years}:</strong> Goal achieved — reassess and set new targets`];
  }
}

// ============================================
// QUIZ
// ============================================
function addOptionalQuizSection() {
  const profileCard = document.querySelector('.user-profile-card');
  if (!profileCard) return;
  const existing = profileCard.querySelector('.optional-quiz-section, .quiz-results-section');
  if (existing) existing.remove();

  if (userData.literacyLevel) {
    const levelText = userData.literacyLevel.charAt(0).toUpperCase() + userData.literacyLevel.slice(1);
    const descs = { novice:"We'll keep explanations simple and jargon-free.", intermediate:"We'll cover the essentials and a bit more.", experienced:"We'll get straight to the numbers." };
    const section     = document.createElement('div');
    section.className = 'quiz-results-section';
    section.innerHTML = `
      <div class="literacy-badge">
        <span class="literacy-badge-label">Knowledge Level</span>
        <span class="literacy-badge-level">${levelText}</span>
        <span class="literacy-badge-desc"> — ${descs[userData.literacyLevel]}</span>
      </div>
      <button id="retake-quiz" class="btn-quiz" style="margin-top:12px">Retake Quiz</button>`;
    profileCard.appendChild(section);
    document.getElementById('retake-quiz').addEventListener('click', function () {
      userData.literacyLevel = null; addOptionalQuizSection(); showFinancialLiteracyQuiz();
    });
  } else {
    const section     = document.createElement('div');
    section.className = 'optional-quiz-section';
    section.innerHTML = `
      <div>
        <h4>Assess Your Financial Knowledge</h4>
        <p>Takes 2 minutes — personalizes your dashboard</p>
      </div>
      <button id="open-quiz-btn" class="btn-quiz">Start Quiz</button>`;
    profileCard.appendChild(section);
    document.getElementById('open-quiz-btn').addEventListener('click', showFinancialLiteracyQuiz);
  }
}

function showFinancialLiteracyQuiz() {
  const existing = document.querySelector('.modal-overlay.quiz-modal');
  if (existing) existing.remove();

  const questions = [
    { q:'What is a Tax-Free Savings Account (TFSA)?', opts:['A type of mortgage for first-time homebuyers','An account where all earnings and withdrawals are tax-free','A government pension plan for retirement'], correct:1 },
    { q:'What does investment diversification mean?', opts:['Putting all your money into one high-performing stock','Spreading investments across different assets to reduce risk','Taking out a loan to invest more money'], correct:1 },
    { q:'How does compound interest work?', opts:['A fee charged by banks for account maintenance','Interest earned only on the original principal','Interest earned on both principal and accumulated interest'], correct:2 },
    { q:'What is the primary purpose of an emergency fund?', opts:['To save for vacations or luxury purchases','To cover unexpected expenses without going into debt','To invest in high-risk opportunities'], correct:1 },
    { q:'What is the 50/30/20 budget rule?', opts:['50% wants, 30% needs, 20% savings','50% needs, 30% wants, 20% savings','50% savings, 30% needs, 20% wants'], correct:1 }
  ];

  const modal     = document.createElement('div');
  modal.className = 'modal-overlay quiz-modal';
  const box       = document.createElement('div');
  box.className   = 'modal-box modal-quiz';
  box.innerHTML   = `
    <div class="modal-quiz-header">
      <h3>Financial Literacy Assessment</h3>
      <p>5 questions — answer honestly to personalize your dashboard</p>
    </div>
    <div class="modal-quiz-body">
      ${questions.map((q,qi) => `
        <div class="quiz-question">
          <div class="quiz-question-label"><span class="quiz-q-num">${qi+1}</span><p>${q.q}</p></div>
          ${q.opts.map((opt,oi) => `
            <label class="quiz-option">
              <input type="radio" name="quiz-q${qi}" value="${oi}">
              <span>${opt}</span>
            </label>`).join('')}
        </div>`).join('')}
      <div class="quiz-error" id="quiz-error-msg"></div>
    </div>
    <div class="quiz-actions">
      <button class="btn-cancel-quiz" id="cancel-quiz">Cancel</button>
      <button class="btn-submit-quiz" id="submit-quiz">Submit Assessment</button>
    </div>`;

  modal.appendChild(box);
  document.body.appendChild(modal);
  modal.addEventListener('click', e => { if (e.target === modal) modal.remove(); });
  document.getElementById('cancel-quiz').addEventListener('click', () => modal.remove());

  document.getElementById('submit-quiz').addEventListener('click', function () {
    const errorEl = document.getElementById('quiz-error-msg');
    const answers = questions.map((_,qi) => {
      const checked = document.querySelector(`input[name="quiz-q${qi}"]:checked`);
      return checked ? parseInt(checked.value) : null;
    });
    if (answers.includes(null)) { errorEl.textContent='Please answer all questions before submitting.'; errorEl.style.display='block'; return; }
    errorEl.style.display = 'none';
    const score = answers.reduce((acc,ans,i) => acc + (ans === questions[i].correct ? 1 : 0), 0);
    userData.literacyLevel = score <= 2 ? 'novice' : score <= 3 ? 'intermediate' : 'experienced';
    showQuizResults(score, modal);
  });
}

function showQuizResults(score, modal) {
  const levelText = userData.literacyLevel.charAt(0).toUpperCase() + userData.literacyLevel.slice(1);
  const box       = modal.querySelector('.modal-box');
  box.className   = 'modal-box';
  box.style.cssText = 'padding:40px;text-align:center';
  box.innerHTML   = `
    <div class="quiz-score-circle">${score}/5</div>
    <div class="quiz-result-level">Knowledge Level: <strong>${levelText}</strong></div>
    <div class="quiz-result-desc">Your dashboard and explanations will now be tailored to your level.</div>
    <button class="btn-continue" id="close-quiz-results">View My Dashboard</button>`;
  document.getElementById('close-quiz-results').addEventListener('click', function () {
    modal.remove(); addOptionalQuizSection(); populateDashboardCards();
  });
}

// ============================================
// AI RESPONSE
// ============================================
function getAIResponse(question) {
  const q   = question.toLowerCase();
  const map = [
    ['tfsa',      'A TFSA lets your money grow tax-free. Max out contributions before investing in a non-registered account.'],
    ['rrsp',      'RRSPs reduce your taxable income now and grow tax-deferred. Best used if you expect a lower tax bracket at retirement.'],
    ['mortgage',  'Aim for a 20% down payment to avoid CMHC insurance. Keep your GDS ratio under 32%.'],
    ['invest',    'For long-term investing, low-cost index ETFs like XEQT or VGRO are a strong starting point for most Canadians.'],
    ['budget',    '50% needs, 30% wants, 20% savings is a solid starting framework — adjust for your goals.'],
    ['debt',      'Tackle high-interest debt first (avalanche method). Once cleared, redirect those payments to savings.'],
    ['emergency', 'Aim for 3–6 months of expenses in a liquid, accessible account like a HYSA.'],
    ['gic',       'GICs are low-risk fixed-rate savings. Great for capital preservation over 1–5 year terms.'],
    ['etf',       'ETFs are low-cost funds that track an index. XEQT and VGRO are popular all-in-one options for Canadians.'],
  ];
  for (const [kw, response] of map) { if (q.includes(kw)) return response; }
  return "Good question. For personalized advice, consider speaking with a fee-only financial advisor — FinSight AI provides general guidance only.";
}

// ============================================
// MODALS & ERRORS
// ============================================
function showValidationPopup(title, message) {
  const existing = document.querySelector('.modal-overlay.validation-popup');
  if (existing) existing.remove();
  const modal     = document.createElement('div');
  modal.className = 'modal-overlay validation-popup';
  const box       = document.createElement('div');
  box.className   = 'modal-box';
  box.innerHTML   = `
    <div class="validation-icon">!</div>
    <h3 class="modal-title">${title}</h3>
    <p class="modal-subtitle">${message}</p>
    <button class="btn-got-it" id="close-validation">Got It</button>`;
  modal.appendChild(box);
  document.body.appendChild(modal);
  document.getElementById('close-validation').addEventListener('click', () => modal.remove());
  modal.addEventListener('click', e => { if (e.target === modal) modal.remove(); });
}

function showFormError(message) {
  removeFormError();
  const err     = document.createElement('div');
  err.className = 'form-error';
  err.textContent = message;
  const form = document.querySelector('#login-screen .login-form');
  if (form) form.appendChild(err);
}

function showInlineError(form, message) {
  const existing = form.querySelector('.form-error');
  if (existing) existing.remove();
  const err     = document.createElement('div');
  err.className = 'form-error';
  err.textContent = message;
  form.appendChild(err);
}

function removeFormError() {
  const err = document.querySelector('.form-error');
  if (err) err.remove();
}

// ============================================
// DASHBOARD CUSTOMIZATION
// ============================================

function showCustomizationMenu() {
  const dashboard = document.querySelector('.dashboard-content');
  if (!dashboard) return;
  
  // Remove existing customization menu
  hideCustomizationMenu();
  
  // Create dropdown menu
  const menu = document.createElement('div');
  menu.className = 'customization-menu';
  menu.innerHTML = `
    <div class="menu-header">Dashboard Options</div>
    <div class="menu-item" id="edit-dashboard">✏️ Edit Dashboard</div>
    <div class="menu-item" id="edit-profile">👤 Edit Profile</div>
    <div class="menu-item" id="sign-out">🚪 Sign Out</div>
  `;
  
  // Position menu below profile icon
  const profileIcon = document.querySelector('.profile-icon');
  if (profileIcon) {
    const rect = profileIcon.getBoundingClientRect();
    menu.style.position = 'fixed';
    menu.style.top = `${rect.bottom + 8}px`;
    menu.style.left = `${rect.left - 75}px`;
    menu.style.zIndex = '1000';
  }
  
  document.body.appendChild(menu);
  
  // Add event listeners
  document.getElementById('edit-dashboard').addEventListener('click', function() {
    enableEditMode();
    hideCustomizationMenu();
  });
  
  document.getElementById('edit-profile').addEventListener('click', function() {
    showProfileEditor();
    hideCustomizationMenu();
  });
  
  document.getElementById('sign-out').addEventListener('click', function() {
    auth.signOut().then(() => {
      userData = { email:'', name:'', age:'', bank:'', goals:[], timeline:5, literacyLevel:null, isNewUser:true };
      showScreen('login-screen');
    });
    hideCustomizationMenu();
  });
  
  // Update profile icon to indicate customize mode
  if (profileIcon) {
    profileIcon.style.background = 'var(--accent)';
    profileIcon.style.color = 'white';
    profileIcon.textContent = '⚙';
  }
}

function hideCustomizationMenu() {
  // Remove menu
  const menu = document.querySelector('.customization-menu');
  if (menu) menu.remove();
  
  // Remove remove buttons
  document.querySelectorAll('.chip-remove').forEach(btn => btn.remove());
  
  // Remove draggable functionality
  makeChipsNotDraggable();
  
  // Remove plus button
  const plusBtn = document.querySelector('.add-chip-btn');
  if (plusBtn) plusBtn.remove();
  
  // Disable card editing
  disableCardEditMode();
  
  // Reset profile icon
  const profileIcon = document.querySelector('.profile-icon');
  if (profileIcon) {
    profileIcon.style.background = '';
    profileIcon.style.color = '';
    profileIcon.textContent = userData.name.charAt(0).toUpperCase();
  }
}

function enableEditMode() {
  console.log('Enabling edit mode...');
  
  // Add remove buttons to all existing chips
  const chipResult = addRemoveButtonsToChips();
  console.log('Remove buttons added:', chipResult);
  
  // Make chips draggable
  const dragResult = makeChipsDraggable();
  console.log('Chips made draggable:', dragResult);
  
  // Add plus button
  const plusResult = addPlusButton();
  console.log('Plus button added:', plusResult);
  
  // Enable card editing
  const cardResult = enableCardEditMode();
  console.log('Card edit mode enabled:', cardResult);
  
  console.log('Edit mode enabled successfully');
}

function showProfileEditor() {
  const modal = document.createElement('div');
  modal.className = 'modal-overlay profile-editor-modal';
  modal.innerHTML = `
    <div class="modal-box">
      <div class="profile-editor-header">
        <h3>Edit Profile</h3>
        <button class="close-btn" id="close-profile">✕</button>
      </div>
      <div class="profile-editor-content">
        <div class="profile-field">
          <label>Email</label>
          <input type="email" id="edit-email" value="${userData.email || ''}" readonly>
        </div>
        <div class="profile-field">
          <label>Username</label>
          <input type="text" id="edit-username" value="${userData.email ? userData.email.split('@')[0] : ''}">
        </div>
        <div class="profile-field">
          <label>Name</label>
          <input type="text" id="edit-name" value="${userData.name || ''}">
        </div>
        <div class="profile-field">
          <label>Age</label>
          <input type="number" id="edit-age" value="${userData.age || ''}" min="18" max="120">
        </div>
        <div class="profile-field">
          <label>Bank</label>
          <input type="text" id="edit-bank" value="${userData.bank || ''}">
        </div>
        <div class="profile-field">
          <label>Postal Code</label>
          <input type="text" id="edit-postal" value="${userData.postalCode || ''}" placeholder="A1A 1A1">
        </div>
        <div class="profile-field">
          <label>Phone</label>
          <input type="tel" id="edit-phone" value="${userData.phone || ''}" placeholder="(555) 123-4567">
        </div>
        <div class="profile-actions">
          <button class="btn-save" id="save-profile">Save Changes</button>
          <button class="btn-cancel" id="cancel-profile">Cancel</button>
        </div>
      </div>
    </div>
  `;
  
  document.body.appendChild(modal);
  
  // Event listeners
  document.getElementById('close-profile').addEventListener('click', () => {
    modal.remove();
  });
  
  document.getElementById('save-profile').addEventListener('click', () => {
    userData.email = document.getElementById('edit-email').value;
    userData.name = document.getElementById('edit-name').value;
    userData.age = parseInt(document.getElementById('edit-age').value);
    userData.bank = document.getElementById('edit-bank').value;
    userData.postalCode = document.getElementById('edit-postal').value;
    userData.phone = document.getElementById('edit-phone').value;
    
    // Update profile display
    populateUserProfile();
    
    modal.remove();
    console.log('Profile updated');
  });
  
  document.getElementById('cancel-profile').addEventListener('click', () => {
    modal.remove();
  });
}

function addRemoveButtonsToChips() {
  const tags = document.querySelectorAll('.tag');
  tags.forEach(tag => {
    if (!tag.querySelector('.chip-remove')) {
      const removeBtn = document.createElement('button');
      removeBtn.className = 'chip-remove';
      removeBtn.innerHTML = '×';
      removeBtn.addEventListener('click', function(e) {
        e.stopPropagation();
        tag.remove();
      });
      tag.style.position = 'relative';
      tag.appendChild(removeBtn);
    }
  });
}

function makeChipsDraggable() {
  const tags = document.querySelectorAll('.tag');
  tags.forEach(tag => {
    tag.draggable = true;
    tag.style.cursor = 'move';
    tag.addEventListener('dragstart', handleDragStart);
    tag.addEventListener('dragover', handleDragOver);
    tag.addEventListener('drop', handleDrop);
  });
}

function makeChipsNotDraggable() {
  const tags = document.querySelectorAll('.tag');
  tags.forEach(tag => {
    tag.draggable = false;
    tag.style.cursor = 'default';
  });
}

function addPlusButton() {
  const profileCard = document.querySelector('.user-profile-card');
  if (!profileCard) return;
  
  const plusBtn = document.createElement('button');
  plusBtn.className = 'add-chip-btn';
  plusBtn.innerHTML = '+';
  plusBtn.addEventListener('click', showAddChipDialog);
  profileCard.appendChild(plusBtn);
}

function showAddChipDialog() {
  const chipName = prompt('Enter new chip name:');
  if (chipName && chipName.trim()) {
    addNewChip(chipName.trim());
  }
}

function addNewChip(name) {
  const profileCard = document.querySelector('.user-profile-card');
  if (!profileCard) return;
  
  const goalsSection = profileCard.querySelector('.profile-goals');
  if (!goalsSection) return;
  
  const newTag = document.createElement('span');
  newTag.className = 'tag';
  newTag.textContent = name;
  goalsSection.appendChild(newTag);
  
  // Add to userData
  if (!userData.customChips) userData.customChips = [];
  userData.customChips.push(name);
}

// Drag and drop handlers
let draggedElement = null;

function handleDragStart(e) {
  draggedElement = e.target;
  e.target.style.opacity = '0.5';
}

function handleDragOver(e) {
  e.preventDefault();
  e.dataTransfer.dropEffect = 'move';
}

function handleDrop(e) {
  e.preventDefault();
  if (e.target.classList.contains('tag') && draggedElement !== e.target) {
    const parent = e.target.parentNode;
    const allTags = Array.from(parent.children);
    const draggedIndex = allTags.indexOf(draggedElement);
    const targetIndex = allTags.indexOf(e.target);
    
    if (draggedIndex < targetIndex) {
      parent.insertBefore(draggedElement, e.target);
    } else {
      parent.insertBefore(e.target, draggedElement);
    }
  }
  
  if (draggedElement) {
    draggedElement.style.opacity = '1';
    draggedElement = null;
  }
}