package com.example.proyecto_alkaravision

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            PantallaLogin()
        }
    }
}

@Composable
fun PantallaLogin() {

    var usuario by remember { mutableStateOf("") }
    var contrasena by remember { mutableStateOf("") }
    var recordar by remember { mutableStateOf(false) }
    var mostrarContrasena by remember { mutableStateOf(false) }

    var mensaje by remember { mutableStateOf("") }

    val azul = Color(0xFF087DEB)
    val celeste = Color(0xFF16C5FF)
    val fondo = Color(0xFF050914)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF071B3A),
                        fondo,
                        Color.Black
                    )
                )
            )
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .imePadding()
                .padding(horizontal = 28.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            // Nombre de la empresa

            Text(
                text = "ALKARAVISION",
                color = Color.White,
                fontSize = 27.sp,
                fontWeight = FontWeight.ExtraBold
            )

            Spacer(modifier = Modifier.height(5.dp))

            Text(
                text = "SISTEMA DE TELEVISIÓN POR CABLE E INTERNET",
                color = celeste,
                fontSize = 9.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(22.dp))

            // Logo de la empresa

            Image(
                painter = painterResource(
                    id = R.drawable.logo_alkaravision
                ),
                contentDescription = "Logo Alkaravision",
                modifier = Modifier.size(190.dp)
            )

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "¡Bienvenido!",
                color = Color.White,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = "Inicia sesión para continuar",
                color = Color.LightGray,
                fontSize = 14.sp
            )

            Spacer(modifier = Modifier.height(30.dp))

            // Campo usuario

            OutlinedTextField(
                value = usuario,
                onValueChange = { usuario = it },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Usuario") },
                placeholder = {
                    Text("Ingresa tu usuario")
                },
                leadingIcon = {
                    Icon(
                        Icons.Default.Person,
                        contentDescription = null
                    )
                },
                singleLine = true,
                shape = RoundedCornerShape(14.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = celeste,
                    unfocusedBorderColor = azul,
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    focusedLabelColor = celeste,
                    unfocusedLabelColor = Color.LightGray,
                    cursorColor = celeste,
                    focusedLeadingIconColor = celeste,
                    unfocusedLeadingIconColor = Color.LightGray
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Campo contraseña

            OutlinedTextField(
                value = contrasena,
                onValueChange = { contrasena = it },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Contraseña") },
                placeholder = {
                    Text("Ingresa tu contraseña")
                },
                leadingIcon = {
                    Icon(
                        Icons.Default.Lock,
                        contentDescription = null
                    )
                },
                trailingIcon = {
                    IconButton(
                        onClick = {
                            mostrarContrasena = !mostrarContrasena
                        }
                    ) {
                        Icon(
                            imageVector = if (mostrarContrasena)
                                Icons.Default.Visibility
                            else
                                Icons.Default.VisibilityOff,
                            contentDescription = "Mostrar contraseña"
                        )
                    }
                },
                visualTransformation = if (mostrarContrasena)
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
                    cursorColor = celeste,
                    focusedLeadingIconColor = celeste,
                    unfocusedLeadingIconColor = Color.LightGray
                )
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Recordar usuario y recuperar contraseña

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Checkbox(
                    checked = recordar,
                    onCheckedChange = { recordar = it },
                    colors = CheckboxDefaults.colors(
                        checkedColor = azul,
                        uncheckedColor = Color.LightGray
                    )
                )

                Text(
                    text = "Recordarme",
                    color = Color.LightGray,
                    fontSize = 12.sp
                )

                Spacer(modifier = Modifier.weight(1f))

                TextButton(
                    onClick = {
                        mensaje = "Contacta con soporte para recuperar tu contraseña"
                    }
                ) {
                    Text(
                        text = "¿Olvidaste tu contraseña?",
                        color = celeste,
                        fontSize = 11.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Botón iniciar sesión

            Button(
                onClick = {
                    if (usuario.isBlank() || contrasena.isBlank()) {
                        mensaje = "Completa todos los campos"
                    } else {
                        mensaje = "Conecta aquí la validación del usuario"
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(55.dp),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = azul
                )
            ) {

                Text(
                    text = "INICIAR SESIÓN",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )

                Spacer(modifier = Modifier.width(10.dp))

                Icon(
                    Icons.Default.Login,
                    contentDescription = null
                )
            }

            if (mensaje.isNotEmpty()) {
                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = mensaje,
                    color = celeste,
                    fontSize = 12.sp
                )
            }

            Spacer(modifier = Modifier.height(30.dp))

            HorizontalDivider(
                color = Color(0xFF183B65)
            )

            Spacer(modifier = Modifier.height(15.dp))

            Text(
                text = "© 2026 ALKARAVISION",
                color = Color.Gray,
                fontSize = 11.sp
            )

            Text(
                text = "Todos los derechos reservados",
                color = Color.Gray,
                fontSize = 10.sp
            )
        }
    }
}
