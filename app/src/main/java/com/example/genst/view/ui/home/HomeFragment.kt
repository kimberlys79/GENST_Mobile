package com.example.genst.view.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.genst.databinding.FragmentHomeBinding
import com.example.genst.view.ui.inspeksipage.InspeksiActivity
import com.example.genst.view.ui.notification.NotificationActivity

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.ivGensetPerkinsPs.setOnClickListener {
            val intent = Intent(activity, InspeksiActivity::class.java)
            intent.putExtra(InspeksiActivity.GENERATOR_ID, 1)
            intent.putExtra(InspeksiActivity.GENERATOR_NAME, "GE0020 - Perkins")
            startActivity(intent)
        }

        binding.ivGensetCAT.setOnClickListener {
            val intent = Intent(activity, InspeksiActivity::class.java)
            intent.putExtra(InspeksiActivity.GENERATOR_ID, 2)
            intent.putExtra(InspeksiActivity.GENERATOR_NAME, "GE0078 - Caterpilar")
            startActivity(intent)
        }

        binding.ivGensetPerkinsAs.setOnClickListener {
            val intent = Intent(activity, InspeksiActivity::class.java)
            intent.putExtra(InspeksiActivity.GENERATOR_ID, 3)
            intent.putExtra(InspeksiActivity.GENERATOR_NAME, "GE0082 - Perkins")
            startActivity(intent)
        }

        binding.ivGensetWilson.setOnClickListener {
            val intent = Intent(activity, InspeksiActivity::class.java)
            intent.putExtra(InspeksiActivity.GENERATOR_ID, 4)
            intent.putExtra(InspeksiActivity.GENERATOR_NAME, "GE0051 - Wilson")
            startActivity(intent)
        }

        binding.notificationIcon.setOnClickListener {
            val intent = Intent(activity, NotificationActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}