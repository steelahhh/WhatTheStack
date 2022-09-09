package com.haroldadmin.whatthestack

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import com.haroldadmin.whatthestack.ui.pages.ExceptionPage
import com.haroldadmin.whatthestack.ui.theme.WhatTheStackTheme

/**
 * An Activity which displays various pieces of information regarding the exception which occurred.
 */
class WhatTheStackActivity : AppCompatActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    WindowCompat.setDecorFitsSystemWindows(window, true)

    val type = intent.getStringExtra(KEY_EXCEPTION_TYPE) ?: ""
    val message = intent.getStringExtra(KEY_EXCEPTION_MESSAGE) ?: ""
    val stackTrace = intent.getStringExtra(KEY_EXCEPTION_STACKTRACE) ?: ""

    setContent {
      WhatTheStackTheme { ExceptionPage(type = type, message = message, stackTrace = stackTrace) }
    }
  }
}
