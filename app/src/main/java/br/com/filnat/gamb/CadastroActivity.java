package br.com.filnat.gamb;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


public class CadastroActivity extends AppCompatActivity {

    private Button buttonCadastrar;
    private EditText editTextNome, editTextEmail, editTextSenha;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        editTextNome = findViewById(R.id.editTextNome);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextSenha = findViewById(R.id.editTextSenha);
        buttonCadastrar = findViewById(R.id.buttonCadastrar);

        setButtonCadastrar();

    }


    private void setButtonCadastrar() {
        buttonCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cadastrar();
            }
        });
    }


    private void cadastrar() {
        String nome = editTextNome.getText().toString();
        String email = editTextEmail.getText().toString();
        String senha = editTextSenha.getText().toString();

        if (email.isEmpty() || senha.isEmpty()) {
            Toast.makeText(this, R.string.text, Toast.LENGTH_LONG).show();
        } else {

        }
    }
}