import java.util.concurrent.locks.Lock

fun main(args: Array<String>) {

    val people = listOf(Person("hyy", 25), Person("zal", 26))

    //使用lambda表达式过滤集合
    println(people.filter { it.age < 26 })

    //手动过滤集合
    val result = mutableListOf<Person>()

    for (person in people) {
        if (person.age < 26) result.add(person)
    }

    println(result)

    lookForHyy(people)
    lookForHyy2(people)
    lookForHyy3(people)
    lookForHyy5(people)

    //匿名函数 显式指定返回类型
    people.filter (fun (person): Boolean { return person.age < 26 })
    //表达式体匿名函数
    people.filter (fun (person) = person.age < 26)
}

//使用lambda作为函数参数 被编译的时候一般会产生一个匿名对象 这样会很消耗性能
//为了消除lambda带来的运行时开销  可以使用内联函数inline 标识
//inline标识符的作用是函数体会被直接替换到函数被调用的地方
inline fun <T> synchronized(lock: Lock, action: () -> T): T {
    lock.lock()
    try {
        return action()
    }
    finally {
        lock.unlock()
    }
}

fun foo(lock: Lock) {
    println("Before sync")

    synchronized(lock) {
        println("Action")
    }

    println("After sync")
}

//foo 函数被编译后的代码如下

fun _foo_(lock: Lock) {
    println("Before sync")

    lock.lock()
    try {
        println("Action")
    }
    finally {
        lock.unlock()
    }

    println("After sync")
}

//kotlin 中的filter map 默认都是内联函数
data class Person(val name: String, val age: Int)

//lambda 返回语句
//普通循环 寻找hyy
fun lookForHyy(people: List<Person>) {
    for (person in people) {
        if (person.name == "hyy") {
            println("Found")
            return
        }
    }

    println("Hyy is not found")
}

//使用forEach的lambda 寻找hyy
//非局部返回
fun lookForHyy2(people: List<Person>) {
    people.forEach {
        if (it.name == "hyy") {
            println("Found")
            return
        }
    }

    println("Hyy is not found")
}

//局部返回 使用标签
fun lookForHyy3(people: List<Person>) {
    people.forEach label@{
        if (it.name == "hyy") {
            println("Found")
            return@label
        }
    }

    println("Hyy is not found")
}

//局部返回 使用标签
fun lookForHyy4(people: List<Person>) {
    people.forEach {
        if (it.name == "hyy") {
            println("Found")
            return@forEach//使用lambda作为参数的函数的函数名作为标签
        }
    }

    println("Hyy is not found")
}

//局部返回 使用匿名函数
fun lookForHyy5(people: List<Person>) {
    people.forEach(
            fun (person) {
                if (person.name == "hyy") return
                println("${person.name} is not Hyy")
            }
    )

    println("Hyy is not found")
}


