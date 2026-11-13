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
import com.example.proyecto_alkaravision.local.ProductoDao
import com.example.proyecto_alkaravision.local.ProductoEntity
import com.example.proyecto_alkaravision.ui.components.CamaraSection
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

    var codigo by rememberSaveable (producto.id) {
        mutableStateOf(producto.codigo)
    }

    var nombre by rememberSaveable (producto.id) {
        mutableStateOf(producto.nombre)
    }

    var categoria by rememberSaveable (producto.id) {
        mutableStateOf(producto.categoria)
    }

    var marca by rememberSaveable (producto.id) {
        mutableStateOf(producto.marca)
    }

    var precio by rememberSaveable (producto.id) {
        mutableStateOf(producto.precio.toString())
    }

    var cantidad by rememberSaveable (producto.id) {
        mutableStateOf(producto.cantidad.toString())
    }

    var descripcion by rememberSaveable (producto.id) {
        mutableStateOf(producto.descripcion)
    }

    var foto by rememberSaveable(producto.id) {
        mutableStateOf<String?>(producto.foto)
    }

    var cargando by remember {
        mutableStateOf(false)
    }

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

        Spacer(modifier = Modifier.height(15.dp))

        CamaraSection(
            fotoActual = foto,
            onFotoCapturada = { nuevaFoto ->
                foto = nuevaFoto
            }
        )

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = {

                val precioConvertido = precio.trim()
                    .replace(',', '.')
                    .toDoubleOrNull()

                val cantidadConvertida = cantidad.trim().toIntOrNull()

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
                    !precioConvertido.isFinite() ||
                    precioConvertido <= 0.0
                ) {

                    Toast.makeText(
                        context,
                        "El precio debe ser un número mayor que cero",
                        Toast.LENGTH_LONG
                    ).show()

                } else if (
                    cantidadConvertida == null ||
                    cantidadConvertida < 0
                ) {

                    Toast.makeText(
                        context,
                        "La cantidad debe ser un número entero igual o mayor que cero",
                        Toast.LENGTH_LONG
                    ).show()

                } else {

                    cargando = true


                    scope.launch {
                        try {

                            val codigoLimpio = codigo.trim()

                            val productoExistente = withContext(Dispatchers.IO) {
                                productoDao.buscarProducto(codigoLimpio)
                            }

                            if (
                                productoExistente != null &&
                                productoExistente.id != producto.id
                            ) {

                                Toast.makeText(
                                    context,
                                    "Ese código ya pertenece a otro producto",
                                    Toast.LENGTH_LONG
                                ).show()

                            } else {

                                withContext(Dispatchers.IO) {
                                    productoDao.actualizarProducto(
                                        ProductoEntity(
                                            id = producto.id,
                                            codigo = codigoLimpio,
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
                                    "Producto actualizado correctamente",
                                    Toast.LENGTH_SHORT
                                ).show()

                                onVolver()
                            }

                        } catch (e: Exception) {

                            Toast.makeText(
                                context,
                                "Error al actualizar el producto",
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
