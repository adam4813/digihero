package me.awardsunlocked.digihero;

import android.app.*;
import android.content.*;
import android.os.*;
import android.view.*;
import android.widget.*;
import java.io.*;
import java.util.*;
import android.util.*;

public class HeroEditorActivity extends ListActivity {
	// private Hero current;
	private ArrayList<Hero> heroes;
	public static AlertDialog newHeroDlg;
	private ArrayAdapter<Hero> adapter;

	/** Called when the activity is first created. */
	@SuppressWarnings("unchecked")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		this.heroes = (ArrayList<Hero>) getLastNonConfigurationInstance();

		if (this.heroes == null) {
			try {
				File dir = getDir("heroes", MODE_PRIVATE);
				if (dir.isDirectory()) {
					Log.i("Digital Hero", dir.getAbsolutePath());
					this.heroes = new ArrayList<Hero>();
					for (String fname : dir.list()) {
						File f = new File(dir, fname);
						Log.i("Digital Hero", "Read: " + f.getAbsolutePath());
						FileInputStream infile = new FileInputStream(f);
						ObjectInputStream is = new ObjectInputStream(infile);
						Hero h = (Hero) is.readObject();
						this.heroes.add(h);
						is.close();
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			if (this.heroes == null) {
				this.heroes = new ArrayList<Hero>();
			}
		}

		final AlertDialog.Builder builder = new AlertDialog.Builder(this);

		builder.setTitle("New Hero");

		LayoutInflater inflater = this.getLayoutInflater();

		builder.setView(inflater.inflate(R.layout.dialog_newhero, null));

		builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				EditText e = (EditText) newHeroDlg.findViewById(R.id.atk);
				Integer atk = Integer.parseInt(e.getText().toString());
				e = (EditText) newHeroDlg.findViewById(R.id.def);
				Integer def = Integer.parseInt(e.getText().toString());
				e = (EditText) newHeroDlg.findViewById(R.id.hp);
				Integer hp = Integer.parseInt(e.getText().toString());
				e = (EditText) newHeroDlg.findViewById(R.id.name);
				String name = e.getText().toString();
				Hero h = new Hero(atk, def, hp, 0, name);
				heroes.add(h);
				adapter.notifyDataSetChanged();
				try {
					File dir = getDir("heroes", MODE_PRIVATE);
					File f = new File(dir, h.getName());
					Log.i("Digital Hero", "Write: " + f.getAbsolutePath());
					FileOutputStream outfile = new FileOutputStream(f);
					ObjectOutputStream os = new ObjectOutputStream(outfile);
					os.writeObject(h);
					os.close();
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		});
		builder.setNegativeButton("Cancel",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) { // Canceled.
					}
				});

		newHeroDlg = builder.create();

		this.adapter = new ArrayAdapter<Hero>(this,
				android.R.layout.simple_list_item_2, heroes) {
			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				TwoLineListItem row;
				if (convertView == null) {
					LayoutInflater inflater = (LayoutInflater) getApplicationContext()
							.getSystemService(LAYOUT_INFLATER_SERVICE);
					row = (TwoLineListItem) inflater.inflate(
							android.R.layout.simple_list_item_2, null);
				} else {
					row = (TwoLineListItem) convertView;
				}
				row.setLongClickable(true);
				Hero h = heroes.get(position);
				row.getText1().setText(
						h.getName() + " (" + h.getHp() + "/" + h.getMaxhp()
								+ ")");
				row.getText2().setText(
						"A: " + h.getAtk() + ", D: " + h.getDef());
				return row;
			}
		};

		this.setListAdapter(adapter);

		registerForContextMenu(getListView());
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenu.ContextMenuInfo menuInfo) {
		if (v.getId() == getListView().getId()) {
			AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;

			menu.setHeaderTitle(this.heroes.get(info.position).getName());
			menu.add(Menu.NONE, 0, 0, "Delete");
			menu.add(Menu.NONE, 1, 1, "Make Current");
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
		if (item.getItemId() == 0) {

			File dir = getDir("heroes", MODE_PRIVATE);
			File f = new File(dir, this.heroes.get(info.position).getName());
			f.delete();
			Log.i("Digital Hero", "Delete: " + f.getAbsolutePath());

			this.heroes.remove(info.position);
			this.adapter.notifyDataSetChanged();

		}

		return true;
	}

	@Override
	public Object onRetainNonConfigurationInstance() {
		final ArrayList<Hero> hlist = this.heroes;
		return hlist;
	}

	@Override
	public void onPause() {
		super.onPause();
		try {
			File dir = getDir("heroes", MODE_PRIVATE);
			for (Hero h : this.heroes) {
				File f = new File(dir, h.getName());
				Log.i("Digital Hero", "Write: " + f.getAbsolutePath());
				FileOutputStream outfile = new FileOutputStream(f);
				ObjectOutputStream os = new ObjectOutputStream(outfile);
				os.writeObject(h);
				os.close();
			}

			/*
			 * FileOutputStream outfile = this.openFileOutput("heroes.hero",
			 * this.MODE_PRIVATE); ObjectOutputStream os= new
			 * ObjectOutputStream(outfile); os.writeObject(this.heroes);
			 * os.close();
			 */
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu_heroeditor, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) { // Handle item
															// selection
		switch (item.getItemId()) {
		case R.id.new_hero:
			NewHero();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	public void NewHero() {
		newHeroDlg.show();
	}
}
