package com.davidcharo.deudoresapp

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.widget.doAfterTextChanged
import com.davidcharo.deudoresapp.data.dao.DebtorDao
import com.davidcharo.deudoresapp.data.dao.UserDao
import com.davidcharo.deudoresapp.data.entities.Debtor
import com.davidcharo.deudoresapp.data.entities.User
import com.davidcharo.deudoresapp.databinding.ActivityLoginBinding
import com.davidcharo.deudoresapp.utils.MIN_SIZE_PASSWORD
import com.davidcharo.deudoresapp.utils.validateEmail
import com.davidcharo.deudoresapp.utils.validatePassword

private const val EMPTY = ""

class LoginActivity : AppCompatActivity() {

    private lateinit var loginBainding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loginBainding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(loginBainding.root)

        bindOnChangeListeners()

        loginBainding.loginButton.setOnClickListener {
            val userDao: UserDao = DeudoresApp.databaseUser.UserDao()
            val name = loginBainding.emailEditText.text.toString()
            val user: User = userDao.readUser(name)
            if (user != null) {
                if (validateEmail() && validatePassword()) {
                    val emailLog = loginBainding.emailEditText.text.toString()
                    val passwordLog = loginBainding.passwordEditText.text.toString()
                    if (emailLog == user.email && passwordLog == user.password) {
                        goToMainActivity()
                        cleanViews()
                        loginBainding.passwordTextInputLayout.error = null
                    } else {
                        if (emailLog != user.email && passwordLog != user.password) {
                            loginBainding.emailTextInputLayout.error = getString(R.string.mail_match)
                            loginBainding.passwordTextInputLayout.error = getString(R.string.password_match)
                        } else {
                            if (emailLog != user.email) {
                                loginBainding.emailTextInputLayout.error = getString(R.string.mail_match)
                            } else {
                                loginBainding.passwordTextInputLayout.error = getString(R.string.password_match)
                            }
                        }
                    }
                }else{
                    validateEmail()
                    validatePassword()
                }
            }else{
                Toast.makeText(this, "No Existe", Toast.LENGTH_SHORT).show()
            }
        }

        loginBainding.registerNewAccountButton.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }

    private fun goToMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(intent)
        finish()
    }

    private fun bindOnChangeListeners() {
        with(loginBainding){
            emailEditText.doAfterTextChanged {
                validateEmail()
                validateFields()
            }
            passwordEditText.doAfterTextChanged {
                validatePassword()
                validateFields()
            }
        }
    }

    private fun validateFields() {
        with(loginBainding){
            val fields = listOf(
                validateEmail(),
                validatePassword()
            )
            validateFields(fields)
        }
    }

    fun validateFields(areValid: List<Boolean>){
        for (isValid in areValid){
            enableSigInButton(isValid)
            if (!isValid) break
        }
    }

    fun enableSigInButton(isEnable: Boolean) {
        loginBainding.loginButton.isEnabled = isEnable
    }

    private fun cleanViews() {
        with(loginBainding) {
            emailEditText.setText(EMPTY)
            passwordEditText.setText(EMPTY)
        }
    }

    private fun validateEmail(): Boolean {
        val validateEmail = validateEmail(loginBainding.emailEditText.text.toString())
        loginBainding.emailTextInputLayout.error = if (!validateEmail) getString(R.string.unwanted_mail) else null
        return validateEmail
    }

    private fun validatePassword(): Boolean {
        val passwordLog = validatePassword(loginBainding.passwordEditText.text.toString(), MIN_SIZE_PASSWORD)
        loginBainding.passwordTextInputLayout.error = if (!passwordLog) getString(R.string.least_password) else null
        return passwordLog
    }
}
