package com.example.goalog;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
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
 */
public class AddHabitEventActivity extends AppCompatActivity {

    // all fields
    private EditText optionalComment;
    private ImageView image_display;
    private ImageView deleteImage;
    private ImageView deleteLocation;
    private Button camera;
    private Button album;
    private Button cancelButton;
    private Button confirmButton;
    private String eventCommentString;
    private String completeDate;
    private String longitude = "";
    private String latitude = "";
    private String imgPath = "";
    private String edit_location;
    private TextView title;
    private TextView dateComplete;
    private TextView map;
    private TextView location;
    private Uri filePath;
    private Uri imageUri;
    private Bitmap bitmap = null;

    Object editLatitude;
    Object editLongitude ;
    HashMap editLocation;

    FirebaseStorage storage;
    StorageReference storageReference;

    // check whether the Activity is used for add or edit a HabitEvent
    private boolean editMode = false;

    /**
     * set up AddHabitEvent Activity
     * user can upload image, add chosen location or add  optional comment and edit habit event
     * @param savedInstanceState This is a previous saved state
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_habit_event);

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        // get related views
        optionalComment = findViewById(R.id.comment_text);          //create an optional comment
        title = findViewById(R.id.habitTitle);                      //get clicked habit title
        dateComplete = findViewById(R.id.eventDate);                //event complete date
        image_display = (ImageView) findViewById(R.id.first_image); //an imageview to add image
        deleteImage = (ImageView) findViewById(R.id.image_delete);  //delete the added image
        map = (TextView) findViewById(R.id.map_text);               //a textview to access location information
        location = findViewById(R.id.location_text);                //a textview to show location information
        deleteLocation = findViewById(R.id.location_delete);        //delete the added location

        //set today for complete day
        SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd");
        Date today = new Date();
        completeDate = date.format(today);

        Habit clickedHabit = (Habit) getIntent().getSerializableExtra("Habit");                             //get related Habit of this HabitEvent
        String clickedHabitID = (String) clickedHabit.getHabitID();                                               //get related Habit ID of this HabitEvent
        HabitEvent needUpdatedEvent = (HabitEvent) getIntent().getSerializableExtra("Update HabitEvent");   //get related HabitEvent detail

        image_display.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setChoosePhoto();
            }
        });//click to add optional photograph

        deleteImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filePath = Uri.parse("");
                image_display.setImageResource(R.mipmap.ic_upload_image);
                deleteImage.setVisibility(View.INVISIBLE);
            }
        });//click to delete photograph

        map.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent mapIntent = new Intent(AddHabitEventActivity.this, MapActivity.class);
                setLocationResultLauncher.launch(mapIntent);
            }
        });//click to get location

        deleteLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                location.setVisibility(View.INVISIBLE);
                deleteLocation.setVisibility(View.INVISIBLE);
                latitude = "";
                longitude = "";
            }
        });//click to delete location

        //if there's a selected HabitEvent, this is an edit HabitEvent page
        if (needUpdatedEvent != null) {
            // edit mode
            editMode = true;
            // filled in HabitEvent details
            optionalComment.setText(needUpdatedEvent.getEventComment());
            editLocation = needUpdatedEvent.getLocation();
            editLatitude = editLocation.get("latitude");
            editLongitude = editLocation.get("longitude");

            if (editLatitude != "" && editLongitude != "") {
                edit_location = "(" + editLatitude + "," + editLongitude + ")";
                location.setText(edit_location);
                deleteLocation.setVisibility(View.VISIBLE);
            }//show detail when have a location
            else if(editLatitude == "" && editLongitude == "" && !latitude.equals("") && !longitude.equals("")) {
                edit_location = "(" + latitude + "," + longitude + ")";
                location.setText(edit_location);
                deleteLocation.setVisibility(View.VISIBLE);
            }//show detail when add a new location
            if (needUpdatedEvent.getImage().equals("")) {
                filePath = Uri.parse(needUpdatedEvent.getImage());
                image_display.setImageResource(R.mipmap.ic_upload_image);
                deleteImage.setVisibility(View.INVISIBLE);
            } //show when no image
            else {
                filePath = Uri.parse(needUpdatedEvent.getImage());
                image_display.setImageURI(filePath);
                deleteImage.setVisibility(View.VISIBLE);
            }//show when have a image
        } else {
            // add mode
            editMode = false;
        }

        title.setText(clickedHabit.getHabitTitle());    //set title with habit title
        dateComplete.setText(completeDate);             //display today date

        storage = FirebaseStorage.getInstance();        //initialized with the default Firebase
        storageReference = storage.getReference();      //initialized at the root Firebase Storage location

        confirmButton = (Button) findViewById(R.id.confirm_habit_event);
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eventCommentString = optionalComment.getText().toString();

                final FirebaseFirestore database = FirebaseFirestore.getInstance();
                final CollectionReference collectionReference = database.collection(currentUser.getEmail()).document(clickedHabitID).collection("HabitEvent");
                final DocumentReference selectedHabit = database.collection(currentUser.getEmail()).document(clickedHabitID);

                // edit mode
                if (editMode) {
                    editMode = false;
                    // updates HabitEvent to firebase
                    editLatitude = editLocation.get("latitude");
                    editLongitude = editLocation.get("longitude");

                    if (filePath != null) {
                        StorageReference ref = storageReference.child("images/" + UUID.randomUUID().toString());
                        ref.putFile(filePath);
                        imgPath = filePath.toString();
                        needUpdatedEvent.setImage(imgPath);
                    } //add image when file path not null
                    else if (needUpdatedEvent.getImage() == "") {
                        StorageReference ref = storageReference.child("images/" + UUID.randomUUID().toString());
                        ref.putFile(filePath);
                        imgPath = filePath.toString();
                        needUpdatedEvent.setImage(imgPath);
                    }//update image when no image at imageview

                    if (needUpdatedEvent.getLocation().get("latitude") == "" && needUpdatedEvent.getLocation().get("longitude") == "" ){
                        HashMap<String, String> newLocation = new HashMap<String, String>() {{
                            put("latitude", latitude);
                            put("longitude", longitude);
                        }};
                        needUpdatedEvent.setLocation(newLocation);
                    }//update when no location added before and add a new location
                    else if(needUpdatedEvent.getLocation().get("latitude") != latitude && needUpdatedEvent.getLocation().get("longitude") != longitude && latitude != "" || longitude != ""){
                        HashMap<String, String> newLocation = new HashMap<String, String>() {{
                            put("latitude", latitude);
                            put("longitude", longitude);
                        }};
                        needUpdatedEvent.setLocation(newLocation);
                    }//update when change a location
                    else if(latitude == "" || longitude == "") {
                        HashMap<String, String> newLocation = new HashMap<String, String>() {{
                            put("latitude", latitude);
                            put("longitude", longitude);
                        }};
                        needUpdatedEvent.setLocation(newLocation);
                    }//update when user delete the location

                    needUpdatedEvent.setEventComment(eventCommentString);

                    HashMap<String, Object> data = new HashMap<>();
                    data.put("Event", needUpdatedEvent);
                    collectionReference.document(needUpdatedEvent.getEventID()).update(data);
                    // back to last page
                    Intent intent = new Intent(AddHabitEventActivity.this, HabitEventListViewActivity.class);
                    Toast.makeText(v.getContext(), "Event Successfully Edited", Toast.LENGTH_SHORT).show();
                    finish();
                }else {
                    // add mode
                    if (filePath != null) {
                        StorageReference ref = storageReference.child("images/" + UUID.randomUUID().toString());
                        ref.putFile(filePath);
                        imgPath = filePath.toString();
                    }//save image to storage under images folder

                    HashMap<String, String> newLocation = new HashMap<String, String>() {{
                        put("latitude", latitude);
                        put("longitude", longitude);
                    }};

                    final String habitEventID = UUID.randomUUID().toString().replace("-", "");//generate random habit event ID
                    HabitEvent newHabitEvent = new HabitEvent(habitEventID, eventCommentString, completeDate, clickedHabit.getHabitTitle(), imgPath, newLocation);
                    HashMap<String, HabitEvent> data = new HashMap<>();
                    data.put("Event", newHabitEvent);
                    if (newHabitEvent != null)
                        // update new HabitEvent to firebase
                        collectionReference.document(newHabitEvent.getEventID()).set(data);
                        // update latest finish date
                        selectedHabit.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()) {
                                    DocumentSnapshot document = task.getResult();
                                    if (document.exists()) {
                                        HashMap<String, Object> habitMap = (HashMap<String, Object>) document.getData().get("HabitClass");
                                        habitMap.put("latestFinishDate", completeDate);
                                        HashMap<String, Object> data = new HashMap<>();
                                        data.put("HabitClass", habitMap);
                                        selectedHabit.update(data);
                                    } else {
                                        Log.d("TAG", "No such document");
                                    }
                                } else {
                                    Log.d("TAG", "get failed with ", task.getException());
                                }
                            }
                        });


                    // back to user page (the one with today's habits list)
                    Intent intent = new Intent(AddHabitEventActivity.this, UserPageActivity.class);
                    Toast.makeText(v.getContext(), "New Event Created", Toast.LENGTH_SHORT).show();
                    startActivity(intent);
                }
            }
        });
    }

    /**
     * A view to select choose type
     *
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
                if(ActivityCompat.checkSelfPermission(AddHabitEventActivity.this,Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(AddHabitEventActivity.this,Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(AddHabitEventActivity.this,Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                    selection.dismiss();
                    Toast.makeText(AddHabitEventActivity.this, "Permission denied!", Toast.LENGTH_SHORT).show();
                    return;
                }
                selection.dismiss();
                openCamera();
            }
        });//take photo

        album.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ActivityCompat.checkSelfPermission(AddHabitEventActivity.this,Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(AddHabitEventActivity.this,Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(AddHabitEventActivity.this,Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                    selection.dismiss();
                    Toast.makeText(AddHabitEventActivity.this, "Permission denied!", Toast.LENGTH_SHORT).show();
                    return;
                }
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

        requestPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
    }


    /**
     * Open phone camera
     *
     */
    private void openCamera() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        openCameraResultLauncher.launch(takePictureIntent);
    }

    /**
     * open camera request result
     *
     */
    ActivityResultLauncher<Intent> openCameraResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() != RESULT_CANCELED) {
                        if (result.getResultCode() == RESULT_OK) {
                            Bundle extras = result.getData().getExtras();
                            Bitmap imageBitmap = (Bitmap) extras.get("data");
                            imageUri = getImageUri(getApplicationContext(), imageBitmap);
                            filePath = imageUri;                            //copy image uri
                            image_display.setImageBitmap(imageBitmap);      //display image on screen
                            deleteImage.setVisibility(View.VISIBLE);        //display delete button
                            saveImage();
                        }
                    }
                }
            });

    /**
     * Open phone gallery
     *
     */

    private void openGallery() {
        Intent photoPicker = new Intent(Intent.ACTION_PICK);
        photoPicker.setType("image/*");
        openGalleryResultLauncher.launch(photoPicker);
    }
    /**
     * open gallery request return
     *
     */
    ActivityResultLauncher<Intent> openGalleryResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() != RESULT_CANCELED) {
                        if (result.getResultCode() == RESULT_OK) {
                            filePath = result.getData().getData();
                            InputStream imageStream = null;//open input stream
                            try {
                                imageStream = getContentResolver().openInputStream(filePath);
                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            }
                            Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                            bitmap = selectedImage.copy(selectedImage.getConfig(), selectedImage.isMutable());//copy bitmap
                            image_display.setImageBitmap(bitmap);
                            imageUri = getImageUri(getApplicationContext(), bitmap);
                            filePath = imageUri;
                            deleteImage.setVisibility(View.VISIBLE);
                        }
                    }
                }
            });

    /**
     * save picture to gallery
     *
     */
    public void saveImage() {
        try {
            InputStream imageStream = getContentResolver().openInputStream(imageUri);//open input stream
            Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
            bitmap = selectedImage.copy(selectedImage.getConfig(), selectedImage.isMutable());//copy bitmap
            image_display.setImageBitmap(bitmap);

            if (bitmap != null) {
                Intent intent = new Intent();
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();//create a byte array output stream
                intent.putExtra(MediaStore.EXTRA_OUTPUT, bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream));//reduce the size of the image and put extra Data
            } else {
                Toast.makeText(AddHabitEventActivity.this, "Cannot save image!", Toast.LENGTH_SHORT).show();
            }
        } catch (FileNotFoundException e) {

        }
    }

    /**
     * get image uri
     * code from https://stackoverflow.com/questions/67844042/update-user-profileimage-in-firebase-android-studio
     * @param inContext
     * @param inImage
     * @return
     */
    public Uri getImageUri(Context inContext, Bitmap inImage) {
        Bitmap OutImage = Bitmap.createScaledBitmap(inImage, 1000, 1000, true);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), OutImage, "Title", null);
        return Uri.parse(path);
    }

    /**
     * go to map and return location and what result code returned
     *
     */
    ActivityResultLauncher<Intent> setLocationResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() != RESULT_CANCELED) {
                        if (result.getResultCode() == RESULT_OK) {
                            latitude = result.getData().getStringExtra("latitude");
                            longitude = result.getData().getStringExtra("longitude"); //get data from map
                            String display_text = "(" + latitude + "," + longitude + ")";
                            location.setText(display_text);
                            location.setVisibility(View.VISIBLE);
                            deleteLocation.setVisibility(View.VISIBLE);
                        }
                    }
                }
            });
}













