package roch.manga_library

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.lifecycleScope
import com.example.compose.MangaTheme
import com.google.android.gms.auth.api.identity.Identity
import roch.manga_library.sign_in.GoogleAuthUiClient

/**
 * The main activity of the application.
 */
class MainActivity : ComponentActivity() {
    private val googleAuthUiClient by lazy {
        GoogleAuthUiClient(
            context = applicationContext,
            oneTapClient = Identity.getSignInClient(applicationContext)
        )
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MangaTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    App(
                        lifeCycle = lifecycleScope,
                        googleAuthClient = googleAuthUiClient,
                        applicationContext = applicationContext
                    )
                }
            }
        }
    }
}

