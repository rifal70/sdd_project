package com.example.sdd_project.ui.view

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
import androidx.compose.material3.Button
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
    var listPercentage: List<String> = listOf()
    var jsontoArray:List<Int> = listOf()
    var paramArray: MutableList<Int> = mutableListOf()

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
                            listPercentage = listPercentage + donutChartDataLista.percentage.toString()
                            for (entry in donutChartDataLista.data) {
                                println("Date: ${entry.trx_date}, Nominal: ${entry.nominal}")
                            }
                        }
                    }
                    "lineChart" -> {
                        Log.d("TAG", "PortofolioScreen: ${idata.data}")
                        //parsing
                        jsontoArray = jsonStringToArrayLine(idata.data.toString())
                    }
                    else -> {
                        Log.d("TAG", "PortofolioScreen: Unknown type")
                    }
                }
            }
            jsontoArray = jsontoArray.sorted()

//            jsontoArray.forEach { itemJson ->
//                val paramArray = Array(jsontoArray.size) { 0 }
//                for (itemJson in jsontoArray) {
//                    val index = jsontoArray.indexOf(itemJson)
//                    paramArray[index] += 1
//                }
//            }

            val paramArray = Array(jsontoArray.size) { 0 }
            for (json in jsontoArray) {
                val index = jsontoArray.indexOf(json)
                paramArray[index] += 1
            }

            Log.d("TAG", "PortofolioScreen paramArray All: " + paramArray.joinToString())
            Log.d("TAG", "PortofolioScreen list: $list")
            DefaultUI(list, listPercentage, paramArray.toList(), startActivityLauncher, LocalContext.current)
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
fun DefaultUI(array: List<String>, arrayPercentage: List<String>, dataList: List<Int>, startActivityLauncher: ActivityResultLauncher<Intent>, context: Context) {
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

        Button(
            onClick = {
                val intent = Intent(context, NewActivity::class.java)
                val valueLabel = array.toCollection(ArrayList())
                val valuePercentage = arrayPercentage.toCollection(ArrayList())
                Log.d("TAG", "DefaultUI valueLabel: $valueLabel")
                intent.putStringArrayListExtra("value_label", valueLabel)
                intent.putStringArrayListExtra("value_percentage", valuePercentage)
                startActivityLauncher.launch(intent)
            },
            modifier = Modifier.padding(top = 16.dp)
        ) {
            Text(text = "Click here to donut chart")
        }

        Button(
            onClick = {
                val intent = Intent(context, PromoActivity::class.java)
                val valueLabel = array.toCollection(ArrayList())
                val valuePercentage = arrayPercentage.toCollection(ArrayList())
                Log.d("TAG", "DefaultUI valueLabel: $valueLabel")
                intent.putStringArrayListExtra("value_label", valueLabel)
                intent.putStringArrayListExtra("value_percentage", valuePercentage)
                startActivityLauncher.launch(intent)
            },
            modifier = Modifier.padding(top = 16.dp, bottom = 24.dp)
        ) {
            Text(text = "Click here to promo list")
        }

        Text(
            text = "Line Chart Portofolio",
            style = MaterialTheme.typography.displaySmall,
            color = Color.Black
        )

        //Grafik Linechart
        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .padding(30.dp)
                .padding(top = 18.dp)
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

            val dataTanggal: List<Int> = listOf(1,2,3,4,5,6,7,8,9,10,11,12)
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

                    val xAxisLabelY = size.height + 20f // Adjust the y-coordinate for spacing
                    dataTanggal.forEachIndexed { index, value ->
                        val x = index * scaleX
                        val y = size.height - value * scaleY

                        drawIntoCanvas {
                            it.nativeCanvas.drawText(
                                value.toString(),
                                x - 10f,
                                xAxisLabelY, // Use the adjusted y-coordinate
                                android.graphics.Paint().apply {
                                    color = android.graphics.Color.BLACK
                                    textSize = 15f
                                }
                            )
                        }
                    }

                    val yAxisLabelX = -20f // Adjust the x-coordinate for spacing
                    for (i in 0 until maxY) {
                        val yLabel = (0 + i)
                        drawIntoCanvas {
                            it.nativeCanvas.drawText(
                                yLabel.toInt().toString(),
                                yAxisLabelX,
                                size.height - i * scaleY - 10f, // Adjust the y-coordinate for centering
                                android.graphics.Paint().apply {
                                    color = android.graphics.Color.BLACK
                                    textSize = 14f
                                }
                            )
                        }
                    }

                    // Draw text labels at each point
//                    drawIntoCanvas {
//                        it.nativeCanvas.drawText(
//                            value.toString(),
//                            x - 10f, // Adjust the x-coordinate for centering
//                            y - 10f, // Adjust the y-coordinate for centering
//                            android.graphics.Paint().apply {
////                                color = android.graphics.Color.BLACK
////                                textSize = 20f // Adjust the text size as needed
//                            }
//                        )
//                    }

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