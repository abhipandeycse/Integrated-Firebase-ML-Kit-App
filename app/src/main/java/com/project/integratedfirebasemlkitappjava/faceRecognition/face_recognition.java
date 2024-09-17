package com.project.integratedfirebasemlkitappjava.faceRecognition;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import com.project.integratedfirebasemlkitappjava.R;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.face.Face;
import com.google.mlkit.vision.face.FaceDetection;
import com.google.mlkit.vision.face.FaceDetector;
import com.google.mlkit.vision.face.FaceDetectorOptions;

import java.util.List;

public class face_recognition extends AppCompatActivity {
    Button button;
    TextView textView;
    ImageView imageView;

    private static final int REQUEST_IMAGE_CAPTURE = 124;
    InputImage firebaseVision;
    FaceDetector visionFaceDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_face_recognition);

        button = findViewById(R.id.camera_btn);
        textView= findViewById(R.id.text1);
        imageView = findViewById(R.id.imageView);

        FirebaseApp.initializeApp(this);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFile();
            }
        });
        Toast.makeText(this,"App is Started!",Toast.LENGTH_SHORT).show();
    }

    private void openFile() {
        //we will direct you to the dialog with the data that we have scanned from your face
        //AFTER PROCESSING THE RESULT FROM THIS IMAGE WE ARE GOING TO DISPLAY THE IMAGE IN THE DIALOG
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
        } else {
            Toast.makeText(this, "Failed!", Toast.LENGTH_SHORT).show();
        }
    }



    //since we are using startActivityForResult we need to override onActivityResultMethod
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Bundle bundle = data.getExtras();
        Bitmap bitmap = (Bitmap) bundle.get("data");
        faceDetectionProcess(bitmap);
        Toast.makeText(this, "Success!", Toast.LENGTH_SHORT).show();
    }

    public void faceDetectionProcess(Bitmap bitmap) {
        textView.setText("Processing Image.....");
        final StringBuilder builder = new StringBuilder();
        BitmapDrawable bitmapDrawable =(BitmapDrawable) imageView.getDrawable();

        InputImage inputImage = InputImage.fromBitmap(bitmap,0);

        FaceDetectorOptions highAccuracyOpt = new FaceDetectorOptions.Builder()
                .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_ACCURATE)
                .setLandmarkMode(FaceDetectorOptions.LANDMARK_MODE_ALL)
                .setClassificationMode(FaceDetectorOptions.CLASSIFICATION_MODE_ALL).enableTracking().build();
        FaceDetector faceDetector = FaceDetection.getClient(highAccuracyOpt);

        Task<List<Face>> result = faceDetector.process(inputImage).addOnSuccessListener(new OnSuccessListener<List<Face>>() {
            @Override
            public void onSuccess(List<Face> faces) {
                if(faces.size()!=0){
                    if(faces.size()==1){
                        builder.append(faces.size()+" Face Detected\n\n");
                    } else if (faces.size()>1){
                        builder.append(faces.size()+" Faces Detected\n\n");
                    }
                }
                for(Face face : faces){
                    //for every face we are going to do the processing individually

                    //tilting and rotational probability
                    int id =face.getTrackingId();
                    float rotationY = face.getHeadEulerAngleY();
                    float rotationZ = face.getHeadEulerAngleZ();

                    builder.append("1. Face Tracking ID ["+id+"]\n");
                    builder.append("2. Head Rotation to Right ["+String.format("%.2f",rotationY)+" deg. ]\n");
                    builder.append("3. Head Tilted Sideways ["+String.format("%.2f",rotationZ)+" deg. ]\n");

                    //smiling probability
                    if(face.getSmilingProbability()>0){
                        float smilingProbability = face.getSmilingProbability();
                        builder.append("4. Smiling Probability ["+String.format("%.2f",smilingProbability)+"]\n");
                    }


                    //left eye opened probability
                    if(face.getLeftEyeOpenProbability()>0){
                        float leftEyeOpenedProbability = face.getLeftEyeOpenProbability();
                        builder.append("5. Left Eye Opened Probability ["+String.format("%.2f",leftEyeOpenedProbability)+"]\n");
                    }

                    //right eye opened probability
                    if(face.getRightEyeOpenProbability()>0){
                        float rightEyeOpenedProbability = face.getRightEyeOpenProbability();
                        builder.append("6. Right Eye Opened Probability ["+String.format("%.2f",rightEyeOpenedProbability)+"]\n");
                    }

                    builder.append("\n");
                }
                showDetection("Face Detection",builder,true);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                StringBuilder builder1 = new StringBuilder();
                builder1.append("Sorry! There is an Error!");
                showDetection("Face Detection",builder,false);
            }
        });
    }

    public void showDetection(final String title,final StringBuilder builder, boolean success) {
        if(success==true){
            textView.setText(null);
            textView.setMovementMethod(new ScrollingMovementMethod());

            if(builder.length()!=0){
                textView.append(builder);
                if(title.substring(0,title.indexOf(' ')).equals("ORC")){
                    textView.append("\n(Hold the string to copy it!)");
                } else{
                    textView.append("\n(Hold the string to copy it!)");
                }

                textView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        ClipboardManager clipboardManager =(ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                        ClipData clipData = ClipData.newPlainText(title,builder);
                        clipboardManager.setPrimaryClip(clipData);
                        return true;
                    }
                });
            } else {
                textView.append(title.substring(0,title.indexOf(' '))+" Failed to find Anything!");
            }
        }else if(success == false){
            textView.setText(null);
            textView.setMovementMethod(new ScrollingMovementMethod());
            textView.append(builder);
        }
    }
    }
