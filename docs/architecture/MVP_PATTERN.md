# MVP (Model-View-Presenter) Pattern

## 📖 Overview

MVP is a **derivative of MVC** (Model-View-Controller) that separates the presentation layer from the business logic. It's one of the oldest and most straightforward Android architecture patterns.

---

## 🏗️ Architecture Diagram

```
┌─────────────────────────────────────────────────────────┐
│                     USER INTERACTION                     │
└──────────────────────┬──────────────────────────────────┘
                       │
                       ▼
           ┌───────────────────────┐
           │        VIEW           │
           │  (Activity/Fragment)  │
           │                       │
           │  - Renders UI         │
           │  - Handles clicks     │
           │  - Updates display    │
           └───────────┬───────────┘
                       │
                       │ implements ViewContract
                       │
                       ▼
           ┌───────────────────────┐
           │   VIEW CONTRACT       │
           │    (Interface)        │
           │                       │
           │  - showLoading()      │
           │  - showMovies(List)   │
           │  - showError(msg)     │
           └───────────┬───────────┘
                       │
                       │ called by
                       │
                       ▼
           ┌───────────────────────┐
           │     PRESENTER         │
           │  (Business Logic)     │
           │                       │
           │  - Fetches data       │
           │  - Processes logic    │
           │  - Updates View       │
           └───────────┬───────────┘
                       │
                       │ uses
                       │
                       ▼
           ┌───────────────────────┐
           │       MODEL           │
           │   (Data Layer)        │
           │                       │
           │  - Repository         │
           │  - API Service        │
           │  - Database           │
           └───────────────────────┘
```

---

## 🔑 Key Components

### 1. View
- **Responsibility**: Display data and capture user interactions
- **Implementation**: Activity, Fragment, or custom View
- **Characteristics**:
  - Passive (no business logic)
  - Implements View interface
  - Holds reference to Presenter
  - Lifecycle-aware

### 2. Presenter
- **Responsibility**: Business logic and view updates
- **Implementation**: Plain Kotlin class
- **Characteristics**:
  - Holds reference to View (via interface)
  - Communicates with Model
  - No Android framework dependencies
  - Easy to unit test

### 3. Model
- **Responsibility**: Data operations
- **Implementation**: Repository, API, Database
- **Characteristics**:
  - Data source abstraction
  - Can be shared across features
  - Independent of View and Presenter

---

## 💻 Code Implementation

### Step 1: Define View Contract

```kotlin
// MovieListContract.kt
interface MovieListContract {
    interface View {
        fun showLoading()
        fun hideLoading()
        fun showMovies(movies: List<Movie>)
        fun showError(message: String)
        fun showEmptyState()
    }
    
    interface Presenter {
        fun attachView(view: View)
        fun detachView()
        fun loadMovies()
        fun searchMovies(query: String)
        fun onMovieClick(movie: Movie)
    }
}
```

### Step 2: Implement Model (Repository)

```kotlin
// MovieRepository.kt
class MovieRepository(
    private val apiService: MovieApiService,
    private val movieDao: MovieDao
) {
    suspend fun getPopularMovies(): Result<List<Movie>> {
        return try {
            val response = apiService.getPopularMovies()
            if (response.isSuccessful) {
                response.body()?.let { movies ->
                    // Cache to database
                    movieDao.insertAll(movies)
                    Result.success(movies)
                } ?: Result.failure(Exception("Empty response"))
            } else {
                // Fallback to cached data
                Result.success(movieDao.getAllMovies())
            }
        } catch (e: Exception) {
            // Return cached data on network error
            val cachedMovies = movieDao.getAllMovies()
            if (cachedMovies.isNotEmpty()) {
                Result.success(cachedMovies)
            } else {
                Result.failure(e)
            }
        }
    }
    
    suspend fun searchMovies(query: String): Result<List<Movie>> {
        return try {
            val response = apiService.searchMovies(query)
            if (response.isSuccessful) {
                response.body()?.let { movies ->
                    Result.success(movies)
                } ?: Result.failure(Exception("Empty response"))
            } else {
                Result.failure(Exception("Search failed"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
```

### Step 3: Implement Presenter

