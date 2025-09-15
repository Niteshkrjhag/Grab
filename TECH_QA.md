# GRAB - Technical Implementation Q&A

This document covers technical implementation questions about specific technologies, frameworks, and coding decisions in the GRAB project.

## 🎯 Android Development Fundamentals

### Q1: Why did you choose Jetpack Compose over traditional XML layouts?

**Answer:**

**Technical Advantages:**
```kotlin
// Traditional XML approach
// layout_article_card.xml (80+ lines of XML)
// ArticleCardViewHolder.kt (50+ lines of binding code)
// Requires findViewById, manual state management, complex animations

// Jetpack Compose approach
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
            .clickable { onArticleClick(article) }
            .animateContentSize(), // Built-in animations
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            AsyncImage(
                model = article.imageUrl,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .clip(RoundedCornerShape(8.dp))
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = article.title,
                style = MaterialTheme.typography.headlineSmall,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = article.source.name,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                
                IconButton(onClick = { onBookmarkClick(article) }) {
                    Icon(
                        imageVector = if (article.isBookmarked) {
                            Icons.Filled.Bookmark
                        } else {
                            Icons.Outlined.BookmarkBorder
                        },
                        contentDescription = "Bookmark article",
                        tint = if (article.isBookmarked) {
                            MaterialTheme.colorScheme.primary
                        } else {
                            MaterialTheme.colorScheme.onSurface
                        }
                    )
                }
            }
        }
    }
}
```

**Key Benefits:**
1. **Declarative UI**: Describe what the UI should look like, not how to build it
2. **Less Code**: 50% reduction in UI-related code compared to XML + ViewBinding
3. **Type Safety**: Compile-time checks for all UI components
4. **Performance**: Fewer view layers, better rendering performance
5. **Animations**: Built-in support for smooth transitions
6. **State Management**: Automatic UI updates when state changes

### Q2: Explain your MVVM implementation and why you chose it over MVP or MVC.

**Answer:**

**MVVM Implementation:**
```kotlin
// Model (Domain Layer)
data class Article(
    val title: String,
    val description: String,
    val url: String,
    val imageUrl: String?,
    val publishedAt: String,
    val source: Source,
    val isBookmarked: Boolean = false
)

// View (Compose UI)
@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val bookmarkedArticles by viewModel.bookmarkedArticles.collectAsState()
    
    // UI reacts automatically to state changes
    when (uiState) {
        is HomeUiState.Loading -> LoadingIndicator()
        is HomeUiState.Success -> {
            ArticleList(
                articles = uiState.articles,
                bookmarkedArticles = bookmarkedArticles,
                onArticleClick = viewModel::onArticleClicked,
                onBookmarkClick = viewModel::toggleBookmark,
                onRefresh = viewModel::refreshNews
            )
        }
        is HomeUiState.Error -> ErrorMessage(
            message = uiState.message,
            onRetry = viewModel::refreshNews
        )
    }
}

// ViewModel (Presentation Logic)
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getLatestNewsUseCase: GetLatestNewsUseCase,
    private val toggleBookmarkUseCase: ToggleBookmarkUseCase,
    private val analyticsManager: AnalyticsManager
) : ViewModel() {
    
    private val _uiState = MutableStateFlow<HomeUiState>(HomeUiState.Loading)
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()
    
    val bookmarkedArticles: StateFlow<List<Article>> = 
        getBookmarkedArticlesUseCase()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = emptyList()
            )
    
    init {
        loadNews()
    }
    
    fun loadNews() {
        viewModelScope.launch {
            _uiState.value = HomeUiState.Loading
            
            getLatestNewsUseCase()
                .onSuccess { articles ->
                    _uiState.value = HomeUiState.Success(articles)
                    analyticsManager.trackEvent("news_loaded", mapOf("count" to articles.size))
                }
                .onFailure { error ->
                    _uiState.value = HomeUiState.Error(error.message ?: "Unknown error")
                    analyticsManager.trackError("news_load_failed", error)
                }
        }
    }
    
    fun toggleBookmark(article: Article) {
        viewModelScope.launch {
            toggleBookmarkUseCase(article)
                .onSuccess {
                    analyticsManager.trackEvent("article_bookmarked", mapOf(
                        "article_url" to article.url,
                        "action" to if (article.isBookmarked) "removed" else "added"
                    ))
                }
        }
    }
    
    fun onArticleClicked(article: Article) {
        analyticsManager.trackEvent("article_clicked", mapOf("article_url" to article.url))
        // Navigation handled by UI layer
    }
    
    fun refreshNews() {
        loadNews() // Reuse existing logic
    }
}

// UI State Management
sealed class HomeUiState {
    object Loading : HomeUiState()
    data class Success(val articles: List<Article>) : HomeUiState()
    data class Error(val message: String) : HomeUiState()
}
```

**Why MVVM over MVP/MVC:**

| Pattern | Pros | Cons | Best For |
|---------|------|------|----------|
| **MVC** | Simple, familiar | Tight coupling, hard to test | Simple apps |
| **MVP** | Testable, separation | Boilerplate, memory leaks | Legacy Android |
| **MVVM** | Reactive, lifecycle-aware, testable | Learning curve | Modern Android |

**MVVM Advantages in Android:**
1. **Lifecycle Awareness**: ViewModels survive configuration changes
2. **Reactive Programming**: Automatic UI updates with StateFlow/LiveData
3. **Testability**: ViewModels don't depend on Android framework
4. **Separation**: Clear boundaries between UI and business logic

### Q3: How do you handle dependency injection with Hilt? Walk me through your setup.

**Answer:**

