package com.example.DragAndDropProject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.os.Bundle;
import android.view.DragEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private List<TextView> dragItems = new ArrayList<>();
    private List<TextView> dropAreas = new ArrayList<>();
    private Button showAnswerButton;
    private Button resetButton;
    private boolean[] isAnswered;
    private boolean[] isAnsweredCorrectly;
    private final String[] dragItemTexts = {
            "Founding of Rome",
            "America is discovered",
            "Protestant Reformation",
            "American Revolution",
            "French Revolution",
            "End of World War II",
            "The Moon Landing",
            "Fall of the Berlin Wall"
    };
    private String[] dropAreaTexts = {
            "753 BCE",
            "1492",
            "1517",
            "1776",
            "1789",
            "1945",
            "1969",
            "1989"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize your drag items dynamically
        int[] dragItemIds = {
                R.id.dragItem1,
                R.id.dragItem2,
                R.id.dragItem3,
                R.id.dragItem4,
                R.id.dragItem5,
                R.id.dragItem6,
                R.id.dragItem7,
                R.id.dragItem8
        };


        // Create a copy of the dragItemTexts
        List<String> shuffledDragItemTexts = new ArrayList<>(Arrays.asList(dragItemTexts));

        // Shuffle the Texts so that the order of events is randomised
        Collections.shuffle(shuffledDragItemTexts);

        for (int i = 0; i < dragItemIds.length; i++) {
            TextView dragItem = findViewById(dragItemIds[i]);
            dragItem.setText(shuffledDragItemTexts.get(i));
            dragItems.add(dragItem);
        }

        // Initialize your drop areas with fixed texts
        int[] dropAreaIds = {
                R.id.dropArea1,
                R.id.dropArea2,
                R.id.dropArea3,
                R.id.dropArea4,
                R.id.dropArea5,
                R.id.dropArea6,
                R.id.dropArea7,
                R.id.dropArea8
        };

        for (int i = 0; i < dropAreaIds.length; i++) {
            TextView dropArea = findViewById(dropAreaIds[i]);
            dropArea.setText(dropAreaTexts[i]);
            dropAreas.add(dropArea);
        }

        // Initialize isAnswered array based on the number of drop areas
        isAnswered = new boolean[dropAreas.size()];
        isAnsweredCorrectly = new boolean[dropAreas.size()];

        // Set up drag item listeners for each drag item
        setUpDragItemListeners();

        // Set up drop area listeners for each drop area
        setUpDropAreaListeners();

        // Initialize showAnswerButton and set onClickListener
        showAnswerButton = findViewById(R.id.showButton);
        // Initially disable the button until all answers are made
        showAnswerButton.setEnabled(false);
        showAnswerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Show answers (red/green colors)
                for (int i = 0; i < dropAreas.size(); i++) {
                    TextView dropArea = dropAreas.get(i);
                    if (isAnsweredCorrectly[i]) {
                        dropArea.setBackgroundColor(ContextCompat.getColor(MainActivity.this, R.color.correct));
                    } else {
                        dropArea.setBackgroundColor(ContextCompat.getColor(MainActivity.this, R.color.wrong));
                    }
                }
                // Disable the button after showing answers
                showAnswerButton.setEnabled(false);
            }
        });


        // Initialize showAnswerButton and set onClickListener
        resetButton = findViewById(R.id.resetButton);

        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < dropAreas.size(); i++) {
                    TextView dropArea = dropAreas.get(i);
                    dropArea.setBackgroundColor(ContextCompat.getColor(MainActivity.this, R.color.holo_blue_light));
                    dropArea.setText(dropAreaTexts[i]);  // Reset text to original
                    isAnswered[i] = false;
                    isAnsweredCorrectly[i] = false;
                    showAnswerButton.setEnabled(false);

                    TextView dragItem = dragItems.get(i);
                    dragItem.setBackgroundColor(ContextCompat.getColor(MainActivity.this, R.color.holo_green_light));
                }
                // Re-Enable the OnLongClickListeners
                setUpDragItemListeners();
                // Re-Enable the OnDragListeners
                setUpDropAreaListeners();
            }
        });
    }

    // Method to check if all answers are made
    private void checkAllAnswered() {
        for (boolean answered : isAnswered) {
            if (!answered) {
                showAnswerButton.setEnabled(true); //TODO REMOVE
                return; // Exit if any answer is not made
            }
        }
        // Enable the button if all answers are made
        showAnswerButton.setEnabled(true);
    }

    // Method to set up drag item listeners for each drag item
    private void setUpDragItemListeners() {
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
    }

    // Method to set up drop area listeners for each drop area
    private void setUpDropAreaListeners() {
        for (int i = 0; i < dropAreas.size(); i++) {
            final int index = i;

            dropAreas.get(i).setOnDragListener(new View.OnDragListener() {
                @Override
                public boolean onDrag(View view, DragEvent dragEvent) {
                    TextView draggedView = (TextView) dragEvent.getLocalState();

                    switch (dragEvent.getAction()) {
                        case DragEvent.ACTION_DRAG_STARTED:
                            return true;
                        case DragEvent.ACTION_DRAG_ENTERED:
                            if (!isAnswered[index]) {
                                view.setBackgroundColor(ContextCompat.getColor(view.getContext(), R.color.holo_blue_dark));
                            }
                            return true;
                        case DragEvent.ACTION_DRAG_EXITED:
                            if (!isAnswered[index]) {
                                view.setBackgroundColor(ContextCompat.getColor(view.getContext(), R.color.holo_blue_light));
                            }
                            return true;
                        case DragEvent.ACTION_DROP:
                            view.setBackgroundColor(ContextCompat.getColor(view.getContext(), R.color.holo_blue_dark));
                            draggedView.setBackgroundColor(ContextCompat.getColor(view.getContext(), R.color.green_assigned));

                            // Disable OnLongClickListener of event
                            draggedView.setOnLongClickListener(null);
                            // Disable OnDragListener of year
                            view.setOnDragListener(null);

                            String dropAreaText = dropAreas.get(index).getText().toString();
                            String dragItemText = draggedView.getText().toString();

                            // Concatenate dropAreaText and dragItemText with a line break
                            String combinedText = dropAreaText + ":\n" + dragItemText;
                            dropAreas.get(index).setText(combinedText);

                            // Check if droppedText matches the corresponding year in dropAreaTexts array
                            int dropAreaIndex = findDropAreaIndex(dragItemText); // Find the index of droppedText in dragItemTexts
                            isAnsweredCorrectly[index] = dropAreaIndex != -1 && dropAreaText.equals(dropAreaTexts[dropAreaIndex]);
                            isAnswered[index] = true;
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
    }

    // Helper method to find the index of droppedText in dragItemTexts
    private int findDropAreaIndex(String droppedText) {
        for (int i = 0; i < dragItemTexts.length; i++) {
            if (dragItemTexts[i].equals(droppedText)) {
                return i;
            }
        }
        return -1; // Not found
    }
}