```kotlin
// MovieListPresenter.kt
class MovieListPresenter(
    private val repository: MovieRepository,
    private val coroutineScope: CoroutineScope = CoroutineScope(Dispatchers.Main)
) : MovieListContract.Presenter {
    
    private var view: MovieListContract.View? = null
    
    override fun attachView(view: MovieListContract.View) {
        this.view = view
    }
    
    override fun detachView() {
        view = null
        coroutineScope.cancel() // Clean up coroutines
    }
    
    override fun loadMovies() {
        view?.showLoading()
        
        coroutineScope.launch {
            val result = withContext(Dispatchers.IO) {
                repository.getPopularMovies()
            }
            
            view?.hideLoading()
            
            result.fold(
                onSuccess = { movies ->
                    if (movies.isEmpty()) {
                        view?.showEmptyState()
                    } else {
                        view?.showMovies(movies)
                    }
                },
                onFailure = { error ->
                    view?.showError(error.message ?: "Unknown error")
                }
            )
        }
    }
    
    override fun searchMovies(query: String) {
        if (query.isEmpty()) {
            loadMovies()
            return
        }
        
        view?.showLoading()
        
        coroutineScope.launch {
            val result = withContext(Dispatchers.IO) {
                repository.searchMovies(query)
            }
            
            view?.hideLoading()
            
            result.fold(
                onSuccess = { movies ->
                    if (movies.isEmpty()) {
                        view?.showEmptyState()
                    } else {
                        view?.showMovies(movies)
                    }
                },
                onFailure = { error ->
                    view?.showError(error.message ?: "Search failed")
                }
            )
        }
    }
    
    override fun onMovieClick(movie: Movie) {
        // Navigate to details (through View callback)
        // view?.navigateToDetails(movie.id)
    }
}
```

### Step 4: Implement View (Activity)

```kotlin
// MovieListActivity.kt
class MovieListActivity : AppCompatActivity(), MovieListContract.View {
    
    private lateinit var binding: ActivityMovieListBinding
    private lateinit var presenter: MovieListContract.Presenter
    private lateinit var adapter: MovieAdapter
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMovieListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        // Initialize Presenter
        val repository = MovieRepository(
            apiService = RetrofitClient.movieApi,
            movieDao = AppDatabase.getInstance(this).movieDao()
        )
        presenter = MovieListPresenter(repository)
        presenter.attachView(this)
        
        setupRecyclerView()
        setupListeners()
        
        // Load initial data
        presenter.loadMovies()
    }
    
    private fun setupRecyclerView() {
        adapter = MovieAdapter { movie ->
            presenter.onMovieClick(movie)
        }
        binding.recyclerView.apply {
            layoutManager = GridLayoutManager(this@MovieListActivity, 2)
            adapter = this@MovieListActivity.adapter
        }
    }
    
    private fun setupListeners() {
        binding.swipeRefresh.setOnRefreshListener {
            presenter.loadMovies()
        }
        
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let { presenter.searchMovies(it) }
                return true
            }
            
            override fun onQueryTextChange(newText: String?): Boolean {
                // Debounce search (use Handler or Coroutines)
                return false
            }
        })
    }
    
    // View Contract Implementation
    override fun showLoading() {
        binding.progressBar.visibility = View.VISIBLE
        binding.recyclerView.visibility = View.GONE
        binding.errorView.visibility = View.GONE
    }
    
    override fun hideLoading() {
        binding.progressBar.visibility = View.GONE
        binding.swipeRefresh.isRefreshing = false
    }
    
    override fun showMovies(movies: List<Movie>) {
        binding.recyclerView.visibility = View.VISIBLE
        binding.errorView.visibility = View.GONE
        adapter.submitList(movies)
    }
    
    override fun showError(message: String) {
        binding.recyclerView.visibility = View.GONE
        binding.errorView.visibility = View.VISIBLE
        binding.errorMessage.text = message
        binding.retryButton.setOnClickListener {
            presenter.loadMovies()
        }
    }
    
    override fun showEmptyState() {
        binding.recyclerView.visibility = View.GONE
        binding.errorView.visibility = View.VISIBLE
        binding.errorMessage.text = "No movies found"
    }
    
    override fun onDestroy() {
        super.onDestroy()
        presenter.detachView()
    }
}
```

