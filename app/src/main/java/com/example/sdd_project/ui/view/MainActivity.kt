package com.example.sdd_project.ui.view

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.sdd_project.data.PortofolioData
import com.example.sdd_project.data.loadPortfolioDataFromJson
import com.example.sdd_project.view_model.PortofolioViewModel

class MainActivity : ComponentActivity() {
    private val portofolioViewModel = PortofolioViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PortofolioScreen(portofolioViewModel)
        }
        // Parse JSON data and update the ViewModel
        val jsonData = loadPortfolioDataFromJson() // Implement JSON loading
        Log.d("jsonData", "onCreate: $jsonData")
        portofolioViewModel.setPortofolioData(jsonData as List<PortofolioData>)
    }
}
