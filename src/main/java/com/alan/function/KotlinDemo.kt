import org.apache.commons.codec.binary.StringUtils

fun main(args: Array<String>) {
    val c: Int = sum(1,2)
    println("sum==>"+c.toString())
    printSum(getStringLength("kjhdsf8uw3h")!!,2)
    printSum(incrementX(),maxOf(7,9))
    printProduct("2","4")
    forE()
    println(describe("Hello"))

    range()
}

fun sum(a: Int, b: Int) = a + b

fun printSum(a: Int, b: Int) {
    println("sum of $a and $b is ${a + b}")
}

val PI = 3.14
var x = 0

fun incrementX(): Int {
    x += 1
    return x
}

fun maxOf(a: Int, b: Int) = if (a > b) a else b

fun parseInt(str: String) = if (str == null || str.isEmpty()) null else Integer.valueOf(str)

fun printProduct(arg1: String, arg2: String) {
    val x = parseInt(arg1)
    val y = parseInt(arg2)

    // 直接使用 `x * y` 会导致编译错误，因为他们可能为 null
    if (x != null && y != null) {
        // 在空检测后，x 与 y 会自动转换为非空值（non-nullable）
        println(x * y)
    }
    else {
        println("either '$arg1' or '$arg2' is not a number")
    }
}

fun getStringLength(obj: Any) = if (obj !is String || obj == null) null else obj.length

fun forE() {
    val items = listOf("apple", "banana", "orange")
    for (item in items) {
        println(item)
    }
    for (index in items.indices) {
        println("item at $index is ${items[index]}")
    }
    var index = 0
    while (index < items.size) {
        println("item at $index is ${items[index]}")
        index++
    }

    when {
        "orange" in items -> println("juicy")
        "apple" in items -> println("apple is fine too")
    }

    val fruits = listOf("banana", "avocado", "apple", "kiwifruit")
    fruits
            .filter { it.startsWith("a") }
            .sortedBy { it }
            .map { it.toUpperCase() }
            .forEach { println(it) }
}

fun describe(obj: Any): String =
        when (obj) {
            1          -> "One"
            "Hello"    -> "Greeting"
            is Long    -> "Long"
            !is String -> "Not a string"
            else       -> "Unknown"
        }

fun range(){
    val x = 5
    val y = 9
    if (x in 1..y+1) {
        println("fits in range")
    }

    val list = listOf("a", "b", "c")

    if (-1 !in 0..list.lastIndex) {
        println("-1 is out of range")
    }
    if (list.size !in list.indices) {
        println("list size is out of valid list indices range, too")
    }

    for (x in 1..5) {
        print(x)
    }

    println()
    for (x in 1..10 step 2) {
        print(x)
    }
    println()
    for (x in 9 downTo 0 step 3) {
        print(x)
    }
}