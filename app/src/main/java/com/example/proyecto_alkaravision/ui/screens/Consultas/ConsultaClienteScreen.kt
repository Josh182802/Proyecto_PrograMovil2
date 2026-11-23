
package com.example.proyecto_alkaravision.ui.screens.Consultas

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.proyecto_alkaravision.local.ClienteDao
import com.example.proyecto_alkaravision.local.ClienteEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun ConsultaClientesScreen(
    clienteDao: ClienteDao,
    onVolver: () -> Unit,
    onEditar: (ClienteEntity) -> Unit
) {

    var busqueda by rememberSaveable { mutableStateOf("") }

    val clientes by clienteDao.buscarClientesPorTexto(busqueda)
        .collectAsState(initial = emptyList())

    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    var clienteEliminar by remember {
        mutableStateOf<ClienteEntity?>(null)
    }

    var eliminando by remember {
        mutableStateOf(false)
    }

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
            .padding(20.dp)
    ) {

        Button(
            onClick = onVolver,
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF087DEB)
            ),
            shape = RoundedCornerShape(12.dp)
        ) {

            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Volver"
            )

            Spacer(modifier = Modifier.width(8.dp))

            Text("VOLVER AL MENÚ")
        }

        Spacer(modifier = Modifier.height(25.dp))

        Text(
            text = "CLIENTES",
            color = Color.White,
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold
        )

        Text(
            text = "Clientes registrados",
            color = celeste,
            fontSize = 15.sp
        )

        Spacer(modifier = Modifier.height(20.dp))

        OutlinedTextField(
            value = busqueda,
            onValueChange = { busqueda = it },
            modifier = Modifier.fillMaxWidth(),
            placeholder = {
                Text(
                    text = "Buscar por nombre, identidad, municipio, departamento...",
                    color = Color.LightGray
                )
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Buscar",
                    tint = celeste
                )
            },
            trailingIcon = {
                if (busqueda.isNotEmpty()) {
                    IconButton(
                        onClick = { busqueda = "" }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Limpiar búsqueda",
                            tint = celeste
                        )
                    }
                }
            },
            singleLine = true,
            shape = RoundedCornerShape(14.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = celeste,
                unfocusedBorderColor = celeste.copy(alpha = 0.5f),
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White,
                cursorColor = celeste,
                focusedLeadingIconColor = celeste,
                unfocusedLeadingIconColor = celeste
            )
        )

        Spacer(modifier = Modifier.height(15.dp))

        Text(
            text = "Clientes encontrados: ${clientes.size}",
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp
        )

        Spacer(modifier = Modifier.height(15.dp))

        if (clientes.isEmpty()) {

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Icon(
                        imageVector = Icons.Default.People,
                        contentDescription = null,
                        tint = celeste,
                        modifier = Modifier.size(65.dp)
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = if (busqueda.isNotBlank()) {
                            "No se encontraron clientes"
                        } else {
                            "No hay clientes registrados"
                        },
                        color = Color.White,
                        fontSize = 16.sp
                    )

                    if (busqueda.isNotBlank()) {
                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = "Intenta con otro nombre, identidad, municipio o departamento",
                            color = Color.LightGray,
                            fontSize = 13.sp
                        )
                    }
                }
            }

        } else {

            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {

                items(
                    items = clientes,
                    key = { it.id }
                ) { cliente ->

                    TarjetaCliente(
                        cliente = cliente,
                        onEditar = { onEditar(cliente) },
                        onEliminar = { clienteEliminar = cliente }
                    )
                }
            }
        }
    }

    clienteEliminar?.let { cliente ->

        AlertDialog(
            onDismissRequest = {
                if (!eliminando) {
                    clienteEliminar = null
                }
            },
            title = {
                Text("Eliminar cliente")
            },
            text = {
                Text(
                    "¿Estás seguro de que deseas eliminar a ${cliente.nombre}?"
                )
            },
            confirmButton = {

                TextButton(
                    enabled = !eliminando,
                    onClick = {

                        eliminando = true

                        scope.launch {

                            try {

                                withContext(Dispatchers.IO) {
                                    clienteDao.eliminarCliente(cliente)
                                }

                                Toast.makeText(
                                    context,
                                    "Cliente eliminado correctamente",
                                    Toast.LENGTH_SHORT
                                ).show()

                                clienteEliminar = null

                            } catch (e: Exception) {

                                Toast.makeText(
                                    context,
                                    "No se pudo eliminar el cliente. Inténtalo nuevamente.",
                                    Toast.LENGTH_LONG
                                ).show()

                            } finally {
                                eliminando = false
                            }
                        }
                    }
                ) {

                    if (eliminando) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(18.dp),
                            strokeWidth = 2.dp
                        )
                    } else {
                        Text(
                            "ELIMINAR",
                            color = Color.Red
                        )
                    }
                }
            },
            dismissButton = {

                TextButton(
                    enabled = !eliminando,
                    onClick = {
                        clienteEliminar = null
                    }
                ) {
                    Text("CANCELAR")
                }
            }
        )
    }
}

@Composable
fun TarjetaCliente(
    cliente: ClienteEntity,
    onEditar: () -> Unit,
    onEliminar: () -> Unit
) {

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF10294A)
        )
    ) {

        Column(
            modifier = Modifier.padding(18.dp)
        ) {

            Text(
                text = cliente.nombre,
                color = Color.White,
                fontSize = 19.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(12.dp))

            DatoCliente("Identidad", cliente.identidad)
            DatoCliente("Teléfono", cliente.telefono)
            DatoCliente("Correo electrónico", cliente.correo)
            DatoCliente("Departamento", cliente.departamento)
            DatoCliente("Municipio", cliente.municipio)
            DatoCliente("Colonia, barrio o aldea", cliente.localidad)
            DatoCliente("Dirección exacta", cliente.direccionExacta)

            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick = onEditar,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF087DEB)
                )
            ) {

                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Editar"
                )

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = "EDITAR CLIENTE",
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = onEliminar,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFD32F2F)
                )
            ) {

                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Eliminar"
                )

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = "ELIMINAR CLIENTE",
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
fun DatoCliente(
    titulo: String,
    valor: String
) {

    Column(
        modifier = Modifier.padding(vertical = 4.dp)
    ) {

        Text(
            text = titulo,
            color = Color(0xFF16C5FF),
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold
        )

        Text(
            text = valor,
            color = Color.White,
            fontSize = 14.sp
        )
    }
}
