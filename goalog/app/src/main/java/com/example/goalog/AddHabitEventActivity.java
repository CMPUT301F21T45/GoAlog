package com.example.goalog;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;


public class AddHabitEventActivity extends AppCompatActivity {
    private EditText optimalComment;
    private ImageView image;
    private ImageView deleteIcon;
    private Button camera;
    private Button album;
    private Button cancelButton;
    private Button confirmButton;
    private String eventCommentString;
    private String completeDate;
    private Uri filePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_habit_event);
        optimalComment = findViewById(R.id.comment_text);//create an optional comment
        image = (ImageView) findViewById(R.id.first_image);
        deleteIcon = (ImageView) findViewById(R.id.image_delete);

        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setChoosePhoto();
            }
        });//click to add optional photograph

        deleteIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                image.setImageResource(R.mipmap.ic_upload_image);
                deleteIcon.setVisibility(View.INVISIBLE);

            }
        });
        final FirebaseFirestore database = FirebaseFirestore.getInstance();
        final CollectionReference collectionReference = database.collection("user003");
        FirebaseStorage storage;
        StorageReference storageReference;

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        confirmButton = (Button) findViewById(R.id.confirm_habit_event);
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create a new habit event
                // Upload it into firebase.
                SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd");
                Date today = new Date();
                completeDate = date.format(today);
                eventCommentString = optimalComment.getText().toString();

                //HabitEvent newHabitEvent = new HabitEvent(filePath);
                //HashMap<String, HabitEvent> data = new HashMap<>();
                //data.put("HabitEvent", newHabitEvent);
                if (filePath != null){
                    StorageReference ref = storageReference.child("images/"+ UUID.randomUUID().toString());
                    ref.putFile(filePath);

                    //collectionReference.document(newHabitEvent.getFilePath().toString()).set(data);
                }
                //HabitEvent newHabitEvent = new HabitEvent
                //HashMap<String, HabitEvent> data = new HashMap<>();
                //data.put("HabitClass", newHabit);
                //if (newHabit != null) {
                    //collectionReference.document(newHabit.getHabitTitle()).set(data);
                //}



            }


        });
    }




    /*
     * selection(take a photo or open gallery)
     */
    private void setChoosePhoto(){
        View chooseTypeView = LayoutInflater.from(this).inflate(R.layout.choose_type_view, null);
        AlertDialog selection = new AlertDialog.Builder(this).setView(chooseTypeView).setCancelable(false).create();
        selection.show();

        camera = (Button) selection.getWindow().findViewById(R.id.choose_camera);
        album = (Button) selection.getWindow().findViewById(R.id.choose_album);
        cancelButton = (Button) selection.getWindow().findViewById(R.id.cancel_action);

        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selection.dismiss();
                openCamera();
            }
        });

        album.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selection.dismiss();
                openGallery();
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selection.dismiss();
            }
        });
    }

    /*
     * open phone camera
     */
    private void openCamera() {
        //File imgDir = new File(getFilePath(null));
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        openCameraResultLauncher.launch(takePictureIntent);
    }

    ActivityResultLauncher<Intent> openCameraResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() != RESULT_CANCELED) {
                        if (result.getResultCode() == RESULT_OK) {
                            filePath = result.getData().getData();
                            Bundle extras = result.getData().getExtras();
                            Bitmap imageBitmap = (Bitmap) extras.get("data");
                            image.setImageBitmap(imageBitmap);//Display image on screen
                            deleteIcon.setVisibility(View.VISIBLE);
                        }
                    }
                }
            });

    /*
     * open phone photo gallery
     */
    private void openGallery() {
        Intent photoPicker = new Intent(Intent.ACTION_PICK);
        photoPicker.setType("image/*");
        openGalleryResultLauncher.launch(photoPicker);
    }

    ActivityResultLauncher<Intent> openGalleryResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() != RESULT_CANCELED) {
                        if (result.getResultCode() == RESULT_OK) {
                            filePath = result.getData().getData();
                            image.setImageURI(filePath);
                            deleteIcon.setVisibility(View.VISIBLE);
                        }
                    }
                }
            });









}
