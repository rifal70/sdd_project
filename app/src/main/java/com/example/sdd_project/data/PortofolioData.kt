package com.example.sdd_project.data

data class PortofolioData(
    val data: List<PortofolioData_>
)

data class PortofolioData_(
    val type: String,
    val data: Any
)

data class DonutChartData(
    val label: String,
    val percentage: Double,
    val data: List<TransactionData>
)

data class TransactionData(
    val trx_date: String,
    val nominal: Int
)

data class LineChartData(
    val month: List<Int>
)
