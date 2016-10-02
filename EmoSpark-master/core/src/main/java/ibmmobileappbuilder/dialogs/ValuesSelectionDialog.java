package ibmmobileappbuilder.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.SearchView;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import ibmmobileappbuilder.core.R;
import ibmmobileappbuilder.ds.Datasource;
import ibmmobileappbuilder.ds.Distinct;
import ibmmobileappbuilder.ds.SearchOptions;

/**
 * Selection dialog that takes unique values from a datasource column. Useful for implementing filters
 */
public class ValuesSelectionDialog extends SelectionDialog {
    private ArrayAdapter<String> adapter;
    private Distinct datasource;
    private String columnName;
    private ListView listView;
    private boolean isFiltering;
    private ArrayList<String> checkedValues;
    private boolean canceled;
    private SearchOptions searchOptions;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(
                getActivity(),
                // http://stackoverflow.com/a/28561766/3344594
                Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT ?
                        R.style.SelectionDialog :
                        R.style.SelectionDialog_PreL
        );

        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();

        canceled = false;

        // Set up the listview
        View view = inflater.inflate(R.layout.selection_dialog, null);
        listView = (ListView) view.findViewById(android.R.id.list);

        createAdapter();
        listView.setAdapter(adapter);
        listView.setChoiceMode(
                multipleChoice ? AbsListView.CHOICE_MODE_MULTIPLE : AbsListView.CHOICE_MODE_SINGLE
        );

        if (haveSearch) {
            SearchView searchView = (SearchView) view.findViewById(R.id.search);
            searchView.setVisibility(View.VISIBLE);

            searchView.setOnQueryTextListener(
                    new SearchView.OnQueryTextListener() {
                        @Override
                        public boolean onQueryTextSubmit(String s) {
                            if (!"".equals(s)) {
                                isFiltering = true;
                                adapter.getFilter().filter(s);

                                // clear previous selection
                                listView.clearChoices();
                                return true;
                            }
                            return false;
                        }

                        @Override
                        public boolean onQueryTextChange(String s) {
                            return false;
                        }
                    }
            );

            searchView.setOnCloseListener(
                    new SearchView.OnCloseListener() {
                        @Override
                        public boolean onClose() {
                            if (isFiltering) {
                                adapter.getFilter().filter(null);
                                isFiltering = false;
                                return true;
                            }
                            return false;
                        }
                    }
            );
        }

        // add an OK button and listener
        Button okBtn = (Button) view.findViewById(R.id.search_btn);
        okBtn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ValuesSelectionDialog.this.dismiss();
                    }
                }
        );

        if (getArguments() != null) {
            checkedValues = getArguments().getStringArrayList(INITIAL_VALUES);
        }
        builder.setTitle(title)
                .setView(view);

        if (datasource != null) {
            datasource.getUniqueValuesFor(columnName,
                    new Datasource.Listener<List<String>>() {
                        @Override
                        public void onSuccess(List<String> objects) {
                            adapter.clear();
                            adapter.addAll(new ArrayList<>(objects));
                        }

                        @Override
                        public void onFailure(Exception e) {
                            //TODO notify
//                            BusProvider.getInstance().post(new DatasourceFailureEvent());
                            ValuesSelectionDialog.this.dismiss();
                        }
                    }
            );
        }

        builder.setTitle(title);

        return builder.create();
    }

    public ValuesSelectionDialog setDatasource(Datasource datasource) {
        if (!(datasource instanceof Distinct)) {
            throw new IllegalArgumentException("Datasource must implement Distinct interface");
        }

        this.datasource = (Distinct) datasource;
        return this;
    }

    public ValuesSelectionDialog setSearchOptions(SearchOptions searchOptions) {
        this.searchOptions = searchOptions;
        return this;
    }

    public ValuesSelectionDialog setColumnName(String columnName) {
        this.columnName = columnName;
        return this;
    }

    private void createAdapter() {
        adapter = new ArrayAdapter<String>(
                getActivity(),
                multipleChoice ?
                        R.layout.dialog_item_multiple_choice :
                        R.layout.dialog_item_single_choice
        ) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                String value = adapter.getItem(position);
                if (checkedValues != null && checkedValues.contains(value)) {
                    listView.setItemChecked(position, true);
                }

                return super.getView(position, convertView, parent);
            }
        };

    }

    @Override
    public void onCancel(final DialogInterface dialog) {
        super.onCancel(dialog);
        canceled = true;
    }

    @Override
    public void onDismiss(final DialogInterface dialog) {
        if (!canceled && selectionListener != null) {
            SparseBooleanArray checked = listView.getCheckedItemPositions();
            ArrayList<String> res = new ArrayList<>(checked.size());
            for (int i = 0; i < checked.size(); i++) {
                if (checked.valueAt(i)) {
                    res.add(adapter.getItem(checked.keyAt(i)));
                }
            }

            // invoke callback
            selectionListener.onSelected(res);
        }

        super.onDismiss(dialog);
    }

}
