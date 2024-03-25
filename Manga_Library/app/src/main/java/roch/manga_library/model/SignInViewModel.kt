package roch.manga_library.model

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import roch.manga_library.sign_in.SignInResult
import roch.manga_library.sign_in.SignInState

class SignInViewModel: ViewModel() {

    private val _state = MutableStateFlow(SignInState())
    val state = _state.asStateFlow()

    /**
     * Updates the sign in state to the given [result]
     * @param result The result of the sign in.
     */
    fun onSignInResult(result: SignInResult) {
        _state.update { it.copy(
            isSignInSuccessful = result.data != null,
            signInError = result.errorMessage
        ) }
    }

    /**
     * Resets the sign in state to the default state.
     */
    fun resetState() {
        _state.update { SignInState() }
    }
}