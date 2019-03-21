package rs.example.pokemonpettingapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;
import android.view.animation.Animation;


public class MainActivityFragment extends Fragment {
    private PetView petView; // handles touch events and draws
    private boolean dialogOnScreen = false;
    private int pokeId = 25; //corresponds to pokedex number
    private boolean spriteMode = true; //true = color, false = greyscale
    private Animation shakeAnimation;
    FrameLayout petFrame;
    int cry = R.raw.cry001; //change this cry programmatically based on current pokemon
    ImageView pokeImg;



    // called when Fragment's view needs to be created
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view =
                inflater.inflate(R.layout.fragment_main, container, false);

        setHasOptionsMenu(true); // this fragment has menu items to display

        // get reference to the DoodleView
        petView = (PetView) view.findViewById(R.id.petView);

        pokeImg = (ImageView) view.findViewById(R.id.pokeImageView);

        petFrame = (FrameLayout) view.findViewById(R.id.petFrame);


        //pokeImg.startAnimation(shakeAnimation);

        return view;
    }

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
                    Toast.makeText(getContext(), R.string.toast_grayscale_sprite_toggle, Toast.LENGTH_SHORT).show();
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