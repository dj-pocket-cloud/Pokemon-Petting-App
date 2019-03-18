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
import android.widget.SeekBar;

public class PokemonSelectFragment extends DialogFragment {
    private RadioButton bulbasaur;
    private RadioButton charmander;
    private RadioButton squirtle;
    private RadioButton pikachu;

    // create an AlertDialog and return it
    @Override
    public Dialog onCreateDialog(Bundle bundle) {
        // create dialog
        AlertDialog.Builder builder =
                new AlertDialog.Builder(getActivity());
        View pokemonDialogView = getActivity().getLayoutInflater().inflate(
                R.layout.fragment_pokemon_select, null);
        builder.setView(pokemonDialogView); // add GUI to dialog

        // set the AlertDialog's message
        builder.setTitle(R.string.title_pokemon_dialog);
        
        bulbasaur = (RadioButton) pokemonDialogView.findViewById(
                R.id.radio_bulbasaur);
        charmander = (RadioButton) pokemonDialogView.findViewById(
                R.id.radio_charmander);
        squirtle = (RadioButton) pokemonDialogView.findViewById(
                R.id.radio_squirtle);
        pikachu = (RadioButton) pokemonDialogView.findViewById(
                R.id.radio_pikachu);

        // use current drawing color to set SeekBar values
        final PetView petView = getPetViewFragment().getPetView();

        // add Set Pokemon Button
        builder.setPositiveButton(R.string.button_set_pokemon,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        petView.setPokemon(); //TODO: change parameter once this method is implmemented
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
