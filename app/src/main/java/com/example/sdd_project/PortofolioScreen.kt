package com.example.sdd_project

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Button
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.sdd_project.data.DonutChartData
import com.example.sdd_project.view_model.PortofolioViewModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


@Composable
fun PortofolioScreen(portofolioViewModel: PortofolioViewModel) {
    val portofolioData by portofolioViewModel.portofolioData.collectAsState()
    var list: List<String> = listOf()
    var jsontoArray:List<Int> = listOf()

    Log.d("TAG", "PortofolioScreen: " + portofolioData.size)
    val startActivityLauncher: ActivityResultLauncher<Intent> =
        rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == ComponentActivity.RESULT_OK) {
            }
        }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        portofolioData.forEach { item ->
            item.data.forEach { idata ->
                when (idata.type) {
                    "donutChart" -> {
                        val dataDonut: List<DonutChartData> = idata.data as List<DonutChartData>
                        Log.d("TAG", "PortofolioScreenaaa: $dataDonut")
                        Log.d("TAG", "PortofolioScreenaaa id: ${idata.data}")

                        val gson = Gson()
                        val listType = object : TypeToken<List<DonutChartData>>() {}.type
                        val jsonDonutString: String = gson.toJson(dataDonut)
                        val donutChartDataList: List<DonutChartData> = gson.fromJson(jsonDonutString, listType)
                        Log.d("TAG", "PortofolioScreen: donutChartDataList $donutChartDataList")

                        for (donutChartDataLista in donutChartDataList) {
                            println("Label: ${donutChartDataLista.label}")
                            println("Percentage: ${donutChartDataLista.percentage}")
                            println("Data:")
                            list = list + donutChartDataLista.label
                            for (entry in donutChartDataLista.data) {
                                println("Date: ${entry.trx_date}, Nominal: ${entry.nominal}")
                            }
                        }
                    }
                    "lineChart" -> {
                        Log.d("TAG", "PortofolioScreen: ${idata.data}")
                        //parsing
                        jsontoArray = jsonStringToArrayLine(idata.data.toString())
                        Log.d("parsing", "PortofolioScreen: $jsontoArray")
                    }
                    else -> {
                        Log.d("TAG", "PortofolioScreen: Unknown type")
                    }
                }
            }
            Log.d("TAG", "PortofolioScreen list: $list")
            DefaultUI(list, jsontoArray, startActivityLauncher, LocalContext.current)
        }
    }
}

fun jsonStringToArrayLine(jsonString: String): List<Int> {
    val gson = Gson()
    val type = object : TypeToken<Map<String, List<Int>>>() {}.type
    val jsonObject = gson.fromJson<Map<String, List<Int>>>(jsonString, type)
    return jsonObject["month"] ?: emptyList()
}

@Composable
fun DefaultUI(array: List<String>, dataList: List<Int>, startActivityLauncher: ActivityResultLauncher<Intent>, context: Context) {
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
                val intent = Intent(context, NewActivity::class.java)
                val valueLabel = array.toCollection(ArrayList())
                Log.d("TAG", "DefaultUI valueLabel: $valueLabel")
                intent.putStringArrayListExtra("value_label", valueLabel)
                startActivityLauncher.launch(intent)
            },
            modifier = Modifier.padding(top = 16.dp)
        ) {
            Text(text = "Click here to donut chart")
        }

        //Grafik Linechart
        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .padding(30.dp)
        ) {
            // Draw X-axis
            drawLine(
                start = Offset(0f, size.height),
                end = Offset(size.width, size.height),
                color = Color.Black,
                strokeWidth = 2f
            )

            // Draw Y-axis
            drawLine(
                start = Offset(0f, 0f),
                end = Offset(0f, size.height),
                color = Color.Black,
                strokeWidth = 2f
            )

            // Draw line chart with text labels
            if (dataList.isNotEmpty()) {
                val maxY = dataList.maxOrNull() ?: 1
                val scaleX = size.width / (dataList.size - 1).toFloat()
                val scaleY = size.height / maxY.toFloat()

                dataList.forEachIndexed { index, value ->
                    val x = index * scaleX
                    val y = size.height - value * scaleY

                    // Draw points
                    drawCircle(
                        color = Color.Black,
                        center = Offset(x, y),
                        radius = 4f
                    )

                    // Draw text labels at each point
                    drawIntoCanvas {
                        it.nativeCanvas.drawText(
                            value.toString(),
                            x - 10f, // Adjust the x-coordinate for centering
                            y - 10f, // Adjust the y-coordinate for centering
                            android.graphics.Paint().apply {
                                color = android.graphics.Color.BLACK
                                textSize = 20f // Adjust the text size as needed
                            }
                        )
                    }

                    // Draw connecting lines
                    if (index < dataList.size - 1) {
                        val nextX = (index + 1) * scaleX
                        val nextY = size.height - dataList[index + 1] * scaleY

                        drawLine(
                            start = Offset(x, y),
                            end = Offset(nextX, nextY),
                            color = Color.Black,
                            strokeWidth = 2f
                        )
                    }
                }
            }
        }
    }
}
@Preview
@Composable
fun DefaultUIPreview() {
//    DefaultUI()
}