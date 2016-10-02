package ibmmobileappbuilder.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;

import java.util.Arrays;

import ibmmobileappbuilder.core.R;

public class ImagePickerOptionsDialog extends DialogFragment{
    boolean removeEnabled;
    private OnOptionSelectedListener mListener;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), getTheme());

        String[] actions = getResources().getStringArray(R.array.image_picker_actions);
        if(!getRemoveEnabled()){
            actions = Arrays.copyOf(actions, 2);
        }
        builder.setItems(actions, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                OnOptionSelectedListener listener = getListener();
                if(listener != null) {
                    switch (which) {
                        case 0:
                            listener.fromCamera();
                            break;
                        case 1:
                            listener.fromStorage();
                            break;
                        case 2:
                            listener.remove();
                            break;
                    }
                }

            }
        });

        return builder.create();
    }

    public void setRemoveEnabled(boolean re){
        this.removeEnabled = re;
    }

    public boolean getRemoveEnabled(){
        return removeEnabled;
    }

    public void setListener(OnOptionSelectedListener listener){
        mListener = listener;
    }

    public OnOptionSelectedListener getListener(){
        return mListener;
    }

    public interface OnOptionSelectedListener{
        void fromStorage();
        void fromCamera();
        void remove();
    }
}
