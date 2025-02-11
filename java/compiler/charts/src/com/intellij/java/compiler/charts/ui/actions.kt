// Copyright 2000-2024 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license.
package com.intellij.java.compiler.charts.ui

import com.intellij.java.compiler.charts.CompilationChartsBundle
import com.intellij.openapi.actionSystem.*
import com.intellij.openapi.fileEditor.OpenFileDescriptor
import com.intellij.openapi.module.ModuleManager
import com.intellij.openapi.options.ShowSettingsUtil
import com.intellij.openapi.project.Project
import com.intellij.openapi.roots.ui.configuration.ProjectStructureConfigurable
import com.intellij.openapi.vfs.LocalFileSystem
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.util.ui.JBUI
import java.awt.Cursor
import java.awt.Font
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import java.awt.font.TextAttribute
import java.nio.file.Paths
import javax.swing.JComponent
import javax.swing.JLabel

interface CompilationChartsAction {
  fun isAccessible(): Boolean
  fun actionPerformed(e: MouseEvent)
  fun position(): Position
  fun label(): JLabel

  enum class Position {
    LEFT,
    RIGHT,
    LIST
  }
}

class OpenDirectoryAction(private val project: Project, private val name: String, private val close: () -> Unit) : CompilationChartsAction {
  override fun isAccessible() = true
  override fun position() = CompilationChartsAction.Position.LEFT
  override fun label(): JLabel = JLabel().apply {
    icon = Settings.Popup.MODULE_IMAGE
    toolTipText = CompilationChartsBundle.message("charts.action.open.module.directory")
    border = JBUI.Borders.emptyRight(10)
    addMouseListener(ActionMouseAdapter(this, this@OpenDirectoryAction))
  }

  override fun actionPerformed(e: MouseEvent) {
    val module = ModuleManager.getInstance(project).findModuleByName(name) ?: return
    val path = findModuleDirectory(module.moduleFilePath)?: return
    close()
    OpenFileDescriptor(project, path, -1).navigate(true)
  }

  private fun findModuleDirectory(path: String): VirtualFile? = LocalFileSystem.getInstance().let { fs ->
    val file = fs.findFileByPath(path)
    if (file != null) return file.parent
    val parent = Paths.get(path).parent ?: return null
    return fs.findFileByPath(parent.toString())
  }
}

class OpenProjectStructureAction(
  private val project: Project,
  private val name: String,
  private val close: () -> Unit,
) : CompilationChartsAction {
  override fun isAccessible() = true
  override fun position() = CompilationChartsAction.Position.RIGHT
  override fun label(): JLabel = JLabel().apply {
    icon = Settings.Popup.EDIT_IMAGE
    toolTipText = CompilationChartsBundle.message("charts.action.open.project.structure")
    border = JBUI.Borders.emptyLeft(10)
    addMouseListener(ActionMouseAdapter(this, this@OpenProjectStructureAction))
  }

  override fun actionPerformed(e: MouseEvent) {
    val module = ModuleManager.getInstance(project).findModuleByName(name) ?: return
    val projectStructure = ProjectStructureConfigurable.getInstance(project)
    ShowSettingsUtil.getInstance().editConfigurable(project, projectStructure) {
      close()
      projectStructure.select(module.name, "Modules", true)
    }
  }
}

