import java.time.LocalDate

data class Article(
    val title: String,
    val date: LocalDate,
    val content: String,
    val files: List<String>
)