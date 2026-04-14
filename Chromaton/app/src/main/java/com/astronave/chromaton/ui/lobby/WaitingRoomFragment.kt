package com.astronave.chromaton.ui.lobby

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.astronave.chromaton.R
import com.astronave.chromaton.databinding.FragmentWaitingRoomBinding

class WaitingRoomFragment : Fragment() {

    private var _binding: FragmentWaitingRoomBinding? = null
    private val binding get() = _binding!!

    private val viewModel: WaitingRoomViewModel by viewModels()

    private lateinit var playerAdapter: PlayerAdapter
    private var gameId: Int = -1
    private var isHost: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWaitingRoomBinding.inflate(inflater, container, false)
        return binding.root // Retornamos la raíz del XML inflado
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        gameId = arguments?.getInt("gameId") ?: -1
        isHost = arguments?.getBoolean("isHost") ?: false

        if (gameId == -1) {
            // Error de seguridad: Si no hay ID, volvemos atrás
            findNavController().popBackStack()
            return
        }

        binding.tvRoomId.text = "Sala: $gameId"

        if (isHost) {
            binding.btnStartGame.visibility = View.VISIBLE
        }

        binding.btnStartGame.setOnClickListener {
            viewModel.startGame(gameId) // Debes crear esta función en el ViewModel que llame al apiService.startGame
        }

        viewModel.gameStatus.observe(viewLifecycleOwner) { status ->
            if (status == "playing") {
                val bundle = bundleOf("gameId" to gameId)
                findNavController().navigate(R.id.action_waitingRoom_to_gameFragment, bundle)
            }
        }

        setupRecyclerView()
        setupObservers()
        viewModel.startPolling(gameId)
    }

    private fun setupRecyclerView() {
        playerAdapter = PlayerAdapter()
        binding.rvPlayers.apply {
            adapter = playerAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun setupObservers() {
        viewModel.players.observe(viewLifecycleOwner) { listaDeJugadores ->
            // El "map" de React aquí es submitList
            // ListAdapter se encarga de comparar qué cambió y actualizar solo eso
            Log.d("DEBUG_LOBBY", "Lista recibida: ${listaDeJugadores.size} jugadores")
            playerAdapter.submitList(listaDeJugadores)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}