// Copyright 2000-2024 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license.

package org.jetbrains.kotlin.idea.quickfix

import com.intellij.codeInspection.util.IntentionName
import com.intellij.modcommand.ActionContext
import com.intellij.modcommand.ModPsiUpdater
import org.jetbrains.kotlin.config.ApiVersion
import org.jetbrains.kotlin.idea.base.projectStructure.languageVersionSettings
import org.jetbrains.kotlin.idea.base.psi.replaced
import org.jetbrains.kotlin.idea.base.resources.KotlinBundle
import org.jetbrains.kotlin.idea.codeinsight.api.applicable.intentions.KotlinPsiUpdateModCommandAction
import org.jetbrains.kotlin.psi.KtExpression
import org.jetbrains.kotlin.psi.KtPsiFactory
import org.jetbrains.kotlin.psi.createExpressionByPattern
import org.jetbrains.kotlin.psi.psiUtil.endOffset

class NumberConversionFix(
    element: KtExpression,
    elementContext: ElementContext,
    private val conversionType: ConversionType = ConversionType.EXPRESSION,
) : KotlinPsiUpdateModCommandAction.ElementBased<KtExpression, NumberConversionFix.ElementContext>(element, elementContext) {

    enum class ConversionType(val intentionText: (String) -> @IntentionName String) {
        EXPRESSION({ KotlinBundle.message("convert.expression.to.0", it) }),
        LEFT_HAND_SIDE({ KotlinBundle.message("convert.left.hand.side.to.0", it) }),
        RIGHT_HAND_SIDE({ KotlinBundle.message("convert.right.hand.side.to.0", it) }),
    }

    data class ElementContext(
        val typePresentation: String,
        val fromInt: Boolean,
        val fromChar: Boolean,
        val fromFloatOrDouble: Boolean,
        val fromNullable: Boolean,
        val toChar: Boolean,
        val toInt: Boolean,
        val toByteOrShort: Boolean,
    )

    override fun getFamilyName() = KotlinBundle.message("insert.number.conversion")
    override fun getActionName(
        actionContext: ActionContext,
        element: KtExpression,
        elementContext: ElementContext,
    ): String = conversionType.intentionText(elementContext.typePresentation)

    override fun invoke(
        actionContext: ActionContext,
        element: KtExpression,
        elementContext: ElementContext,
        updater: ModPsiUpdater,
    ) {
        val psiFactory = KtPsiFactory(actionContext.project)
        val apiVersion = element.languageVersionSettings.apiVersion
        val dot = if (elementContext.fromNullable) "?." else "."
        val expressionToInsert = when {
            elementContext.fromChar && apiVersion >= ApiVersion.KOTLIN_1_5 ->
                if (elementContext.toInt) {
                    psiFactory.createExpressionByPattern("$0${dot}code", element)
                } else {
                    psiFactory.createExpressionByPattern("$0${dot}code${dot}to$1()", element, elementContext.typePresentation)
                }

            !elementContext.fromInt && elementContext.toChar && apiVersion >= ApiVersion.KOTLIN_1_5 ->
                psiFactory.createExpressionByPattern("$0${dot}toInt()${dot}toChar()", element)

            elementContext.fromFloatOrDouble && elementContext.toByteOrShort && apiVersion >= ApiVersion.KOTLIN_1_3 ->
                psiFactory.createExpressionByPattern("$0${dot}toInt()${dot}to$1()", element, elementContext.typePresentation)

            else ->
                psiFactory.createExpressionByPattern("$0${dot}to$1()", element, elementContext.typePresentation)
        }
        val newExpression = element.replaced(expressionToInsert)
        updater.moveCaretTo(newExpression.endOffset)
    }
}