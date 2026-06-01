package com.potpilot.cookbook.ui.components

import android.content.Context
import android.media.RingtoneManager
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Replay
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay

/** Finds the first "N minute(s)" / "N-M minutes" duration in a step, in minutes (or null). */
private val DURATION = Regex("(\\d+)\\s*(?:-\\s*\\d+\\s*)?min(?:ute)?s?", RegexOption.IGNORE_CASE)

fun parseStepMinutes(step: String): Int? =
    DURATION.find(step)?.groupValues?.get(1)?.toIntOrNull()?.takeIf { it in 1..240 }

/** Renders recipe steps as a numbered list, each with an optional inline countdown timer. */
@Composable
fun StepsList(steps: List<String>, modifier: Modifier = Modifier) {
    Column(modifier, verticalArrangement = Arrangement.spacedBy(12.dp)) {
        steps.forEachIndexed { index, step ->
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    Surface(
                        shape = RoundedCornerShape(50),
                        color = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier.size(26.dp),
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text("${index + 1}", style = MaterialTheme.typography.labelLarge)
                        }
                    }
                    Text(
                        text = step,
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.weight(1f).padding(top = 2.dp),
                    )
                }
                parseStepMinutes(step)?.let { minutes ->
                    StepTimer(
                        totalSeconds = minutes * 60,
                        modifier = Modifier.padding(start = 38.dp),
                    )
                }
            }
        }
    }
}

/** A compact start/pause/reset countdown that rings and vibrates when it finishes. */
@Composable
fun StepTimer(totalSeconds: Int, modifier: Modifier = Modifier) {
    val context = LocalContext.current
    var remaining by remember(totalSeconds) { mutableIntStateOf(totalSeconds) }
    var running by remember(totalSeconds) { mutableStateOf(false) }
    val finished = remaining == 0

    LaunchedEffect(running) {
        while (running && remaining > 0) {
            delay(1000)
            remaining -= 1
        }
        if (remaining == 0 && running) {
            running = false
            ring(context)
        }
    }

    val container = when {
        finished -> MaterialTheme.colorScheme.tertiaryContainer
        running -> MaterialTheme.colorScheme.primaryContainer
        else -> MaterialTheme.colorScheme.surfaceVariant
    }

    Surface(shape = RoundedCornerShape(50), color = container, modifier = modifier) {
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(start = 12.dp, end = 6.dp)) {
            Icon(Icons.Filled.Timer, contentDescription = null, modifier = Modifier.size(16.dp))
            Text(
                text = if (finished) "Time's up!" else format(remaining),
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(horizontal = 8.dp),
            )
            if (!finished) {
                IconButton(onClick = { running = !running }, modifier = Modifier.size(36.dp)) {
                    Icon(
                        imageVector = if (running) Icons.Filled.Pause else Icons.Filled.PlayArrow,
                        contentDescription = if (running) "Pause timer" else "Start timer",
                    )
                }
            }
            AnimatedVisibility(visible = running || finished || remaining != totalSeconds) {
                IconButton(
                    onClick = { running = false; remaining = totalSeconds },
                    modifier = Modifier.size(36.dp),
                ) {
                    Icon(Icons.Filled.Replay, contentDescription = "Reset timer", modifier = Modifier.size(18.dp))
                }
            }
        }
    }
}

private fun format(seconds: Int): String = "%d:%02d".format(seconds / 60, seconds % 60)

/** Vibrate and play the default notification sound when a timer completes. Best-effort. */
private fun ring(context: Context) {
    runCatching {
        val vibrator = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val mgr = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
            mgr.defaultVibrator
        } else {
            @Suppress("DEPRECATION")
            context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        }
        vibrator.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE))
    }
    runCatching {
        val uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        RingtoneManager.getRingtone(context, uri)?.play()
    }
}
