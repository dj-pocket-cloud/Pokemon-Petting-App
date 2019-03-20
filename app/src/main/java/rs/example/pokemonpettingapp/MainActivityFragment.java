package rs.example.pokemonpettingapp;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import static android.widget.Toast.*;

public class MainActivityFragment extends Fragment {
    private PetView petView; // handles touch events and draws
    private float acceleration;
    private float currentAcceleration;
    private float lastAcceleration;
    private boolean dialogOnScreen = false;
    private int pokeId = 25; //corresponds to pokedex number
    private boolean spriteMode = true; //true = color, false = greyscale
    private Animation shakeAnimation;
    FrameLayout petFrame;
    MediaPlayer mediaPlayer = new MediaPlayer();
    int cry = R.raw.cry001; //change this cry programmatically based on current pokemon
    ImageView pokeImg;

    // value used to determine whether user shook the device to erase
    private static final int ACCELERATION_THRESHOLD = 100000;

    // used to identify the request for using external storage, which
    // the save image feature needs
    private static final int SAVE_IMAGE_PERMISSION_REQUEST_CODE = 1;

    // called when Fragment's view needs to be created
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view =
                inflater.inflate(R.layout.fragment_main, container, false);

        setHasOptionsMenu(true); // this fragment has menu items to display

        // load the shake animation that's used for animating the pokemon
        shakeAnimation = AnimationUtils.loadAnimation(getActivity(), R.anim.pokemon_jump);
        shakeAnimation.setRepeatCount(3); // animation repeats 3 times


        // get reference to the DoodleView
        petView = (PetView) view.findViewById(R.id.petView);

        pokeImg = (ImageView) view.findViewById(R.id.pokeImageView);

        petFrame = (FrameLayout) view.findViewById(R.id.petFrame);

        // initialize acceleration values
        acceleration = 0.00f;
        currentAcceleration = SensorManager.GRAVITY_EARTH;
        lastAcceleration = SensorManager.GRAVITY_EARTH;

        //pokeImg.startAnimation(shakeAnimation);

        return view;
    }



    // start listening for sensor events
    @Override
    public void onResume() {
        super.onResume();
        enableAccelerometerListening(); // listen for shake event
    }

    // enable listening for accelerometer events
    private void enableAccelerometerListening() {
        // get the SensorManager
        SensorManager sensorManager =
                (SensorManager) getActivity().getSystemService(
                        Context.SENSOR_SERVICE);

        // register to listen for accelerometer events
        sensorManager.registerListener(sensorEventListener,
                sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_NORMAL);
    }

    // stop listening for accelerometer events
    @Override
    public void onPause() {
        super.onPause();
        disableAccelerometerListening(); // stop listening for shake
    }

    // disable listening for accelerometer events
    private void disableAccelerometerListening() {
        // get the SensorManager
        SensorManager sensorManager =
                (SensorManager) getActivity().getSystemService(
                        Context.SENSOR_SERVICE);

        // stop listening for accelerometer events
        sensorManager.unregisterListener(sensorEventListener,
                sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER));
    }

    // event handler for accelerometer events
    private final SensorEventListener sensorEventListener =
            new SensorEventListener() {
                // use accelerometer to determine whether user shook device
                @Override
                public void onSensorChanged(SensorEvent event) {
                    // ensure that other dialogs are not displayed
                    if (!dialogOnScreen) {
                        // get x, y, and z values for the SensorEvent
                        float x = event.values[0];
                        float y = event.values[1];
                        float z = event.values[2];

                        // save previous acceleration value
                        lastAcceleration = currentAcceleration;

                        // calculate the current acceleration
                        currentAcceleration = x * x + y * y + z * z;

                        // calculate the change in acceleration
                        acceleration = currentAcceleration *
                                (currentAcceleration - lastAcceleration);

                        //TODO: make sure this part of the code works
                        // if the acceleration is above a certain threshold play the current pokemon cry
                        if (acceleration > ACCELERATION_THRESHOLD) {
                            mediaPlayer.create(getContext(), cry);
                            mediaPlayer.start();
                        }
                    }
                }

                // required method of interface SensorEventListener
                @Override
                public void onAccuracyChanged(Sensor sensor, int accuracy) {}
            };

    // displays the fragment's menu items
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.petview_fragment_menu, menu);
    }

    // handle choice from options menu
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // switch based on the MenuItem id
        switch (item.getItemId()) {
            case R.id.sprite:
                //swap sprite mode
                spriteMode = !spriteMode;
                setSprite(pokeId);
                if (spriteMode) {
                    petFrame.setBackgroundResource(R.drawable.pokebackground);
                    Toast.makeText(getContext(), R.string.toast_color_sprite_toggle, Toast.LENGTH_SHORT).show();
                }
                else {
                    petFrame.setBackgroundResource(R.drawable.pokebackgroundgrey);
                    Toast.makeText(getContext(), R.string.toast_color_sprite_toggle, Toast.LENGTH_SHORT).show();
                    animatePokemon();
                }
                return true;
            case R.id.pokemon:
                PokemonSelectFragment pokemonFragment = new PokemonSelectFragment();
                pokemonFragment.show(getFragmentManager(), "pokemon dialog");
                return true;
            case R.id.color:
                ColorDialogFragment colorDialog = new ColorDialogFragment();
                colorDialog.show(getFragmentManager(), "color dialog");
                return true; // consume the menu event
        }

        return super.onOptionsItemSelected(item);
    }

    // returns the DoodleView
    public PetView getPetView() {
        return petView;
    }

    // indicates whether a dialog is displayed
    public void setDialogOnScreen(boolean visible) {
        dialogOnScreen = visible;
    }

    //IMPORTANT: ANY time a new id is set, the sprites will change
    public void setPokeId(int id){
        pokeId = id;
        setSprite(pokeId);
    }

    public void animatePokemon(){
        pokeImg.startAnimation(shakeAnimation);
    }

    public int getPokeId() {
        return pokeId;
    }

    public void setSprite(int id) {
        switch (id) {
            case 1:
                if (spriteMode) pokeImg.setImageResource(R.drawable.aabcolor);
                else pokeImg.setImageResource(R.drawable.aabgrey);
                cry = R.raw.cry001;
                break;
            case 4:
                if (spriteMode) pokeImg.setImageResource(R.drawable.aaecolor);
                else pokeImg.setImageResource(R.drawable.aaegrey);
                cry = R.raw.cry004;
                break;
            case 7:
                if (spriteMode) pokeImg.setImageResource(R.drawable.aahcolor);
                else pokeImg.setImageResource(R.drawable.aahgrey);
                cry = R.raw.cry007;
                break;
            case 25:
                if (spriteMode) pokeImg.setImageResource(R.drawable.abfcolor);
                else pokeImg.setImageResource(R.drawable.abfgrey);
                cry = R.raw.cry025;
                break;
        }
    }
}