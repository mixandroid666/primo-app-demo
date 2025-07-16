import com.example.primo_app_demo.data.repository.ArticleRepository
import com.example.primo_app_demo.domain.model.Article
import com.example.primo_app_demo.ui.home.HomeUIState
import com.example.primo_app_demo.ui.home.HomeViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Before
import org.junit.Test
import io.mockk.*
import org.junit.Assert.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
class HomeViewModelTest {

    private lateinit var viewModel: HomeViewModel
    private lateinit var repository: ArticleRepository

    // Test dispatcher for running coroutines in a controlled environment
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        MockKAnnotations.init(this) // Initialize MockK
        Dispatchers.setMain(testDispatcher)
        repository = mockk()
        viewModel = HomeViewModel(repository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        unmockkAll() // Clear all MockK mocks
    }

    @Test
    fun `loadArticles should set uiState to Loading initially`() = runTest {
        // Arrange
        coEvery { repository.loadArticles() } returns emptyList()

        // Act
        viewModel.loadArticles()

        // Assert
        assertEquals(HomeUIState.Loading, viewModel.uiState)
    }

    @Test
    fun `loadArticles should set uiState to Success when repository returns articles`() = runTest {
        // Arrange
        val mockArticles = listOf(
            Article(title = "Test Article 1", link = "link1", pubDate = "2025-07-15", description = "Description 1"),
            Article(title = "Test Article 2", link = "link2", pubDate = "2025-07-16", description = "Description 2")
        )
        coEvery { repository.loadArticles() } returns mockArticles

        // Act
        viewModel.loadArticles()
        advanceUntilIdle() // Advance coroutines to complete operations

        // Assert
        assertEquals(HomeUIState.Success(mockArticles), viewModel.uiState)
    }

    @Test
    fun `loadArticles should set uiState to Empty when repository returns empty list`() = runTest {
        // Arrange
        coEvery { repository.loadArticles() } returns emptyList()

        // Act
        viewModel.loadArticles()
        advanceUntilIdle()

        // Assert
        assertEquals(HomeUIState.Empty, viewModel.uiState)
    }

    @Test
    fun `loadArticles should set uiState to Error when repository throws exception`() = runTest {
        // Arrange
        val mockErrorMessage = "Some error occurred"
        coEvery { repository.loadArticles() } throws Exception(mockErrorMessage)

        // Act
        viewModel.loadArticles()
        advanceUntilIdle()

        // Assert
        assertEquals(HomeUIState.Error(mockErrorMessage), viewModel.uiState)
    }
}