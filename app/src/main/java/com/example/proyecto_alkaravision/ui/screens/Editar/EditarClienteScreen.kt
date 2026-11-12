
package com.example.proyecto_alkaravision.ui.screens.Editar

import android.util.Patterns
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
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.proyecto_alkaravision.local.ClienteDao
import com.example.proyecto_alkaravision.local.ClienteEntity
import com.example.proyecto_alkaravision.ui.components.UbicacionHonduras
import com.example.proyecto_alkaravision.ui.screens.Add.CampoProducto
import com.example.proyecto_alkaravision.ui.screens.Add.SelectorUbicacion
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun EditarClienteScreen(
    cliente: ClienteEntity,
    clienteDao: ClienteDao,
    onVolver: () -> Unit
) {
    val contexto = LocalContext.current
    val scope = rememberCoroutineScope()

    var nombre by rememberSaveable { mutableStateOf(cliente.nombre) }
    var identidad by rememberSaveable { mutableStateOf(cliente.identidad) }
    var telefono by rememberSaveable { mutableStateOf(cliente.telefono) }
    var correo by rememberSaveable { mutableStateOf(cliente.correo) }

    var departamento by rememberSaveable {
        mutableStateOf(cliente.departamento)
    }

    var municipio by rememberSaveable {
        mutableStateOf(cliente.municipio)
    }

    var localidad by rememberSaveable {
        mutableStateOf(cliente.localidad)
    }

    var direccionExacta by rememberSaveable {
        mutableStateOf(cliente.direccionExacta)
    }

    var cargando by remember { mutableStateOf(false) }

    val municipiosDisponibles =
        UbicacionHonduras.municipiosPorDepartamento[departamento]
            ?: emptyList()

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

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {

            IconButton(onClick = onVolver) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Volver",
                    tint = Color.White
                )
            }

            Text(
                text = "VOLVER",
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(25.dp))

        Icon(
            imageVector = Icons.Default.Edit,
            contentDescription = "Editar cliente",
            tint = celeste,
            modifier = Modifier.size(60.dp)
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "EDITAR CLIENTE",
            color = Color.White,
            fontSize = 25.sp,
            fontWeight = FontWeight.Bold
        )

        Text(
            text = "Modifica los datos del cliente",
            color = Color.LightGray,
            fontSize = 14.sp
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

        Spacer(modifier = Modifier.height(12.dp))

        SelectorUbicacion(
            etiqueta = "Departamento",
            valor = departamento,
            opciones = UbicacionHonduras.departamentos,
            habilitado = true,
            onSeleccionar = {
                departamento = it
                municipio = ""
            }
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Municipio
        SelectorUbicacion(
            etiqueta = "Municipio",
            valor = municipio,
            opciones = municipiosDisponibles,
            habilitado = departamento.isNotBlank(),
            onSeleccionar = {
                municipio = it
            }
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Colonia, barrio o aldea
        CampoProducto(
            valor = localidad,
            onValorChange = { localidad = it },
            etiqueta = "Colonia, barrio o aldea"
        )

        // Dirección exacta
        CampoProducto(
            valor = direccionExacta,
            onValorChange = { direccionExacta = it },
            etiqueta = "Dirección exacta",
            multilinea = true
        )

        Spacer(modifier = Modifier.height(25.dp))

        Button(
            onClick = {

                if (
                    nombre.isBlank() ||
                    identidad.isBlank() ||
                    telefono.isBlank() ||
                    correo.isBlank() ||
                    departamento.isBlank() ||
                    municipio.isBlank() ||
                    localidad.isBlank() ||
                    direccionExacta.isBlank()
                ) {
                    Toast.makeText(
                        contexto,
                        "Completa todos los campos",
                        Toast.LENGTH_SHORT
                    ).show()

                    return@Button
                }

                if (!Patterns.EMAIL_ADDRESS
                        .matcher(correo.trim())
                        .matches()
                ) {
                    Toast.makeText(
                        contexto,
                        "Ingresa un correo electrónico válido",
                        Toast.LENGTH_SHORT
                    ).show()

                    return@Button
                }

                scope.launch {
                    cargando = true

                    try {
                        val clienteExistente = withContext(Dispatchers.IO) {
                            clienteDao.buscarCliente(identidad.trim())
                        }

                        if (
                            clienteExistente != null &&
                            clienteExistente.id != cliente.id
                        ) {
                            Toast.makeText(
                                contexto,
                                "Ya existe un cliente con esa identidad",
                                Toast.LENGTH_LONG
                            ).show()

                        } else {

                            val clienteActualizado = ClienteEntity(
                                id = cliente.id,
                                nombre = nombre.trim(),
                                identidad = identidad.trim(),
                                telefono = telefono.trim(),
                                correo = correo.trim(),
                                departamento = departamento,
                                municipio = municipio,
                                localidad = localidad.trim(),
                                direccionExacta = direccionExacta.trim()
                            )

                            withContext(Dispatchers.IO) {
                                clienteDao.actualizarCliente(
                                    clienteActualizado
                                )
                            }

                            Toast.makeText(
                                contexto,
                                "Cliente actualizado correctamente",
                                Toast.LENGTH_SHORT
                            ).show()

                            onVolver()
                        }

                    } catch (e: Exception) {

                        Toast.makeText(
                            contexto,
                            "Error al actualizar: ${e.message}",
                            Toast.LENGTH_LONG
                        ).show()

                    } finally {
                        cargando = false
                    }
                }
            },
            enabled = !cargando,
            modifier = Modifier
                .fillMaxWidth()
                .height(55.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = azul
            ),
            shape = RoundedCornerShape(14.dp)
        ) {

            if (cargando) {
                CircularProgressIndicator(
                    color = Color.White,
                    modifier = Modifier.size(25.dp),
                    strokeWidth = 2.dp
                )
            } else {
                Text(
                    text = "GUARDAR CAMBIOS",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))
    }
}
