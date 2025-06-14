package com.example.genst.view.ui.profil

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.genst.R
import com.example.genst.data.result.Results
import com.example.genst.databinding.FragmentProfilBinding
import com.example.genst.injection.Data
import com.example.genst.view.ui.authentication.LoginActivity
import com.example.genst.view.ui.laporaninspeksi.LaporanViewModel
import com.example.genst.view.ui.riwayatlaporan.RiwayatLaporanFragment
import com.example.genst.view.utils.FactoryViewModel

class ProfilFragment : Fragment() {

    private lateinit var binding: FragmentProfilBinding
    private val profilViewModel by viewModels<ProfilViewModel> {
        FactoryViewModel.getInstance(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfilBinding.inflate(inflater, container, false)
        val root: View = binding.root

        profilViewModel.getUserDetail()
        getUserDetailObserver()

        setupMenuClickListeners()

        return root
    }

    private fun getUserDetailObserver() {
        profilViewModel.userDetailResponse.observe(viewLifecycleOwner) { userDetailResult ->
            when (userDetailResult) {
                is Results.Success -> {
                    val userDetail = userDetailResult.data.userDetail?.firstOrNull()
                    Log.d("ProfilFragment", "User Detail Success: $userDetail")
                    binding.tvNamaUser.text = userDetail?.name
                    binding.tvBadgeNumber.text = userDetail?.badgeNumber
                    binding.tvProfileEmail.text = userDetail?.email
                }
                is Results.Error -> {
                    Log.e("ProfilFragment", "Error fetching user detail: ${userDetailResult.error}")

                    binding.tvNamaUser.text = "_"
                    binding.tvBadgeNumber.text = "_"
                    binding.tvProfileEmail.text = "_"
                }
                is Results.Loading -> {
                    Log.d("ProfilFragment", "Loading user detail")
                }
            }
        }
    }

    //Fungsi untuk menangani klik pada item menu di profil
    private fun setupMenuClickListeners() {
        // Tombol Pengaturan
        binding.ivIconPengaturan.setOnClickListener {
            //Arahkan ke halaman pengaturan
            // startActivity(Intent(activity, PengaturanActivity::class.java))
        }

        //Tombol bahasa
        binding.ivBahasa.setOnClickListener {
            //Arahkan ke halaman bahasa
            //   startActivity(Intent(activity, BahasaActivity::class.java))
        }
        binding.ivRiwayatInspeksi.setOnClickListener {
            //Arahkan ke halaman riwayat inspeksi
            parentFragmentManager.beginTransaction()
                .replace(R.id.nav_host_fragment_activity_main, RiwayatLaporanFragment())
                .addToBackStack(null)
                .commit()
        }

        binding.btnLogOut.setOnClickListener {
            profilViewModel.logout()
            startActivity(Intent(activity, LoginActivity::class.java))
        }
    }
}