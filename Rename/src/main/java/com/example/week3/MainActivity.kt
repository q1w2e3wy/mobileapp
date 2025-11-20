package com.example.week3

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface(modifier = Modifier.fillMaxSize(), color = Color(0xFFF5F5F7)) {
                    WeeklyExpenseScreen()
                }
            }
        }
    }
}

@Composable
fun WeeklyExpenseScreen() {
    val days = listOf("월요일", "화요일", "수요일", "목요일", "금요일", "토요일", "일요일")
    var currentDayIndex by remember { mutableStateOf(0) }
    val dailyExpenses = remember { mutableStateListOf(0f, 0f, 0f, 0f, 0f, 0f, 0f) }
    var expenseInput by remember { mutableStateOf("") }

    var budgetInput by remember { mutableStateOf("") }
    var weeklyBudget by remember { mutableStateOf<Float?>(null) }
    var budgetSetMsgVisible by remember { mutableStateOf(false) }

    var showLargeDay by remember { mutableStateOf(false) }
    var showResultScreen by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    fun totalSpent(): Float = dailyExpenses.sum()
    val total = totalSpent()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        if (showLargeDay) {
            Box(
                Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Text(days[currentDayIndex], fontSize = 36.sp, color = Color(0xFF222222))
            }
        }

        Spacer(Modifier.height(8.dp))

        // 목표 예산 입력 UI
        if (weeklyBudget == null) {
            Text("💰 이번 주 목표 예산", fontSize = 20.sp)
            Row(verticalAlignment = Alignment.CenterVertically) {
                BasicTextField(
                    value = budgetInput,
                    onValueChange = { budgetInput = it },
                    textStyle = TextStyle(fontSize = 18.sp),
                    modifier = Modifier
                        .weight(1f)
                        .background(Color.White, RoundedCornerShape(10.dp))
                        .padding(12.dp)
                )
                Spacer(Modifier.width(8.dp))
                Button(onClick = {
                    val v = budgetInput.toFloatOrNull()
                    if (v == null) Toast.makeText(context, "숫자만 입력하세요", Toast.LENGTH_SHORT).show()
                    else {
                        weeklyBudget = v
                        budgetInput = ""
                        budgetSetMsgVisible = true
                        scope.launch {
                            delay(1200)
                            budgetSetMsgVisible = false
                        }
                    }
                }) { Text("설정") }
            }
            if (budgetSetMsgVisible) Text("✔ 예산이 설정되었습니다", color = Color(0xFF008800))
        } else {
            Text("이번 주 목표 예산: ${weeklyBudget!!.toInt()}원", fontSize = 18.sp)
        }

        Spacer(Modifier.height(12.dp))

        // 요일 이동 버튼
        Row(verticalAlignment = Alignment.CenterVertically) {
            if (currentDayIndex > 0) Button({ currentDayIndex-- }) { Text("◀") }
            else Spacer(Modifier.width(48.dp))

            Spacer(Modifier.weight(1f))
            Text(days[currentDayIndex], fontSize = 22.sp)
            Spacer(Modifier.weight(1f))

            if (currentDayIndex < 6) Button({ currentDayIndex++ }) { Text("▶") }
            else Spacer(Modifier.width(48.dp))
        }

        Spacer(Modifier.height(12.dp))

        // 지출 입력
        Text("📅 ${days[currentDayIndex]} - 오늘 지출: ${dailyExpenses[currentDayIndex].toInt()}원", fontSize = 18.sp)
        Row(verticalAlignment = Alignment.CenterVertically) {
            BasicTextField(
                value = expenseInput,
                onValueChange = { expenseInput = it },
                textStyle = TextStyle(fontSize = 18.sp),
                modifier = Modifier
                    .weight(1f)
                    .background(Color.White, RoundedCornerShape(10.dp))
                    .padding(12.dp)
            )
            Spacer(Modifier.width(8.dp))
            Button(onClick = {
                val v = expenseInput.toFloatOrNull()
                if (v == null) Toast.makeText(context, "숫자만 입력하세요", Toast.LENGTH_SHORT).show()
                else {
                    dailyExpenses[currentDayIndex] += v
                    expenseInput = ""
                    Toast.makeText(context, "${v.toInt()}원 추가됨", Toast.LENGTH_SHORT).show()
                }
            }) { Text("추가") }
        }

        Spacer(Modifier.height(20.dp))

        // 그래프
        StyledBarGraph(values = dailyExpenses.toList(), modifier = Modifier.fillMaxWidth().height(280.dp))

        Spacer(Modifier.height(12.dp))

        // 일요일 지출 추가 후 결과 보기
        if (currentDayIndex == 6 && dailyExpenses[6] > 0) {
            Text("총 지출: ${total.toInt()}원", fontSize = 18.sp)
            Spacer(Modifier.height(8.dp))
            Button({ showResultScreen = true }, modifier = Modifier.fillMaxWidth()) {
                Text("결과 보기")
            }
        } else {
            Text("현재 누적: ${total.toInt()}원", color = Color.Gray)
        }

        // 결과 화면
        if (showResultScreen) {
            Surface(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xAA000000)),
                color = Color.Transparent
            ) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Card(shape = RoundedCornerShape(16.dp), modifier = Modifier.fillMaxWidth(0.9f)) {
                        Column(
                            modifier = Modifier.padding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text("📌 이번 주 결과", fontSize = 22.sp)
                            Spacer(Modifier.height(16.dp))

                            Text("총 지출: ${total.toInt()}원", fontSize = 19.sp)
                            Spacer(Modifier.height(12.dp))

                            if (weeklyBudget != null) {
                                val diff = total - weeklyBudget!!
                                when {
                                    diff == 0f -> Text("🎉 목표 예산 딱 달성! 완벽합니다!", color = Color(0xFF008800), fontSize = 18.sp)
                                    diff > 0 -> Text("⚠ 예산 초과: ${diff.toInt()}원", color = Color.Red, fontSize = 18.sp)
                                    else -> Text("💚 예산 절약: ${(-diff).toInt()}원", color = Color(0xFF009944), fontSize = 18.sp)
                                }
                            }

                            Spacer(Modifier.height(20.dp))

                            // 다시하기 버튼
                            Button(onClick = {
                                for (i in 0..6) dailyExpenses[i] = 0f
                                weeklyBudget = null
                                currentDayIndex = 0
                                showResultScreen = false
                            }, colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6200EE)), modifier = Modifier.fillMaxWidth()) {
                                Text("다시하기", color = Color.White)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun StyledBarGraph(values: List<Float>, modifier: Modifier = Modifier) {
    val maxValue = (values.maxOrNull() ?: 1f).coerceAtLeast(1f)
    val colors = listOf(
        Color(0xFF6A5AE0), Color(0xFF4DD0E1), Color(0xFFFF7043),
        Color(0xFFFFD54F), Color(0xFF66BB6A), Color(0xFF42A5F5), Color(0xFFE57373)
    )
    val dayShort = listOf("월", "화", "수", "목", "금", "토", "일")

    // 전체 그래프 테두리
    Box(
        modifier = modifier
            .padding(12.dp)
            .border(2.dp, Color.Black, RoundedCornerShape(12.dp))
            .padding(12.dp) // 테두리 안쪽 여백
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.Bottom,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            values.forEachIndexed { index, value ->
                val heightFraction = value / maxValue

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("${value.toInt()}원", fontSize = 12.sp)

                    Box(
                        modifier = Modifier
                            .width(28.dp)
                            .height(180.dp),
                        contentAlignment = Alignment.BottomCenter
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .fillMaxHeight(fraction = heightFraction)
                                .background(colors[index], RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp))
                        )
                    }

                    Text(dayShort[index], fontSize = 14.sp)
                }
            }
        }
    }
}