**Hilt Module Structure:**
```kotlin
// 1. Application Class
@HiltAndroidApp
class NewsApplication : Application()

// 2. Database Module
@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    
    @Provides
    @Singleton
    fun provideNewsDatabase(@ApplicationContext context: Context): NewsDatabase {
        return Room.databaseBuilder(
            context,
            NewsDatabase::class.java,
            "news_database"
        )
        .addMigrations(MIGRATION_1_2, MIGRATION_2_3)
        .fallbackToDestructiveMigration() // Only for development
        .build()
    }
    
    @Provides
    fun provideArticleDao(database: NewsDatabase): ArticleDao = database.articleDao()
    
    @Provides
    fun provideUserDao(database: NewsDatabase): UserDao = database.userDao()
}

// 3. Network Module
@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    
    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(ApiKeyInterceptor())
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = if (BuildConfig.DEBUG) {
                    HttpLoggingInterceptor.Level.BODY
                } else {
                    HttpLoggingInterceptor.Level.NONE
                }
            })
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build()
    }
    
    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.NEWS_API_BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(
                GsonBuilder()
                    .setDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
                    .create()
            ))
            .build()
    }
    
    @Provides
    @Singleton
    fun provideNewsApi(retrofit: Retrofit): NewsApi = retrofit.create(NewsApi::class.java)
}

// 4. Repository Module
@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    
    @Binds
    abstract fun bindNewsRepository(
        newsRepositoryImpl: NewsRepositoryImpl
    ): NewsRepository
    
    @Binds
    abstract fun bindAuthRepository(
        authRepositoryImpl: AuthRepositoryImpl
    ): AuthRepository
    
    @Binds
    abstract fun bindUserManager(
        localUserManagerImpl: LocalUserManagerImpl
    ): LocalUserManager
}

// 5. Use Case Module
@Module
@InstallIn(ViewModelComponent::class)
abstract class UseCaseModule {
    
    @Binds
    abstract fun bindGetLatestNewsUseCase(
        getLatestNewsUseCaseImpl: GetLatestNewsUseCaseImpl
    ): GetLatestNewsUseCase
    
    @Binds
    abstract fun bindToggleBookmarkUseCase(
        toggleBookmarkUseCaseImpl: ToggleBookmarkUseCaseImpl
    ): ToggleBookmarkUseCase
}

// 6. Qualifier Annotations for multiple implementations
@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class LocalDataSource

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class RemoteDataSource

@Module
@InstallIn(SingletonComponent::class)
abstract class DataSourceModule {
    
    @Binds
    @LocalDataSource
    abstract fun bindLocalDataSource(
        newsLocalDataSourceImpl: NewsLocalDataSourceImpl
    ): NewsDataSource
    
    @Binds
    @RemoteDataSource
    abstract fun bindRemoteDataSource(
        newsRemoteDataSourceImpl: NewsRemoteDataSourceImpl
    ): NewsDataSource
}

// Usage in Repository
@Singleton
class NewsRepositoryImpl @Inject constructor(
    @LocalDataSource private val localDataSource: NewsDataSource,
    @RemoteDataSource private val remoteDataSource: NewsDataSource,
    private val cacheManager: CacheManager
) : NewsRepository {
    // Implementation using both data sources
}
```

**ViewModel Injection:**
```kotlin
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getLatestNewsUseCase: GetLatestNewsUseCase,
    private val toggleBookmarkUseCase: ToggleBookmarkUseCase,
    private val analyticsManager: AnalyticsManager,
    @ApplicationContext private val context: Context
) : ViewModel() {
    // ViewModel implementation
}

// Usage in Composable
@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel() // Automatic injection
) {
    // UI implementation
}
```

**Testing with Hilt:**
```kotlin
// Test Module
@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [RepositoryModule::class]
)
abstract class TestRepositoryModule {
    
    @Binds
    abstract fun bindTestNewsRepository(
        fakeNewsRepository: FakeNewsRepository
    ): NewsRepository
}

// Test with Hilt
@HiltAndroidTest
class HomeScreenTest {
    
    @get:Rule
    val hiltRule = HiltAndroidRule(this)
    
    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()
    
    @Before
    fun setup() {
        hiltRule.inject()
    }
    
    @Test
    fun homeScreen_displaysArticles() {
        composeTestRule.setContent {
            HomeScreen()
        }
        
        composeTestRule.onNodeWithText("Test Article").assertIsDisplayed()
    }
}
```

## 🗄️ Database & Data Management

### Q4: Why did you choose Room over other database solutions?

**Answer:**

