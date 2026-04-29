package com.astronave.chromaton.ui.lobby

import android.os.Bundle
import android.view.*
import android.widget.Toast
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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentWaitingRoomBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val gameId = arguments?.getInt("gameId") ?: -1
        val isHost = arguments?.getBoolean("isHost") ?: false

        if (gameId == -1) {
            findNavController().popBackStack()
            return
        }

        binding.tvRoomId.text = "Sala: $gameId"

        // El botón solo es visible para quien creó la sala[cite: 1]
        binding.btnStartGame.visibility = if (isHost) View.VISIBLE else View.GONE

        binding.btnStartGame.setOnClickListener {
            viewModel.startGame(gameId)
        }

        setupRecyclerView()
        setupObservers(gameId)
        viewModel.startPolling(gameId)
    }

    private fun setupObservers(gameId: Int) {
        viewModel.players.observe(viewLifecycleOwner) { lista ->
            playerAdapter.submitList(lista)
        }

        viewModel.gameStatus.observe(viewLifecycleOwner) { status ->
            if (status == "playing") {
                // Al detectar "playing", todos los jugadores saltan al GameFragment[cite: 1]
                val bundle = bundleOf("gameId" to gameId)
                findNavController().navigate(R.id.action_waitingRoom_to_gameFragment, bundle)
            }
        }

        viewModel.error.observe(viewLifecycleOwner) { msg ->
            msg?.let { Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show() }
        }
    }

    private fun setupRecyclerView() {
        playerAdapter = PlayerAdapter()
        binding.rvPlayers.apply {
            adapter = playerAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}