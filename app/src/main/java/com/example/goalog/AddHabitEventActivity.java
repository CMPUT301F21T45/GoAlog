package com.example.goalog;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
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

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
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
public class AddHabitEventActivity extends AppCompatActivity  {

    // all fields
    private EditText optionalComment;
    private ImageView image;
    private ImageView deleteIcon;
    private Button camera;
    private Button album;
    private Button cancelButton;
    private Button confirmButton;
    private String eventCommentString;
    private String completeDate;
    private String imgPath = "";
    private TextView title;
    private TextView dateComplete;
    private Uri filePath;
    private Uri imageUri;
    private Bitmap bitmap = null;
    private Context PostImage;

    FirebaseStorage storage;
    StorageReference storageReference;

    // check whether the Activity is used for add or edit a HabitEvent
    private boolean editMode = false;

    @Override
    protected void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_habit_event);

        // get related views
        optionalComment = findViewById(R.id.comment_text);          //create an optional comment
        title = findViewById(R.id.habitTitle);                      //get clicked habit title
        dateComplete = findViewById(R.id.eventDate);                //event complete date
        image = (ImageView) findViewById(R.id.first_image);         //an imageview to add image
        deleteIcon = (ImageView) findViewById(R.id.image_delete);   //delete the added image

        //set today for complete day
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
        });//click to delete photograph

        Habit clickedHabit = (Habit) getIntent().getSerializableExtra("Habit");// get related Habit of this HabitEvent
        String clickedHabitID = (String) clickedHabit.getHabitID();//get related Habit ID of this HabitEvent
        HabitEvent needUpdatedEvent = (HabitEvent) getIntent().getSerializableExtra("Update HabitEvent");//get related HabitEvent detail

        //if there's a selected HabitEvent, this is an edit HabitEvent page
        if (needUpdatedEvent != null) {
            // edit mode
            editMode = true;
            // filled in HabitEvent details
            optionalComment.setText(needUpdatedEvent.getEventComment());
            image.setImageURI(Uri.parse(needUpdatedEvent.getImage()));
            deleteIcon.setVisibility(View.VISIBLE);
            if (needUpdatedEvent.getImage() == ""){
                image.setImageResource(R.mipmap.ic_upload_image);
                deleteIcon.setVisibility(View.INVISIBLE);
            }

        } else {
            // add mode
            editMode = false;
        }

        title.setText(clickedHabit.getHabitTitle());//set title with habit title
        dateComplete.setText(completeDate);//display today date

        storage = FirebaseStorage.getInstance();//initialized with the default Firebase
        storageReference = storage.getReference();//initialized at the root Firebase Storage location

        confirmButton = (Button) findViewById(R.id.confirm_habit_event);
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eventCommentString = optionalComment.getText().toString();

                final FirebaseFirestore database = FirebaseFirestore.getInstance();
                final CollectionReference collectionReference = database.collection("user003").document(clickedHabitID).collection("HabitEvent");

                // edit mode
                if (editMode) {
                    editMode = false;
                    // updates HabitEvent to firebase
                    if (filePath != null) {
                        StorageReference ref = storageReference.child("images/" + UUID.randomUUID().toString());
                        ref.putFile(filePath);
                        imgPath = filePath.toString();
                        needUpdatedEvent.setImage(imgPath);
                    }
                    needUpdatedEvent.setEventComment(eventCommentString);

                    //needUpdatedEvent.setImage(imgPath);
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
                    }//save image to storage under images folder

                    final String habitEventID = UUID.randomUUID().toString().replace("-", "");//generate random habit event ID
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
    /*
     * A view to select choose type
     */
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
        });//take photo

        album.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selection.dismiss();
                openGallery();
            }
        });//choose photo from gallery

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selection.dismiss();
            }
        });//return to add page

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
        }//request permission

    }

    /*
     * Open phone camera
     */
    private void openCamera() {
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
                            PostImage = getApplicationContext();
                            Bundle extras = result.getData().getExtras();
                            Bitmap imageBitmap = (Bitmap) extras.get("data");
                            imageUri = getImageUri(getApplicationContext(), imageBitmap);
                            filePath = imageUri;                    //copy image uri
                            image.setImageBitmap(imageBitmap);      //display image on screen
                            deleteIcon.setVisibility(View.VISIBLE); //display delete button
                            saveImage();
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
    public void saveImage(){
        try{
            InputStream imageStream = getContentResolver().openInputStream(imageUri);//open input stream
            Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
            bitmap = selectedImage.copy(selectedImage.getConfig(),selectedImage.isMutable());//copy bitmap
            image.setImageBitmap(bitmap);

            if (bitmap != null) {
                Intent intent = new Intent();
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();//create a byte array output stream
                intent.putExtra(MediaStore.EXTRA_OUTPUT, bitmap.compress(Bitmap.CompressFormat.JPEG,100,outputStream));//reduce the size of the image and put extra Data
            } else {
                Toast.makeText(PostImage,"Cannot save image!", Toast.LENGTH_SHORT).show();
            }
        } catch (FileNotFoundException e){
        }
    }

    /*
     * get image uri
     * code from https://stackoverflow.com/questions/67844042/update-user-profileimage-in-firebase-android-studio
     */
    public Uri getImageUri(Context inContext, Bitmap inImage) {
        Bitmap OutImage = Bitmap.createScaledBitmap(inImage, 1000, 1000,true);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), OutImage, "Title", null);
        return Uri.parse(path);
    }
}













