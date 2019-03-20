package rs.example.pokemonpettingapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.Toast;

public class PokemonSelectFragment extends DialogFragment {
    private RadioGroup radioGroup;
    private RadioButton bulbasaur;
    private RadioButton charmander;
    private RadioButton squirtle;
    private RadioButton pikachu;
    private int pokeId;

    // create an AlertDialog and return it
    @Override
    public Dialog onCreateDialog(Bundle bundle) {
        // create dialog
        AlertDialog.Builder builder =
                new AlertDialog.Builder(getActivity());
        final View pokemonDialogView = getActivity().getLayoutInflater().inflate(
                R.layout.fragment_pokemon_select, null);
        builder.setView(pokemonDialogView); // add GUI to dialog

        // set the AlertDialog's message
        builder.setTitle(R.string.title_pokemon_dialog);

        // use current drawing color to set SeekBar values
        final PetView petView = getPetViewFragment().getPetView();
        final MainActivityFragment fragment = getPetViewFragment();
        pokeId = fragment.getPokeId();

        radioGroup = (RadioGroup) pokemonDialogView.findViewById(
                R.id.pokemonGroup);
        bulbasaur = (RadioButton) pokemonDialogView.findViewById(
                R.id.radio_bulbasaur);
        View.OnClickListener bulbasaur_listener = new View.OnClickListener(){
            public void onClick(View v) {
                pokeId = 1;
            }
        };
        bulbasaur.setOnClickListener(bulbasaur_listener);
        charmander = (RadioButton) pokemonDialogView.findViewById(
                R.id.radio_charmander);
        View.OnClickListener charmander_listener = new View.OnClickListener(){
            public void onClick(View v) {
                pokeId = 4;
            }
        };
        charmander.setOnClickListener(charmander_listener);
        squirtle = (RadioButton) pokemonDialogView.findViewById(
                R.id.radio_squirtle);
        View.OnClickListener squirtle_listener = new View.OnClickListener(){
            public void onClick(View v) {
                pokeId = 7;
            }
        };
        squirtle.setOnClickListener(squirtle_listener);
        pikachu = (RadioButton) pokemonDialogView.findViewById(
                R.id.radio_pikachu);
        View.OnClickListener pikachu_listener = new View.OnClickListener(){
            public void onClick(View v) {
                pokeId = 25;
            }
        };
        pikachu.setOnClickListener(pikachu_listener);


        // add Set Pokemon Button
        builder.setPositiveButton(R.string.button_set_pokemon,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        fragment.setPokeId(pokeId);
                        switch (pokeId) {
                            case 1:
                                Toast.makeText(getContext(), "Pokemon changed to Bulbasaur", Toast.LENGTH_SHORT).show();
                                break;
                            case 4:
                                Toast.makeText(getContext(), "Pokemon changed to Charmander", Toast.LENGTH_SHORT).show();
                                break;
                            case 7:
                                Toast.makeText(getContext(), "Pokemon changed to Squirtle", Toast.LENGTH_SHORT).show();
                                break;
                            case 25:
                                Toast.makeText(getContext(), "Pokemon changed to Pikachu", Toast.LENGTH_SHORT).show();
                                break;
                        }
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
}
