package com.example.sdd_project.view_model

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sdd_project.api.apiService
import com.example.sdd_project.data.PromoData
import kotlinx.coroutines.launch

class PromoViewModel : ViewModel() {
    private var _promoData by mutableStateOf<PromoData?>(null)
    val promoData: PromoData? get() = _promoData

    fun fetchPromos() {
        viewModelScope.launch {
            try {
                // Fetch data from API
                val response = apiService.fetchPromos()

                // Update the promo data
                _promoData = response
            } catch (e: Exception) {
                // Handle error, e.g., log it or show an error message
                e.printStackTrace()
            }
        }
    }
}
