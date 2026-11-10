package com.example.proyecto_alkaravision.ui.screens.Editar

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
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
import com.example.proyecto_alkaravision.local.ProductoDao
import com.example.proyecto_alkaravision.local.ProductoEntity
import com.example.proyecto_alkaravision.ui.screens.Add.CampoProducto
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun EditarProductoScreen(
    producto: ProductoEntity,
    productoDao: ProductoDao,
    onVolver: () -> Unit
) {

    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    var codigo by remember(producto.id) { mutableStateOf(producto.codigo) }
    var nombre by remember(producto.id) { mutableStateOf(producto.nombre) }
    var categoria by remember(producto.id) { mutableStateOf(producto.categoria) }
    var marca by remember(producto.id) { mutableStateOf(producto.marca) }
    var precio by remember(producto.id) { mutableStateOf(producto.precio.toString()) }
    var cantidad by remember(producto.id) { mutableStateOf(producto.cantidad.toString()) }
    var descripcion by remember(producto.id) { mutableStateOf(producto.descripcion) }

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
            colors = ButtonDefaults.buttonColors(
                containerColor = azul
            ),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.align(Alignment.Start)
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Volver"
            )

            Spacer(modifier = Modifier.width(8.dp))

            Text("VOLVER A PRODUCTOS")
        }

        Spacer(modifier = Modifier.height(20.dp))

        Icon(
            imageVector = Icons.Default.Edit,
            contentDescription = null,
            tint = celeste,
            modifier = Modifier.size(55.dp)
        )

        Text(
            text = "EDITAR PRODUCTO",
            color = Color.White,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(20.dp))

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

                                productoDao.actualizarProducto(
                                    ProductoEntity(
                                        id = producto.id,
                                        codigo = codigo.trim(),
                                        nombre = nombre.trim(),
                                        categoria = categoria.trim(),
                                        marca = marca.trim(),
                                        precio = precioConvertido,
                                        cantidad = cantidadConvertida,
                                        descripcion = descripcion.trim()
                                    )
                                )
                            }

                            Toast.makeText(
                                context,
                                "Producto actualizado correctamente",
                                Toast.LENGTH_SHORT
                            ).show()

                            onVolver()

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
                    color = Color.White
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
