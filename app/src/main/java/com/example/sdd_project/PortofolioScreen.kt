package com.example.sdd_project

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Button
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.sdd_project.data.DonutChartData
import com.example.sdd_project.data.LineChartData
import com.example.sdd_project.view_model.PortofolioViewModel
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun PortofolioScreen(portofolioViewModel: PortofolioViewModel) {
    val portofolioData by portofolioViewModel.portofolioData.collectAsState()
    Log.d("TAG", "PortofolioScreen: " + portofolioData.size)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        portofolioData.forEach { item ->
            item.data.forEach { idata ->
                when (idata.type) {
                    "donutChart" -> {
                        Log.d("TAG", "PortofolioScreen: ${idata.type}")
                        if (idata.data is DonutChartData) {
                            val donutChartData = item.data as DonutChartData
                            DonutChart(donutChartData)
                        }
                    }
                    "lineChart" -> {
                        Log.d("TAG", "PortofolioScreen: ${idata.type}")
                        if (idata.data is LineChartData) {
                            val lineChartData = item.data as LineChartData
                            LineChart(lineChartData)
                        }
                    }
                    else -> {
                        Log.d("TAG", "PortofolioScreen: Unknown type")
                        DefaultUI()
                    }
                }

            }
        }
    }
}

@Composable
fun DonutChart(data: DonutChartData) {
    // Implement your Donut Chart UI here
    Log.d("TAG", "DonutChart: $data")
}

@Composable
fun LineChart(data: LineChartData) {
    // Implement your Donut Chart UI here
    Log.d("TAG", "LineChart: $data")
}
@Composable
fun DefaultUI() {
    var text by remember { mutableStateOf("Hello, World!") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.bodyLarge,
            color = Color.Black
        )

        BasicTextField(
            value = text,
            onValueChange = { newText ->
                text = newText
            },
            singleLine = true,
            textStyle = LocalTextStyle.current.copy(color = Color.Black)
        )

        Button(
            onClick = {
                text = "Testing!"
            },
            modifier = Modifier.padding(top = 16.dp)
        ) {
            Text(text = "Click here")
        }
    }
}

@Preview
@Composable
fun DefaultUIPreview() {
    DefaultUI()
}