**Room Implementation Example:**
```kotlin
// Entity
@Entity(tableName = "articles")
data class ArticleEntity(
    @PrimaryKey val url: String,
    val title: String,
    val description: String?,
    val content: String?,
    @ColumnInfo(name = "image_url") val imageUrl: String?,
    @ColumnInfo(name = "published_at") val publishedAt: String,
    @ColumnInfo(name = "source_id") val sourceId: String,
    @ColumnInfo(name = "source_name") val sourceName: String,
    @ColumnInfo(name = "is_bookmarked") val isBookmarked: Boolean = false,
    @ColumnInfo(name = "created_at") val createdAt: Long = System.currentTimeMillis()
)

// DAO with advanced queries
@Dao
interface ArticleDao {
    
    @Query("SELECT * FROM articles ORDER BY published_at DESC")
    fun getAllArticlesFlow(): Flow<List<ArticleEntity>>
    
    @Query("""
        SELECT * FROM articles 
        WHERE title LIKE '%' || :query || '%' 
           OR description LIKE '%' || :query || '%'
        ORDER BY published_at DESC
        LIMIT :limit
    """)
    suspend fun searchArticles(query: String, limit: Int): List<ArticleEntity>
    
    @Query("SELECT * FROM articles WHERE is_bookmarked = 1 ORDER BY created_at DESC")
    fun getBookmarkedArticlesFlow(): Flow<List<ArticleEntity>>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(articles: List<ArticleEntity>)
    
    @Update
    suspend fun updateArticle(article: ArticleEntity)
    
    @Query("DELETE FROM articles WHERE is_bookmarked = 0 AND created_at < :timestamp")
    suspend fun deleteOldArticles(timestamp: Long)
    
    @Transaction
    suspend fun refreshArticles(newArticles: List<ArticleEntity>) {
        deleteOldArticles(System.currentTimeMillis() - TimeUnit.DAYS.toMillis(7))
        insertAll(newArticles)
    }
}

// Database with migrations
@Database(
    entities = [ArticleEntity::class, SourceEntity::class, UserEntity::class],
    version = 3,
    exportSchema = true
)
@TypeConverters(Converters::class)
abstract class NewsDatabase : RoomDatabase() {
    abstract fun articleDao(): ArticleDao
    abstract fun sourceDao(): SourceDao
    abstract fun userDao(): UserDao
    
    companion object {
        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE articles ADD COLUMN is_bookmarked INTEGER NOT NULL DEFAULT 0")
            }
        }
        
        val MIGRATION_2_3 = object : Migration(2, 3) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("""
                    CREATE TABLE IF NOT EXISTS sources (
                        id TEXT NOT NULL PRIMARY KEY,
                        name TEXT NOT NULL,
                        description TEXT,
                        url TEXT,
                        category TEXT
                    )
                """)
            }
        }
    }
}

// Type Converters for complex objects
class Converters {
    @TypeConverter
    fun fromSource(source: Source): String {
        return Gson().toJson(source)
    }
    
    @TypeConverter
    fun toSource(sourceJson: String): Source {
        return Gson().fromJson(sourceJson, Source::class.java)
    }
    
    @TypeConverter
    fun fromStringList(list: List<String>): String {
        return list.joinToString(",")
    }
    
    @TypeConverter
    fun toStringList(data: String): List<String> {
        return if (data.isEmpty()) emptyList() else data.split(",")
    }
}
```

**Why Room over alternatives:**

| Database | Pros | Cons | Use Case |
|----------|------|------|----------|
| **Room** | Type safety, SQL validation, migration support | Learning curve | Complex queries, relational data |
| **SQLite** | Direct control, performance | Boilerplate, error-prone | Simple apps, legacy code |
| **Realm** | Easy to use, object database | Large binary size, vendor lock-in | Rapid prototyping |
| **DataStore** | Type safety, async | No complex queries | Simple key-value storage |

**Room Advantages:**
1. **Compile-time SQL verification**: Catches SQL errors at build time
2. **Type safety**: Automatic conversion between database and Kotlin types
3. **Migration support**: Structured schema evolution
4. **Reactive queries**: Automatic UI updates with Flow/LiveData
5. **Performance**: Built on SQLite with optimizations

### Q5: How do you handle data synchronization between local and remote sources?

**Answer:**

**Repository Pattern with Smart Caching:**
```kotlin
@Singleton
class NewsRepositoryImpl @Inject constructor(
    private val localDataSource: NewsLocalDataSource,
    private val remoteDataSource: NewsRemoteDataSource,
    private val cacheManager: CacheManager,
    private val networkMonitor: NetworkMonitor
) : NewsRepository {
    
    override fun getLatestNews(): Flow<Resource<List<Article>>> = flow {
        // Emit loading state
        emit(Resource.Loading())
        
        // Always emit local data first (offline-first approach)
        val localArticles = localDataSource.getAllArticles()
        if (localArticles.isNotEmpty()) {
            emit(Resource.Success(localArticles))
        }
        
        // Try to fetch fresh data if online
        if (networkMonitor.isOnline()) {
            try {
                val remoteArticles = remoteDataSource.getLatestNews()
                
                // Update local database
                localDataSource.refreshArticles(remoteArticles)
                
                // Update cache timestamp
                cacheManager.updateLastFetchTime()
                
                // Emit fresh data
                emit(Resource.Success(remoteArticles))
            } catch (exception: Exception) {
                // If we have local data, show it with error
                if (localArticles.isNotEmpty()) {
                    emit(Resource.Error(
                        message = "Failed to fetch latest news",
                        data = localArticles
                    ))
                } else {
                    emit(Resource.Error("No internet connection and no cached data"))
                }
            }
        }
    }.distinctUntilChanged()
    
    override suspend fun bookmarkArticle(article: Article): Result<Unit> {
        return try {
            // Update locally immediately for responsive UI
            val updatedArticle = article.copy(
                isBookmarked = !article.isBookmarked,
                lastModified = System.currentTimeMillis()
            )
            
            localDataSource.updateArticle(updatedArticle)
            
            // Queue for remote sync when online
            if (networkMonitor.isOnline()) {
                try {
                    remoteDataSource.updateBookmark(updatedArticle.url, updatedArticle.isBookmarked)
                } catch (exception: Exception) {
                    // Queue for later sync
                    enqueueSyncOperation(
                        SyncOperation.BookmarkUpdate(
                            articleUrl = updatedArticle.url,
                            isBookmarked = updatedArticle.isBookmarked,
                            timestamp = updatedArticle.lastModified
                        )
                    )
                }
            } else {
                // Queue for sync when online
                enqueueSyncOperation(
                    SyncOperation.BookmarkUpdate(
                        articleUrl = updatedArticle.url,
                        isBookmarked = updatedArticle.isBookmarked,
                        timestamp = updatedArticle.lastModified
                    )
                )
            }
            
            Result.success(Unit)
        } catch (exception: Exception) {
            Result.failure(exception)
        }
    }
    
    private suspend fun enqueueSyncOperation(operation: SyncOperation) {
        syncQueue.enqueue(operation)
        scheduleSyncWorker()
    }
}

// Resource wrapper for handling loading/success/error states
sealed class Resource<T>(
    val data: T? = null,
    val message: String? = null
) {
    class Success<T>(data: T) : Resource<T>(data)
    class Error<T>(message: String, data: T? = null) : Resource<T>(data, message)
    class Loading<T>(data: T? = null) : Resource<T>(data)
}

// Smart cache manager
@Singleton
class CacheManager @Inject constructor(
    private val dataStore: DataStore<Preferences>
) {
    private val CACHE_EXPIRY_TIME = 5 * 60 * 1000L // 5 minutes
    private val LAST_FETCH_TIME = longPreferencesKey("last_fetch_time")
    
    suspend fun isCacheValid(): Boolean {
        val lastFetchTime = dataStore.data.first()[LAST_FETCH_TIME] ?: 0L
        return (System.currentTimeMillis() - lastFetchTime) < CACHE_EXPIRY_TIME
    }
    
    suspend fun updateLastFetchTime() {
        dataStore.edit { preferences ->
            preferences[LAST_FETCH_TIME] = System.currentTimeMillis()
        }
    }
}

// Sync worker for background synchronization
class NewsSyncWorker @AssistedInject constructor(
    @Assisted private val context: Context,
    @Assisted private val params: WorkerParameters,
    private val newsRepository: NewsRepository,
    private val syncQueue: SyncQueue
) : CoroutineWorker(context, params) {
    
    override suspend fun doWork(): Result {
        return try {
            // Process pending sync operations
            val pendingOperations = syncQueue.getAllPending()
            
            pendingOperations.forEach { operation ->
                when (operation) {
                    is SyncOperation.BookmarkUpdate -> {
                        newsRepository.syncBookmark(operation.articleUrl, operation.isBookmarked)
                        syncQueue.markCompleted(operation.id)
                    }
                    is SyncOperation.ReadStatusUpdate -> {
                        newsRepository.syncReadStatus(operation.articleUrl, operation.isRead)
                        syncQueue.markCompleted(operation.id)
                    }
                }
            }
            
            Result.success()
        } catch (exception: Exception) {
            if (runAttemptCount < 3) {
                Result.retry()
            } else {
                Result.failure()
            }
        }
    }
    
    @AssistedFactory
    interface Factory {
        fun create(context: Context, params: WorkerParameters): NewsSyncWorker
    }
}
```

