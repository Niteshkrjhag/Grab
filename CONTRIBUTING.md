# Contributing to GRAB

Thank you for your interest in contributing to GRAB! This document provides guidelines and information for contributors to ensure a smooth collaboration process.

## 📋 Table of Contents

- [Code of Conduct](#code-of-conduct)
- [Getting Started](#getting-started)
- [Development Setup](#development-setup)
- [Contribution Guidelines](#contribution-guidelines)
- [Code Style and Standards](#code-style-and-standards)
- [Pull Request Process](#pull-request-process)
- [Issue Reporting](#issue-reporting)
- [Feature Requests](#feature-requests)

## 📜 Code of Conduct

This project adheres to a [Code of Conduct](CODE_OF_CONDUCT.md). By participating, you are expected to uphold this code. Please report unacceptable behavior to the project maintainers.

## 🚀 Getting Started

### Prerequisites
- Android Studio Hedgehog (2023.1.1) or later
- JDK 11 or later
- Git for version control
- Basic knowledge of Kotlin and Android development

### Development Setup

1. **Fork the Repository**
   ```bash
   # Click the "Fork" button on GitHub, then clone your fork
   git clone https://github.com/YOUR_USERNAME/Grab.git
   cd Grab
   ```

2. **Set Up Upstream Remote**
   ```bash
   git remote add upstream https://github.com/Niteshkrjhag/Grab.git
   git remote -v  # Verify remotes
   ```

3. **Create Development Branch**
   ```bash
   git checkout -b feature/your-feature-name
   # or
   git checkout -b bugfix/issue-description
   ```

4. **Set Up API Keys**
   ```bash
   # Copy the example file
   cp secrets.properties.example secrets.properties
   
   # Add your API keys (ask maintainers for test keys if needed)
   NEWS_API_KEY="your_test_key"
   GEMINI_API_KEY="your_test_key"
   ```

5. **Build and Test**
   ```bash
   ./gradlew clean build
   ./gradlew test
   ```

## 🛠️ Contribution Guidelines

### Types of Contributions

We welcome the following types of contributions:

#### 🐛 Bug Fixes
- Fix existing issues or bugs
- Improve error handling
- Resolve performance issues

#### ✨ New Features
- Add new functionality
- Enhance existing features
- Improve user experience

#### 📚 Documentation
- Improve README or other docs
- Add code comments
- Create tutorials or guides

#### 🧪 Testing
- Add unit tests
- Write integration tests
- Improve test coverage

#### 🎨 UI/UX Improvements
- Enhance visual design
- Improve accessibility
- Optimize user interactions

### Contribution Areas

| Area | Description | Skill Level |
|------|-------------|-------------|
| **Core Features** | News feed, search, bookmarks | Intermediate |
| **AI Integration** | Gemini API, chatbot features | Advanced |
| **Authentication** | Firebase Auth, Google Sign-In | Intermediate |
| **Database** | Room operations, caching | Intermediate |
| **UI Components** | Jetpack Compose screens | Beginner |
| **Testing** | Unit tests, UI tests | All levels |
| **Documentation** | README, code comments | Beginner |

## 📝 Code Style and Standards

### Kotlin Style Guide

Follow the [official Kotlin coding conventions](https://kotlinlang.org/docs/coding-conventions.html):

```kotlin
// ✅ Good: Proper naming and structure
class ArticleRepository @Inject constructor(
    private val apiService: NewsApi,
    private val localDatabase: NewsDatabase,
    private val cacheManager: CacheManager
) {
    suspend fun getLatestNews(): Result<List<Article>> = withContext(Dispatchers.IO) {
        try {
            val response = apiService.getTopHeadlines()
            localDatabase.articleDao().insertAll(response.articles)
            Result.success(response.articles)
        } catch (exception: Exception) {
            handleNetworkError(exception)
        }
    }
}

// ❌ Bad: Poor naming and structure
class repo(private val api: NewsApi) {
    fun get() = api.getTopHeadlines()
}
```

### Architecture Guidelines

#### MVVM Pattern
```kotlin
// ✅ Good: Proper ViewModel implementation
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val newsUseCases: NewsUseCases,
    private val userManager: LocalUserManager
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()
    
    fun loadNews() {
        viewModelScope.launch {
            newsUseCases.getLatestNews()
                .onSuccess { articles ->
                    _uiState.value = _uiState.value.copy(
                        articles = articles,
                        isLoading = false
                    )
                }
                .onFailure { error ->
                    handleError(error)
                }
        }
    }
}
```

#### Dependency Injection
```kotlin
// ✅ Good: Proper Hilt module
@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    
    @Binds
    abstract fun bindNewsRepository(
        newsRepositoryImpl: NewsRepositoryImpl
    ): NewsRepository
}
```

### UI Development

#### Jetpack Compose Best Practices
```kotlin
// ✅ Good: Reusable, stateless composable
@Composable
fun ArticleCard(
    article: Article,
    onArticleClick: (Article) -> Unit,
    onBookmarkClick: (Article) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onArticleClick(article) },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        // Card content
    }
}

// ❌ Bad: Stateful, tightly coupled
@Composable
fun BadArticleCard(viewModel: HomeViewModel) {
    val articles by viewModel.articles.collectAsState()
    // Direct ViewModel access in UI
}
```

### Testing Standards

#### Unit Tests
```kotlin
class ArticleRepositoryTest {
    
    @Mock
    private lateinit var mockApi: NewsApi
    
    @Mock
    private lateinit var mockDatabase: NewsDatabase
    
    private lateinit var repository: ArticleRepository
    
    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        repository = ArticleRepository(mockApi, mockDatabase)
    }
    
    @Test
    fun `getLatestNews returns success when API call succeeds`() = runTest {
        // Given
        val expectedArticles = listOf(
            Article(title = "Test Article", /* other fields */)
        )
        whenever(mockApi.getTopHeadlines()).thenReturn(
            NewsResponse(articles = expectedArticles)
        )
        
        // When
        val result = repository.getLatestNews()
        
        // Then
        assertTrue(result.isSuccess)
        assertEquals(expectedArticles, result.getOrNull())
    }
}
```

## 🔄 Pull Request Process

### Before Submitting

1. **Sync with Upstream**
   ```bash
   git fetch upstream
   git checkout main
   git merge upstream/main
   git push origin main
   ```

2. **Run Tests**
   ```bash
   ./gradlew test
   ./gradlew connectedAndroidTest  # If device/emulator available
   ./gradlew lint
   ```

3. **Code Review Checklist**
   - [ ] Code follows project style guidelines
   - [ ] All tests pass
   - [ ] New features include appropriate tests
   - [ ] Documentation updated if necessary
   - [ ] No merge conflicts
   - [ ] Commits are well-structured and descriptive

### Pull Request Template

```markdown
## Description
Brief description of changes made.

## Type of Change
- [ ] Bug fix (non-breaking change which fixes an issue)
- [ ] New feature (non-breaking change which adds functionality)
- [ ] Breaking change (fix or feature that would cause existing functionality to not work as expected)
- [ ] Documentation update

## Testing
- [ ] Unit tests pass
- [ ] Integration tests pass
- [ ] Manual testing completed

## Screenshots (if applicable)
Add screenshots to help explain your changes.

## Checklist
- [ ] My code follows the style guidelines of this project
- [ ] I have performed a self-review of my own code
- [ ] I have commented my code, particularly in hard-to-understand areas
- [ ] I have made corresponding changes to the documentation
- [ ] My changes generate no new warnings
- [ ] New and existing unit tests pass locally with my changes
```

### Commit Message Format

Use conventional commit format:

```bash
# Format: type(scope): description

# Examples:
feat(auth): add Google Sign-In integration
fix(database): resolve article caching issue
docs(readme): update installation instructions
test(home): add unit tests for HomeViewModel
refactor(networking): optimize API response handling
```

## 🐛 Issue Reporting

### Bug Reports

Use the bug report template:

```markdown
**Bug Description**
A clear and concise description of what the bug is.

**To Reproduce**
Steps to reproduce the behavior:
1. Go to '...'
2. Click on '....'
3. Scroll down to '....'
4. See error

**Expected Behavior**
A clear and concise description of what you expected to happen.

**Screenshots**
If applicable, add screenshots to help explain your problem.

**Device Information:**
 - Device: [e.g. Pixel 7]
 - OS: [e.g. Android 13]
 - App Version: [e.g. 1.0.0]

**Additional Context**
Add any other context about the problem here.
```

### Performance Issues

Include profiling information:
- Memory usage patterns
- CPU usage during specific operations
- Network performance metrics
- Battery usage analysis

## 💡 Feature Requests

### Feature Request Template

```markdown
**Feature Description**
A clear and concise description of the feature you'd like to see.

**Problem Statement**
Describe the problem this feature would solve.

**Proposed Solution**
Describe how you envision this feature working.

**Alternative Solutions**
Describe any alternative solutions or features you've considered.

**Implementation Considerations**
- Impact on existing features
- Technical complexity
- Performance implications
- UI/UX considerations

**Additional Context**
Add any other context, mockups, or examples about the feature request.
```

## 📞 Getting Help

### Communication Channels

- **GitHub Issues**: For bug reports and feature requests
- **GitHub Discussions**: For general questions and community discussions
- **Email**: Contact maintainers directly for sensitive issues

### Development Support

- **Architecture Questions**: Tag issues with `architecture` label
- **Code Review**: Submit PR and request review from maintainers
- **Testing Help**: Tag issues with `testing` label

## 🏆 Recognition

Contributors will be recognized in:
- README.md contributors section
- Release notes for significant contributions
- GitHub contributor graphs and statistics

Thank you for contributing to GRAB! Your efforts help make this project better for everyone.