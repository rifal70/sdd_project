package com.example.sdd_project.app

import android.app.Application
import com.example.sdd_project.view_model.PortofolioViewModel

class PortofolioApp(var portofolioViewModel: PortofolioViewModel) : Application() {
    override fun onCreate() {
        super.onCreate()
        portofolioViewModel = PortofolioViewModel()
        val portofolioApp = PortofolioApp(portofolioViewModel)
    }
}