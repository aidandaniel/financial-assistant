# FinSight AI - Internal Documentation

**CONFIDENTIAL - INTERNAL USE ONLY**

## Overview
FinSight AI is a comprehensive financial planning web application designed to help users manage their finances, set goals, and receive personalized financial guidance.

## Authentication System

### Mock Implementation (Current)
The application currently uses a mock authentication system for development:

```javascript
class MockAuth {
  // Simulates Firebase Auth behavior
  // Accepts any valid email + 6+ char password
  // Provides session management
}
```

**Test Credentials:**
- Email: Any valid email (e.g., `user@example.com`)
- Password: Any 6+ character password (e.g., `password123`)

### Production Migration
To implement real Firebase:

1. **Replace MockAuth class** with Firebase imports:
```javascript
import { initializeApp } from "https://www.gstatic.com/firebasejs/10.12.0/firebase-app.js";
import { getAuth, onAuthStateChanged, signInWithEmailAndPassword, ... } from "https://www.gstatic.com/firebasejs/10.12.0/firebase-auth.js";
```

2. **Update Firebase Config** with real project credentials
3. **Remove Mock System** entirely
4. **Test All Auth Flows**

## Application Architecture

### File Structure
```
financial-assistant/
|-- index.html          # Main application markup
|-- script.js           # Application logic and auth
|-- styles.css          # Complete styling system
|-- .docs/              # Internal documentation
```

### Key Components

#### Authentication Flow
1. **Login Screen**: Email/password or Google OAuth
2. **Auth State Listener**: Automatic routing based on login status
3. **Session Management**: Persistent user data during session

#### User Onboarding
1. **Welcome**: Name collection and personalization
2. **Details**: Age verification (18+) and Canadian bank selection
3. **Goals**: Multi-select financial goals
4. **Timeline**: Goal completion planning (1-10 years)
5. **Assessment**: Optional financial literacy quiz

#### Dashboard System
- **Account Overview**: Balance tracking and bank integration
- **Investment Portfolio**: Goal-based recommendations
- **Timeline Planning**: Personalized financial milestones
- **Quick Actions**: Transaction viewing, budget setting
- **AI Assistant**: Keyword-based financial Q&A

## Styling System

### CSS Architecture
- **CSS Variables**: Consistent theming throughout
- **Component-Based**: Modular styling approach
- **Responsive Design**: Mobile-first breakpoints

### Key Color Palette
```css
:root {
  --primary-color: #0ea5e9;    /* Brand blue */
  --text-primary: #0f172a;     /* Main text */
  --text-muted: #64748b;       /* Secondary text */
  --error-color: #dc2626;      /* Error/warning */
  --background: #ffffff;      /* Main background */
  --border: #e2e8f0;          /* Borders and dividers */
}
```

### Modal System
- **Validation Popups**: Red-themed error/warning dialogs
- **Quiz Modal**: Full-screen assessment interface
- **Responsive Design**: Mobile-optimized modal layouts

## JavaScript Architecture

### Core Functions

#### Authentication
```javascript
handleLogin()           // Email/password authentication
handleRegistration()    // New user account creation
handleGoogleSignIn()    // OAuth integration
```

#### Navigation
```javascript
showScreen(screenId)    // Screen routing system
populateDashboard()     // Dashboard data loading
populateUserProfile()   // User profile updates
```

#### Financial Features
```javascript
getGoalBasedPortfolio() // Investment recommendations
addGoalBasedTimeline()  // Financial planning timeline
getAIResponse(question) // Financial Q&A system
```

### Data Management
- **User Data Object**: Centralized user state
- **Session Persistence**: In-memory data storage
- **State Synchronization**: Real-time UI updates

## Feature Implementation Details

### Bank Validation
Canadian bank validation array:
```javascript
const canadianBanks = [
  'RBC', 'Royal Bank', 'TD', 'Toronto-Dominion', 'Scotiabank',
  'BMO', 'Bank of Montreal', 'CIBC', 'National Bank', 'Desjardins',
  'Tangerine', 'Simplii', 'EQ Bank', 'HSBC', 'ATB Financial',
  // Add more banks as needed
];
```

### Goal System
Supported financial goals:
- `saving` - General savings goals
- `emergency-fund` - Emergency fund building
- `investing` - Investment portfolio growth
- `growing` - Wealth accumulation
- `debt-payoff` - Debt reduction strategies
- `learning` - Financial education

### Literacy Assessment
5-question quiz determining:
- **Novice**: Basic explanations needed
- **Intermediate**: Standard financial guidance
- **Experienced**: Advanced strategies and insights

## Development Guidelines

### Code Standards
- **ES6+ Features**: Modern JavaScript patterns
- **Semantic HTML**: Proper element usage
- **CSS Methodology**: BEM-style naming
- **Error Handling**: Comprehensive try/catch blocks

### Testing Approach
- **Mock Authentication**: Development-friendly auth system
- **Console Logging**: Debug information for troubleshooting
- **Responsive Testing**: Mobile and desktop compatibility

### Performance Considerations
- **Efficient DOM Manipulation**: Minimal reflows/repaints
- **Event Delegation**: Dynamic content handling
- **Lazy Loading**: On-demand component initialization

## Security Considerations

### Current Implementation
- Input validation and sanitization
- Password length requirements (6+ characters)
- Age verification (18+ requirement)
- Secure session management

### Production Requirements
- Real Firebase Authentication
- HTTPS enforcement
- CSRF protection
- Rate limiting for auth attempts
- Environment variable configuration

## Deployment Checklist

### Pre-Deployment
- [ ] Replace mock auth with real Firebase
- [ ] Update Firebase configuration
- [ ] Test all authentication flows
- [ ] Verify responsive design
- [ ] Check browser compatibility

### Post-Deployment
- [ ] Monitor authentication errors
- [ ] Track user journey analytics
- [ ] Performance optimization
- [ ] Security audit completion

## Troubleshooting Guide

### Common Issues

#### Authentication Problems
**Symptoms**: Login not working, auth state not updating
**Solutions**:
1. Check browser console for errors
2. Verify Firebase configuration
3. Test with different browsers
4. Clear browser cache and localStorage

#### Modal Display Issues
**Symptoms**: Popups not appearing, styling problems
**Solutions**:
1. Verify CSS loading
2. Check z-index conflicts
3. Test responsive breakpoints
4. Validate HTML structure

#### Dashboard Loading Problems
**Symptoms**: Empty dashboard, missing user data
**Solutions**:
1. Check userData object population
2. Verify onboarding completion
3. Test populateDashboard() function
4. Check for JavaScript errors

### Debug Mode
Enable detailed logging:
```javascript
console.log('Auth state changed:', user);
console.log('Login attempt started');
console.log('Dashboard population:', userData);
```

## Future Development

### Planned Enhancements
1. **Real Firebase Integration**: Production authentication
2. **Bank API Connectivity**: Live account integration
3. **Advanced AI**: Machine learning recommendations
4. **Budget Management**: Comprehensive budgeting tools
5. **Investment Tracking**: Portfolio performance monitoring

### Technical Improvements
1. **Progressive Web App**: Offline functionality
2. **Push Notifications**: Financial reminders
3. **Data Export**: User data portability
4. **Multi-language Support**: International expansion
5. **Advanced Security**: Enhanced protection measures

## Contact Information

For technical questions or issues:
- Check browser console first
- Review this documentation
- Test development environment
- Verify all prerequisites

---

**Document Version**: 1.0
**Last Updated**: Current development cycle
**Classification**: Internal - Confidential
