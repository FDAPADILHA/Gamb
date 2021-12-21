package br.com.filnat.gamb;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class CadastroActivity extends AppCompatActivity {

    private Button buttonCadastrar;
    private EditText editTextNome, editTextEmail, editTextSenha;

    private FirebaseAuth auth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseUser usuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        editTextNome = findViewById(R.id.editTextNome);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextSenha = findViewById(R.id.editTextSenha);
        buttonCadastrar = findViewById(R.id.buttonCadastrar);

        setButtonCadastrar();

        auth = FirebaseAuth.getInstance();

        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (usuario != null) {
                    Intent intent = new Intent(CadastroActivity.this, MainActivity.class);
                    startActivity(intent);
                }
            }
        };

    }


    private void setButtonCadastrar() {
        buttonCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cadastrar();
            }
        });
    }


    //    private void cadastrar() {
//        String nome = editTextNome.getText().toString();
//        String email = editTextEmail.getText().toString();
//        String senha = editTextSenha.getText().toString();
//
//        if (email.isEmpty() || senha.isEmpty()) {
//            Toast.makeText(this, R.string.text, Toast.LENGTH_LONG).show();
//        } else {
//
//        }
//    }
    private void cadastrar() {
        String nome = editTextNome.getText().toString();
        String email = editTextEmail.getText().toString();
        String senha = editTextSenha.getText().toString();

        if (email.isEmpty() || senha.isEmpty()) {
            Toast.makeText(this, R.string.text, Toast.LENGTH_LONG).show();
        } else {
            auth.createUserWithEmailAndPassword(email, senha)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                usuario = auth.getCurrentUser();
                            } else {
                                task.getException().toString();
                                Toast.makeText(CadastroActivity.this, R.string.cadfail, Toast.LENGTH_LONG).show();
                            }
                        }
                    });
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        auth.addAuthStateListener(authStateListener);
    }
}