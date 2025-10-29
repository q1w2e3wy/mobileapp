package com.example.week5

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ClickGameApp()
        }
    }
}

@Composable
fun ClickGameApp() {
    var score by remember { mutableStateOf(0) }           // 점수
    var timeLeft by remember { mutableStateOf(30_000L) }  // 남은 시간 (밀리초)
    var isRunning by remember { mutableStateOf(false) }   // 게임 진행 상태
    var result by remember { mutableStateOf("") }         // 결과 메시지

    // 타이머 (isRunning이 true일 때만 실행됨)
    LaunchedEffect(isRunning) {
        if (isRunning) {
            while (timeLeft > 0) {
                delay(100L)
                timeLeft -= 100L
            }
            isRunning = false
            // 게임 끝나면 결과 표시
            result = if (score >= 60) "🎉 성공! 점수: $score" else "❌ 실패... 점수: $score"
        }
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("⏱ 남은 시간: ${timeLeft / 1000}초", fontSize = 30.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(20.dp))

        Text("점수: $score", fontSize = 30.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(20.dp))

        // 클릭 버튼 (게임 중일 때만 활성화)
        Button(
            onClick = { if (isRunning) score++ },
            enabled = isRunning
        ) {
            Text("Click!", fontSize = 24.sp)
        }

        Spacer(modifier = Modifier.height(30.dp))

        Row {
            Button(
                onClick = {
                    // 게임 시작
                    score = 0
                    timeLeft = 30_000L
                    result = ""
                    isRunning = true
                },
                enabled = !isRunning
            ) {
                Text("Start")
            }

            Spacer(modifier = Modifier.width(16.dp))

            Button(
                onClick = {
                    // 초기화
                    score = 0
                    timeLeft = 30_000L
                    isRunning = false
                    result = ""
                }
            ) {
                Text("Reset")
            }
        }

        Spacer(modifier = Modifier.height(30.dp))

        // 결과 메시지 표시
        if (result.isNotEmpty()) {
            Text(result, fontSize = 24.sp, fontWeight = FontWeight.Bold)
        }
    }
}
