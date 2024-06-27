package com.example.DragAndDropProject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.os.Bundle;
import android.view.DragEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private List<TextView> dragItems = new ArrayList<>();
    private List<TextView> dropAreas = new ArrayList<>();
    private Button showAnswerButton;
    private boolean[] isAnswered;
    private boolean[] isAnsweredCorrectly;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize your drag items
        dragItems.add((TextView) findViewById(R.id.dragItem1));
        dragItems.add((TextView) findViewById(R.id.dragItem2));
        dragItems.add((TextView) findViewById(R.id.dragItem3));
        dragItems.add((TextView) findViewById(R.id.dragItem4));

        // Initialize your drop areas
        dropAreas.add((TextView) findViewById(R.id.dropArea1));
        dropAreas.add((TextView) findViewById(R.id.dropArea2));
        dropAreas.add((TextView) findViewById(R.id.dropArea3));
        dropAreas.add((TextView) findViewById(R.id.dropArea4));

        // Initialize isAnswered array based on the number of drop areas
        isAnswered = new boolean[dropAreas.size()];
        isAnsweredCorrectly = new boolean[dropAreas.size()];

        // Set up drag listeners for each drag item
        for (int i = 0; i < dragItems.size(); i++) {
            final int index = i;
            dragItems.get(i).setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    View.DragShadowBuilder myShadow = new View.DragShadowBuilder(dragItems.get(index));
                    // Start the drag.
                    view.startDragAndDrop(null,  // The data to be dragged.
                            myShadow,                 // The drag shadow builder.
                            dragItems.get(index),     // No need to use local data.
                            0                         // Flags. Not currently used, set to 0.
                    );

                    return false;
                }
            });
        }

        // Set up drag listeners for each drop area
        for (int i = 0; i < dropAreas.size(); i++) {
            final int index = i;
            dropAreas.get(i).setOnDragListener(new View.OnDragListener() {
                @Override
                public boolean onDrag(View view, DragEvent dragEvent) {
                    View draggedView = (View) dragEvent.getLocalState();

                    switch (dragEvent.getAction()) {
                        case DragEvent.ACTION_DRAG_STARTED:
                            return true;
                        case DragEvent.ACTION_DRAG_ENTERED:
                            if (!isAnswered[index]) {
                                view.setBackgroundColor(ContextCompat.getColor(view.getContext(), R.color.fuschia));
                            }
                            return true;
                        case DragEvent.ACTION_DRAG_EXITED:
                            if (!isAnswered[index]) {
                                view.setBackgroundColor(ContextCompat.getColor(view.getContext(), R.color.holo_blue_light));
                            }
                            return true;
                        case DragEvent.ACTION_DROP:
                            view.setBackgroundColor(ContextCompat.getColor(view.getContext(), R.color.holo_blue_dark));

                            isAnswered[index] = true;
                            isAnsweredCorrectly[index] = draggedView.getId() == dragItems.get(index).getId();
                            checkAllAnswered(); // Check if all answers are made
                            return true;
                        case DragEvent.ACTION_DRAG_ENDED:
                            return true;
                        default:
                            return false;
                    }
                }
            });
        }

        // Initialize showAnswerButton and set onClickListener
        showAnswerButton = findViewById(R.id.showButton);
        showAnswerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Show answers (red/green colors)
                for (int i = 0; i < dropAreas.size(); i++) {
                    TextView dropArea = dropAreas.get(i);
                    if (isAnsweredCorrectly[i]) {
                        dropArea.setBackgroundColor(ContextCompat.getColor(MainActivity.this, R.color.green));
                    } else {
                        dropArea.setBackgroundColor(ContextCompat.getColor(MainActivity.this, R.color.red));
                    }
                }
                // Disable the button after showing answers
                showAnswerButton.setEnabled(false);
            }
        });
        // Initially disable the button until all answers are made
        showAnswerButton.setEnabled(false);
    }

    // Method to check if all answers are made
    private void checkAllAnswered() {
        for (boolean answered : isAnswered) {
            if (!answered) {
                return; // Exit if any answer is not made
            }
        }
        // Enable the button if all answers are made
        showAnswerButton.setEnabled(true);
    }
}
