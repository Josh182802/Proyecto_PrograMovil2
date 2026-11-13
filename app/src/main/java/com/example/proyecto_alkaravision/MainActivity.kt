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
import android.content.Context
import androidx.compose.ui.platform.LocalContext
import androidx.compose.runtime.saveable.rememberSaveable
import com.example.proyecto_alkaravision.local.AlkaravisionDatabase
import com.example.proyecto_alkaravision.local.UsuarioDao
import com.example.proyecto_alkaravision.local.UsuarioEntity
import com.example.proyecto_alkaravision.security.PasswordUtils
import com.example.proyecto_alkaravision.local.ProductoDao
import com.example.proyecto_alkaravision.ui.screens.Add.AddProductoScreen
import com.example.proyecto_alkaravision.ui.screens.Consultas.ConsultaProductosScreen
import com.example.proyecto_alkaravision.local.ProductoEntity
import com.example.proyecto_alkaravision.ui.screens.Editar.EditarProductoScreen
import com.example.proyecto_alkaravision.local.ClienteDao
import com.example.proyecto_alkaravision.local.ClienteEntity
import com.example.proyecto_alkaravision.ui.screens.Add.AddClienteScreen
import com.example.proyecto_alkaravision.ui.screens.Consultas.ConsultaClientesScreen
import com.example.proyecto_alkaravision.ui.screens.Editar.EditarClienteScreen
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val baseDatos = AlkaravisionDatabase.obtenerBaseDatos(this)
        val usuarioDao = baseDatos.usuarioDao()
        val productoDao = baseDatos.productoDao()
        val clienteDao = baseDatos.clienteDao()

        setContent {

            var sesionIniciada by rememberSaveable {
                mutableStateOf(false)
            }

            if (sesionIniciada) {
                PantallaPrincipal(
                    productoDao = productoDao,
                    clienteDao = clienteDao,
                    onCerrarSesion = {
                        sesionIniciada = false
                    }
                )
            } else {
                PantallaLogin(
                    usuarioDao = usuarioDao,
                    onLoginExitoso = {
                        sesionIniciada = true
                    }
                )
            }
        }
    }
}


