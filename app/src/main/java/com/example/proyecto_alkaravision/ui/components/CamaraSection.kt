
package com.example.proyecto_alkaravision.ui.components

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import java.io.File
import java.io.FileOutputStream

@Composable
fun CamaraSection(
    fotoActual: String?,
    onFotoCapturada: (String?) -> Unit
) {
    val context = LocalContext.current

    val azulMarino = Color(0xFF071B3A)
    val azulOscuro = Color(0xFF0B2547)
    val azul = Color(0xFF087DEB)
    val celeste = Color(0xFF16C5FF)

    var imagen by remember(fotoActual) {
        mutableStateOf(
            fotoActual?.let {
                BitmapFactory.decodeFile(it)?.asImageBitmap()
            }
        )
    }

    val launcherCamara = rememberLauncherForActivityResult(
        ActivityResultContracts.TakePicturePreview()
    ) { bitmap: Bitmap? ->

        if (bitmap != null) {
            try {
                val archivo = File(
                    context.filesDir,
                    "producto_${System.currentTimeMillis()}.jpg"
                )

                FileOutputStream(archivo).use { salida ->
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 90, salida)
                }

                imagen = bitmap.asImageBitmap()
                onFotoCapturada(archivo.absolutePath)

                Toast.makeText(
                    context,
                    "Fotografía guardada correctamente",
                    Toast.LENGTH_SHORT
                ).show()

            } catch (e: Exception) {
                Toast.makeText(
                    context,
                    "Error al guardar la fotografía",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    val permisoCamara = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { concedido ->

        if (concedido) {
            launcherCamara.launch(null)
        } else {
            Toast.makeText(
                context,
                "Debes habilitar el permiso de cámara",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .border(
                width = 1.dp,
                color = celeste.copy(alpha = 0.45f),
                shape = RoundedCornerShape(18.dp)
            ),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = azulOscuro
        )
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Icon(
                    imageVector = Icons.Default.PhotoCamera,
                    contentDescription = null,
                    tint = celeste,
                    modifier = Modifier.size(26.dp)
                )

                Spacer(modifier = Modifier.width(10.dp))

                Column {
                    Text(
                        text = "Fotografía del producto",
                        color = Color.White,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        text = "Agrega una imagen a tu inventario",
                        color = Color.LightGray,
                        fontSize = 12.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(190.dp)
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                azulMarino,
                                Color(0xFF050914)
                            )
                        ),
                        RoundedCornerShape(14.dp)
                    )
                    .border(
                        width = 1.dp,
                        color = celeste.copy(alpha = 0.35f),
                        shape = RoundedCornerShape(14.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {

                if (imagen != null) {

                    Image(
                        bitmap = imagen!!,
                        contentDescription = "Fotografía del producto",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )

                } else {

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                        Icon(
                            imageVector = Icons.Default.PhotoCamera,
                            contentDescription = null,
                            tint = celeste,
                            modifier = Modifier.size(48.dp)
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            text = "Sin fotografía",
                            color = Color.LightGray,
                            fontSize = 14.sp
                        )

                        Text(
                            text = "Presiona el botón para capturar",
                            color = Color.Gray,
                            fontSize = 11.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {

                    if (
                        ContextCompat.checkSelfPermission(
                            context,
                            Manifest.permission.CAMERA
                        ) == PackageManager.PERMISSION_GRANTED
                    ) {
                        launcherCamara.launch(null)
                    } else {
                        permisoCamara.launch(Manifest.permission.CAMERA)
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = azul
                )
            ) {

                Icon(
                    imageVector = Icons.Default.PhotoCamera,
                    contentDescription = null,
                    tint = Color.White
                )

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = if (imagen == null)
                        "TOMAR FOTOGRAFÍA"
                    else
                        "VOLVER A TOMAR",
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            }

            if (imagen != null) {

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedButton(
                    onClick = {
                        imagen = null
                        onFotoCapturada(null)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(45.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = celeste
                    ),
                    border = androidx.compose.foundation.BorderStroke(
                        1.dp,
                        celeste
                    )
                ) {

                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = null
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    Text("QUITAR FOTOGRAFÍA")
                }
            }
        }
    }
}
