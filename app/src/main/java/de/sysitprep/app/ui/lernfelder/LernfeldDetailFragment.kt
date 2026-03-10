package de.sysitprep.app.ui.lernfelder

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import de.sysitprep.app.R
import de.sysitprep.app.data.model.ExamType
import de.sysitprep.app.data.model.Fachrichtung
import de.sysitprep.app.data.model.LernfelderData
import de.sysitprep.app.data.model.SessionType
import de.sysitprep.app.databinding.FragmentLernfeldDetailBinding

class LernfeldDetailFragment : Fragment() {

    private var _binding: FragmentLernfeldDetailBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentLernfeldDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val lernfeldNumber = arguments?.getInt("lernfeldNumber") ?: return
        val fachrichtungStr = arguments?.getString("fachrichtung") ?: return
        val fachrichtung = Fachrichtung.valueOf(fachrichtungStr)

        val lernfeld = LernfelderData.allLernfelder.find {
            it.number == lernfeldNumber && (it.fachrichtungen.contains(fachrichtung) || it.fachrichtungen.contains(Fachrichtung.GEMEINSAM))
        } ?: return

        binding.tvLfNumberHeader.text = "Lernfeld ${lernfeld.number}"
        binding.tvLfTitle.text = lernfeld.title
        binding.tvLfDescription.text = lernfeld.description
        binding.tvQuestionCount.text = "${lernfeld.stunden} Std. | ${fachrichtung.displayName}"

        binding.btnStartTraining.setOnClickListener {
            findNavController().navigate(
                R.id.quizFragment,
                Bundle().apply {
                    putString("sessionType", SessionType.LERNFELD.name)
                    putInt("lernfeld", lernfeldNumber)
                    putString("fachrichtung", fachrichtungStr)
                    putString("examType", ExamType.AP1.name)
                }
            )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
