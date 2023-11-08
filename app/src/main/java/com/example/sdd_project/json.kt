package com.example.sdd_project

import com.example.sdd_project.data.PortofolioData
import com.google.gson.Gson

fun loadPortfolioDataFromJson(): List<*>? {
    val gson = Gson()
    val json = """
        {
          "data": [
            {
              "type": "donutChart",
              "data": [
                {
                  "label": "Tarik Tunai",
                  "percentage": "55",
                  "data": [
                    {
                      "trx_date": "21/01/2023",
                      "nominal": 1000000
                    },
                    {
                      "trx_date": "20/01/2023",
                      "nominal": 500000
                    },
                    {
                      "trx_date": "19/01/2023",
                      "nominal": 1000000
                    }
                  ]
                },
                {
                  "label": "QRIS Payment",
                  "percentage": "31",
                  "data": [
                    {
                      "trx_date": "21/01/2023",
                      "nominal": 159000
                    },
                    {
                      "trx_date": "20/01/2023",
                      "nominal": 35000
                    },
                    {
                      "trx_date": "19/01/2023",
                      "nominal": 1500
                    }
                  ]
                },
                {
                  "label": "Topup Gopay",
                  "percentage": "7.7",
                  "data": [
                    {
                      "trx_date": "21/01/2023",
                      "nominal": 200000
                    },
                    {
                      "trx_date": "20/01/2023",
                      "nominal": 195000
                    },
                    {
                      "trx_date": "19/01/2023",
                      "nominal": 5000000
                    }
                  ]
                },
                {
                  "label": "Lainnya",
                  "percentage": "6.3",
                  "data": [
                    {
                      "trx_date": "21/01/2023",
                      "nominal": 1000000
                    },
                    {
                      "trx_date": "20/01/2023",
                      "nominal": 500000
                    },
                    {
                      "trx_date": "19/01/2023",
                      "nominal": 1000000
                    }
                  ]
                }
              ]
            },
            {
              "type": "lineChart",
              "data": {
                "month": [
                  3,
                  7,
                  8,
                  10,
                  5,
                  10,
                  1,
                  3,
                  5,
                  10,
                  7,
                  7
                ]
              }
            }
          ]
        }
    """.trimIndent()

    return listOf(gson.fromJson(json, PortofolioData::class.java))
}
