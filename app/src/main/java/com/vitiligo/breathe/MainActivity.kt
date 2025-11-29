package com.vitiligo.breathe

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.rememberNavController
import com.vitiligo.breathe.ui.navigation.BreatheScaffold
import com.vitiligo.breathe.ui.theme.BreatheTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BreatheTheme {
                BreatheScaffold(
                    navController = rememberNavController(),
                )
            }
        }
    }
}
