package com.haroldadmin.whatthestack.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

internal val StackTraceColor = Color(0xFFe06b75)
internal val StackTraceContainerColor = Color(0xFF292c34)

@Composable
internal fun WhatTheStackTheme(
  darkTheme: Boolean = isSystemInDarkTheme(),
  content: @Composable () -> Unit
) {
  val dynamicColor = Build.VERSION.SDK_INT >= Build.VERSION_CODES.S
  val colorScheme =
    when {
      dynamicColor && darkTheme -> dynamicDarkColorScheme(LocalContext.current)
      dynamicColor && !darkTheme -> dynamicLightColorScheme(LocalContext.current)
      darkTheme -> darkColorScheme()
      else -> lightColorScheme()
    }

  MaterialTheme(colorScheme = colorScheme, content = content)
}
