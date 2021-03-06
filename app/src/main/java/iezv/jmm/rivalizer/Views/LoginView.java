package iezv.jmm.rivalizer.Views;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import iezv.jmm.rivalizer.MainActivity;
import iezv.jmm.rivalizer.R;

public class LoginView extends AppCompatActivity {

    EditText emailLogin;
    EditText passwordLogin;
    Button buttonLogin;
    Button buttonGoRegister;

    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_view);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mAuth = FirebaseAuth.getInstance();

        if (mAuth.getCurrentUser() != null) {
            startActivity(new Intent(LoginView.this, MainActivity.class));
            finish();
        }

        emailLogin = findViewById(R.id.email_login);
        passwordLogin = findViewById(R.id.password_login);
        buttonLogin = findViewById(R.id.button_login);
        buttonGoRegister = findViewById(R.id.button_go_register);



        eventListener();
    }

    public void eventListener(){

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = emailLogin.getText().toString();
                String password = passwordLogin.getText().toString();

                mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(LoginView.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    updateUI(user);
                                } else {
                                    // If sign in fails, display a message to the user.
                                    Toast.makeText(LoginView.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();
                                    updateUI(null);
                                }

                                // ...
                            }
                        });
            }
        });

        buttonGoRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginView.this, PlayerRegister.class);
                startActivity(intent);
            }
        });
    }

    public void updateUI(FirebaseUser user){
        if(user == null){
            Toast.makeText(LoginView.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
        }else{
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
    }


    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();

    }

}
