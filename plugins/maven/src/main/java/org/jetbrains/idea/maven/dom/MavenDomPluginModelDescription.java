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
package org.jetbrains.idea.maven.dom;

import com.intellij.openapi.module.Module;
import com.intellij.psi.xml.XmlFile;
import com.intellij.psi.xml.XmlTag;
import com.intellij.util.xml.DomFileDescription;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.idea.maven.dom.plugin.MavenDomPluginModel;

public final class MavenDomPluginModelDescription extends DomFileDescription<MavenDomPluginModel> {
  public MavenDomPluginModelDescription() {
    super(MavenDomPluginModel.class, "plugin");
  }

  @Override
  public boolean isMyFile(@NotNull XmlFile file, @Nullable Module module) {
    XmlTag rootTag = file.getRootTag();
    assert rootTag != null; // rootTag.getName() == "plugin"

    return rootTag.findFirstSubTag("mojos") != null && rootTag.findFirstSubTag("artifactId") != null;
  }
}
