import java.time.LocalDate

class ArticleBuilder {
    private lateinit var title: String
    private lateinit var date: LocalDate
    private lateinit var content: String
    private lateinit var files: List<String>

    fun title(predicate: () -> String) {
        this.title = predicate()
    }

    fun date(predicate: () -> LocalDate) {
        this.date = predicate()
    }
    fun content(predicate: () -> String) {
        this.content = predicate()
    }
    fun files(predicate: () -> List<String>) {
        this.files = predicate()
    }

    fun build() = Article(title, date, content, files)
}

fun article(predicate: ArticleBuilder.() -> Unit) = ArticleBuilder().apply(predicate).build()