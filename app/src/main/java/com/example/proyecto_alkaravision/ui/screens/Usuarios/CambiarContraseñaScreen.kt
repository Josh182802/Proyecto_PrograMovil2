package com.example.proyecto_alkaravision.ui.screens.Usuarios

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.platform.LocalContext
import android.widget.Toast
import com.example.proyecto_alkaravision.local.UsuarioDao
import com.example.proyecto_alkaravision.security.PasswordUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun CambiarContrasenaScreen(
    usuarioDao: UsuarioDao,
    usuarioActual: String,
    onVolver: () -> Unit
) {

    var contrasenaActual by remember { mutableStateOf("") }
    var nuevaContrasena by remember { mutableStateOf("") }
    var confirmarContrasena by remember { mutableStateOf("") }

    var mostrarActual by remember { mutableStateOf(false) }
    var mostrarNueva by remember { mutableStateOf(false) }
    var mostrarConfirmar by remember { mutableStateOf(false) }

    var cargando by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    val azul = Color(0xFF087DEB)
    val celeste = Color(0xFF16C5FF)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF071B3A),
                        Color(0xFF050914),
                        Color.Black
                    )
                )
            )
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(modifier = Modifier.height(25.dp))

            Icon(
                imageVector = Icons.Default.LockReset,
                contentDescription = null,
                tint = celeste,
                modifier = Modifier.size(65.dp)
            )

            Spacer(modifier = Modifier.height(15.dp))

            Text(
                text = "CAMBIAR CONTRASEÑA",
                color = Color.White,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "Actualiza las credenciales de tu cuenta",
                color = Color.LightGray,
                fontSize = 13.sp
            )

            Spacer(modifier = Modifier.height(30.dp))

            OutlinedTextField(
                value = contrasenaActual,
                onValueChange = { contrasenaActual = it },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Contraseña actual") },
                leadingIcon = {
                    Icon(Icons.Default.Lock, contentDescription = null)
                },
                trailingIcon = {
                    IconButton(onClick = {
                        mostrarActual = !mostrarActual
                    }) {
                        Icon(
                            if (mostrarActual) Icons.Default.Visibility
                            else Icons.Default.VisibilityOff,
                            contentDescription = "Mostrar contraseña"
                        )
                    }
                },
                visualTransformation = if (mostrarActual)
                    VisualTransformation.None
                else
                    PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password
                ),
                singleLine = true,
                shape = RoundedCornerShape(14.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = celeste,
                    unfocusedBorderColor = azul,
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    focusedLabelColor = celeste,
                    unfocusedLabelColor = Color.LightGray,
                    cursorColor = celeste
                )
            )

            Spacer(modifier = Modifier.height(18.dp))

            OutlinedTextField(
                value = nuevaContrasena,
                onValueChange = { nuevaContrasena = it },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Nueva contraseña") },
                leadingIcon = {
                    Icon(Icons.Default.Lock, contentDescription = null)
                },
                trailingIcon = {
                    IconButton(onClick = {
                        mostrarNueva = !mostrarNueva
                    }) {
                        Icon(
                            if (mostrarNueva) Icons.Default.Visibility
                            else Icons.Default.VisibilityOff,
                            contentDescription = "Mostrar contraseña"
                        )
                    }
                },
                visualTransformation = if (mostrarNueva)
                    VisualTransformation.None
                else
                    PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password
                ),
                singleLine = true,
                shape = RoundedCornerShape(14.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = celeste,
                    unfocusedBorderColor = azul,
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    focusedLabelColor = celeste,
                    unfocusedLabelColor = Color.LightGray,
                    cursorColor = celeste
                )
            )

            Spacer(modifier = Modifier.height(18.dp))

            OutlinedTextField(
                value = confirmarContrasena,
                onValueChange = { confirmarContrasena = it },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Confirmar nueva contraseña") },
                leadingIcon = {
                    Icon(Icons.Default.Lock, contentDescription = null)
                },
                trailingIcon = {
                    IconButton(onClick = {
                        mostrarConfirmar = !mostrarConfirmar
                    }) {
                        Icon(
                            if (mostrarConfirmar) Icons.Default.Visibility
                            else Icons.Default.VisibilityOff,
                            contentDescription = "Mostrar contraseña"
                        )
                    }
                },
                visualTransformation = if (mostrarConfirmar)
                    VisualTransformation.None
                else
                    PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password
                ),
                singleLine = true,
                shape = RoundedCornerShape(14.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = celeste,
                    unfocusedBorderColor = azul,
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    focusedLabelColor = celeste,
                    unfocusedLabelColor = Color.LightGray,
                    cursorColor = celeste
                )
            )

            Spacer(modifier = Modifier.height(18.dp))

            Text(
                text = "La contraseña debe contener:",
                color = celeste,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "• Mínimo 8 caracteres\n" +
                        "• Una letra mayúscula y una minúscula\n" +
                        "• Un número\n" +
                        "• Un carácter especial",
                color = Color.LightGray,
                fontSize = 12.sp,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(30.dp))

            Button(
                onClick = {

                    when {

                        contrasenaActual.isBlank() ||
                                nuevaContrasena.isBlank() ||
                                confirmarContrasena.isBlank() -> {

                            Toast.makeText(
                                context,
                                "Completa todos los campos",
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                        nuevaContrasena.length < 8 ||
                                !nuevaContrasena.any { it.isUpperCase() } ||
                                !nuevaContrasena.any { it.isLowerCase() } ||
                                !nuevaContrasena.any { it.isDigit() } ||
                                !nuevaContrasena.any { !it.isLetterOrDigit() } -> {

                            Toast.makeText(
                                context,
                                "La nueva contraseña no cumple los requisitos",
                                Toast.LENGTH_LONG
                            ).show()
                        }

                        nuevaContrasena != confirmarContrasena -> {

                            Toast.makeText(
                                context,
                                "Las contraseñas nuevas no coinciden",
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                        else -> {

                            scope.launch {

                                cargando = true

                                try {

                                    val resultado = withContext(Dispatchers.IO) {

                                        val usuario = usuarioDao.buscarUsuario(
                                            usuarioActual
                                        )

                                        if (usuario == null) {

                                            "No se encontró el usuario"

                                        } else if (
                                            !PasswordUtils.verificarContrasena(
                                                contrasenaActual,
                                                usuario.passwordHash,
                                                usuario.salt
                                            )
                                        ) {

                                            "La contraseña actual es incorrecta"

                                        } else if (
                                            PasswordUtils.verificarContrasena(
                                                nuevaContrasena,
                                                usuario.passwordHash,
                                                usuario.salt
                                            )
                                        ) {

                                            "Debes utilizar una contraseña diferente"

                                        } else {

                                            val nuevoSalt =
                                                PasswordUtils.generarSalt()

                                            val nuevoHash =
                                                PasswordUtils.generarHash(
                                                    nuevaContrasena,
                                                    nuevoSalt
                                                )

                                            usuarioDao.actualizarContrasena(
                                                usuario.id,
                                                nuevoHash,
                                                nuevoSalt
                                            )

                                            "CORRECTO"
                                        }
                                    }

                                    if (resultado == "CORRECTO") {

                                        Toast.makeText(
                                            context,
                                            "Contraseña actualizada correctamente",
                                            Toast.LENGTH_LONG
                                        ).show()

                                        contrasenaActual = ""
                                        nuevaContrasena = ""
                                        confirmarContrasena = ""

                                        onVolver()

                                    } else {

                                        Toast.makeText(
                                            context,
                                            resultado,
                                            Toast.LENGTH_LONG
                                        ).show()
                                    }

                                } catch (e: Exception) {

                                    Toast.makeText(
                                        context,
                                        "No se pudo actualizar la contraseña",
                                        Toast.LENGTH_LONG
                                    ).show()

                                } finally {

                                    cargando = false
                                }
                            }
                        }
                    }
                },
                enabled = !cargando,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(55.dp),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = azul
                )
            ) {

                Text(
                    text = if (cargando)
                        "ACTUALIZANDO..."
                    else
                        "GUARDAR CONTRASEÑA",
                    color = Color.White,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(15.dp))

            OutlinedButton(
                onClick = onVolver,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(14.dp)
            ) {
                Icon(
                    Icons.Default.ArrowBack,
                    contentDescription = null
                )

                Spacer(modifier = Modifier.width(8.dp))

                Text("VOLVER AL MENÚ")
            }

            Spacer(modifier = Modifier.height(25.dp))
        }
    }
}
