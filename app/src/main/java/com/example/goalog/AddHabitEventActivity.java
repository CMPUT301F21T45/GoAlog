package com.example.goalog;

import android.app.AlertDialog;
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
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.UUID;

/**
 * An Activity page with fields to show attributes of an HabitEvent object and makes update to firestore
 * Used in two cases:
 *  1. the add HabitEvent page, with all fields blank and needed to be filled by user
 *  2. the HabitEvent detail page, with all fields of a given HabitEvent and allows user to edit
 *
 *  Currently unsolved issues: the optional photograph of HabitEvent is not yet implemented,
 *  so there are some related unused attributes as well as related commented-out code
 */
public class AddHabitEventActivity extends AppCompatActivity {

    // all fields
    private EditText optimalComment;
    private ImageView image;
    private ImageView deleteIcon;
    private Button camera;
    private Button album;
    private Button cancelButton;
    private Button confirmButton;
    private String eventCommentString;
    private String completeDate;
    private TextView title;
    private TextView dateComplete;
    private Uri filePath;

    // check whether the Activity is used for add or edit a HabitEvent
    private boolean editMode = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_habit_event);

        // get related views
        optimalComment = findViewById(R.id.comment_text);//create an optional comment
        title = findViewById(R.id.habitTitle);
        dateComplete = findViewById(R.id.eventDate);
        image = (ImageView) findViewById(R.id.first_image);
        deleteIcon = (ImageView) findViewById(R.id.image_delete);
        SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd");
        Date today = new Date();
        completeDate = date.format(today);

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

        // get related Habit of this HabitEvent
        Habit clickedHabit = (Habit) getIntent().getSerializableExtra("Habit");
        String clickedHabitID = (String) clickedHabit.getHabitID();
        // get HabitEvent
        HabitEvent needUpdatedEvent = (HabitEvent) getIntent().getSerializableExtra("Update HabitEvent");
        //if there's a selected HabitEvent, this is an edit HabitEvent page
        if (needUpdatedEvent != null) {
            // edit mode
            editMode = true;
            // filled in HabitEvent details
            optimalComment.setText(needUpdatedEvent.getEventComment());
        } else {
            // add mode
            editMode = false;
        }
        // event
        title.setText(clickedHabit.getHabitTitle());
        dateComplete.setText(completeDate);

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
                eventCommentString = optimalComment.getText().toString();
                final FirebaseFirestore database = FirebaseFirestore.getInstance();
                final CollectionReference collectionReference = database.collection("user003").document(clickedHabitID).collection("HabitEvent");

                // edit mode
                if (editMode) {
                    editMode = false;
                    // updates HabitEvent to firebase
                    needUpdatedEvent.setEventComment(eventCommentString);
                    HashMap<String, Object> data = new HashMap<>();
                    data.put("Event", needUpdatedEvent);
                    collectionReference.document(needUpdatedEvent.getEventID()).update(data);
                    // back to last page
                    Intent intent = new Intent(AddHabitEventActivity.this, HabitEventListViewActivity.class);
                    Toast.makeText(v.getContext(), "Event Successfully Edited", Toast.LENGTH_SHORT).show();
                    startActivity(intent);
                }
                // add mode
                else {
                    if (filePath != null) {
                        StorageReference ref = storageReference.child("images/" + UUID.randomUUID().toString());
                        ref.putFile(filePath);
                    }
                    final String habitEventID = UUID.randomUUID().toString().replace("-", "");
                    HabitEvent newHabitEvent = new HabitEvent(habitEventID, eventCommentString, completeDate, clickedHabit.getHabitTitle(), filePath.toString());
                    HashMap<String, HabitEvent> data = new HashMap<>();
                    data.put("Event", newHabitEvent);
                    if (newHabitEvent != null) {
                        // updates new HabitEvent to firebase
                        collectionReference.document(newHabitEvent.getEventID()).set(data);
                    }
                    // back to user page (the one with today's habits list)
                    Intent intent = new Intent(AddHabitEventActivity.this, UserPageActivity.class);
                    Toast.makeText(v.getContext(), "New Event Created", Toast.LENGTH_SHORT).show();
                    startActivity(intent);
                    }
                }

        });
    }
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


    // open phone camera

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



/*

    public String getFilePath(String dir){
        String path;
        if(Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())){
            path = getExternalFilesDir(dir).getAbsolutePath();
        } else{
            path = getFilesDir() + File.separator +dir;
        }
        File file = new File(path);
        if (!file.exists()){
            file.mkdir();
        }
        return path;
    }
*/









