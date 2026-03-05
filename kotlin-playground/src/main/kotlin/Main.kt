import java.security.MessageDigest

/**
 * Kotlin 练习入口。
 * 直接在这里写函数，在 main 里调用即可运行。
 * 命令行运行：./gradlew :kotlin-playground:run
 */

fun main() {
    val bean = Bean().apply {
        this.name = "hello"
    }
    print(md5("hello"))

}
fun md5(input: String): String =
    "md5_"+MessageDigest.getInstance("MD5")
        .digest(input.toByteArray(Charsets.UTF_8))
        .joinToString("") { "%02x".format(it) }

data class Bean(var name: String? = null)


// ========== 在这里写你的 Kotlin 练习代码 ==========

// 示例：扩展函数
// fun String.reversedWords(): String = split(" ").map { it.reversed() }.joinToString(" ")
// fun exampleExtension() {
//     println("hello world".reversedWords()) // olleh dlrow
// }
