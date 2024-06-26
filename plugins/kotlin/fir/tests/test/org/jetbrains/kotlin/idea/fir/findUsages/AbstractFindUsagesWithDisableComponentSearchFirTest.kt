// Copyright 2000-2022 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.

package org.jetbrains.kotlin.idea.fir.findUsages

import org.jetbrains.kotlin.findUsages.AbstractFindUsagesWithDisableComponentSearchTest
import org.jetbrains.kotlin.idea.fir.invalidateCaches
import org.jetbrains.kotlin.idea.test.runAll

abstract class AbstractFindUsagesWithDisableComponentSearchFirTest : AbstractFindUsagesWithDisableComponentSearchTest() {

    override val ignoreLog: Boolean get() = true

    override fun tearDown() {
        runAll(
            { project.invalidateCaches() },
            { super.tearDown() },
        )
    }
}