class ShowModuleDependenciesAction(
  private val project: Project, private val name: String,
  private val component: JComponent,
  private val close: () -> Unit,
) : CompilationChartsAction {
  private val action = ActionManager.getInstance().getAction("ShowModulesDependencies")

  override fun isAccessible() = action != null

  override fun actionPerformed(e: MouseEvent) {
    @Suppress("UNCHECKED_CAST")
    val context = object : ActionDataContext {
      private val module = ModuleManager.getInstance(project).findModuleByName(name)
      override fun <T : Any?> getData(key: DataKey<T?>): T? = when (key) {
        CommonDataKeys.PROJECT -> project as T
        LangDataKeys.MODULE_CONTEXT_ARRAY -> notNullArrayOf(module) as T
        PlatformCoreDataKeys.CONTEXT_COMPONENT -> component as T
        else -> null
      }
    }

    close()

    action.actionPerformed(AnActionEvent.createEvent(action, context, null, ActionPlaces.POPUP, ActionUiKind.TOOLBAR, e))
  }

  override fun position() = CompilationChartsAction.Position.LIST

  override fun label() = JLabel(action?.templateText).apply {
    foreground = JBUI.CurrentTheme.Link.Foreground.ENABLED
    font = font.deriveFont(Font.PLAIN)

    addMouseListener(ActionMouseAdapter(this, this@ShowModuleDependenciesAction))
  }
}

class ShowMatrixDependenciesAction(
  private val project: Project, private val name: String,
  private val component: JComponent,
  private val close: () -> Unit,
) : CompilationChartsAction {
  private val action = ActionManager.getInstance().getAction("DSM.Analyze")

  override fun isAccessible() = action != null

  override fun actionPerformed(e: MouseEvent) {
    @Suppress("UNCHECKED_CAST")
    val context = object : ActionDataContext {
      val module = ModuleManager.getInstance(project).findModuleByName(name)
      override fun <T : Any?> getData(key: DataKey<T?>): T? = when (key) {
        CommonDataKeys.PROJECT -> project as T
        LangDataKeys.MODULE -> module as T?
        PlatformCoreDataKeys.CONTEXT_COMPONENT -> component as T
        else -> null
      }
    }

    close()

    action.actionPerformed(AnActionEvent.createEvent(action, context, null, ActionPlaces.POPUP, ActionUiKind.TOOLBAR, e))
  }

  override fun position() = CompilationChartsAction.Position.LIST

  override fun label() = JLabel(action?.templateText).apply {
    foreground = JBUI.CurrentTheme.Link.Foreground.ENABLED
    font = font.deriveFont(Font.PLAIN)

    addMouseListener(ActionMouseAdapter(this, this@ShowMatrixDependenciesAction))
  }
}


private class ActionMouseAdapter(private val parent: JLabel, private val action: CompilationChartsAction) : MouseAdapter() {
  override fun mouseClicked(e: MouseEvent) {
    action.actionPerformed(e)
  }

  override fun mouseEntered(e: MouseEvent) {
    parent.cursor = Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)
    parent.font = parent.font.deriveFont(mapOf(TextAttribute.UNDERLINE to TextAttribute.UNDERLINE_ON))
  }

  override fun mouseExited(e: MouseEvent) {
    parent.cursor = Cursor.getDefaultCursor()
    parent.font = parent.font.deriveFont(mapOf(TextAttribute.UNDERLINE to -1))
  }
}

@Suppress("removal", "OVERRIDE_DEPRECATION")
private interface ActionDataContext : DataContext {
  override fun getData(dataId: String): Any? {
    if (CommonDataKeys.PROJECT.`is`(dataId)) return getData(CommonDataKeys.PROJECT)
    if (LangDataKeys.MODULE.`is`(dataId)) return getData(LangDataKeys.MODULE)
    if (LangDataKeys.MODULE_CONTEXT_ARRAY.`is`(dataId)) return getData(LangDataKeys.MODULE_CONTEXT_ARRAY)
    if (PlatformCoreDataKeys.CONTEXT_COMPONENT.`is`(dataId)) return getData(PlatformCoreDataKeys.CONTEXT_COMPONENT)
    return null
  }
}

private inline fun <reified T> notNullArrayOf(vararg elements: T): Array<T> {
  val size = elements.count { it != null }
  val array = arrayOfNulls<T>(size)
  var index = 0
  for (element in elements) {
    if (element != null) array[index++] = element
  }
  @Suppress("UNCHECKED_CAST")
  return array as Array<T>
}