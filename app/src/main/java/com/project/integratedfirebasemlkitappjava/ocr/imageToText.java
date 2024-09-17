package com.project.integratedfirebasemlkitappjava.ocr;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.project.integratedfirebasemlkitappjava.R;
import static android.icu.lang.UProperty.AGE;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Bundle;
import android.provider.MediaStore;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.text.Text;
import com.google.mlkit.vision.text.TextRecognition;
import com.google.mlkit.vision.text.TextRecognizer;
import com.google.mlkit.vision.text.latin.TextRecognizerOptions;

import java.util.Locale;

public class imageToText extends AppCompatActivity {
    private static final int PICK_IMAGE = 123;
    //widgets
    private ImageView imageView;
    private TextView textView;
    private Button chooseImageButton,speechImageButton;

    //variables that we will need inside this project
    InputImage inputImage;
    TextRecognizer textRecognizer;
    TextToSpeech textToSpeech;
    public Bitmap textImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_to_text);

        textRecognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS);

        imageView = findViewById(R.id.imageView);
        textView = findViewById(R.id.text);
        chooseImageButton = findViewById(R.id.chooseImageButton);
        speechImageButton = findViewById(R.id.speechButton);

        chooseImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //this method allows user to choose from the gallery
                openGallery();
            }
        });
        textToSpeech= new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {
                if(i!=TextToSpeech.ERROR){
                    textToSpeech.setLanguage(Locale.US);
                }
            }
        });

        speechImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textToSpeech.speak(textView.getText().toString(),TextToSpeech.QUEUE_FLUSH,null);
            }
        });
    }

    private void openGallery() {
        Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
        getIntent.setType("image/*");

        Intent pickIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickIntent.setType("image/*");

        Intent chooserIntent  = Intent.createChooser(getIntent,"Select Image");
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS,new Intent[]{pickIntent});
        startActivityForResult(chooserIntent,PICK_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==PICK_IMAGE){
            if(data!=null){
                byte[] byteArray  = new byte[0];
                String filePath = null;
                try{
                    inputImage = InputImage.fromFilePath(this,data.getData());
                    Bitmap resultUri = inputImage.getBitmapInternal();
                    Glide.with(imageToText.this).load(resultUri).into(imageView);


                    //Process text block above we have loaded the image now we need to read that image
                    Task<Text> result = textRecognizer.process(inputImage).addOnSuccessListener(new OnSuccessListener<Text>() {
                        @Override
                        public void onSuccess(Text text) {
                            //Start ML KIT -process textBlock
                            StringBuilder result = new StringBuilder();
                            for(Text.TextBlock block : text.getTextBlocks()){
                                String textBlock = block.getText();
                                Point[] blockCornerPoint = block.getCornerPoints();
                                Rect blockFrame = block.getBoundingBox();
                                for(Text.Line line : block.getLines()){
                                    String lineBlock = line.getText();
                                    Point[] lineCornerPoint = line.getCornerPoints();
                                    Rect lineFrame = line.getBoundingBox();

                                    for(Text.Element element : line.getElements()){
                                        String elementText = element.getText();
                                        Point[] elementCornerPoint = element.getCornerPoints();
                                        Rect elementFrame = element.getBoundingBox();
                                        result.append(elementText);
                                    }
                                    textView.setText(result);
                                }
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(imageToText.this,"Failed!",Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    protected void onPause() {
        if(!textToSpeech.isSpeaking()){
            super.onPause();
        }
    }
    }
