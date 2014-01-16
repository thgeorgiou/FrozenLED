package com.sakisds.frozenled.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.SparseBooleanArray;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.sakisds.frozenled.R;
import com.sakisds.frozenled.userdata.DataManager;
import com.sakisds.frozenled.userdata.NotificationPref;

import java.util.List;

/**
 * Created by stratisg on 5/9/2013.
 */
public class DeletableListFragment extends ListFragment {
    List<NotificationPref> mData;

    public DeletableListFragment() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_list, container, false);
    }

    @Override
    public void onResume() {
        setAdapter();

        // Prepare listview
        getListView().setLongClickable(true);
        getListView().setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
        getListView().setMultiChoiceModeListener(new MultipleChoiceListener());
        super.onResume();
    }

    public void onListItemClick(ListView list, View view, int position, long id) {
        EditDialog dialog = new EditDialog();
        NotificationPref notif = mData.get(position);
        dialog.setSourceNotification(notif);
        dialog.show(getFragmentManager(), "tag");
    }

    public void setAdapter() {
        // Override to create a list adapter
    }

    public void clearSelection() {
        getListView().clearChoices();
    }

    private void askToDelete(SparseBooleanArray checked) {
        new AlertDialog.Builder(getActivity())
                .setMessage(R.string.dialog_sure)
                .setCancelable(true)
                .setPositiveButton(R.string.dialog_yes, new DialogOnClick(checked))
                .setNegativeButton(R.string.dialog_no, null)
                .show();
    }

    private class MultipleChoiceListener implements ListView.MultiChoiceModeListener {
        @Override
        public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {
            final int checkedCount = getListView().getCheckedItemCount();
            switch (checkedCount) {
                case 0:
                    mode.setTitle(null);
                    break;
                case 1:
                    mode.setTitle("One item selected");
                    break;
                default:
                    mode.setTitle("" + checkedCount + " items selected");
                    break;
            }
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            // Respond to clicks on the actions in the CAB
            switch (item.getItemId()) {
                case R.id.action_delete:
                    SparseBooleanArray checked = getListView().getCheckedItemPositions().clone();
                    askToDelete(checked);
                    mode.finish(); // Action picked, so close the CAB
                    return true;
                default:
                    return false;
            }
        }

        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            // Inflate the menu for the CAB
            MenuInflater inflater = mode.getMenuInflater();
            inflater.inflate(R.menu.context_delete, menu);
            return true;
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return true;
        }
    }

    private class DialogOnClick implements DialogInterface.OnClickListener {
        SparseBooleanArray mChecked;

        public DialogOnClick(SparseBooleanArray checked) {
            mChecked = checked;
        }

        @Override
        public void onClick(DialogInterface dialogInterface, int id) {
            ListView listView = getListView();
            SharedPreferences prefs = getActivity().getSharedPreferences(DataManager.SHAREDPREFS_NOTIFICATIONS, Context.MODE_PRIVATE);

            int cntChoice = listView.getCount();

            for (int i = 0; i < cntChoice; i++) {
                if (mChecked.get(i)) {
                    DataManager.removeNotification(prefs, mData.get(i));
                }
            }

            setAdapter();
        }
    }
}
