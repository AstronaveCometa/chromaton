package com.astronave.chromaton.ui.lobby

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.astronave.chromaton.R
import com.astronave.chromaton.databinding.FragmentLobbyBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputEditText

class LobbyFragment : Fragment() {

    private var _binding: FragmentLobbyBinding? = null
    private val binding get() = _binding!!

    private val viewModel: LobbyViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLobbyBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupObservers()
        setupListeners()
    }

    private fun setupObservers() {
        // 1. Datos del usuario
        viewModel.userEmail.observe(viewLifecycleOwner) { email ->
            binding.tvUserEmail.text = email ?: "Usuario desconocido"
        }

        // 2. Navegación al Login (Logout)
        viewModel.navigateToLogin.observe(viewLifecycleOwner) { shouldNavigate ->
            if (shouldNavigate) {
                findNavController().navigate(R.id.action_lobbyFragment_to_loginFragment)
            }
        }

        // 3. Observar si se creó una partida con éxito
        viewModel.gameCreated.observe(viewLifecycleOwner) { gameId ->
            if (gameId != null) {
                val bundle = Bundle().apply {
                    putInt("gameId", gameId)
                    putBoolean("isHost", true)
                }
                findNavController().navigate(R.id.action_lobbyFragment_to_waitingRoomFragment, bundle)
            }
        }

        viewModel.gameJoined.observe(viewLifecycleOwner) { gameId ->
            if (gameId != null) {
                val bundle = Bundle().apply {
                    putInt("gameId", gameId)
                    putBoolean("isHost", false)
                }
                findNavController().navigate(R.id.action_lobbyFragment_to_waitingRoomFragment, bundle)
            }
        }

        // 5. Mostrar errores generales (Contraseña incorrecta, timeout, etc.)
        viewModel.error.observe(viewLifecycleOwner) { errorMessage ->
            if (errorMessage != null) {
                Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setupListeners() {
        // Botón Logout
        binding.btnLogout.setOnClickListener {
            viewModel.logout()
        }

        // Botón Crear Partida
        binding.btnCreateGame.setOnClickListener {
            showCreateGameDialog()
        }

        // Botón Unirse a Partida
        binding.btnJoinGame.setOnClickListener {
            showJoinGameDialog()
        }
    }

    private fun showCreateGameDialog() {
        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_game_password, null)
        val etPassword = dialogView.findViewById<TextInputEditText>(R.id.etDialogPassword)

        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Nueva Partida Privada")
            .setMessage("Define una contraseña para la sala")
            .setView(dialogView)
            .setPositiveButton("Crear") { _, _ ->
                val password = etPassword.text.toString()
                if (password.isNotEmpty()) {
                    viewModel.createPrivateGame(password)
                } else {
                    Toast.makeText(requireContext(), "La contraseña no puede estar vacía", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    private fun showJoinGameDialog() {
        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_join_game, null)
        val etGameId = dialogView.findViewById<TextInputEditText>(R.id.etJoinGameId)
        val etPassword = dialogView.findViewById<TextInputEditText>(R.id.etJoinPassword)

        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Unirse a Partida")
            .setMessage("Ingresa el ID y la contraseña de la sala")
            .setView(dialogView)
            .setPositiveButton("Unirse") { _, _ ->
                val idText = etGameId.text.toString()
                val password = etPassword.text.toString()

                if (idText.isNotEmpty() && password.isNotEmpty()) {
                    viewModel.joinPrivateGame(idText.toInt(), password)
                } else {
                    Toast.makeText(requireContext(), "Completa todos los campos", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}