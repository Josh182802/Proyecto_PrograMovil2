
package com.example.proyecto_alkaravision.ui.screens.Add

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.proyecto_alkaravision.local.ProductoDao
import com.example.proyecto_alkaravision.local.ProductoEntity
import com.example.proyecto_alkaravision.ui.components.CamaraSection
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun AddProductoScreen(
    productoDao: ProductoDao,
    onVolver: () -> Unit
) {

    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    var codigo by rememberSaveable { mutableStateOf("") }
    var nombre by rememberSaveable { mutableStateOf("") }
    var categoria by rememberSaveable { mutableStateOf("") }
    var marca by rememberSaveable { mutableStateOf("") }
    var precio by rememberSaveable { mutableStateOf("") }
    var cantidad by rememberSaveable { mutableStateOf("") }
    var descripcion by rememberSaveable { mutableStateOf("") }
    var foto by rememberSaveable { mutableStateOf<String?>(null) }

    var cargando by remember { mutableStateOf(false) }

    val celeste = Color(0xFF16C5FF)
    val azul = Color(0xFF087DEB)

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
                    text = "REGISTRAR PRODUCTO",
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            Icon(
                imageVector = Icons.Default.Inventory2,
                contentDescription = null,
                tint = celeste,
                modifier = Modifier.size(65.dp)
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "Nuevo Producto",
                color = Color.White,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = "Completa los datos del producto",
                color = Color.LightGray,
                fontSize = 13.sp
            )

            Spacer(modifier = Modifier.height(25.dp))

            CampoProducto(
                valor = codigo,
                onValorChange = { codigo = it },
                etiqueta = "Código del producto"
            )

            CampoProducto(
                valor = nombre,
                onValorChange = { nombre = it },
                etiqueta = "Nombre del producto"
            )

            CampoProducto(
                valor = categoria,
                onValorChange = { categoria = it },
                etiqueta = "Categoría"
            )

            CampoProducto(
                valor = marca,
                onValorChange = { marca = it },
                etiqueta = "Marca"
            )

            CampoProducto(
                valor = precio,
                onValorChange = { precio = it },
                etiqueta = "Precio",
                tipo = KeyboardType.Decimal
            )

            CampoProducto(
                valor = cantidad,
                onValorChange = { cantidad = it },
                etiqueta = "Cantidad disponible",
                tipo = KeyboardType.Number
            )

            CampoProducto(
                valor = descripcion,
                onValorChange = { descripcion = it },
                etiqueta = "Descripción",
                multilinea = true
            )

            Spacer(modifier = Modifier.height(15.dp))

            // COMPONENTE DE CÁMARA
            CamaraSection(
                fotoActual = foto,
                onFotoCapturada = { nuevaFoto ->
                    foto = nuevaFoto
                }
            )

            Spacer(modifier = Modifier.height(20.dp))

            Button(
                onClick = {

                    val precioConvertido = precio.toDoubleOrNull()
                    val cantidadConvertida = cantidad.toIntOrNull()

                    if (
                        codigo.isBlank() ||
                        nombre.isBlank() ||
                        categoria.isBlank() ||
                        marca.isBlank() ||
                        precio.isBlank() ||
                        cantidad.isBlank() ||
                        descripcion.isBlank()
                    ) {

                        Toast.makeText(
                            context,
                            "Completa todos los campos",
                            Toast.LENGTH_SHORT
                        ).show()

                    } else if (
                        precioConvertido == null ||
                        precioConvertido < 0 ||
                        cantidadConvertida == null ||
                        cantidadConvertida < 0
                    ) {

                        Toast.makeText(
                            context,
                            "Verifica el precio y la cantidad",
                            Toast.LENGTH_SHORT
                        ).show()

                    } else {

                        cargando = true

                        scope.launch {
                            try {

                                withContext(Dispatchers.IO) {
                                    productoDao.insertarProducto(
                                        ProductoEntity(
                                            codigo = codigo.trim(),
                                            nombre = nombre.trim(),
                                            categoria = categoria.trim(),
                                            marca = marca.trim(),
                                            precio = precioConvertido,
                                            cantidad = cantidadConvertida,
                                            descripcion = descripcion.trim(),
                                            foto = foto
                                        )
                                    )
                                }

                                Toast.makeText(
                                    context,
                                    "Producto registrado correctamente",
                                    Toast.LENGTH_SHORT
                                ).show()

                                codigo = ""
                                nombre = ""
                                categoria = ""
                                marca = ""
                                precio = ""
                                cantidad = ""
                                descripcion = ""
                                foto = null

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
                        text = "GUARDAR PRODUCTO",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(25.dp))
        }
    }
}

@Composable
fun CampoProducto(
    valor: String,
    onValorChange: (String) -> Unit,
    etiqueta: String,
    tipo: KeyboardType = KeyboardType.Text,
    multilinea: Boolean = false
) {

    OutlinedTextField(
        value = valor,
        onValueChange = onValorChange,
        label = { Text(etiqueta) },
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        shape = RoundedCornerShape(12.dp),
        singleLine = !multilinea,
        minLines = if (multilinea) 3 else 1,
        keyboardOptions = KeyboardOptions(
            keyboardType = tipo
        ),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Color(0xFF16C5FF),
            unfocusedBorderColor = Color(0xFF55718F),
            focusedLabelColor = Color(0xFF16C5FF),
            unfocusedLabelColor = Color.LightGray,
            focusedTextColor = Color.White,
            unfocusedTextColor = Color.White,
            cursorColor = Color(0xFF16C5FF)
        )
    )
}
