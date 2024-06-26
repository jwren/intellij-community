// "Replace with 'c.newFun(this)'" "true"
// WITH_STDLIB

class X {
    @Deprecated("", ReplaceWith("c.newFun(this)"))
    fun oldFun(c: Char): Char = c.newFun(this)
}

fun Char.newFun(x: X): Char = this

fun foo(x: X?, p: Boolean, s: String) {
    val chars = s.filter {
        val v = if (p)
            x?.<caret>oldFun(it)
        else
            null
        v != 'a'
    }
}

// FUS_QUICKFIX_NAME: org.jetbrains.kotlin.idea.quickfix.replaceWith.DeprecatedSymbolUsageFix
// FUS_K2_QUICKFIX_NAME: org.jetbrains.kotlin.idea.k2.codeinsight.fixes.replaceWith.DeprecatedSymbolUsageFix