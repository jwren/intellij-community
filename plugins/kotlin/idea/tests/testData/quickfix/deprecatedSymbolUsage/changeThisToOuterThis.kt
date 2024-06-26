// "Replace with 's.newFun(this)'" "true"

open class Base {
    @Deprecated("", ReplaceWith("s.newFun(this)"))
    fun oldFun(s: String){}

    open inner class Inner
}

class Derived : Base() {
    inner class InnerDerived : Base.Inner() {
        fun foo() {
            <caret>oldFun("a")
        }
    }
}

fun String.newFun(x: Base){}

// FUS_QUICKFIX_NAME: org.jetbrains.kotlin.idea.quickfix.replaceWith.DeprecatedSymbolUsageFix
// FUS_K2_QUICKFIX_NAME: org.jetbrains.kotlin.idea.k2.codeinsight.fixes.replaceWith.DeprecatedSymbolUsageFix