package com.haroldadmin.whatthestack.ui.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign

@Composable
internal fun OutlinedIconButton(
  text: String,
  @DrawableRes iconId: Int,
  onClick: () -> Unit,
  contentDescription: String,
  modifier: Modifier = Modifier,
) {
  OutlinedButton(
    onClick = onClick,
    modifier = modifier.fillMaxWidth(),
  ) {
    Icon(painter = painterResource(id = iconId), contentDescription = contentDescription)
    Text(
      text = text,
      modifier = Modifier.fillMaxWidth(),
      textAlign = TextAlign.Center,
    )
  }
}
