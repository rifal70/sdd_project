package com.example.sdd_project.ui.view

import android.graphics.Paint
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlin.math.cos
import kotlin.math.sin

class DonutActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Donut Chart",
                            style = MaterialTheme.typography.h3,
                            color = Color.Black,
                            modifier = Modifier
                                .padding(bottom = 40.dp)
                        )
                        val label: ArrayList<String>? = intent.getStringArrayListExtra("value_label")
                        val percentage: ArrayList<String>? = intent.getStringArrayListExtra("value_percentage")

                        if (label != null && percentage != null) {
                            val percentage: List<String> = listOf("30", "45.5", "60", "75.2")
                            val listInt: List<Int> = percentage.map { it.toDouble().toInt() }
                            Log.d("TAG", "onCreate: listInt$listInt")
                            DoughnutChart(label,listInt)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun DoughnutChart(
    str: List<String>,
    percentage: List<Int>,
    values: List<Int> = percentage,
    colors: List<Color> = listOf(
        Color(0xFFFF6384),
        Color(0xFFFFCE56),
        Color(0xFF36A2EB),
        Color(0xFF9C27B0),
        Color(0xFFFF5722),
        Color(0xFFCDDC39)
    ),
    legend: List<String> = str,
    size: Dp = 200.dp,
    thickness: Dp = 36.dp
) {
    val sumOfValues = values.sum()
    val proportions = values.map {
        it * 100 / sumOfValues
    }
    val sweepAngles = proportions.map {
        360 * it / 100
    }

    Canvas(
        modifier = Modifier.size(size = size)
    ) {
        var startAngle = -90f
        val sizePx = size.toPx()

        for (i in values.indices) {
            val angle = startAngle + sweepAngles[i] / 2
            val textX = center.x + (sizePx / 4) * cos(Math.toRadians(angle.toDouble())).toFloat()
            val textY = center.y + (sizePx / 4) * sin(Math.toRadians(angle.toDouble())).toFloat()

            drawArc(
                color = colors[i],
                startAngle = startAngle,
                sweepAngle = sweepAngles[i].toFloat(),
                useCenter = false,
                style = Stroke(width = thickness.value, cap = StrokeCap.Butt)
            )
            drawIntoCanvas {
                it.nativeCanvas.drawText(
                    "${proportions[i]}%",
                    textX,
                    textY,
                    Paint().apply {
                        color = Color.Black.toArgb()
                        textSize = 14f.dp.toPx()
                        textAlign = Paint.Align.CENTER
                    }
                )
            }

            startAngle += sweepAngles[i]
        }
    }
    Spacer(modifier = Modifier.height(32.dp))
    Column {
        for (i in values.indices) {
            DisplayLegend(color = colors[i], legend = legend[i])
        }
    }
}

@Composable
fun DisplayLegend(color: Color, legend: String) {
    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(10.dp)
                .background(color = color, shape = CircleShape)
        )

        Spacer(modifier = Modifier.width(4.dp))

        Text(
            text = legend,
            color = Color.Black
        )
    }
}