package com.example.sdd_project.view_model

import androidx.lifecycle.ViewModel
import com.example.sdd_project.data.PortofolioData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class PortofolioViewModel : ViewModel() {
    private val _portofolioData = MutableStateFlow(emptyList<PortofolioData>())
    val portofolioData: StateFlow<List<PortofolioData>> = _portofolioData

    fun setPortofolioData(data: List<PortofolioData>) {
        _portofolioData.value = data
    }
}
