package br.com.filnat.gamb;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class MainActivity extends AppCompatActivity{

    private EditText editTextLogin, editTextSenhaLogin;
    private Button buttonCadastre;
    private Button buttonAcessar;

    private FirebaseAuth auth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseUser usuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextLogin = findViewById(R.id.editTextLogin);
        editTextSenhaLogin = findViewById(R.id.editTextSenhaLogin);
        buttonCadastre = findViewById(R.id.buttonCadastre);
        buttonAcessar = findViewById(R.id.buttonAcessar);

        setButtonAcessar();
        setButtonCadastre();

        auth = FirebaseAuth.getInstance();

        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                usuario = auth.getCurrentUser();
                if(usuario != null){
                    Intent intent = new Intent(MainActivity.this, ToolsFunctionsActivity.class);
                    startActivity(intent);
                }
            }
        };

    }
    public void setButtonCadastre() {

        buttonCadastre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, CadastroActivity.class );
                startActivity(intent);
            }
        });
    }

    public void setButtonAcessar() {

        buttonAcessar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            Logar();
            }
        });
    }

    private void Logar() {
        String email = editTextLogin.getText().toString();
        String senha = editTextSenhaLogin.getText().toString();

        if(email.isEmpty() || senha.isEmpty()){
            Toast.makeText(this, R.string.text, Toast.LENGTH_LONG).show();
        } else {
            auth.signInWithEmailAndPassword(email, senha)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                usuario = auth.getCurrentUser();
//                                if(usuario != null){
//                                    Intent intent = new Intent(MainActivity.this, ToolsFunctionsActivity.class);
//                                    startActivity(intent);
//                                }
                            }else{
                                task.getException().toString();
                                Toast.makeText(MainActivity.this, R.string.textfail, Toast.LENGTH_LONG).show();
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