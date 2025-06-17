package com.example.w3wthemovie.android.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.w3wthemovie.android.W3WTheMovieTheme

@Composable
fun OfflineMessageView(
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(MaterialTheme.colorScheme.tertiary)
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
    ) {
        Text(
            "⚠\uFE0F You are not connected to the internet",
            color = MaterialTheme.colorScheme.onTertiary,
            style = MaterialTheme.typography.bodySmall,
        )
    }
}

@Preview
@Composable
private fun OfflineMessageViewPreview() {
    W3WTheMovieTheme {
        OfflineMessageView()
    }
}