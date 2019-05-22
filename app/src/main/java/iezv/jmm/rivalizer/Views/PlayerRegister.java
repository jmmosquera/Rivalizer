package iezv.jmm.rivalizer.Views;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
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
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import iezv.jmm.rivalizer.MainActivity;
import iezv.jmm.rivalizer.POJO.Player;
import iezv.jmm.rivalizer.R;

public class PlayerRegister extends AppCompatActivity {

    private static final int LOAD_IMAGE_CODE = 42;

    EditText emailRegister;
    EditText nameRegister;
    EditText zipRegister;
    EditText passwordRegister;
    EditText getPasswordRegisterRepeat;
    Button uploadPhoto;
    Button fulfillRegister;

    private FirebaseAuth mAuth;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference usersDB = database.getReference("users");
    private StorageReference mStorageRef;
    private GoogleMap mMap;
    Uri avatarUri;
    Bitmap avatar = null;
    ImageView avatarPrev;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_register);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mStorageRef = FirebaseStorage.getInstance().getReference();

        mAuth = FirebaseAuth.getInstance();

        emailRegister = findViewById(R.id.email_register);
        nameRegister = findViewById(R.id.name_register);
        zipRegister = findViewById(R.id.zip_code);
        passwordRegister = findViewById(R.id.password_register);
        getPasswordRegisterRepeat = findViewById(R.id.password_register_repeat);
        fulfillRegister = findViewById(R.id.fulfill_register);
        uploadPhoto = findViewById((R.id.upload_photo));
        avatarPrev = findViewById(R.id.game_prev);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        eventListener();
    }

    public void eventListener(){

        uploadPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, LOAD_IMAGE_CODE);
            }
        });

        fulfillRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.v("BOTON", "BOTON PULSADO");
                String email = emailRegister.getText().toString();
                String password = passwordRegister.getText().toString();
                Log.v("BOTON", "EMAIL: "+email+" PASSWORD: "+password);
                mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(PlayerRegister.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.v("ZZT", "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.v("ZZT", "createUserWithEmail:failure", task.getException());
                            Toast.makeText(PlayerRegister.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }

                        // ...
                    }
                });


            }
        });
    }

    public void updateUI(FirebaseUser user){
        Log.v("BOTON", user.toString());
        if(user == null){
            Toast.makeText(PlayerRegister.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
        }else{
            DatabaseReference userRef = usersDB.child(user.getUid());
            Player newPlayer = new Player(user.getUid(), nameRegister.getText().toString(), "FOTO", zipRegister.getText().toString());
            userRef.setValue(newPlayer);
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
    }

    //Recibe los datos del método anterior. En caso de que la imagen que queremos guardar exceda cierto tamaño, la redimensiona hasta que esté por debajo de dichas dimensiones.
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){



        /*Uri file = Uri.fromFile(new File("path/to/images/rivers.jpg"));
        StorageReference riversRef = mStorageRef.child("images/rivers.jpg");

        riversRef.putFile(file)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // Get a URL to the uploaded content
                        Uri downloadUrl = taskSnapshot.getDownloadUrl();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle unsuccessful uploads
                        // ...
                    }
                });*/


        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == this.RESULT_CANCELED) {
            return;
        }
        if (requestCode == LOAD_IMAGE_CODE) {
            Log.v("DATA: ", data+"");
            if (data != null) {
                Log.v("IF DATA", "ENTRA");
                avatarUri = data.getData();

                final StorageReference avatarRef = mStorageRef.child("images/"+avatarUri.getLastPathSegment());

                avatarRef.putFile(avatarUri)
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                // Get a URL to the uploaded content
                                avatarRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        Uri downloadUrl = uri;
                                        Log.v("Download URL ", downloadUrl+"");
                                        Picasso.with(PlayerRegister.this).load(downloadUrl).into(avatarPrev);
                                    }
                                });

                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) {
                                Log.v("Download URL ", "EMOSIDO FALLADO");
                                // Handle unsuccessful uploads
                                // ...
                            }
                        });

                /*Picasso.with(PlayerRegister.this).load(avatarUri).into(avatarPrev);
                try {
                    avatar = MediaStore.Images.Media.getBitmap(this.getContentResolver(), avatarUri);
                    int halfHeight;
                    int halfWidth;
                    if(avatar.getWidth() > 500 || avatar.getHeight() > 500){
                        halfHeight = avatar.getWidth() / 2;
                        halfWidth = avatar.getHeight() / 2;
                        while (halfWidth > 500 || halfHeight > 500) {
                            halfHeight = halfHeight / 2;
                            halfWidth = halfWidth / 2;
                        }
                        avatar = Bitmap.createScaledBitmap(avatar, halfHeight, halfWidth, true);

                    }
                    //coverPath = saveToInternalStorage(cover);

                    //imageview.setImageBitmap(bitmap);

                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(PlayerRegister.this, "Failed!", Toast.LENGTH_SHORT).show();
                }*/
            }

        }
    }

}
