// "Replace with 's.newFun(this)'" "true"
// WITH_STDLIB

class X {
    @Deprecated("", ReplaceWith("s.newFun(this)"))
    fun oldFun(s: String): String = s.newFun(this)
}

fun String.newFun(x: X): String = this

fun foo(x: X?, p: Boolean) {
    val v = if (p)
        x?.<caret>oldFun("a")
    else
        null
}

// FUS_QUICKFIX_NAME: org.jetbrains.kotlin.idea.quickfix.replaceWith.DeprecatedSymbolUsageFix
// FUS_K2_QUICKFIX_NAME: org.jetbrains.kotlin.idea.k2.codeinsight.fixes.replaceWith.DeprecatedSymbolUsageFix