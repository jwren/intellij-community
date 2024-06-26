/*
 * Copyright 2000-2012 JetBrains s.r.o.
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
package org.jetbrains.jps.model.serialization.module;

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.jps.model.library.sdk.JpsSdkType;
import org.jetbrains.jps.model.module.JpsModule;
import org.jetbrains.jps.model.serialization.JpsMacroExpander;

import java.util.List;

@ApiStatus.Internal
public abstract class JpsModuleClasspathSerializer {
  private final String myClasspathId;

  protected JpsModuleClasspathSerializer(String classpathId) {
    myClasspathId = classpathId;
  }

  public final String getClasspathId() {
    return myClasspathId;
  }

  public abstract void loadClasspath(@NotNull JpsModule module,
                                     @Nullable String classpathDir,
                                     @NotNull String baseModulePath,
                                     JpsMacroExpander expander,
                                     List<String> paths,
                                     JpsSdkType<?> projectSdkType);
}
