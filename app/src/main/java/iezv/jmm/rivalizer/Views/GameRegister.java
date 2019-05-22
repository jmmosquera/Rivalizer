package iezv.jmm.rivalizer.Views;

import android.content.Intent;
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
import iezv.jmm.rivalizer.POJO.Game;
import iezv.jmm.rivalizer.R;

public class GameRegister extends AppCompatActivity {

    private static final int LOAD_IMAGE_CODE = 42;

    EditText nameGame;
    EditText gameDescr;
    EditText gameRules;
    Button uploadPhoto;
    ImageView gamePrev;
    Button registerGame;

    private FirebaseAuth mAuth;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference gamesDB = database.getReference("games");
    private StorageReference mStorageRef;
    Uri coverUri;
    StorageReference avatarSR;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_register);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mStorageRef = FirebaseStorage.getInstance().getReference();

        mAuth = FirebaseAuth.getInstance();

        nameGame = findViewById(R.id.game_name);
        gameDescr = findViewById(R.id.game_descr);
        gameRules = findViewById(R.id.game_rules);
        uploadPhoto = findViewById(R.id.upload_photo);
        gamePrev = findViewById(R.id.game_prev);
        registerGame = findViewById(R.id.register_game);

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

        registerGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.v("BOTON", "BOTON PULSADO");
                String gameName = nameGame.getText().toString();
                String descrGame = gameDescr.getText().toString();
                String rulesGame = gameRules.getText().toString();

                FirebaseUser user = mAuth.getCurrentUser();

                if(user == null){
                    Toast.makeText(GameRegister.this, "Debes estar conectado para enviar un juego nuevo.", Toast.LENGTH_SHORT).show();
                }else{
                    String key = gamesDB.push().getKey();
                    Game newGame = new Game(key, gameName, coverUri.toString(), descrGame, rulesGame);
                    gamesDB.child(key).setValue(newGame);
                    Intent intent = new Intent(GameRegister.this, MainActivity.class);
                    startActivity(intent);
                }


            }
        });
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
                coverUri = data.getData();

                final StorageReference avatarRef = mStorageRef.child("images/"+coverUri.getLastPathSegment());

                avatarRef.putFile(coverUri)
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                // Get a URL to the uploaded content
                                avatarRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        coverUri = uri;

                                        Log.v("Download URL ", coverUri+"");
                                        Picasso.with(GameRegister.this).load(coverUri).into(gamePrev);
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
