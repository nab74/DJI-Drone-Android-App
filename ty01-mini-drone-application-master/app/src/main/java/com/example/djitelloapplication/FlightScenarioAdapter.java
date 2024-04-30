package com.example.djitelloapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.chaquo.python.PyObject;
import com.chaquo.python.Python;

import java.util.List;

public class FlightScenarioAdapter extends RecyclerView.Adapter<FlightScenarioAdapter.ViewHolder> {

    private List<String> flightScenarios;
    private Context context;

    public FlightScenarioAdapter(Context context, List<String> flightScenarios) {
        this.context = context;
        this.flightScenarios = flightScenarios;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View scenarioView = inflater.inflate(R.layout.item_flight_scenario, parent, false);

        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(scenarioView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(FlightScenarioAdapter.ViewHolder holder, int position) {
        final String scenario = flightScenarios.get(position);
        holder.scenarioTextView.setText(scenario);
        Button button = holder.playButton;
        button.setText("Play");
        button.setEnabled(true);
//        holder.playButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                executePythonScript(scenario);
//            }
//        });
    }

//    private void executePythonScript(String scriptName) {
//        Python py = Python.getInstance();
//        PyObject pyObj = py.getModule("python_module");
//        pyObj.callAttr(scriptName);
//    }

    @Override
    public int getItemCount() {
        return flightScenarios.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView scenarioTextView;
        public Button playButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            scenarioTextView = (TextView) itemView.findViewById(R.id.scenario_name);
            playButton = (Button) itemView.findViewById(R.id.play_button);
        }
    }

}
