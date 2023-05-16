import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import java.time.LocalDate
import java.time.format.DateTimeFormatter

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

fun parseArticle(document: Document) = article {
    title {
        document.select("div.bg-white.p-4.mb-5 > h2").text().trim()
    }
    date {
        LocalDate.parse(
            document.select("div.float-left.mr-4").text().trim(),
            DateTimeFormatter.ofPattern("y년 M월 d일")
        )
    }
    content {
        document.select("div.bg-white.p-4.mb-5 > div:nth-child(5)").html()
    }
    files {
        document.select("div.bg-white.p-4.mb-5 > div:nth-child(5) > ul > li > a")
            .eachAttr("href")
            .map { "https://scatch.ssu.ac.kr$it" }
    }
}