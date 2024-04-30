package com.example.djitelloapplication;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chaquo.python.PyObject;
import com.chaquo.python.Python;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class ScenarioViewActivity extends AppCompatActivity {
    private FlightScenarioAdapter adapter;
    private Button addScenario;
    private Button deleteScenario;
    private RecyclerView scenarioView;
    private static final int FILE_PICKER_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); // remove title bar from android screen
//        getSupportActionBar().hide();
        setContentView(R.layout.activity_scenario_list);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setNavigationBarColor(Color.parseColor("#000000"));
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setStatusBarColor(Color.parseColor("#000000"));
        }

        scenarioView = findViewById(R.id.scenario_list_recycler_view);
        scenarioView.setLayoutManager(new LinearLayoutManager(this));

        List<String> flightScenarios = getScenarioNamesFromAssets();
        adapter = new FlightScenarioAdapter(this, flightScenarios);
        scenarioView.setAdapter(adapter);

        deleteScenario = findViewById(R.id.delete_scenario);
        deleteScenario.setOnClickListener(v -> {
            // Update product button click logic
        });

        addScenario = findViewById(R.id.add_scenario);
        addScenario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchFilePicker();
            }
        });
    }

    private List<String> getScenarioNamesFromAssets() {
        List<String> flightScenarios = new ArrayList<>();
        AssetManager assetManager = getAssets();
        try {
            String[] scriptFiles = assetManager.list("python_module");
            if (scriptFiles != null) {
                for (String scriptFile : scriptFiles) {
                    // Check if the file has a .py extension
                    if (scriptFile.endsWith(".py")) {
                        flightScenarios.add(scriptFile);
                    }
                    else{
                        Toast.makeText(ScenarioViewActivity.this, "Invalid file type", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Error loading scripts from assets", Toast.LENGTH_SHORT).show();
        }
        return flightScenarios;
    }

    private void launchFilePicker() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.setType("*/*");
        startActivityForResult(intent, FILE_PICKER_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == FILE_PICKER_REQUEST_CODE && resultCode == RESULT_OK) {
            Uri selectedFileUri = data.getData();
            String selectedFilePath = getPathFromUri(selectedFileUri);
            copyFileToAssets(selectedFilePath);
        }
    }

    private String getPathFromUri(Uri uri) {
        String filePath = null;
        if (uri != null) {
            String[] projection = { MediaStore.Images.Media.DATA };
            Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
            if (cursor != null) {
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                cursor.moveToFirst();
                filePath = cursor.getString(column_index);
                cursor.close();
            }
        }
        return filePath;
    }


    private void copyFileToAssets(String fileName) {
        try {
            // Open the input stream for the script in the assets directory
            InputStream inputStream = getAssets().open("python_module/" + fileName);

            // Get the output directory in the app's internal storage
            File outputDir = new File(getFilesDir(), "python_module");
            if (!outputDir.exists()) {
                outputDir.mkdirs(); // Create the directory if it doesn't exist
            }

            // Construct the output file path
            String outputFilePath = outputDir.getAbsolutePath() + "/" + fileName;

            // Open the output stream
            OutputStream outputStream = new FileOutputStream(outputFilePath);

            // Copy the script
            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, length);
            }

            // Close streams
            inputStream.close();
            outputStream.close();

            // Optionally reload the Python module
            // reloadPythonModule();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void reloadPythonModule() {
        Python py = Python.getInstance();
        PyObject mainModule = py.getModule("__main__");
        mainModule.callAttr("reload_module");
    }
}

