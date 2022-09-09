package com.haroldadmin.whatthestack.ui.pages

import android.content.Intent
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import android.net.Uri
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.haroldadmin.whatthestack.R
import com.haroldadmin.whatthestack.generateStackoverflowSearchUrl
import com.haroldadmin.whatthestack.ui.components.OutlinedIconButton
import com.haroldadmin.whatthestack.ui.components.OverlineLabel
import com.haroldadmin.whatthestack.ui.preview.SampleData
import com.haroldadmin.whatthestack.ui.theme.StackTraceColor
import com.haroldadmin.whatthestack.ui.theme.StackTraceContainerColor
import com.haroldadmin.whatthestack.ui.theme.WhatTheStackTheme
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun ExceptionPage(type: String, message: String, stackTrace: String) {
  val clipboard = LocalClipboardManager.current
  val context = LocalContext.current
  val coroutineScope = rememberCoroutineScope()

  val snackbarHostState = remember { SnackbarHostState() }
  val snackbarMessage = stringResource(id = R.string.copied_message)

  Scaffold(
    snackbarHost = { SnackbarHost(snackbarHostState) },
  ) {
    Column(
      modifier =
        Modifier.padding(horizontal = 16.dp).verticalScroll(rememberScrollState()).padding(it)
    ) {
      PageHeader()
      ExceptionDetails(type = type, message = message, modifier = Modifier.padding(vertical = 8.dp))
      ExceptionOptions(
        onCopy = {
          coroutineScope.launch {
            clipboard.setText(AnnotatedString(stackTrace))
            snackbarHostState.showSnackbar(snackbarMessage)
          }
        },
        onShare = {
          val sendIntent: Intent =
            Intent().apply {
              this.action = Intent.ACTION_SEND
              this.putExtra(Intent.EXTRA_TEXT, stackTrace)
              this.type = "text/plain"
            }

          val shareIntent = Intent.createChooser(sendIntent, "Stacktrace")
          context.startActivity(shareIntent)
        },
        onSearch = {
          val searchQuery = "$type: $message"
          val url = generateStackoverflowSearchUrl(searchQuery)
          val searchIntent =
            Intent().apply {
              action = Intent.ACTION_VIEW
              data = Uri.parse(url)
            }
          context.startActivity(searchIntent)
        },
        onRestart = {
          val applicationContext = context.applicationContext
          val packageManager = applicationContext.packageManager
          val packageName = applicationContext.packageName

          val launchIntent = packageManager.getLaunchIntentForPackage(packageName)
          if (launchIntent != null) {
            launchIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            context.startActivity(launchIntent)
          }
        }
      )
      Stacktrace(stackTrace = stackTrace, modifier = Modifier.padding(vertical = 16.dp))
    }
  }
}

@Composable
fun PageHeader() {
  Text(
    stringResource(id = R.string.header_text),
    style = MaterialTheme.typography.displaySmall,
    modifier = Modifier.padding(vertical = 4.dp),
  )
  Spacer(modifier = Modifier.height(4.dp))
  Text(
    text = stringResource(id = R.string.explanation_text),
  )
}

@Composable
fun ExceptionDetails(type: String, message: String, modifier: Modifier) {
  Column(modifier = modifier) {
    OverlineLabel(label = stringResource(id = R.string.exception_name))
    Text(
      text = type,
      fontFamily = FontFamily.Monospace,
    )
    Spacer(modifier = Modifier.height(8.dp))
    OverlineLabel(label = stringResource(id = R.string.exception_message))
    Text(
      text = message,
      fontFamily = FontFamily.Monospace,
    )
  }
}

@Composable
fun ExceptionOptions(
  onCopy: () -> Unit,
  onShare: () -> Unit,
  onRestart: () -> Unit,
  onSearch: () -> Unit,
  modifier: Modifier = Modifier
) {
  Column(modifier = modifier) {
    Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
      OutlinedIconButton(
        text = stringResource(id = R.string.copy_stacktrace),
        iconId = R.drawable.ic_outline_content_copy_24,
        onClick = onCopy,
        contentDescription = "Copy",
        modifier = Modifier.padding(vertical = 4.dp).weight(1f),
      )
      OutlinedIconButton(
        text = stringResource(id = R.string.share_stacktrace),
        iconId = R.drawable.ic_outline_share_24,
        onClick = onShare,
        contentDescription = "Share",
        modifier = Modifier.padding(vertical = 4.dp).weight(1f),
      )
    }
    OutlinedIconButton(
      text = stringResource(id = R.string.search_stackoverflow),
      iconId = R.drawable.ic_round_search_24,
      onClick = onSearch,
      contentDescription = "Search Stackoverflow",
      modifier = Modifier.padding(vertical = 4.dp)
    )
    OutlinedIconButton(
      text = stringResource(id = R.string.restart_application),
      iconId = R.drawable.ic_baseline_refresh_24,
      onClick = onRestart,
      contentDescription = "Restart"
    )
  }
}

@Composable
fun Stacktrace(stackTrace: String, modifier: Modifier) {
  Column(modifier) {
    OverlineLabel(label = stringResource(id = R.string.stacktrace))
    Surface(
      modifier = Modifier.padding(top = 4.dp),
      shape = RoundedCornerShape(12.dp),
      color = StackTraceContainerColor,
      tonalElevation = 4.dp,
    ) {
      SelectionContainer {
        Text(
          text = stackTrace,
          style = MaterialTheme.typography.bodySmall,
          fontFamily = FontFamily.Monospace,
          color = StackTraceColor,
          modifier = Modifier.padding(4.dp).horizontalScroll(rememberScrollState())
        )
      }
    }
  }
}

@Preview
@Composable
fun ExceptionPagePreview() {
  WhatTheStackTheme {
    ExceptionPage(
      type = SampleData.ExceptionType,
      message = SampleData.ExceptionMessage,
      stackTrace = SampleData.Stacktrace
    )
  }
}

@Preview(uiMode = UI_MODE_NIGHT_YES)
@Composable
fun ExceptionPagePreviewNightMode() {
  WhatTheStackTheme {
    ExceptionPage(
      type = SampleData.ExceptionType,
      message = SampleData.ExceptionMessage,
      stackTrace = SampleData.Stacktrace
    )
  }
}
