// "Replace with 'newFun(b, a, null)'" "true"

@Deprecated("", ReplaceWith("newFun(b, a, null)"))
fun oldFun(a: Int, b: String) {
}

fun newFun(b: String, a: Int, x: Any?){}

fun foo() {
    <caret>oldFun(1, "x")
}

// FUS_QUICKFIX_NAME: org.jetbrains.kotlin.idea.quickfix.replaceWith.DeprecatedSymbolUsageFix
// FUS_K2_QUICKFIX_NAME: org.jetbrains.kotlin.idea.k2.codeinsight.fixes.replaceWith.DeprecatedSymbolUsageFix