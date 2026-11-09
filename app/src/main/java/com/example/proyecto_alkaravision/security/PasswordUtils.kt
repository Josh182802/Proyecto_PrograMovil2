package com.example.proyecto_alkaravision.security

import android.util.Base64
import java.security.MessageDigest
import java.security.SecureRandom
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.PBEKeySpec

object PasswordUtils {

    private const val ITERACIONES = 120000
    private const val LONGITUD_HASH = 256

    fun generarSalt(): String {
        val salt = ByteArray(16)
        SecureRandom().nextBytes(salt)

        return Base64.encodeToString(salt, Base64.NO_WRAP)
    }

    fun generarHash(contrasena: String, salt: String): String {

        val saltBytes = Base64.decode(salt, Base64.NO_WRAP)

        val especificacion = PBEKeySpec(
            contrasena.toCharArray(),
            saltBytes,
            ITERACIONES,
            LONGITUD_HASH
        )

        val fabrica = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256")
        val hash = fabrica.generateSecret(especificacion).encoded

        especificacion.clearPassword()

        return Base64.encodeToString(hash, Base64.NO_WRAP)
    }

    fun verificarContrasena(
        contrasena: String,
        hashGuardado: String,
        salt: String
    ): Boolean {

        val hashIngresado = generarHash(contrasena, salt)

        return MessageDigest.isEqual(
            Base64.decode(hashGuardado, Base64.NO_WRAP),
            Base64.decode(hashIngresado, Base64.NO_WRAP)
        )
    }
}