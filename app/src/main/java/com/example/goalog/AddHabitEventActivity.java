package com.example.goalog;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
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
    private TextView title;
    private TextView dateComplete;
    private Uri filePath;
    private boolean editMode = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_habit_event);
        optimalComment = findViewById(R.id.comment_text);//create an optional comment
        title = findViewById(R.id.habitTitle);
        dateComplete = findViewById(R.id.eventDate);

        SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd");
        Date today = new Date();
        completeDate = date.format(today);

        Habit clickedHabit = (Habit) getIntent().getSerializableExtra("Habit");
        String clickedHabitID = (String) clickedHabit.getHabitID();
        HabitEvent needUpdatedEvent = (HabitEvent) getIntent().getSerializableExtra("Update HabitEvent");
        if(needUpdatedEvent != null)
        {
            editMode = true;
            optimalComment.setText(needUpdatedEvent.getEventComment());
        }
        else {
            editMode =false;
        }
        final String habitEventID = UUID.randomUUID().toString().replace("-", "");
        title.setText(clickedHabit.getHabitTitle());
        dateComplete.setText(completeDate);

        confirmButton = (Button) findViewById(R.id.confirm_habit_event);
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eventCommentString = optimalComment.getText().toString();
                // image = (ImageView) findViewById(R.id.first_image);
                //deleteIcon = (ImageView) findViewById(R.id.image_delete);

                final FirebaseFirestore database = FirebaseFirestore.getInstance();
                final CollectionReference collectionReference = database.collection("user003").document(clickedHabitID).collection("HabitEvent");
                if(editMode) {
                    editMode = false;
                    needUpdatedEvent.setEventComment(eventCommentString);
                    HashMap<String,Object> data = new HashMap<>();
                    data.put("Event",needUpdatedEvent);
                    collectionReference.document(needUpdatedEvent.getEventID()).update(data);
                    Intent intent = new Intent(AddHabitEventActivity.this, HabitEventListViewActivity.class);
                    startActivity(intent);
                } else {
                    HabitEvent newHabitEvent = new HabitEvent(habitEventID, eventCommentString, completeDate,clickedHabit.getHabitTitle());
                    HashMap<String, HabitEvent> data = new HashMap<>();
                    data.put("Event", newHabitEvent);
                    if (newHabitEvent != null) {
                        collectionReference.document(newHabitEvent.getEventID()).set(data);
                    }
                    Intent intent = new Intent(AddHabitEventActivity.this, UserPageActivity.class);
                    //intent.putExtra("success",true);
                    startActivity(intent);
                }



            }
        });
    }
}

/*save for later, for optional photograph
        /*
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //setChoosePhoto();
            }
        });//click to add optional photograph

        deleteIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                image.setImageResource(R.mipmap.ic_upload_image);
                deleteIcon.setVisibility(View.INVISIBLE);

            }
        });
        //final FirebaseFirestore database = FirebaseFirestore.getInstance();
        //final CollectionReference collectionReference = database.collection("user003");

        FirebaseStorage storage;
        StorageReference storageReference;

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        FirebaseAuth mAuth = FirebaseAuth.getInstance();

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

                if (filePath != null) {
                    StorageReference ref = storageReference.child("images/" + UUID.randomUUID().toString());
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


     selection(take a photo or open gallery)

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


     * open phone photo gallery

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

     * Get file path


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









