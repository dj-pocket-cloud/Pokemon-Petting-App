package rs.example.pokemonpettingapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;

// class for the Select Color dialog
public class ColorDialogFragment extends DialogFragment {
    private SeekBar alphaSeekBar;
    private SeekBar redSeekBar;
    private SeekBar greenSeekBar;
    private SeekBar blueSeekBar;
    private SeekBar widthSeekBar;
    //private View colorView;
    private int color;
    private ImageView previewView;

    // create an AlertDialog and return it
    @Override
    public Dialog onCreateDialog(Bundle bundle) {
        // create dialog
        AlertDialog.Builder builder =
                new AlertDialog.Builder(getActivity());
        View colorDialogView = getActivity().getLayoutInflater().inflate(
                R.layout.fragment_color, null);
        builder.setView(colorDialogView); // add GUI to dialog

        // set the AlertDialog's message
        builder.setTitle(R.string.title_color_dialog);

        // get the color SeekBars and set their onChange listeners
        alphaSeekBar = (SeekBar) colorDialogView.findViewById(
                R.id.alphaSeekBar);
        redSeekBar = (SeekBar) colorDialogView.findViewById(
                R.id.redSeekBar);
        greenSeekBar = (SeekBar) colorDialogView.findViewById(
                R.id.greenSeekBar);
        blueSeekBar = (SeekBar) colorDialogView.findViewById(
                R.id.blueSeekBar);
        widthSeekBar = (SeekBar) colorDialogView.findViewById(
                R.id.widthSeekBar);
        //colorView = colorDialogView.findViewById(R.id.colorView);

        previewView = (ImageView) colorDialogView.findViewById(R.id.previewView);

        // register SeekBar event listeners
        alphaSeekBar.setOnSeekBarChangeListener(colorChangedListener);
        redSeekBar.setOnSeekBarChangeListener(colorChangedListener);
        greenSeekBar.setOnSeekBarChangeListener(colorChangedListener);
        blueSeekBar.setOnSeekBarChangeListener(colorChangedListener);

        // use current drawing color to set SeekBar values
        final PetView petView = getPetViewFragment().getPetView();
        color = petView.getShapeColor();
        alphaSeekBar.setProgress(Color.alpha(color));
        redSeekBar.setProgress(Color.red(color));
        greenSeekBar.setProgress(Color.green(color));
        blueSeekBar.setProgress(Color.blue(color));

        // add Set Color Button
        builder.setPositiveButton(R.string.button_set_color,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        petView.setShapeColor(color);
                        petView.setLineWidth(widthSeekBar.getProgress());
                    }
                }

        );

        return builder.create(); // return dialog
    }

    // gets a reference to the MainActivityFragment
    private MainActivityFragment getPetViewFragment() {
        return (MainActivityFragment) getFragmentManager().findFragmentById(
                R.id.doodleFragment);
    }

    // tell MainActivityFragment that dialog is now displayed
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        MainActivityFragment fragment = getPetViewFragment();

        if (fragment != null)
            fragment.setDialogOnScreen(true);
    }

    // tell MainActivityFragment that dialog is no longer displayed
    @Override
    public void onDetach() {
        super.onDetach();
        MainActivityFragment fragment = getPetViewFragment();

        if (fragment != null)
            fragment.setDialogOnScreen(false);
    }

    // OnSeekBarChangeListener for the SeekBars in the color dialog
    private final OnSeekBarChangeListener colorChangedListener =
            new OnSeekBarChangeListener() {
                final Bitmap bitmap = Bitmap.createBitmap(
                        400, 100, Bitmap.Config.ARGB_8888);
                final Canvas canvas = new Canvas(bitmap); // draws into bitmap
                // display the updated color
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress,
                                              boolean fromUser) {

                    if (fromUser) { // user, not program, changed SeekBar progress
                        color = Color.argb(alphaSeekBar.getProgress(),
                                redSeekBar.getProgress(), greenSeekBar.getProgress(),
                                blueSeekBar.getProgress());

                    }
                    //colorView.setBackgroundColor(color);
                    Paint p = new Paint();
                    p.setColor(color);
                    p.setStrokeCap(Paint.Cap.ROUND);
                    p.setStrokeWidth(10);

                    // erase the bitmap and redraw the line
                    bitmap.eraseColor(
                            getResources().getColor(android.R.color.transparent,
                                    getContext().getTheme()));
                    canvas.drawLine(30, 50, 370, 50, p);
                    previewView.setImageBitmap(bitmap);

                }

                //TODO make another listener for line width or integrate it into existing listener
                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {} // required

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {} // required
            };
}