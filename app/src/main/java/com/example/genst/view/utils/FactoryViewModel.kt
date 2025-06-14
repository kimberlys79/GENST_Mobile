package com.example.genst.view.utils

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.genst.data.preference.Repository
import com.example.genst.injection.Data
import com.example.genst.view.ui.home.HomeViewModel
import com.example.genst.view.ui.inspeksipage.InspeksiViewModel
import com.example.genst.view.ui.laporaninspeksi.LaporanViewModel
import com.example.genst.view.ui.authentication.AuthenticationViewModel
import com.example.genst.view.ui.profil.ProfilViewModel
import com.example.genst.view.ui.riwayatlaporan.RiwayatLaporanViewModel

class FactoryViewModel (
    private val repository: Repository
): ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(AuthenticationViewModel::class.java) -> {
                AuthenticationViewModel(repository) as T
            }

            modelClass.isAssignableFrom(HomeViewModel::class.java) -> {
                HomeViewModel(repository) as T
            }

            modelClass.isAssignableFrom(InspeksiViewModel::class.java) -> {
                InspeksiViewModel(repository) as T
            }

            modelClass.isAssignableFrom(LaporanViewModel::class.java) -> {
                LaporanViewModel(repository) as T
            }

            modelClass.isAssignableFrom(ProfilViewModel::class.java) -> {
                ProfilViewModel(repository) as T
            }

            modelClass.isAssignableFrom(RiwayatLaporanViewModel::class.java) ->{
                RiwayatLaporanViewModel(repository) as T
            }

            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }

    companion object {
        @Volatile
        private var instance: FactoryViewModel? = null
        fun getInstance(context: Context): FactoryViewModel =
            instance ?: synchronized(FactoryViewModel::class.java) {
                instance ?: FactoryViewModel(Data.provideRepository(context))
            }.also {
                instance = it
            }
    }
}