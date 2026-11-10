package com.example.proyecto_alkaravision.ui.screens.Editar

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.text.KeyboardOptions
import com.example.proyecto_alkaravision.local.ClienteDao
import com.example.proyecto_alkaravision.local.ClienteEntity
import com.example.proyecto_alkaravision.ui.screens.Add.CampoProducto
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun EditarClienteScreen(
    cliente: ClienteEntity,
    clienteDao: ClienteDao,
    onVolver: () -> Unit
) {

    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    var nombre by remember { mutableStateOf(cliente.nombre) }
    var identidad by remember { mutableStateOf(cliente.identidad) }
    var telefono by remember { mutableStateOf(cliente.telefono) }
    var correo by remember { mutableStateOf(cliente.correo) }
    var direccion by remember { mutableStateOf(cliente.direccion) }
    var cargando by remember { mutableStateOf(false) }

    val azul = Color(0xFF087DEB)
    val celeste = Color(0xFF16C5FF)

    Column(
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
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Button(
            onClick = onVolver,
            colors = ButtonDefaults.buttonColors(containerColor = azul),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.align(Alignment.Start)
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Volver"
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text("VOLVER")
        }

        Spacer(modifier = Modifier.height(20.dp))

        Icon(
            imageVector = Icons.Default.Edit,
            contentDescription = null,
            tint = celeste,
            modifier = Modifier.size(60.dp)
        )

        Text(
            text = "EDITAR CLIENTE",
            color = Color.White,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )

        Text(
            text = "Modifica los datos del cliente",
            color = Color.LightGray,
            fontSize = 13.sp
        )

        Spacer(modifier = Modifier.height(25.dp))

        CampoProducto(
            valor = nombre,
            onValorChange = { nombre = it },
            etiqueta = "Nombre completo"
        )

        CampoProducto(
            valor = identidad,
            onValorChange = { identidad = it },
            etiqueta = "Número de identidad",
            tipo = KeyboardType.Number
        )

        CampoProducto(
            valor = telefono,
            onValorChange = { telefono = it },
            etiqueta = "Teléfono",
            tipo = KeyboardType.Phone
        )

        CampoProducto(
            valor = correo,
            onValorChange = { correo = it },
            etiqueta = "Correo electrónico",
            tipo = KeyboardType.Email
        )

        CampoProducto(
            valor = direccion,
            onValorChange = { direccion = it },
            etiqueta = "Dirección",
            multilinea = true
        )

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = {

                if (
                    nombre.isBlank() ||
                    identidad.isBlank() ||
                    telefono.isBlank() ||
                    correo.isBlank() ||
                    direccion.isBlank()
                ) {
                    Toast.makeText(
                        context,
                        "Completa todos los campos",
                        Toast.LENGTH_SHORT
                    ).show()

                } else if (
                    !android.util.Patterns.EMAIL_ADDRESS
                        .matcher(correo.trim())
                        .matches()
                ) {
                    Toast.makeText(
                        context,
                        "Ingresa un correo electrónico válido",
                        Toast.LENGTH_SHORT
                    ).show()

                } else {

                    cargando = true

                    scope.launch {

                        try {

                            val clienteExistente = withContext(Dispatchers.IO) {
                                clienteDao.buscarCliente(identidad.trim())
                            }

                            if (
                                clienteExistente != null &&
                                clienteExistente.id != cliente.id
                            ) {

                                Toast.makeText(
                                    context,
                                    "Esa identidad pertenece a otro cliente",
                                    Toast.LENGTH_SHORT
                                ).show()

                            } else {

                                withContext(Dispatchers.IO) {

                                    clienteDao.actualizarCliente(
                                        ClienteEntity(
                                            id = cliente.id,
                                            nombre = nombre.trim(),
                                            identidad = identidad.trim(),
                                            telefono = telefono.trim(),
                                            correo = correo.trim(),
                                            direccion = direccion.trim()
                                        )
                                    )
                                }

                                Toast.makeText(
                                    context,
                                    "Cliente actualizado correctamente",
                                    Toast.LENGTH_SHORT
                                ).show()

                                onVolver()
                            }

                        } catch (e: Exception) {

                            Toast.makeText(
                                context,
                                "Error al actualizar: ${e.message}",
                                Toast.LENGTH_LONG
                            ).show()

                        } finally {
                            cargando = false
                        }
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(55.dp),
            shape = RoundedCornerShape(14.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = azul
            ),
            enabled = !cargando
        ) {

            if (cargando) {

                CircularProgressIndicator(
                    modifier = Modifier.size(22.dp),
                    color = Color.White,
                    strokeWidth = 2.dp
                )

            } else {

                Text(
                    text = "GUARDAR CAMBIOS",
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(modifier = Modifier.height(25.dp))
    }
}