---

## ✅ Pros

| Advantage | Explanation |
|-----------|-------------|
| **Simple to understand** | Clear separation: View displays, Presenter decides, Model provides |
| **Testable** | Presenter has no Android dependencies, easy to unit test |
| **Separation of concerns** | View doesn't know about Model, Model doesn't know about View |
| **Reusable Presenter** | Same Presenter can work with different Views (Activity/Fragment) |

---

## ❌ Cons

| Disadvantage | Explanation |
|--------------|-------------|
| **Memory leaks** | Presenter holds View reference; must detach carefully |
| **Boilerplate code** | Lots of interfaces and contracts to maintain |
| **Tight coupling** | View and Presenter are tightly coupled through interface |
| **Lifecycle issues** | Manual lifecycle management required |
| **Difficult with Compose** | Not designed for reactive UI (better with XML views) |

---

## 🧪 Testing Strategy

### Unit Testing Presenter

```kotlin
class MovieListPresenterTest {
    
    private lateinit var presenter: MovieListPresenter
    private lateinit var mockView: MovieListContract.View
    private lateinit var mockRepository: MovieRepository
    
    @Before
    fun setup() {
        mockView = mock()
        mockRepository = mock()
        presenter = MovieListPresenter(mockRepository, TestCoroutineScope())
        presenter.attachView(mockView)
    }
    
    @Test
    fun `loadMovies success shows movies`() = runTest {
        // Given
        val movies = listOf(Movie(1, "Test Movie", "poster.jpg"))
        whenever(mockRepository.getPopularMovies()).thenReturn(Result.success(movies))
        
        // When
        presenter.loadMovies()
        advanceUntilIdle()
        
        // Then
        verify(mockView).showLoading()
        verify(mockView).hideLoading()
        verify(mockView).showMovies(movies)
        verify(mockView, never()).showError(any())
    }
    
    @Test
    fun `loadMovies failure shows error`() = runTest {
        // Given
        val error = Exception("Network error")
        whenever(mockRepository.getPopularMovies()).thenReturn(Result.failure(error))
        
        // When
        presenter.loadMovies()
        advanceUntilIdle()
        
        // Then
        verify(mockView).showLoading()
        verify(mockView).hideLoading()
        verify(mockView).showError("Network error")
        verify(mockView, never()).showMovies(any())
    }
    
    @After
    fun tearDown() {
        presenter.detachView()
    }
}
```

---

## 🎯 When to Use MVP

### ✅ Good For:
- **Small to medium apps** with straightforward UI
- **Legacy projects** already using MVP
- **Teams familiar with MVP** from other platforms
- **XML-based UI** (not reactive)
- **Learning architecture** (simple concept)

### ❌ Not Ideal For:
- **Large-scale apps** (too much boilerplate)
- **Jetpack Compose** (not lifecycle-aware)
- **Complex state management** (better options exist)
- **Modern Android development** (MVVM is standard)

---

## 🔄 Migration Path

### From MVP to MVVM:
1. Replace Presenter with ViewModel
2. Use LiveData/StateFlow instead of View callbacks
3. Remove View interface (no contract needed)
4. Let View observe ViewModel directly

---

## 📚 Key Takeaways

1. **MVP separates concerns** but requires manual wiring
2. **Great for learning** architecture fundamentals
3. **Testing is easy** since Presenter is plain Kotlin
4. **Memory leak prevention** is crucial (detach in onDestroy)
5. **MVVM has largely replaced MVP** in modern Android

---

## 🔗 Related Patterns

- **MVC**: MVP is an evolution of MVC for Android
- **MVVM**: Android's recommended replacement for MVP
- **MVI**: Extends MVP concepts with unidirectional data flow

---

## 📖 Further Reading

- [Android MVP Architecture](https://antonioleiva.com/mvp-android/)
- [Google's MVP Sample](https://github.com/android/architecture-samples/tree/todo-mvp/)
- [MVP vs MVVM Comparison](https://medium.com/upday-devs/android-architecture-patterns-part-2-model-view-presenter-8a6faaae14a5)

