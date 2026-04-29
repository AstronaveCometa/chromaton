package com.astronave.chromaton.ui.game

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.astronave.chromaton.data.network.GameDetailResponse
import com.astronave.chromaton.databinding.FragmentGameBinding
import com.google.firebase.auth.FirebaseAuth

class GameFragment : Fragment() {
    private var _binding: FragmentGameBinding? = null
    private val binding get() = _binding!!
    private val viewModel: GameViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val gameId = arguments?.getInt("gameId") ?: return

        setupObservers()
        viewModel.startPollingGame(gameId)
    }

    private fun setupObservers() {
        viewModel.gameState.observe(viewLifecycleOwner) { state ->
            updateUI(state)
        }
    }

    private fun updateUI(state: GameDetailResponse) {
        // 1. Mostrar de quién es el turno
        val isMyTurn = state.currentPlayerId == FirebaseAuth.getInstance().currentUser?.uid
        binding.tvStatus.text = if (isMyTurn) "¡Tu turno!" else "Esperando al rival..."

        // 2. Renderizar dados (puedes usar un Adapter simple)
        // diceAdapter.submitList(state.dices)
    }
}