package com.example.proyecto_alkaravision.ui.screens.Add

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Icon
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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun AddClienteScreen(
    clienteDao: ClienteDao,
    onVolver: () -> Unit
) {

    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    var nombre by rememberSaveable { mutableStateOf("") }
    var identidad by rememberSaveable { mutableStateOf("") }
    var telefono by rememberSaveable { mutableStateOf("") }
    var correo by rememberSaveable { mutableStateOf("") }

    var departamento by rememberSaveable { mutableStateOf("") }
    var municipio by rememberSaveable { mutableStateOf("") }
    var localidad by rememberSaveable { mutableStateOf("") }
    var direccionExacta by rememberSaveable { mutableStateOf("") }

    var cargando by remember { mutableStateOf(false) }

    val azul = Color(0xFF087DEB)
    val celeste = Color(0xFF16C5FF)

    val municipiosDisponibles =
        UbicacionHonduras.municipiosPorDepartamento[departamento]
            ?: emptyList()

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
                text = "REGISTRAR CLIENTE",
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        Icon(
            imageVector = Icons.Default.PersonAdd,
            contentDescription = null,
            tint = celeste,
            modifier = Modifier.size(60.dp)
        )

        Text(
            text = "Nuevo Cliente",
            color = Color.White,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )

        Text(
            text = "Completa los datos del cliente",
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

        Spacer(modifier = Modifier.height(15.dp))

        SelectorUbicacion(
            etiqueta = "Departamento",
            valor = departamento,
            opciones = UbicacionHonduras.departamentos,
            habilitado = true,
            onSeleccionar = { nuevoDepartamento ->
                departamento = nuevoDepartamento
                municipio = ""
            }
        )

        Spacer(modifier = Modifier.height(12.dp))

        SelectorUbicacion(
            etiqueta = "Municipio",
            valor = municipio,
            opciones = municipiosDisponibles,
            habilitado = departamento.isNotBlank(),
            onSeleccionar = { nuevoMunicipio ->
                municipio = nuevoMunicipio
            }
        )

        Spacer(modifier = Modifier.height(12.dp))

        CampoProducto(
            valor = localidad,
            onValorChange = { localidad = it },
            etiqueta = "Colonia, barrio o aldea"
        )

        CampoProducto(
            valor = direccionExacta,
            onValorChange = { direccionExacta = it },
            etiqueta = "Dirección exacta",
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
                    departamento.isBlank() ||
                    municipio.isBlank() ||
                    localidad.isBlank() ||
                    direccionExacta.isBlank()
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

                            val clienteExistente =
                                withContext(Dispatchers.IO) {
                                    clienteDao.buscarCliente(
                                        identidad.trim()
                                    )
                                }

                            if (clienteExistente != null) {

                                Toast.makeText(
                                    context,
                                    "Ya existe un cliente con esa identidad",
                                    Toast.LENGTH_SHORT
                                ).show()

                            } else {

                                withContext(Dispatchers.IO) {

                                    clienteDao.insertarCliente(
                                        ClienteEntity(
                                            nombre = nombre.trim(),
                                            identidad = identidad.trim(),
                                            telefono = telefono.trim(),
                                            correo = correo.trim(),
                                            departamento = departamento,
                                            municipio = municipio,
                                            localidad = localidad.trim(),
                                            direccionExacta = direccionExacta.trim()
                                        )
                                    )
                                }

                                Toast.makeText(
                                    context,
                                    "Cliente registrado correctamente",
                                    Toast.LENGTH_SHORT
                                ).show()

                                nombre = ""
                                identidad = ""
                                telefono = ""
                                correo = ""
                                departamento = ""
                                municipio = ""
                                localidad = ""
                                direccionExacta = ""
                            }

                        } catch (e: Exception) {

                            Toast.makeText(
                                context,
                                "Error al registrar: ${e.message}",
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
                    text = "GUARDAR CLIENTE",
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(modifier = Modifier.height(25.dp))
    }
}


@Composable
fun SelectorUbicacion(
    etiqueta: String,
    valor: String,
    opciones: List<String>,
    habilitado: Boolean,
    onSeleccionar: (String) -> Unit
) {
    var expandido by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier.fillMaxWidth()
    ) {
        OutlinedTextField(
            value = valor,
            onValueChange = {},
            readOnly = true,
            enabled = habilitado,
            label = {
                Text(etiqueta)
            },
            trailingIcon = {
                IconButton(
                    onClick = {
                        expandido = !expandido
                    },
                    enabled = habilitado
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowDropDown,
                        contentDescription = "Mostrar opciones",
                        tint = Color(0xFF16C5FF)
                    )
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .clickable(enabled = habilitado) {
                    expandido = !expandido
                },
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White,
                focusedBorderColor = Color(0xFF16C5FF),
                unfocusedBorderColor = Color(0xFF16C5FF),
                focusedLabelColor = Color(0xFF16C5FF),
                unfocusedLabelColor = Color.LightGray,
                disabledTextColor = Color.LightGray,
                disabledBorderColor = Color.Gray
            )
        )

        DropdownMenu(
            expanded = expandido,
            onDismissRequest = {
                expandido = false
            },
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .heightIn(max = 250.dp),
            containerColor = Color(0xFF0B2547)
        ) {
            opciones.forEach { opcion ->
                DropdownMenuItem(
                    text = {
                        Text(
                            text = opcion,
                            color = Color.White
                        )
                    },
                    onClick = {
                        onSeleccionar(opcion)
                        expandido = false
                    }
                )
            }
        }
    }
}
