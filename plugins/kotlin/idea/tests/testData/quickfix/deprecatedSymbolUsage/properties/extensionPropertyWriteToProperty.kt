// "Replace with 'm'" "true"

private class C {
    var m: String = ""
}

@Deprecated("", ReplaceWith("m"))
private var C.old: String
    get() = m
    set(value) {
        m = value
    }

private fun use(c: C, s: String) {
    c.old<caret> = s
}
// FUS_QUICKFIX_NAME: org.jetbrains.kotlin.idea.quickfix.replaceWith.DeprecatedSymbolUsageFix
// FUS_K2_QUICKFIX_NAME: org.jetbrains.kotlin.idea.k2.codeinsight.fixes.replaceWith.DeprecatedSymbolUsageFix