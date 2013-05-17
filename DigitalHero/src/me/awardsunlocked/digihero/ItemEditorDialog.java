package me.awardsunlocked.digihero;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

public class ItemEditorDialog {
	private AlertDialog dialog;
	private int editPosition = 0;
	
	public ItemEditorDialog(final ItemEditorActivity act) {
		this(act, null, -1);
    }

	public ItemEditorDialog(final ItemEditorActivity act, final Item i, final int position) {
		this.editPosition = position;
		
    	AlertDialog.Builder builder = new AlertDialog.Builder(act);
    	if (this.editPosition != -1) {
			builder.setTitle("Edit Item: " + i.getName());
		} else {
			builder.setTitle("New Item");
		}

		View v = act.getLayoutInflater().inflate(R.layout.dialog_newitem, null);

		Spinner t = (Spinner) v.findViewById(R.id.type);
		t.setAdapter(new ArrayAdapter<Item.TYPE>(act.getApplicationContext(), android.R.layout.simple_spinner_item, Item.TYPE.values()));
		
		if (i != null) {
			EditText e = (EditText) v.findViewById(R.id.atk);
			e.setText(String.valueOf(i.getAtk()));
			
			e = (EditText) v.findViewById(R.id.def);
			e.setText(String.valueOf(i.getDef()));
			
			e = (EditText) v.findViewById(R.id.hp);
			e.setText(String.valueOf(i.getHp()));
			
			e = (EditText) v.findViewById(R.id.name);
			e.setText(i.getName());
			t.setSelection(i.getType().ordinal(), true);
		}
		
		builder.setView(v);

		builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				if (editPosition != -1) {
					act.editItem(extractItem(), position);
				} else {
					act.addItem(extractItem());
				}
				ItemEditorActivity.currentDialog = null;
			}
		});
		builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				ItemEditorActivity.currentDialog = null;
			}
		});
		this.dialog = builder.create();
    }
	
	public Item extractItem() {
		int atk, def, hp;
		
		EditText e = (EditText) this.dialog.findViewById(R.id.name);
		String name = e.getText().toString();
		
		if (name.isEmpty()) {
			return null;
		}
		
		e = (EditText) this.dialog.findViewById(R.id.atk);
		atk = (e.getText().toString().isEmpty()) ? 0 : Integer.parseInt(e.getText().toString());
		
		e = (EditText) this.dialog.findViewById(R.id.def);
		def = (e.getText().toString().isEmpty()) ? 0 : Integer.parseInt(e.getText().toString());
		
		e = (EditText) this.dialog.findViewById(R.id.hp);
		hp = (e.getText().toString().isEmpty()) ? 0 : Integer.parseInt(e.getText().toString());
		
		Spinner t = (Spinner) this.dialog.findViewById(R.id.type);
		Item.TYPE type = (Item.TYPE)t.getSelectedItem();
		
		Item i = new Item(atk, def, hp, name, type);
		i.save();
		
		return i;
	}
	
	public void show() {
		this.dialog.show();
	}

	public int getEditPosition() {
		return editPosition;
	}
}
