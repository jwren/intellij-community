// "Replace with 'c1.newFun(this, c2)'" "true"
// WITH_STDLIB

class X {
    @Deprecated("", ReplaceWith("c1.newFun(this, c2)"))
    fun oldFun(c1: Char, c2: Char): Char = c1.newFun(this, c2)

    val c: Char = 'a'
}

fun Char.newFun(x: X, c: Char): Char = this

fun foo(s: String, x: X) {
    val chars = s.filter {
        O.x?.<caret>oldFun(it, x.c) != 'a'
    }
}

object O {
    var x: X? = null
}

// FUS_QUICKFIX_NAME: org.jetbrains.kotlin.idea.quickfix.replaceWith.DeprecatedSymbolUsageFix
// FUS_K2_QUICKFIX_NAME: org.jetbrains.kotlin.idea.k2.codeinsight.fixes.replaceWith.DeprecatedSymbolUsageFix