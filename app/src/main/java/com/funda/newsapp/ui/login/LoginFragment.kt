package com.funda.newsapp.ui.login

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import com.funda.newsapp.R
import com.funda.newsapp.common.gone
import com.funda.newsapp.common.viewBinding
import com.funda.newsapp.databinding.FragmentLoginBinding
import com.funda.newsapp.ui.MainActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : Fragment(R.layout.fragment_login) {

    private val binding by viewBinding(FragmentLoginBinding::bind)
    private lateinit var auth: FirebaseAuth
    private var bottomNavigationView: BottomNavigationView? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = FirebaseAuth.getInstance()

        // Bottom Navigation Visibility
        bottomNavigationView = activity?.findViewById(R.id.bottomNavigationView)
        bottomNavigationView?.gone()

        with(binding) {

            tvRegisterLink.setOnClickListener {
                val action = LoginFragmentDirections.actionLoginFragmentToRegisterFragment()
                findNavController().navigate(action)
            }

            btnLogin.setOnClickListener {
                val email = etEmail.text.toString()
                val password = etPassword.text.toString()

                if (email.isNotEmpty() && password.isNotEmpty()) {
                    signIn(email, password)
                } else {
                    Snackbar.make(requireView(), "Fill in all the blanks", Snackbar.LENGTH_SHORT).show()
                }
            }
        }
    }

    //SIGN IN
    private fun signIn(email: String, password: String) {
        if (isValidEmail(email) && isValidPassword(password)) {
            auth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener {
                    findNavController().navigate(R.id.newsFragment)
                }
                .addOnFailureListener {
                    Snackbar.make(requireView(), it.message.orEmpty(), Snackbar.LENGTH_SHORT).show()
                }
        } else {
            Snackbar.make(requireView(), "Invalid email or password", Snackbar.LENGTH_SHORT).show()
        }
    }

    //Email validation
    private fun isValidEmail(email: String): Boolean {
        val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
        return email.matches(emailPattern.toRegex())
    }

    //Password validation
    private fun isValidPassword(password: String): Boolean {
        return password.length >= 6
    }
}
