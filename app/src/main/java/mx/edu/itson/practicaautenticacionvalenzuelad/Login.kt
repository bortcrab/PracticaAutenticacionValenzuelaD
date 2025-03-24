package mx.edu.itson.practicaautenticacionvalenzuelad

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.auth

class Login : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        auth = Firebase.auth
        val email: EditText = findViewById(R.id.etEmail)
        val password: EditText = findViewById(R.id.etPassword)
        val errorTV: TextView = findViewById(R.id.tvError)
        val button: Button = findViewById(R.id.btnLogin)
        val buttonRegister: Button = findViewById(R.id.btnRegister)

        errorTV.visibility = View.INVISIBLE

        buttonRegister.setOnClickListener {
            val intent = Intent(this, SignIn::class.java)
            startActivity(intent)
        }

        button.setOnClickListener {
            var correo = email.text.toString()
            var contra = password.text.toString()
            if (email.text.isEmpty() || password.text.isEmpty()) {
                errorTV.text = "Todos los campos deben ser llenados"
                errorTV.visibility = View.VISIBLE
            } else {
                login(correo, contra)
            }
        }
    }

    fun goToMain(user: FirebaseUser) {
        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra("email", user.email)
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }

    fun showError(text: String = "", visible: Boolean) {
        val errorTV: TextView = findViewById(R.id.tvError)

        errorTV.text = text

        errorTV.visibility = if (visible) View.VISIBLE else View.INVISIBLE
    }

    public override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        if (currentUser != null) {
            goToMain(currentUser)
        }
    }

    fun login(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    showError(visible = false)
                    goToMain(user!!)
                } else {
                    showError("Usuario y/o contraseña equivocados", true)
                }
            }
    }
}