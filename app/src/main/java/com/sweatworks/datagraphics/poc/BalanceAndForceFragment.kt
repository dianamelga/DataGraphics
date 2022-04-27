package com.sweatworks.datagraphics.poc

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.transition.ChangeBounds
import android.transition.TransitionManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.RotateAnimation
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.sweatworks.datagraphics.poc.databinding.FragmentBalanceAndForceBinding
import kotlin.math.roundToInt

class BalanceAndForceFragment : Fragment() {

    private lateinit var binding: FragmentBalanceAndForceBinding
    private lateinit var whiteRingDrawable: Drawable
    private lateinit var redRingDrawable: Drawable
    private lateinit var whiteLineDrawable: Drawable
    private lateinit var redLineDrawable: Drawable

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentBalanceAndForceBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        whiteRingDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.ic_ellipse_white)!!
        redRingDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.ic_ellipse_red)!!
        whiteLineDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.ic_white_line)!!
        redLineDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.ic_red_line)!!

        binding.btnBottomRing.setOnClickListener {
            moveRingVertically(0.1f)
        }

        binding.btnTopRing.setOnClickListener {
            moveRingVertically(-0.1f)
        }

        binding.btnLeftRing.setOnClickListener {
            moveRingHorizontally(-0.1f)
        }

        binding.btnRightRing.setOnClickListener {
            moveRingHorizontally(0.1f)
        }

        // hand force

        binding.btnLeftLine.setOnClickListener {
            rotateLine(-5f)
        }

        binding.btnRightLine.setOnClickListener {
            rotateLine(5f)
        }
    }

    private fun rotateLine(addAngle: Float) {
        val newAngle = binding.handForceLine.rotation + addAngle
        binding.handForceLine.rotation = newAngle
        checkLineColor()
    }

    private fun moveRingVertically(value: Float) {
        val animation = ChangeBounds()
        animation.duration = 500
        TransitionManager.beginDelayedTransition(binding.container, animation)
        val lparams = binding.ivRing.layoutParams as ConstraintLayout.LayoutParams
        lparams.verticalBias = lparams.verticalBias + value
        binding.ivRing.layoutParams = lparams
        checkRingColor()
    }

    private fun moveRingHorizontally(value: Float) {
        val animation = ChangeBounds()
        animation.duration = 500
        TransitionManager.beginDelayedTransition(binding.container, animation)
        val lparams = binding.ivRing.layoutParams as ConstraintLayout.LayoutParams
        lparams.horizontalBias = lparams.horizontalBias + value
        binding.ivRing.layoutParams = lparams
        checkRingColor()
    }

    private fun checkRingColor() {
        val lparams = binding.ivRing.layoutParams as ConstraintLayout.LayoutParams
        val hBias = lparams.horizontalBias.times(100).roundToInt().div(100f)
        val vBias = lparams.verticalBias.times(100).roundToInt().div(100f)
        if (hBias != 0.49f || vBias != 0.52f) {
            binding.ivRing.setImageDrawable(redRingDrawable)
        } else {
            binding.ivRing.setImageDrawable(whiteRingDrawable)
        }
    }

    private fun checkLineColor() {
        val rotation = binding.handForceLine.rotation
        if (rotation != 0f) {
            binding.handForceLine.setImageDrawable(redLineDrawable)
        } else {
            binding.handForceLine.setImageDrawable(whiteLineDrawable)
        }
    }

    companion object {
        val TAG = BalanceAndForceFragment::javaClass.name

        @JvmStatic
        fun newInstance() = BalanceAndForceFragment()
    }
}