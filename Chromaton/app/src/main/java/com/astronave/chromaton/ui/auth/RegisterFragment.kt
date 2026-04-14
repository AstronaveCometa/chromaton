package com.astronave.chromaton.ui.auth

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.astronave.chromaton.R
import com.astronave.chromaton.databinding.FragmentRegisterBinding

class RegisterFragment : Fragment(R.layout.fragment_register) {

    // El binding nos permite acceder a las vistas del XML
    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!

    // Obtenemos el ViewModel
    private val viewModel: AuthViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentRegisterBinding.bind(view)

        viewModel.checkActiveSession()

        // Observamos el estado de la sesión
        viewModel.isUserLoggedIn.observe(viewLifecycleOwner) { loggedIn ->
            if (loggedIn) {
                goToLobby()
            }
        }
        setupObservers()
        setupListeners()
    }

    private fun goToLobby() {
        findNavController().navigate(R.id.action_registerFragment_to_lobbyFragment)
    }

    private fun setupListeners() {
        binding.btnRegister.setOnClickListener {
            val name = binding.etName.text.toString()
            val email = binding.etEmail.text.toString()
            val pass = binding.etPassword.text.toString()

            if (name.isNotEmpty() && email.isNotEmpty() && pass.isNotEmpty()) {
                viewModel.register(email, pass, name)
            } else {
                Toast.makeText(requireContext(), "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show()
            }
        }

        binding.tvGoToLogin.setOnClickListener {
            findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
        }
    }

    private fun setupObservers() {
        // Observamos si el usuario se logueó con éxito
        viewModel.userState.observe(viewLifecycleOwner) { user ->
            if (user != null) {
                Toast.makeText(requireContext(), "Bienvenido, ${user.email}", Toast.LENGTH_SHORT).show()
                // Aquí navegarías a la pantalla principal del juego
            }
        }

        // Observamos errores
        viewModel.errorMessage.observe(viewLifecycleOwner) { error ->
            error?.let {
                Toast.makeText(requireContext(), it, Toast.LENGTH_LONG).show()
            }
        }

        // Observamos el estado de carga para mostrar/ocultar el ProgressBar
        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
            binding.btnRegister.isEnabled = !isLoading
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // Evitamos fugas de memoria
    }
}