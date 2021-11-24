package com.example.goalog;

import static android.content.ContentValues.TAG;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
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
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
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
    private String imgPath = "";
    private String currentPhotoPath;
    private TextView title;
    private TextView dateComplete;
    private Uri filePath;
    private Uri photoURI;

    FirebaseStorage storage;
    StorageReference storageReference;

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
                        imgPath = filePath.toString();
                    }


                    final String habitEventID = UUID.randomUUID().toString().replace("-", "");

                    HabitEvent newHabitEvent = new HabitEvent(habitEventID, eventCommentString, completeDate, clickedHabit.getHabitTitle(), imgPath);
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

    private void setChoosePhoto() {
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
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                Log.e(TAG, "Fail to capture");
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                photoURI = FileProvider.getUriForFile(this,
                        "com.example.android.fileprovider",
                        photoFile);
                //takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                openCameraResultLauncher.launch(takePictureIntent);
            }

        }
    }

    ActivityResultLauncher<Intent> openCameraResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() != RESULT_CANCELED) {
                        if (result.getResultCode() == RESULT_OK) {
                            filePath = photoURI;
                            galleryAddPic(filePath);
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

    /*
     * save picture to gallery
     */
    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private void galleryAddPic(Uri photoPath) {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(currentPhotoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);
    }

    public void saveImageSd(Bitmap bitmap, String fileName) {
        FileOutputStream fos = null;
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            String imagePath = Environment.getExternalStorageDirectory().getPath() + "/service";
            try {
                File file = new File(imagePath, fileName + ".jpg");
                System.out.println(file.getPath());
                fos = new FileOutputStream(file);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                fos.flush();
                Toast.makeText(this, "图片保存成功" + imagePath, Toast.LENGTH_SHORT).show();

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            Uri uri = Uri.parse("file://" + imagePath + fileName);
            intent.setData(uri);
            this.sendBroadcast(intent);
        }
    }
}













