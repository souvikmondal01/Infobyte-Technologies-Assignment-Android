package com.kivous.infobytetechnologies.auth.ui.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.kivous.infobytetechnologies.R
import com.kivous.infobytetechnologies.auth.repositories.AuthRepository
import com.kivous.infobytetechnologies.auth.repositories.AuthRepositoryImp
import com.kivous.infobytetechnologies.auth.ui.viewmodels.AuthViewModel
import com.kivous.infobytetechnologies.auth.ui.viewmodels.AuthViewModelFactory
import com.kivous.infobytetechnologies.auth.ui.viewmodels.LoginListener
import com.kivous.infobytetechnologies.databinding.FragmentLoginBinding
import com.kivous.infobytetechnologies.home.ui.activities.HomeActivity
import com.kivous.infobytetechnologies.utils.Common.auth
import com.kivous.infobytetechnologies.utils.Common.changeIconColorWhenEdittextNotInFocus
import com.kivous.infobytetechnologies.utils.Common.clearEdittext
import com.kivous.infobytetechnologies.utils.Common.showPassword
import com.kivous.infobytetechnologies.utils.Constant.ENTER_EMAIL
import com.kivous.infobytetechnologies.utils.Constant.ENTER_PASSWORD
import com.kivous.infobytetechnologies.utils.Constant.LOGIN_SUCCESS
import com.kivous.infobytetechnologies.utils.Constant.OPEN_GMAIL
import com.kivous.infobytetechnologies.utils.Constant.RESET_PASSWORD_MSG
import com.kivous.infobytetechnologies.utils.Extensions.hide
import com.kivous.infobytetechnologies.utils.Extensions.hideKeyboard
import com.kivous.infobytetechnologies.utils.Extensions.show
import com.kivous.infobytetechnologies.utils.Extensions.toast

class LoginFragment : Fragment(), LoginListener {
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: AuthViewModel
    private val factory = AuthViewModelFactory(AuthRepositoryImp())
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DataBindingUtil.inflate(inflater, R.layout.fragment_login, container, false)
        viewModel = ViewModelProvider(this, factory)[AuthViewModel::class.java]
        binding.viewModel = viewModel
        viewModel.loginListener = this
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        clearEdittext(binding.etEmail, binding.ivCancel)
        changeIconColorWhenEdittextNotInFocus(requireContext(), binding.etEmail, binding.ivCancel)
        changeIconColorWhenEdittextNotInFocus(
            requireContext(),
            binding.etPassword,
            binding.ivVisibility
        )

        //show password on visibility icon click
        var isShow = true
        binding.ivVisibility.setOnClickListener {
            showPassword(isShow, binding.etPassword, binding.ivVisibility)
            isShow = !isShow
        }

        if (auth.currentUser?.uid?.isNotEmpty() == true && auth.currentUser!!.isEmailVerified) {
            if (activity != null && isAdded) {
                requireActivity().startActivity(
                    Intent(
                        requireContext(),
                        HomeActivity::class.java
                    ).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                        .addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                )
                requireActivity().finishAffinity()
            }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    override fun onRegisterTextClick() {
        findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
    }

    override fun onFacebookClick() {
        toast("Coming soon...")
    }

    override fun onGoogleClick() {
        toast("Coming soon...")
    }

    override fun onTwitterClick() {
        toast("Coming soon...")
    }

    override fun onStarted() {
        binding.pb.show()
    }

    override fun onSuccess() {
        hideKeyboard()
        viewModel.loginUser {
            if (it == LOGIN_SUCCESS) {
                binding.pb.hide()

                if (activity != null && isAdded) {
                    requireActivity().startActivity(
                        Intent(
                            requireContext(),
                            HomeActivity::class.java
                        ).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                            .addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                    )
                    requireActivity().finishAffinity()
                }

                return@loginUser
            }
            toast(it)

            binding.pb.hide()
        }
    }

    override fun onEmptyEmail() {
        binding.apply {
            etEmail.apply {
                error = ENTER_EMAIL
                requestFocus()
            }
            pb.hide()
        }
    }

    override fun onEmptyPassword() {
        binding.apply {
            etPassword.apply {
                error = ENTER_PASSWORD
                requestFocus()
            }
            pb.hide()
        }
    }

    override fun restPassword() {
        hideKeyboard()
        viewModel.resetPassword {
            if (it == RESET_PASSWORD_MSG) {
                showSnackBar(it)
            } else {
                toast(it)
            }
            binding.pb.hide()
        }
    }

    private fun showSnackBar(msg: String) {
        val snackBar = Snackbar.make(
            requireView(), msg,
            Snackbar.LENGTH_LONG
        ).setAction(OPEN_GMAIL) {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.setClassName(
                "com.google.android.gm",
                "com.google.android.gm.ConversationListActivityGmail"
            )
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent)
        }
        snackBar.show()
    }

}
