package io.lb.schedule.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Shimmer loading view for timeline
 */
/**
 * Shimmer loading view for timeline
 */
@Composable
fun TimelineShimmerView(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState())
            .padding(vertical = 16.dp)
    ) {
        repeat(3) { laneIndex ->
            EventShimmerLane()
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
private fun EventShimmerLane() {
    Column {
        Spacer(
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 4.dp)
                .width(60.dp)
                .height(12.dp)
                .shimmerAnimation()
        )
        EventShimmerCard()
    }
}

@Composable
private fun EventShimmerCard() {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .height(64.dp),
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Spacer(
                modifier = Modifier
                    .size(32.dp)
                    .shimmerAnimation()
            )

            Spacer(modifier = Modifier.width(8.dp))

            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Spacer(
                    modifier = Modifier
                        .fillMaxWidth(0.8f)
                        .height(12.sp.value.dp)
                        .shimmerAnimation()
                )

                Spacer(modifier = Modifier.height(4.dp))

                Spacer(
                    modifier = Modifier
                        .fillMaxWidth(0.6f)
                        .height(10.sp.value.dp)
                        .shimmerAnimation()
                )
            }
        }
    }
}
