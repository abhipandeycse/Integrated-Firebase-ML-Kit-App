package com.project.integratedfirebasemlkitappjava.faceRecognition;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import com.project.integratedfirebasemlkitappjava.R;
import com.project.integratedfirebasemlkitappjava.R;

//WE ARE TELLING THIS CLASS THAT THIS CLASS IS ACTING AS A DIALOG FRAGMENT
public class resultDialog extends DialogFragment {
    Button btn;
    TextView textView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_resultdialog, container, false);
        String text="";
        btn = view.findViewById(R.id.ok_btn);
        textView=view.findViewById(R.id.dialog);


        //getting the bundle since the data is coming from mainActivity to this fragment'
        Bundle bundle = getArguments();
        text= bundle.getString("RESULT_TEXT");
        textView.setText(text);

        //adding the click event on the button
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        return view;
    }
}
