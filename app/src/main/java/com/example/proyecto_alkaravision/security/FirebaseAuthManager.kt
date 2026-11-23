package com.example.proyecto_alkaravision.security

import com.google.firebase.auth.FirebaseAuth

object FirebaseAuthManager {

    val auth: FirebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }

}
