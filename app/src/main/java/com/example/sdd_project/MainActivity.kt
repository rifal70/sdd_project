package com.example.sdd_project

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.sdd_project.app.PortofolioApp
import com.example.sdd_project.data.PortofolioData
import com.example.sdd_project.view_model.PortofolioViewModel

class MainActivity : ComponentActivity() {
    private val portofolioViewModel = PortofolioViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PortofolioApp(portofolioViewModel)
            PortofolioScreen(portofolioViewModel)
        }
        // Parse JSON data and update the ViewModel
        val jsonData = loadPortfolioDataFromJson() // Implement JSON loading
        Log.d("jsonData", "onCreate: $jsonData")
        portofolioViewModel.setPortofolioData(jsonData as List<PortofolioData>)
    }
}
