package com.bikodbg.sharetopinboard

import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.text.HtmlCompat
import androidx.fragment.app.Fragment
import com.bikodbg.sharetopinboard.databinding.FragmentAboutBinding

class AboutFragment : Fragment() {
    private var _binding: FragmentAboutBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAboutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val aboutHtml = getString(R.string.about)
        val about = HtmlCompat.fromHtml(aboutHtml, HtmlCompat.FROM_HTML_MODE_LEGACY)

        binding.aboutText.text = about
        binding.aboutText.movementMethod = LinkMovementMethod.getInstance()
    }
}
