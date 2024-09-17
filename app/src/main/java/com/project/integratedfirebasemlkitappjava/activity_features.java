package com.project.integratedfirebasemlkitappjava;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.project.integratedfirebasemlkitappjava.faceRecognition.face_recognition;
import com.project.integratedfirebasemlkitappjava.languagetranslation.language_translator;
import com.project.integratedfirebasemlkitappjava.ocr.imageToText;

import java.util.ArrayList;
import java.util.List;

public class activity_features extends AppCompatActivity {
    // 1- Adapter View
    private RecyclerView recyclerView;

    // 2- Data Source
    private List<MLFeatures> featuresList;

    // 3- Adapter
    private CustomFeaturesAdapter myAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_features);

        recyclerView = findViewById(R.id.recyclerView);
        featuresList = new ArrayList<>();

        MLFeatures s1 = new MLFeatures("Language Translation ",R.drawable.languagetranslation);
        MLFeatures s2 = new MLFeatures("Image to Text Conversion", R.drawable.ocr);
        MLFeatures s3 = new MLFeatures("Face Angle Calculation ", R.drawable.facerecognition);


        featuresList.add(s1);
        featuresList.add(s2);
        featuresList.add(s3);

        myAdapter = new CustomFeaturesAdapter(featuresList,new onItemClickListener() {
            @Override
            public void onItemClick(int position) {
                // Handle the click event for each item
                // Start a new activity based on position
                if (position == 0) {
                    Intent intent = new Intent(activity_features.this, language_translator.class);
                    startActivity(intent);
                } else if (position == 1) {
                   Intent intent = new Intent(activity_features.this, imageToText.class);
                   startActivity(intent);
                } else if (position == 2) {
                  Intent intent = new Intent(activity_features.this, face_recognition.class);
                    startActivity(intent);
                }
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(myAdapter);
    }
}