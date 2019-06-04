package iezv.jmm.rivalizer.Views;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
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

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.List;

import iezv.jmm.rivalizer.MainActivity;
import iezv.jmm.rivalizer.POJO.Place;
import iezv.jmm.rivalizer.R;

public class PlaceRegister extends AppCompatActivity {

    private static final int LOAD_IMAGE_CODE = 42;

    EditText placeName;
    EditText placeAddress;
    EditText placeReview;
    Button uploadPhotoP;
    ImageView placePrev;
    Button sendPlace;

    private FirebaseAuth mAuth;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference placesDB = database.getReference("places");
    private StorageReference mStorageRef;
    Uri coverUri;
    StorageReference avatarSR;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_register);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mStorageRef = FirebaseStorage.getInstance().getReference();

        mAuth = FirebaseAuth.getInstance();

        placeName = findViewById(R.id.place_name);
        placeAddress = findViewById(R.id.place_address);
        placeReview = findViewById(R.id.place_review);
        uploadPhotoP = findViewById(R.id.upload_photo_place);
        placePrev = findViewById(R.id.place_prev);
        sendPlace = findViewById(R.id.send_place);



        eventListener();
    }


    public void eventListener(){
        uploadPhotoP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, LOAD_IMAGE_CODE);
            }
        });

        sendPlace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String namePlace = placeName.getText().toString();
                String addressPlace = placeAddress.getText().toString();
                String reviewPlace = placeReview.getText().toString();

                FirebaseUser user = mAuth.getCurrentUser();
                if(user == null){
                    Toast.makeText(PlaceRegister.this, "Debes estar conectado para enviar un juego nuevo.", Toast.LENGTH_SHORT).show();
                } else {
                    String key = placesDB.push().getKey();
                    Place newPlace = null;
                    try {
                        newPlace = new Place(key, namePlace, getCoordinates(addressPlace), coverUri.toString(), reviewPlace);
                    } catch (IOException e) {
                        newPlace = new Place(key, namePlace, addressPlace, coverUri.toString(), reviewPlace);
                        //e.printStackTrace();
                    }
                    placesDB.child(key).setValue(newPlace);
                    Intent intent = new Intent(PlaceRegister.this, MainActivity.class);
                    startActivity(intent);
                }

            }
        });
    }

    //Recibe los datos del método anterior. En caso de que la imagen que queremos guardar exceda cierto tamaño, la redimensiona hasta que esté por debajo de dichas dimensiones.
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){


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
                                        Picasso.with(PlaceRegister.this).load(coverUri).into(placePrev);
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


            }

        }
    }

    public String getCoordinates(String address) throws IOException {
        String coords = "0,0";
        Geocoder geocoder = new Geocoder(this);
        List<Address> addresses;
        addresses = geocoder.getFromLocationName(address, 1);
        if(addresses.size() > 0) {
            double latitude= addresses.get(0).getLatitude();
            double longitude= addresses.get(0).getLongitude();
            coords = latitude+","+longitude;
        }
        return coords;
    }

}
