package com.violin.demo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment

class ComposeLearningFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(
                ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed
            )
            setContent {
                MaterialTheme {
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = Color(0xFFF5F7FB)
                    ) {
                        ComposeLearningScreen(
                            onBack = { requireActivity().finish() }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ComposeLearningScreen(onBack: () -> Unit) {
    var boxAligned by remember { mutableStateOf(false) }
    val demoItems = remember {
        listOf(
            "Column: 纵向堆叠内容",
            "Row: 横向排列内容",
            "Box: 内容叠放与对齐",
            "LazyColumn: 构建可滚动列表",
            "remember: 保存页面内状态"
        )
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = "Compose 布局学习页",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "现在这个页面运行在 Fragment 里，通过 ComposeView 承载 Compose 内容。",
                color = Color(0xFF5E6472),
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(modifier = Modifier.height(12.dp))
            OutlinedButton(onClick = onBack) {
                Text("返回")
            }
        }

        item {
            DemoCard(
                title = "Column + Row",
                desc = "先看最基础的线性布局组合。"
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    DemoTagRow(
                        colors = listOf(
                            Color(0xFF4F46E5),
                            Color(0xFF0EA5E9),
                            Color(0xFF10B981)
                        )
                    )
                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        DemoStatBlock("礼物", "12")
                        DemoStatBlock("在线", "328")
                        DemoStatBlock("热度", "89")
                    }
                }
            }
        }

        item {
            DemoCard(
                title = "Box 对齐",
                desc = "Box 适合做悬浮角标、覆盖层和居中内容。"
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Button(onClick = { boxAligned = !boxAligned }) {
                        Text(if (boxAligned) "切回左上角" else "切到右下角")
                    }
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(140.dp)
                            .background(Color(0xFF1F2937), RoundedCornerShape(20.dp))
                            .padding(16.dp)
                    ) {
                        Text(
                            text = "舞台背景",
                            color = Color.White
                        )
                        Box(
                            modifier = Modifier
                                .size(52.dp)
                                .align(if (boxAligned) Alignment.BottomEnd else Alignment.TopStart)
                                .background(Color(0xFFFF6B6B), RoundedCornerShape(16.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("Gift", color = Color.White)
                        }
                    }
                }
            }
        }

        item {
            DemoCard(
                title = "LazyColumn 列表",
                desc = "用数据驱动 UI，是 Compose 最常见的写法。"
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    demoItems.forEachIndexed { index, item ->
                        Card(
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(14.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(28.dp)
                                        .background(Color(0xFFE0E7FF), RoundedCornerShape(10.dp)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text("${index + 1}", color = Color(0xFF4338CA))
                                }
                                Spacer(modifier = Modifier.width(12.dp))
                                Text(
                                    text = item,
                                    color = Color(0xFF111827)
                                )
                            }
                        }
                    }
                }
            }
        }

        item {
            DemoCard(
                title = "状态驱动",
                desc = "点击按钮时，不直接改 View，而是修改状态并让界面自动重组。"
            ) {
                var selectedTab by remember { mutableStateOf("布局") }
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        listOf("布局", "状态", "列表").forEach { tab ->
                            val active = selectedTab == tab
                            Button(onClick = { selectedTab = tab }) {
                                Text(if (active) "$tab 中" else tab)
                            }
                        }
                    }
                    Text(
                        text = "当前学习主题：$selectedTab",
                        color = Color(0xFF374151)
                    )
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
private fun DemoCard(
    title: String,
    desc: String,
    content: @Composable () -> Unit
) {
    Card(
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier.padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = desc,
                    color = Color(0xFF6B7280),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            content()
        }
    }
}

@Composable
private fun DemoTagRow(colors: List<Color>) {
    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
        colors.forEachIndexed { index, color ->
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(56.dp)
                    .background(color, RoundedCornerShape(18.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "模块 ${index + 1}",
                    color = Color.White,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}

@Composable
private fun RowScope.DemoStatBlock(label: String, value: String) {
    Card(
        modifier = Modifier.weight(1f),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF3F4F6))
    ) {
        Column(
            modifier = Modifier.padding(vertical = 14.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = value,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = label,
                color = Color(0xFF6B7280)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ComposeLearningScreenPreview() {
    MaterialTheme {
        ComposeLearningScreen(onBack = {})
    }
}
