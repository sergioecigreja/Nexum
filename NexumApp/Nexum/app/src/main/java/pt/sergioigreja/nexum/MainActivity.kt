package pt.sergioigreja.nexum

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.content.ContextCompat
import pt.sergioigreja.nexum.ui.theme.NexumTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val permissions = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                arrayOf(
                    Manifest.permission.INTERNET,
                    Manifest.permission.ACCESS_WIFI_STATE,
                    Manifest.permission.CHANGE_WIFI_MULTICAST_STATE
                )
            } else {
                TODO("VERSION.SDK_INT < S")
            }
            val launcherMultiplePermissions = rememberLauncherForActivityResult(
                ActivityResultContracts.RequestMultiplePermissions()
            ) { permissionsMap ->
                val areGranted = permissionsMap.values.reduce { acc, next -> acc && next }
                println(permissionsMap)
                if (areGranted) {
                    println("Granted!")
                } else {
                    println("Not granted!")
                }
            }

            checkAndRequestPermissions(
                context = this,
                permissions = permissions,
                launcher = launcherMultiplePermissions,
                onPermissionsGranted = { Log.d("MainActivity", "Permissions granted") })

            NexumTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Column(modifier = Modifier
                        .padding(innerPadding)
                        .fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement =  Arrangement.Center) {
                        Greeting(baseContext)
                    }

                }
            }
        }
    }
}

fun checkAndRequestPermissions(
    context: Context,
    permissions: Array<String>,
    launcher: ManagedActivityResultLauncher<Array<String>, Map<String, Boolean>>,
    onPermissionsGranted: () -> Unit
) {
    if (
        permissions.all {
            ContextCompat.checkSelfPermission(
                context,
                it
            ) == PackageManager.PERMISSION_GRANTED
        }
    ) {
        onPermissionsGranted()
    } else {
        // Request permissions
        launcher.launch(permissions)
    }
}

@Composable
fun Greeting(context: Context) {
    var message by remember { mutableStateOf("")}
    val udpBroadcaster = UDPBroadcaster(context)
    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        Column(modifier = Modifier
            .padding(innerPadding)
            .fillMaxSize(),
               horizontalAlignment = Alignment.CenterHorizontally,
               verticalArrangement =  Arrangement.Center) {
            TextField(
                value = message,
                onValueChange = { message = it },
                label = { Text("Message to broadcast") }
            )
            Button(onClick = { udpBroadcaster.sendBroadcast(message) }) {
                Text(text = "Broadcast")
            }
            Text(text = udpBroadcaster.status.value)
        }

    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun GreetingPreview() {
    NexumTheme {
        Greeting(LocalContext.current)
    }
}