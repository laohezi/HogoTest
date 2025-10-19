package com.example.musicplayer

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun MusicPlayerScreen(
    onSessionIdUpdated: (Int) -> Unit,
    viewModel: MusicPlayerViewModel = viewModel()
) {
    val playerState by viewModel.playerState.collectAsState()
    val currentMusicUrl by viewModel.currentMusicUrl.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // 标题
        Text(
            text = "音乐播放器",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 32.dp)
        )

        Spacer(modifier = Modifier.height(32.dp))

        // 播放状态显示
        Text(
            text = when (playerState) {
                PlayerState.IDLE -> "准备播放"
                PlayerState.PLAYING -> "正在播放"
                PlayerState.PAUSED -> "已暂停"
                PlayerState.STOPPED -> "已停止"
                PlayerState.ERROR -> "播放错误"
            },
            fontSize = 18.sp,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // 控制按钮
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Button(
                onClick = {
                    when (playerState) {
                        PlayerState.IDLE, PlayerState.STOPPED -> {
                            viewModel.startPlaying { sessionId ->
                                onSessionIdUpdated(sessionId)
                            }
                        }
                        PlayerState.PAUSED -> viewModel.resumePlaying()
                        PlayerState.PLAYING -> viewModel.pausePlaying()
                        PlayerState.ERROR -> {
                            viewModel.startPlaying { sessionId ->
                                onSessionIdUpdated(sessionId)
                            }
                        }
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (playerState == PlayerState.PLAYING)
                        MaterialTheme.colorScheme.secondary
                    else
                        MaterialTheme.colorScheme.primary
                )
            ) {
                Text(
                    text = when (playerState) {
                        PlayerState.PLAYING -> "暂停"
                        PlayerState.PAUSED -> "继续"
                        else -> "播放"
                    }
                )
            }

            Button(
                onClick = { viewModel.stopPlaying() },
                enabled = playerState != PlayerState.IDLE && playerState != PlayerState.STOPPED,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.error
                )
            ) {
                Text("停止")
            }

            Button(onClick = { viewModel.switchSource() }) {
                Text("切换源")
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // 显示当前颜色信息
        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = "播放信息",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                Text("播放地址: $currentMusicUrl")
            }
        }
    }
}