@Composable
fun PantallaLogin(
    usuarioDao: UsuarioDao,
    onLoginExitoso: () -> Unit
) {

    var usuario by rememberSaveable { mutableStateOf("") }
    var contrasena by remember { mutableStateOf("") }
    var recordar by rememberSaveable { mutableStateOf(false) }
    var mostrarContrasena by remember { mutableStateOf(false) }

    var mensaje by remember { mutableStateOf("") }

    val scope = rememberCoroutineScope()

    var inicializado by remember {
        mutableStateOf(false)
    }

    var cargando by remember {
        mutableStateOf(false)
    }

    val azul = Color(0xFF087DEB)
    val celeste = Color(0xFF16C5FF)
    val fondo = Color(0xFF050914)

    val context = LocalContext.current

    val preferencias = remember { context.getSharedPreferences( "alkaravision_preferencias",
            Context.MODE_PRIVATE) }


    LaunchedEffect(Unit) {

        try {

            recordar = preferencias.getBoolean("recordar_usuario", false)

            if (recordar) {
                usuario = preferencias.getString("usuario_recordado", "") ?: ""
            }

            withContext(Dispatchers.IO) {

                if (usuarioDao.contarUsuarios() == 0) {

                    val salt = PasswordUtils.generarSalt()

                    val hash = PasswordUtils.generarHash(
                        "Admin123*",
                        salt
                    )

                    usuarioDao.insertarUsuario(
                        UsuarioEntity(
                            usuario = "admin",
                            passwordHash = hash,
                            salt = salt
                        )
                    )
                }
            }

        } catch (e: Exception) {

            mensaje = "No se pudo inicializar la aplicación. Inténtalo nuevamente."

        } finally {

            inicializado = true
        }
    }


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

            Button(
                onClick = {

                    if (usuario.isBlank() || contrasena.isBlank()) {

                        mensaje = "Completa todos los campos"

                    } else {

                        scope.launch {

                            cargando = true
                            mensaje = ""

                            try {

                                val resultado = withContext(Dispatchers.IO) {

                                    val usuarioEncontrado =
                                        usuarioDao.buscarUsuario(usuario.trim())

                                    if (usuarioEncontrado == null) {

                                        "Usuario o contraseña incorrectos"

                                    } else {

                                        val contrasenaCorrecta =
                                            PasswordUtils.verificarContrasena(
                                                contrasena,
                                                usuarioEncontrado.passwordHash,
                                                usuarioEncontrado.salt
                                            )

                                        if (contrasenaCorrecta) {
                                            "CORRECTO"
                                        } else {
                                            "Usuario o contraseña incorrectos"
                                        }
                                    }
                                }

                                if (resultado == "CORRECTO") {

                                    if (recordar) {

                                        preferencias.edit()
                                            .putBoolean("recordar_usuario", true)
                                            .putString("usuario_recordado", usuario.trim())
                                            .apply()

                                    } else {

                                        preferencias.edit()
                                            .remove("usuario_recordado")
                                            .putBoolean("recordar_usuario", false)
                                            .apply()
                                    }

                                    contrasena = ""
                                    onLoginExitoso()

                                } else {

                                    mensaje = resultado
                                }

                            } catch (e: Exception) {

                                mensaje = "Ocurrió un error al iniciar sesión. Inténtalo nuevamente."

                            } finally {

                                cargando = false
                            }
                        }
                    }
                },
                enabled = inicializado && !cargando,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(55.dp),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = azul
                )
            ) {

                Text(
                    text = if (cargando) "VALIDANDO..." else "INICIAR SESIÓN",
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

@Composable
fun PantallaPrincipal(
    productoDao: ProductoDao,
    clienteDao: ClienteDao,
    onCerrarSesion: () -> Unit
) {

    var mostrarInventario by rememberSaveable { mutableStateOf(false) }
    var mostrarClientes by rememberSaveable { mutableStateOf(false) }

    var opcionInventario by rememberSaveable { mutableStateOf("") }
    var productoSeleccionadoId by rememberSaveable { mutableIntStateOf(-1) }
    var productoSeleccionado by remember { mutableStateOf<ProductoEntity?>(null) }

    var opcionClientes by rememberSaveable { mutableStateOf("") }
    var clienteSeleccionadoId by rememberSaveable { mutableIntStateOf(-1) }
    var clienteSeleccionado by remember { mutableStateOf<ClienteEntity?>(null) }

    val celeste = Color(0xFF16C5FF)
    val azul = Color(0xFF087DEB)

    LaunchedEffect(clienteSeleccionadoId) {
        clienteSeleccionado = if (clienteSeleccionadoId != -1) {
            withContext(Dispatchers.IO) {
                clienteDao.obtenerClientePorId(clienteSeleccionadoId)
            }
        } else {
            null
        }
    }

    LaunchedEffect(productoSeleccionadoId) {
        productoSeleccionado = if (productoSeleccionadoId != -1) {
            withContext(Dispatchers.IO) {
                productoDao.obtenerProductoPorId(productoSeleccionadoId)
            }
        } else {
            null
        }
    }

    if (mostrarInventario) {

        when (opcionInventario) {

            "registrar" -> {
                AddProductoScreen(
                    productoDao = productoDao,
                    onVolver = {
                        opcionInventario = ""
                    }
                )
            }

            "consultar" -> {
                ConsultaProductosScreen(
                    productoDao = productoDao,
                    onVolver = {
                        opcionInventario = ""
                    },
                    onEditar = { producto ->
                        productoSeleccionadoId = producto.id
                        productoSeleccionado = producto
                        opcionInventario = "editar"
                    }
                )
            }

            "editar" -> {
                if (productoSeleccionado != null) {
                    EditarProductoScreen(
                        producto = productoSeleccionado!!,
                        productoDao = productoDao,
                        onVolver = {
                            productoSeleccionadoId = -1
                            productoSeleccionado = null
                            opcionInventario = "consultar"
                        }
                    )
                } else {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color(0xFF050914)),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            CircularProgressIndicator(
                                color = Color(0xFF16C5FF)
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            Text(
                                text = "Recuperando información del producto...",
                                color = Color.White
                            )
                        }
                    }
                }
            }

            else -> {

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color(0xFF050914))
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {

                    Text(
                        text = "INVENTARIO",
                        color = Color.White,
                        fontSize = 27.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(30.dp))

                    Button(
                        onClick = {
                            opcionInventario = "registrar"
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(60.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = azul
                        )
                    ) {
                        Text("REGISTRAR PRODUCTO")
                    }

                    Spacer(modifier = Modifier.height(18.dp))

                    Button(
                        onClick = {
                            opcionInventario = "consultar"
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(60.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = azul
                        )
                    ) {
                        Text("CONSULTAR PRODUCTOS")
                    }

                    Spacer(modifier = Modifier.height(30.dp))

                    OutlinedButton(
                        onClick = {
                            mostrarInventario = false
                        }
                    ) {
                        Icon(
                            Icons.Default.ArrowBack,
                            contentDescription = null
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("VOLVER AL MENÚ")
                    }
                }
            }
        }


    } else if (mostrarClientes) {

        when (opcionClientes) {

            "registrar" -> {
                AddClienteScreen(
                    clienteDao = clienteDao,
                    onVolver = {
                        opcionClientes = ""
                    }
                )
            }

            "consultar" -> {
                ConsultaClientesScreen(
                    clienteDao = clienteDao,
                    onVolver = {
                        opcionClientes = ""
                    },
                    onEditar = { cliente ->
                        clienteSeleccionadoId = cliente.id
                        clienteSeleccionado = cliente
                        opcionClientes = "editar"
                    }
                )
            }

            "editar" -> {

                if (clienteSeleccionado != null) {

                    EditarClienteScreen(
                        cliente = clienteSeleccionado!!,
                        clienteDao = clienteDao,
                        onVolver = {
                            clienteSeleccionadoId = -1
                            clienteSeleccionado = null
                            opcionClientes = "consultar"
                        }
                    )

                } else {

                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color(0xFF050914)),
                        contentAlignment = Alignment.Center
                    ) {

                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {

                            CircularProgressIndicator(
                                color = Color(0xFF16C5FF)
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            Text(
                                text = "Recuperando información del cliente...",
                                color = Color.White
                            )
                        }
                    }
                }
            }

            else -> {

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color(0xFF050914))
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {

                    Text(
                        text = "CLIENTES",
                        color = Color.White,
                        fontSize = 27.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(30.dp))

                    Button(
                        onClick = {
                            opcionClientes = "registrar"
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(60.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = azul
                        )
                    ) {
                        Text("REGISTRAR CLIENTE")
                    }

                    Spacer(modifier = Modifier.height(18.dp))

                    Button(
                        onClick = {
                            opcionClientes = "consultar"
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(60.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = azul
                        )
                    ) {
                        Text("CONSULTAR CLIENTES")
                    }

                    Spacer(modifier = Modifier.height(30.dp))

                    OutlinedButton(
                        onClick = {
                            mostrarClientes = false
                            opcionClientes = ""
                        }
                    ) {
                        Icon(
                            Icons.Default.ArrowBack,
                            contentDescription = null
                        )

                        Spacer(modifier = Modifier.width(8.dp))

                        Text("VOLVER AL MENÚ")
                    }
                }
            }
        }

    } else {


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
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Spacer(modifier = Modifier.height(35.dp))

                Text(
                    text = "ALKARAVISION",
                    color = Color.White,
                    fontSize = 30.sp,
                    fontWeight = FontWeight.ExtraBold
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "SISTEMA DE TELEVISIÓN POR CABLE E INTERNET",
                    color = celeste,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(35.dp))

                Text(
                    text = "¡Bienvenido!",
                    color = Color.White,
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = "Selecciona el módulo que deseas utilizar",
                    color = Color.LightGray,
                    fontSize = 14.sp
                )

                Spacer(modifier = Modifier.height(35.dp))

                ElevatedCard(
                    onClick = {
                        mostrarInventario = true
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(145.dp),
                    colors = CardDefaults.elevatedCardColors(
                        containerColor = Color(0xFF10294A)
                    ),
                    shape = RoundedCornerShape(20.dp)
                ) {

                    Row(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(20.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Icon(
                            imageVector = Icons.Default.Inventory2,
                            contentDescription = null,
                            tint = celeste,
                            modifier = Modifier.size(55.dp)
                        )

                        Spacer(modifier = Modifier.width(20.dp))

                        Column {

                            Text(
                                text = "INVENTARIO",
                                color = Color.White,
                                fontSize = 19.sp,
                                fontWeight = FontWeight.Bold
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                text = "Administración y control de productos",
                                color = Color.LightGray,
                                fontSize = 13.sp
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                ElevatedCard(
                    onClick = {
                        mostrarClientes = true
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(145.dp),
                    colors = CardDefaults.elevatedCardColors(
                        containerColor = Color(0xFF10294A)
                    ),
                    shape = RoundedCornerShape(20.dp)
                ) {

                    Row(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(20.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Icon(
                            imageVector = Icons.Default.People,
                            contentDescription = null,
                            tint = celeste,
                            modifier = Modifier.size(55.dp)
                        )

                        Spacer(modifier = Modifier.width(20.dp))

                        Column {

                            Text(
                                text = "CLIENTES",
                                color = Color.White,
                                fontSize = 19.sp,
                                fontWeight = FontWeight.Bold
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                text = "Administración de clientes registrados",
                                color = Color.LightGray,
                                fontSize = 13.sp
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(35.dp))

                Button(
                    onClick = onCerrarSesion,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(55.dp),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = azul
                    )
                ) {

                    Icon(
                        imageVector = Icons.Default.Logout,
                        contentDescription = null
                    )

                    Spacer(modifier = Modifier.width(10.dp))

                    Text(
                        text = "CERRAR SESIÓN",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
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
}