## 🌐 Networking & API Integration

### Q6: Explain your Retrofit setup and how you handle API responses.

**Answer:**

**Comprehensive Retrofit Configuration:**
```kotlin
// API Interface
interface NewsApi {
    
    @GET("top-headlines")
    suspend fun getTopHeadlines(
        @Query("apiKey") apiKey: String = BuildConfig.NEWS_API_KEY,
        @Query("country") country: String = "us",
        @Query("category") category: String? = null,
        @Query("pageSize") pageSize: Int = 20,
        @Query("page") page: Int = 1
    ): Response<NewsResponse>
    
    @GET("everything")
    suspend fun searchNews(
        @Query("apiKey") apiKey: String = BuildConfig.NEWS_API_KEY,
        @Query("q") query: String,
        @Query("sortBy") sortBy: String = "publishedAt",
        @Query("pageSize") pageSize: Int = 20,
        @Query("page") page: Int = 1,
        @Query("from") fromDate: String? = null,
        @Query("to") toDate: String? = null
    ): Response<NewsResponse>
    
    @POST("bookmarks")
    suspend fun updateBookmark(
        @Header("Authorization") token: String,
        @Body request: BookmarkRequest
    ): Response<BookmarkResponse>
    
    @GET("sources")
    suspend fun getSources(
        @Query("apiKey") apiKey: String = BuildConfig.NEWS_API_KEY,
        @Query("language") language: String = "en",
        @Query("country") country: String = "us"
    ): Response<SourcesResponse>
}

// Response DTOs
data class NewsResponse(
    val status: String,
    val totalResults: Int,
    val articles: List<ArticleDto>
)

data class ArticleDto(
    val title: String,
    val description: String?,
    val content: String?,
    val url: String,
    val urlToImage: String?,
    val publishedAt: String,
    val source: SourceDto
) {
    fun toDomainModel(): Article {
        return Article(
            title = title,
            description = description ?: "",
            content = content ?: "",
            url = url,
            imageUrl = urlToImage,
            publishedAt = publishedAt,
            source = source.toDomainModel()
        )
    }
}

// Network layer implementation
@Singleton
class NewsRemoteDataSourceImpl @Inject constructor(
    private val newsApi: NewsApi,
    private val networkErrorHandler: NetworkErrorHandler
) : NewsRemoteDataSource {
    
    override suspend fun getLatestNews(): List<Article> = withContext(Dispatchers.IO) {
        try {
            val response = newsApi.getTopHeadlines()
            
            when {
                response.isSuccessful -> {
                    response.body()?.articles?.map { it.toDomainModel() } ?: emptyList()
                }
                response.code() == 429 -> {
                    throw RateLimitException("API rate limit exceeded")
                }
                response.code() == 401 -> {
                    throw AuthenticationException("Invalid API key")
                }
                else -> {
                    throw NetworkException("API call failed: ${response.message()}")
                }
            }
        } catch (exception: Exception) {
            when (exception) {
                is IOException -> throw NetworkException("Network error: ${exception.message}")
                is HttpException -> {
                    val errorBody = exception.response()?.errorBody()?.string()
                    val errorResponse = parseErrorResponse(errorBody)
                    throw ApiException(errorResponse.message)
                }
                else -> throw exception
            }
        }
    }
    
    override suspend fun searchNews(query: String): List<Article> = withContext(Dispatchers.IO) {
        if (query.isBlank()) throw IllegalArgumentException("Search query cannot be empty")
        
        try {
            val response = newsApi.searchNews(query = query)
            response.body()?.articles?.map { it.toDomainModel() } ?: emptyList()
        } catch (exception: Exception) {
            networkErrorHandler.handleError(exception)
        }
    }
    
    private fun parseErrorResponse(errorBody: String?): ErrorResponse {
        return try {
            Gson().fromJson(errorBody, ErrorResponse::class.java)
        } catch (e: Exception) {
            ErrorResponse("Unknown error occurred")
        }
    }
}

// Custom error handling
@Singleton
class NetworkErrorHandler @Inject constructor() {
    
    fun handleError(exception: Exception): Nothing {
        when (exception) {
            is SocketTimeoutException -> throw NetworkException("Request timed out")
            is UnknownHostException -> throw NetworkException("No internet connection")
            is HttpException -> {
                when (exception.code()) {
                    400 -> throw BadRequestException("Invalid request parameters")
                    401 -> throw AuthenticationException("Authentication failed")
                    403 -> throw AuthorizationException("Access denied")
                    404 -> throw NotFoundException("Resource not found")
                    429 -> throw RateLimitException("Too many requests")
                    in 500..599 -> throw ServerException("Server error")
                    else -> throw NetworkException("HTTP ${exception.code()}: ${exception.message()}")
                }
            }
            else -> throw NetworkException("Network error: ${exception.message}")
        }
    }
}

// Custom exceptions
sealed class NetworkException(message: String) : Exception(message)
class AuthenticationException(message: String) : NetworkException(message)
class AuthorizationException(message: String) : NetworkException(message)
class RateLimitException(message: String) : NetworkException(message)
class ServerException(message: String) : NetworkException(message)
class BadRequestException(message: String) : NetworkException(message)
class NotFoundException(message: String) : NetworkException(message)
class ApiException(message: String) : NetworkException(message)

// Interceptors
class ApiKeyInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()
        
        // Add API key if not already present
        val url = if (original.url.queryParameter("apiKey") == null) {
            original.url.newBuilder()
                .addQueryParameter("apiKey", BuildConfig.NEWS_API_KEY)
                .build()
        } else {
            original.url
        }
        
        val request = original.newBuilder()
            .url(url)
            .addHeader("User-Agent", "GrabNewsApp/${BuildConfig.VERSION_NAME}")
            .build()
        
        return chain.proceed(request)
    }
}

class AuthInterceptor @Inject constructor(
    private val tokenManager: TokenManager
) : Interceptor {
    
    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()
        
        val token = tokenManager.getAccessToken()
        val request = if (token != null) {
            original.newBuilder()
                .addHeader("Authorization", "Bearer $token")
                .build()
        } else {
            original
        }
        
        val response = chain.proceed(request)
        
        // Handle token refresh on 401
        if (response.code == 401 && token != null) {
            response.close()
            
            return try {
                val newToken = tokenManager.refreshToken()
                val newRequest = original.newBuilder()
                    .addHeader("Authorization", "Bearer $newToken")
                    .build()
                chain.proceed(newRequest)
            } catch (e: Exception) {
                tokenManager.clearTokens()
                response
            }
        }
        
        return response
    }
}
```

