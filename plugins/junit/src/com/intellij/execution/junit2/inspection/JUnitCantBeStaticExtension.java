/*
 * Copyright 2000-2009 JetBrains s.r.o.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.intellij.execution.junit2.inspection;

import com.intellij.execution.junit.JUnitUtil;
import com.intellij.openapi.util.Condition;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiMethod;

public final class JUnitCantBeStaticExtension implements Condition<PsiElement> {
  @Override
  public boolean value(PsiElement member) {
    if (member instanceof PsiMethod method) {
      if (JUnitUtil.isTestMethodOrConfig(method)){
        return true;
      }
    }
    return false;
  }
}