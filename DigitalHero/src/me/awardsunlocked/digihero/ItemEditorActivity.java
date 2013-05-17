package me.awardsunlocked.digihero;

import java.io.File;
import java.util.ArrayList;

import android.app.ListActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TwoLineListItem;

public class ItemEditorActivity extends ListActivity {
	private ArrayList<Item> items;
	private ArrayAdapter<Item> adapter;
	public static File items_dir;
	public static ItemEditorDialog currentDialog = null;

	/** Called when the activity is first created. */
	@SuppressWarnings("unchecked")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		items_dir = getDir("items", MODE_PRIVATE);
		this.items = (ArrayList<Item>) getLastNonConfigurationInstance();

		if (this.items == null) {
			this.items = new ArrayList<Item>();
		}
		
		this.adapter = new ArrayAdapter<Item>(this, android.R.layout.simple_list_item_2, items) {
			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				TwoLineListItem row;
				if (convertView == null) {
					LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
					row = (TwoLineListItem) inflater.inflate(android.R.layout.simple_list_item_2, null);
				} else {
					row = (TwoLineListItem) convertView;
				}
				row.setLongClickable(true);
				Item i = items.get(position);
				row.getText1().setText(i.getName()+ " Type: " + i.getType().toString());
				row.getText2().setText("A: +" + i.getAtk() + ", D: +" + i.getDef() + ", HP: +" + i.getHp());
				return row;
			}
		};

		this.setListAdapter(adapter);
		
		// Attempt to load the items
		if (this.items.size() == 0) {
			loadItems();
		}

		registerForContextMenu(getListView());
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
		if (v.getId() == getListView().getId()) {
			AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;

			menu.setHeaderTitle(this.items.get(info.position).getName());
			menu.add(Menu.NONE, 0, 0, "Edit");
			menu.add(Menu.NONE, 1, 1, "Delete");
			menu.add(Menu.NONE, 2, 2, "Make Current");
		}
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterView.AdapterContextMenuInfo info;
		try {
			info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
		} catch (ClassCastException e) {
			return false;
		}
		switch (item.getItemId()) {
			case 0:
				currentDialog = new ItemEditorDialog(this, this.items.get(info.position), info.position);
				currentDialog.show();
			break;
			case 1:
				deleteItems(info.position);
			break;
		}

		return true;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu_itemeditor, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.new_item:
				currentDialog = new ItemEditorDialog(this);
				currentDialog.show();
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public Object onRetainNonConfigurationInstance() {
		final ArrayList<Item> hlist = this.items;
		return hlist;
	}
	@Override
	public void onSaveInstanceState(Bundle savedInstanceState) {
		super.onSaveInstanceState(savedInstanceState);
		if (currentDialog != null) {
			Item i = currentDialog.extractItem();
			savedInstanceState.putBoolean("dialog", true);
			savedInstanceState.putInt("edit_position", currentDialog.getEditPosition());
			savedInstanceState.putInt("atk", i.getAtk());
			savedInstanceState.putInt("def", i.getDef());
			savedInstanceState.putInt("hp", i.getHp());
			savedInstanceState.putString("name", i.getName());
			savedInstanceState.putString("type", i.getType().toString());
		}
	}
	@Override
	public void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		if (savedInstanceState.getBoolean("dialog") == true) {
			Item i = new Item(savedInstanceState.getInt("atk"),
					savedInstanceState.getInt("def"),
					savedInstanceState.getInt("hp"),
					savedInstanceState.getString("name"),
					Item.TYPE.valueOf(savedInstanceState.getString("type")));
			int position = savedInstanceState.getInt("edit_position");
			currentDialog = new ItemEditorDialog(this, i, position);
			currentDialog.show();
		}
	}

	@Override
	public void onPause() {
		super.onPause();
		saveItems(0);
	}

	// Add an item (called from outside class)
	public void addItem(Item i) {
		if (i != null) {
			this.items.add(i);
			this.adapter.notifyDataSetChanged();
		}
	}
	
	public void editItem(Item i, int position) {
		if (position == -1) {
			addItem(i);
			return;
		}
		if (i != null) {
			this.items.get(position).delete();
			this.items.set(position, i);
			this.adapter.notifyDataSetChanged();
		}
	}
	
	// Save items (use position 0 to save all items)
	public void saveItems(int position) {
		if (position == 0) {
			for (Item i : this.items) {
				i.save();
			}
		} else {
			this.items.get(position).save();
		}
	}
	
	// Load all items
	public void loadItems() {
		if (items_dir.isDirectory()) {
			Log.i("Digital Hero", "Loading items from: " + items_dir.getAbsolutePath());
			for (String name : items_dir.list()) {
				addItem(Item.load(name));
			}
		} else {
			Log.i("Digital Hero", "Failed loading items from: " + items_dir.getAbsolutePath());
		}
	}

	// Delete items (use position 0 to delete all items)
	public void deleteItems(int position) {
		if (position == 0) {
			Log.i("Digital Hero", "Deleting items from: " + items_dir.getAbsolutePath());
			for (Item i : this.items) {
				i.delete();
			}
			this.items.clear();
		} else {
			this.items.get(position).delete();
			this.items.remove(position);
		}
		this.adapter.notifyDataSetChanged();
	}
}
