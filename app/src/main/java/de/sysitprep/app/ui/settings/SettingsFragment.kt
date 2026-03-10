package de.sysitprep.app.ui.settings

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import de.sysitprep.app.R
import de.sysitprep.app.data.model.Fachrichtung
import de.sysitprep.app.databinding.FragmentSettingsBinding

class SettingsFragment : Fragment() {

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!
    private val viewModel: SettingsViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeViewModel()
        viewModel.loadSettings()

        binding.optionSi.setOnClickListener { viewModel.setFachrichtung(Fachrichtung.SI) }
        binding.optionAe.setOnClickListener { viewModel.setFachrichtung(Fachrichtung.AE) }

        binding.switchDarkMode.setOnCheckedChangeListener { _, isChecked ->
            viewModel.setDarkMode(isChecked)
        }

        binding.switchSound.setOnCheckedChangeListener { _, isChecked ->
            viewModel.setSoundEnabled(isChecked)
        }

        binding.rowResetProgress.setOnClickListener {
            MaterialAlertDialogBuilder(requireContext())
                .setTitle(getString(R.string.settings_reset_title))
                .setMessage(getString(R.string.settings_reset_message))
                .setPositiveButton(getString(R.string.settings_reset_confirm)) { _, _ ->
                    viewModel.resetProgress()
                }
                .setNegativeButton(getString(R.string.cancel), null)
                .show()
        }

        binding.rowImpressum.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.growtracker.de/impressum.html"))
            startActivity(intent)
        }
    }

    private fun observeViewModel() {
        viewModel.fachrichtung.observe(viewLifecycleOwner) { fachrichtung ->
            val isSi = fachrichtung == Fachrichtung.SI
            binding.radioSi.isChecked = isSi
            binding.radioAe.isChecked = !isSi
            binding.optionSi.setBackgroundResource(
                if (isSi) R.drawable.bg_option_selected else R.drawable.bg_option_unselected
            )
            binding.optionAe.setBackgroundResource(
                if (!isSi) R.drawable.bg_option_selected else R.drawable.bg_option_unselected
            )
        }

        viewModel.isDarkMode.observe(viewLifecycleOwner) { isDark ->
            binding.switchDarkMode.isChecked = isDark
        }

        viewModel.isSoundEnabled.observe(viewLifecycleOwner) { enabled ->
            binding.switchSound.isChecked = enabled
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
