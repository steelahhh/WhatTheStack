package com.haroldadmin.whatthestack.ui.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight

/** A text label with the "overline" typography style */
@Composable
internal fun OverlineLabel(label: String, modifier: Modifier = Modifier) {
  Text(
    text = label,
    style = MaterialTheme.typography.labelMedium,
    fontWeight = FontWeight.Medium,
    modifier = modifier
  )
}
