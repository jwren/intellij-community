// FIX: Remove 'val' from parameter
open class Base(open <caret>val x: Int) {
    val y = x
}

class Derived(y: Int) : Base(y)
// FUS_QUICKFIX_NAME: org.jetbrains.kotlin.idea.inspections.CanBeParameterInspection$RemoveValVarFix