### Q7: How do you implement pagination with Paging 3 library?

**Answer:**

**Complete Paging 3 Implementation:**
```kotlin
// 1. PagingSource for network data
class NewsPagingSource @Inject constructor(
    private val newsApi: NewsApi,
    private val query: String? = null
) : PagingSource<Int, Article>() {
    
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Article> {
        return try {
            val page = params.key ?: 1
            val pageSize = params.loadSize
            
            val response = if (query.isNullOrBlank()) {
                newsApi.getTopHeadlines(page = page, pageSize = pageSize)
            } else {
                newsApi.searchNews(query = query, page = page, pageSize = pageSize)
            }
            
            if (response.isSuccessful) {
                val articles = response.body()?.articles?.map { it.toDomainModel() } ?: emptyList()
                
                LoadResult.Page(
                    data = articles,
                    prevKey = if (page == 1) null else page - 1,
                    nextKey = if (articles.isEmpty()) null else page + 1
                )
            } else {
                LoadResult.Error(NetworkException("Failed to load news: ${response.message()}"))
            }
        } catch (exception: Exception) {
            LoadResult.Error(exception)
        }
    }
    
    override fun getRefreshKey(state: PagingState<Int, Article>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }
}

// 2. RemoteMediator for offline support
@OptIn(ExperimentalPagingApi::class)
class NewsRemoteMediator @Inject constructor(
    private val newsApi: NewsApi,
    private val database: NewsDatabase,
    private val query: String?
) : RemoteMediator<Int, ArticleEntity>() {
    
    private val articleDao = database.articleDao()
    private val remoteKeyDao = database.remoteKeyDao()
    
    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, ArticleEntity>
    ): MediatorResult {
        return try {
            val page = when (loadType) {
                LoadType.REFRESH -> 1
                LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)
                LoadType.APPEND -> {
                    val remoteKey = database.withTransaction {
                        remoteKeyDao.getRemoteKey(query ?: "general")
                    }
                    remoteKey?.nextPage ?: return MediatorResult.Success(endOfPaginationReached = true)
                }
            }
            
            val response = if (query.isNullOrBlank()) {
                newsApi.getTopHeadlines(page = page, pageSize = state.config.pageSize)
            } else {
                newsApi.searchNews(query = query, page = page, pageSize = state.config.pageSize)
            }
            
            val articles = response.body()?.articles ?: emptyList()
            val endOfPaginationReached = articles.isEmpty()
            
            database.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    if (query.isNullOrBlank()) {
                        articleDao.clearAll()
                    } else {
                        articleDao.clearSearchResults(query)
                    }
                    remoteKeyDao.deleteByQuery(query ?: "general")
                }
                
                val nextPage = if (endOfPaginationReached) null else page + 1
                remoteKeyDao.insertOrReplace(
                    RemoteKeyEntity(
                        query = query ?: "general",
                        nextPage = nextPage
                    )
                )
                
                articleDao.insertAll(articles.map { it.toEntity() })
            }
            
            MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        } catch (exception: Exception) {
            MediatorResult.Error(exception)
        }
    }
}

// 3. Repository with Paging
@Singleton
class NewsRepositoryImpl @Inject constructor(
    private val newsApi: NewsApi,
    private val database: NewsDatabase
) : NewsRepository {
    
    override fun getLatestNews(): Flow<PagingData<Article>> {
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                prefetchDistance = 5,
                enablePlaceholders = false,
                initialLoadSize = 20
            ),
            remoteMediator = NewsRemoteMediator(newsApi, database, null),
            pagingSourceFactory = { database.articleDao().getAllArticlesPaging() }
        ).flow.map { pagingData ->
            pagingData.map { it.toDomainModel() }
        }
    }
    
    override fun searchNews(query: String): Flow<PagingData<Article>> {
        return Pager(
            config = PagingConfig(pageSize = 20),
            pagingSourceFactory = { NewsPagingSource(newsApi, query) }
        ).flow
    }
}

// 4. ViewModel with Paging
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val newsRepository: NewsRepository
) : ViewModel() {
    
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()
    
    val newsFlow: Flow<PagingData<Article>> = searchQuery
        .debounce(300) // Wait for user to stop typing
        .distinctUntilChanged()
        .flatMapLatest { query ->
            if (query.isBlank()) {
                newsRepository.getLatestNews()
            } else {
                newsRepository.searchNews(query)
            }
        }
        .cachedIn(viewModelScope)
    
    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }
}

// 5. Compose UI with Paging
@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel()
) {
    val newsFlow = viewModel.newsFlow.collectAsLazyPagingItems()
    val searchQuery by viewModel.searchQuery.collectAsState()
    
    Column {
        SearchBar(
            query = searchQuery,
            onQueryChange = viewModel::updateSearchQuery,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        )
        
        LazyColumn {
            items(newsFlow) { article ->
                article?.let {
                    ArticleCard(
                        article = it,
                        onArticleClick = { /* Navigate to details */ },
                        onBookmarkClick = { /* Toggle bookmark */ }
                    )
                }
            }
            
            // Handle loading states
            newsFlow.apply {
                when {
                    loadState.refresh is LoadState.Loading -> {
                        item { LoadingIndicator() }
                    }
                    loadState.append is LoadState.Loading -> {
                        item { LoadingIndicator() }
                    }
                    loadState.refresh is LoadState.Error -> {
                        val error = newsFlow.loadState.refresh as LoadState.Error
                        item {
                            ErrorMessage(
                                message = error.error.localizedMessage ?: "Unknown error",
                                onRetry = { newsFlow.retry() }
                            )
                        }
                    }
                    loadState.append is LoadState.Error -> {
                        val error = newsFlow.loadState.append as LoadState.Error
                        item {
                            ErrorMessage(
                                message = error.error.localizedMessage ?: "Error loading more",
                                onRetry = { newsFlow.retry() }
                            )
                        }
                    }
                }
            }
        }
    }
}

// 6. Remote Keys Entity for pagination
@Entity(tableName = "remote_keys")
data class RemoteKeyEntity(
    @PrimaryKey val query: String,
    val nextPage: Int?
)

@Dao
interface RemoteKeyDao {
    @Query("SELECT * FROM remote_keys WHERE query = :query")
    suspend fun getRemoteKey(query: String): RemoteKeyEntity?
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrReplace(remoteKey: RemoteKeyEntity)
    
    @Query("DELETE FROM remote_keys WHERE query = :query")
    suspend fun deleteByQuery(query: String)
}
```

