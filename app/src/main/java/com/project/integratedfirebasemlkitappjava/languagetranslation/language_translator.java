package com.project.integratedfirebasemlkitappjava.languagetranslation;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.mlkit.common.model.DownloadConditions;
import com.google.mlkit.nl.translate.TranslateLanguage;
import com.google.mlkit.nl.translate.Translation;
import com.google.mlkit.nl.translate.Translator;
import com.google.mlkit.nl.translate.TranslatorOptions;
import com.project.integratedfirebasemlkitappjava.R;

public class language_translator extends AppCompatActivity {

    //initialize widgets
    private Spinner fromSpinner, toSpinner;
    private EditText sourceEditText;
    private Button button;
    private TextView translatedTextView;

    //Source Array of Strings - Spinners Data
    String[] fromLanguage = {"Select a Language","English", "Afrikaans", "Hindi", "Bengali", "Belarusian", "Catalan", "Urdu"};
    String[] toLanguage= {"Select a Language","English", "Afrikaans", "Hindi", "Bengali", "Belarusian", "Catalan", "Urdu"};

    //Permissions
    private static final int REQUEST_CODE = 1;
    String languageCode, fromLanguageCode, toLanguageCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_language_translator);
            fromSpinner = findViewById(R.id.idFromSpinner);
            toSpinner = findViewById(R.id.idToSpinner);
            sourceEditText = findViewById(R.id.idEdtSource);
            button = findViewById(R.id.idBtnTranslate);
            translatedTextView = findViewById(R.id.idTvTranslatedTV);


            //spinner 1
            //ADDING DATA TO THE SPINNER
            fromSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    fromLanguageCode = getLanguageCode(fromLanguage[position]);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
            ArrayAdapter fromAdapter = new ArrayAdapter(this, R.layout.spinner_item, fromLanguage);
            fromAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            fromSpinner.setAdapter(fromAdapter);

            //spinner 2
            toSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    toLanguageCode = getLanguageCode(toLanguage[position]);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
            ArrayAdapter toAdapter = new ArrayAdapter(this, R.layout.spinner_item, toLanguage);
            toAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            toSpinner.setAdapter(fromAdapter);


            //adding the functionality to the button and the spinner

            //button
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    translatedTextView.setText("");
                    if (sourceEditText.getText().toString().isEmpty()) {
                        Toast.makeText(language_translator.this, "Please Enter Some Text", Toast.LENGTH_SHORT).show();
                    } else if (fromLanguageCode.isEmpty()) {
                        Toast.makeText(language_translator.this, "Please Select Source Language", Toast.LENGTH_SHORT).show();
                    } else if (toLanguageCode.isEmpty()) {
                        Toast.makeText(language_translator.this, "Please Select Language to Translate", Toast.LENGTH_SHORT).show();
                    } else {
                        //translate the text
                        translateText(fromLanguageCode, toLanguageCode, sourceEditText.getText().toString());
                    }
                }
            });


        }

        private void translateText (String fromLanguageCode, String toLanguageCode, String
        sourceText){
            translatedTextView.setText("Downloading Language Model");
            try {
                TranslatorOptions options = new TranslatorOptions.Builder().setSourceLanguage(fromLanguageCode).setTargetLanguage(toLanguageCode).build();
                Translator translator = Translation.getClient(options);
                DownloadConditions conditions = new DownloadConditions.Builder().build();

                translator.downloadModelIfNeeded(conditions).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        translatedTextView.setText("Translating......");
                        translator.translate(sourceText).addOnSuccessListener(new OnSuccessListener<String>() {
                            @Override
                            public void onSuccess(String s) {
                                translatedTextView.setText(s);
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                //translation
                                Toast.makeText(language_translator.this, "FAIL TO TRANSLATE !", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        //it is failed to download the image
                        Toast.makeText(language_translator.this, "FAIL TO DOWNLOAD THE LANGUAGE!", Toast.LENGTH_SHORT).show();
                    }
                });

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        private String getLanguageCode (String language){
            switch (language) {
                case "English":
                    languageCode = TranslateLanguage.ENGLISH;
                    break;
                case "Hindi":
                    languageCode = TranslateLanguage.HINDI;
                    break;
                case "Afrikaans":
                    languageCode = TranslateLanguage.AFRIKAANS;
                    break;
                case "Bengali":
                    languageCode = TranslateLanguage.BENGALI;
                    break;
                case "Belarusian":
                    languageCode = TranslateLanguage.BELARUSIAN;
                    break;
                case "Catalan":
                    languageCode = TranslateLanguage.CATALAN;
                    break;
                case "Urdu":
                    languageCode = TranslateLanguage.URDU;
                    break;
                default:
                    languageCode = "";
            }
            return languageCode;
        }
    }
