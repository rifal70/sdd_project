package com.example.sdd_project

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
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

class NewActivity : ComponentActivity() {
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

                        var strUser: ArrayList<String>? = intent.getStringArrayListExtra("value_label")
                        strUser?.let { DoughnutChart(it)
                            Log.d("TAG", "onCreate: newact$it")
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
    values: List<Float> = listOf(65f, 40f, 25f, 20f),
    colors: List<Color> = listOf(
        Color(0xFFFF6384),
        Color(0xFFFFCE56),
        Color(0xFF36A2EB),
        Color(0xFF448AFF)
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

        for (i in values.indices) {
            drawArc(
                color = colors[i],
                startAngle = startAngle,
                sweepAngle = sweepAngles[i],
                useCenter = false,
                style = Stroke(width = thickness.value, cap = StrokeCap.Butt)
            )
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