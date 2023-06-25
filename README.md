# 총학공지 펀시스템 크롤러

### 리스폰스 스키마 (Article.kt)
```kotlin
// Article.kt

data class Article(
    val title: String, // 제목
    val date: LocalDate, // 게시글 작성일
    val content: String, // 게시글 내용 (HTML)
    val files: List<String> // 첨부파일 CDN 링크
)
```

### 엔티티 빌더 (ArticleBuilder.kt)
```kotlin
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
```
사용법
```kotlin
val response: Article = article {
    title { "Title" }
    date { LocalDate.parse() }
    content { "Content" }
    files { ListOf("LINK") }
}
```

### 크롤러 (Main.kt)
```kotlin
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import org.jsoup.Jsoup
import org.jsoup.nodes.Document

fun main() = runBlocking {
    val document = async { connectPages("https://scatch.ssu.ac.kr/%ea%b3%b5%ec%a7%80%ec%82%ac%ed%95%ad/") }
    val links = getArticlesPage(document.await())

    links.map {
        async { connectPages(it) }
    }.map { parseArticle(it.await()) }
        .forEach(::println)
}

fun getArticlesPage(pages: Document) = pages.select(".notice_col3 > a")
    .eachAttr("href")
    .map { it!! }

fun connectPages(page: String): Document = Jsoup.connect(page).get()
```
논블로킹으로 하면 더 빠를것 같아서 해봤는데 똑같아요 그냥 비동기 신경 안쓰고 짜면 될것 같음.