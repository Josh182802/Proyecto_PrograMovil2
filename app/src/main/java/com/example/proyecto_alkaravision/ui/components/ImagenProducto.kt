package com.example.proyecto_alkaravision.ui.components

import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp

@Composable
fun ImagenProducto(
    rutaFoto: String?,
    modifier: Modifier = Modifier,
    altura: Int = 150
) {
    val celeste = Color(0xFF16C5FF)
    val azulMarino = Color(0xFF071B3A)

    val imagen = remember(rutaFoto) {
        rutaFoto?.let {
            BitmapFactory.decodeFile(it)?.asImageBitmap()
        }
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(altura.dp)
            .background(
                azulMarino,
                RoundedCornerShape(14.dp)
            )
            .border(
                1.dp,
                celeste.copy(alpha = 0.4f),
                RoundedCornerShape(14.dp)
            ),
        contentAlignment = Alignment.Center
    ) {
        if (imagen != null) {
            Image(
                bitmap = imagen,
                contentDescription = "Fotografía del producto",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        } else {
            Icon(
                imageVector = Icons.Default.Inventory2,
                contentDescription = "Sin fotografía",
                tint = celeste,
                modifier = Modifier.size(48.dp)
            )
        }
    }
}