## 🤖 AI Integration

### Q8: How did you integrate the Gemini AI API and handle its responses?

**Answer:**

**Complete Gemini AI Integration:**
```kotlin
// 1. Gemini Configuration
@Module
@InstallIn(SingletonComponent::class)
object GeminiModule {
    
    @Provides
    @Singleton
    fun provideGenerativeModel(): GenerativeModel {
        return GenerativeModel(
            modelName = "gemini-pro",
            apiKey = BuildConfig.GEMINI_API_KEY,
            generationConfig = generationConfig {
                temperature = 0.7f // Balance creativity and consistency
                topK = 40
                topP = 0.95f
                maxOutputTokens = 1024
                stopSequences = listOf("END")
            },
            safetySettings = listOf(
                SafetySetting(HarmCategory.HARASSMENT, BlockThreshold.MEDIUM_AND_ABOVE),
                SafetySetting(HarmCategory.HATE_SPEECH, BlockThreshold.MEDIUM_AND_ABOVE),
                SafetySetting(HarmCategory.SEXUALLY_EXPLICIT, BlockThreshold.MEDIUM_AND_ABOVE),
                SafetySetting(HarmCategory.DANGEROUS_CONTENT, BlockThreshold.MEDIUM_AND_ABOVE)
            )
        )
    }
}

// 2. Chat Repository Implementation
@Singleton
class GeminiChatRepositoryImpl @Inject constructor(
    private val generativeModel: GenerativeModel,
    private val chatDao: ChatDao,
    private val promptBuilder: PromptBuilder
) : GeminiChatRepository {
    
    // In-memory cache for recent responses
    private val responseCache = LruCache<String, String>(50)
    
    override suspend fun sendMessage(
        message: String,
        context: ChatContext? = null
    ): Flow<ChatResponse> = flow {
        try {
            emit(ChatResponse.Loading)
            
            // Check cache first for common queries
            val cacheKey = generateCacheKey(message, context)
            responseCache.get(cacheKey)?.let { cachedResponse ->
                emit(ChatResponse.Success(cachedResponse))
                return@flow
            }
            
            // Build context-aware prompt
            val prompt = promptBuilder.buildPrompt(message, context)
            
            // Generate response with streaming
            val chat = generativeModel.startChat()
            chat.sendMessageStream(prompt).collect { chunk ->
                chunk.text?.let { text ->
                    emit(ChatResponse.Streaming(text))
                }
            }
            
            // Get final response
            val finalResponse = chat.history.lastOrNull()?.parts?.firstOrNull()?.text
            
            if (finalResponse != null) {
                // Cache the response
                responseCache.put(cacheKey, finalResponse)
                
                // Save to database
                saveChatMessage(message, finalResponse, context)
                
                emit(ChatResponse.Success(finalResponse))
            } else {
                emit(ChatResponse.Error("Failed to generate response"))
            }
            
        } catch (exception: Exception) {
            emit(ChatResponse.Error(handleGeminiError(exception)))
        }
    }.flowOn(Dispatchers.IO)
    
    override suspend fun summarizeArticle(article: Article): Result<String> = 
        withContext(Dispatchers.IO) {
            try {
                val prompt = promptBuilder.buildSummarizationPrompt(article)
                
                val response = generativeModel.generateContent(prompt)
                val summary = response.text ?: "Unable to generate summary"
                
                // Save summary for future reference
                saveSummary(article.url, summary)
                
                Result.success(summary)
            } catch (exception: Exception) {
                Result.failure(GeminiException(handleGeminiError(exception)))
            }
        }
    
    override suspend fun analyzeNewsContext(article: Article): Result<NewsAnalysis> = 
        withContext(Dispatchers.IO) {
            try {
                val prompt = promptBuilder.buildAnalysisPrompt(article)
                
                val response = generativeModel.generateContent(prompt)
                val analysisText = response.text ?: throw GeminiException("No analysis generated")
                
                val analysis = parseAnalysisResponse(analysisText)
                Result.success(analysis)
            } catch (exception: Exception) {
                Result.failure(GeminiException(handleGeminiError(exception)))
            }
        }
    
    private fun handleGeminiError(exception: Exception): String {
        return when (exception) {
            is GoogleGenerativeAIException -> {
                when (exception.message?.contains("quota", ignoreCase = true)) {
                    true -> "API quota exceeded. Please try again later."
                    else -> "AI service temporarily unavailable."
                }
            }
            is IOException -> "Network error. Please check your connection."
            else -> "An unexpected error occurred: ${exception.message}"
        }
    }
    
    private suspend fun saveChatMessage(
        message: String,
        response: String,
        context: ChatContext?
    ) {
        val chatMessage = ChatMessageEntity(
            message = message,
            response = response,
            articleUrl = context?.articleUrl,
            context = context?.type?.name,
            timestamp = System.currentTimeMillis()
        )
        chatDao.insertMessage(chatMessage)
    }
}

// 3. Prompt Builder for context-aware conversations
@Singleton
class PromptBuilder @Inject constructor() {
    
    fun buildPrompt(message: String, context: ChatContext?): String {
        return when (context?.type) {
            ChatContextType.ARTICLE_SUMMARY -> buildArticleSummaryPrompt(message, context.article)
            ChatContextType.NEWS_ANALYSIS -> buildNewsAnalysisPrompt(message, context.article)
            ChatContextType.GENERAL_CHAT -> buildGeneralChatPrompt(message)
            null -> buildGeneralChatPrompt(message)
        }
    }
    
    fun buildSummarizationPrompt(article: Article): String {
        return """
            Please provide a comprehensive yet concise summary of this news article in exactly 80 words.
            Focus on the key facts, main points, and significance of the news.
            
            Article Title: ${article.title}
            Article Content: ${article.content.take(2000)}
            
            Requirements:
            - Exactly 80 words
            - Include key facts and main points
            - Maintain neutral tone
            - Highlight significance or impact
            
            Summary:
        """.trimIndent()
    }
    
    fun buildAnalysisPrompt(article: Article): String {
        return """
            Analyze this news article and provide insights in the following format:
            
            Article: ${article.title}
            Content: ${article.content.take(1500)}
            
            Please provide:
            1. IMPACT: What is the potential impact of this news?
            2. CONTEXT: What background information helps understand this better?
            3. IMPLICATIONS: What are the broader implications?
            4. STAKEHOLDERS: Who are the key people/organizations affected?
            5. TRENDS: How does this relate to current trends?
            
            Keep each section concise but informative.
        """.trimIndent()
    }
    
    private fun buildArticleSummaryPrompt(message: String, article: Article?): String {
        return if (article != null) {
            """
                Context: The user is asking about this news article:
                Title: ${article.title}
                Content: ${article.content.take(1000)}
                
                User Question: $message
                
                Please answer the user's question based on the article content. 
                If the question cannot be answered from the article, say so clearly.
            """.trimIndent()
        } else {
            buildGeneralChatPrompt(message)
        }
    }
    
    private fun buildGeneralChatPrompt(message: String): String {
        return """
            You are GrabBot, an AI assistant for a news application called GRAB.
            You help users understand news, current events, and provide insights.
            
            Guidelines:
            - Be helpful, accurate, and concise
            - Focus on news and current events when possible
            - If you don't know something, admit it
            - Provide balanced perspectives on controversial topics
            
            User: $message
            
            Assistant:
        """.trimIndent()
    }
}

// 4. Chat ViewModel with streaming support
@HiltViewModel
class ChatViewModel @Inject constructor(
    private val geminiChatRepository: GeminiChatRepository,
    private val analyticsManager: AnalyticsManager
) : ViewModel() {
    
    private val _chatState = MutableStateFlow(ChatUiState())
    val chatState: StateFlow<ChatUiState> = _chatState.asStateFlow()
    
    private val _isTyping = MutableStateFlow(false)
    val isTyping: StateFlow<Boolean> = _isTyping.asStateFlow()
    
    fun sendMessage(message: String, article: Article? = null) {
        viewModelScope.launch {
            val trimmedMessage = message.trim()
            if (trimmedMessage.isBlank()) return@launch
            
            // Add user message to chat
            addMessageToChat(ChatMessage.User(trimmedMessage))
            
            // Show typing indicator
            _isTyping.value = true
            
            val context = article?.let {
                ChatContext(
                    type = ChatContextType.ARTICLE_SUMMARY,
                    article = it,
                    articleUrl = it.url
                )
            }
            
            // Track analytics
            analyticsManager.trackEvent("chat_message_sent", mapOf(
                "has_context" to (context != null),
                "message_length" to trimmedMessage.length
            ))
            
            geminiChatRepository.sendMessage(trimmedMessage, context)
                .collect { response ->
                    when (response) {
                        is ChatResponse.Loading -> {
                            // Typing indicator already shown
                        }
                        is ChatResponse.Streaming -> {
                            updateStreamingResponse(response.text)
                        }
                        is ChatResponse.Success -> {
                            _isTyping.value = false
                            finalizeResponse(response.text)
                        }
                        is ChatResponse.Error -> {
                            _isTyping.value = false
                            addMessageToChat(ChatMessage.Error(response.message))
                            analyticsManager.trackError("chat_message_failed", Exception(response.message))
                        }
                    }
                }
        }
    }
    
    fun summarizeArticle(article: Article) {
        viewModelScope.launch {
            _isTyping.value = true
            
            geminiChatRepository.summarizeArticle(article)
                .onSuccess { summary ->
                    _isTyping.value = false
                    addMessageToChat(ChatMessage.Summary(summary, article))
                }
                .onFailure { error ->
                    _isTyping.value = false
                    addMessageToChat(ChatMessage.Error("Failed to summarize: ${error.message}"))
                }
        }
    }
    
    private fun addMessageToChat(message: ChatMessage) {
        _chatState.value = _chatState.value.copy(
            messages = _chatState.value.messages + message
        )
    }
    
    private fun updateStreamingResponse(text: String) {
        val messages = _chatState.value.messages.toMutableList()
        val lastMessage = messages.lastOrNull()
        
        if (lastMessage is ChatMessage.Assistant && lastMessage.isStreaming) {
            messages[messages.lastIndex] = lastMessage.copy(
                content = lastMessage.content + text
            )
        } else {
            messages.add(ChatMessage.Assistant(text, isStreaming = true))
        }
        
        _chatState.value = _chatState.value.copy(messages = messages)
    }
    
    private fun finalizeResponse(text: String) {
        val messages = _chatState.value.messages.toMutableList()
        val lastMessage = messages.lastOrNull()
        
        if (lastMessage is ChatMessage.Assistant && lastMessage.isStreaming) {
            messages[messages.lastIndex] = lastMessage.copy(
                content = text,
                isStreaming = false
            )
        }
        
        _chatState.value = _chatState.value.copy(messages = messages)
    }
}

// 5. Data classes for chat functionality
data class ChatUiState(
    val messages: List<ChatMessage> = emptyList(),
    val isLoading: Boolean = false
)

sealed class ChatMessage {
    data class User(val content: String) : ChatMessage()
    data class Assistant(
        val content: String,
        val isStreaming: Boolean = false
    ) : ChatMessage()
    data class Summary(
        val content: String,
        val article: Article
    ) : ChatMessage()
    data class Error(val content: String) : ChatMessage()
}

sealed class ChatResponse {
    object Loading : ChatResponse()
    data class Streaming(val text: String) : ChatResponse()
    data class Success(val text: String) : ChatResponse()
    data class Error(val message: String) : ChatResponse()
}

data class ChatContext(
    val type: ChatContextType,
    val article: Article?,
    val articleUrl: String?
)

enum class ChatContextType {
    ARTICLE_SUMMARY,
    NEWS_ANALYSIS,
    GENERAL_CHAT
}

// 6. Compose UI for Chat
@Composable
fun ChatScreen(
    article: Article? = null,
    viewModel: ChatViewModel = hiltViewModel()
) {
    val chatState by viewModel.chatState.collectAsState()
    val isTyping by viewModel.isTyping.collectAsState()
    var messageText by remember { mutableStateOf("") }
    
    Column(modifier = Modifier.fillMaxSize()) {
        // Chat messages
        LazyColumn(
            modifier = Modifier.weight(1f),
            reverseLayout = true
        ) {
            // Typing indicator
            if (isTyping) {
                item {
                    TypingIndicator()
                }
            }
            
            // Chat messages
            items(chatState.messages.reversed()) { message ->
                ChatMessageItem(message = message)
            }
        }
        
        // Quick actions for article context
        if (article != null) {
            QuickActionRow(
                onSummarize = { viewModel.summarizeArticle(article) },
                onAnalyze = { viewModel.sendMessage("Analyze this article", article) }
            )
        }
        
        // Message input
        MessageInputField(
            text = messageText,
            onTextChange = { messageText = it },
            onSendClick = {
                viewModel.sendMessage(messageText, article)
                messageText = ""
            },
            enabled = !isTyping
        )
    }
}
```

This comprehensive technical Q&A covers the major implementation details of the GRAB project, demonstrating deep understanding of modern Android development practices, architecture patterns, and specific technology choices.