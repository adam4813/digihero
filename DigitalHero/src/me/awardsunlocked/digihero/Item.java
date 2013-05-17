package me.awardsunlocked.digihero;
import java.io.*;

import android.util.Log;

public class Item implements Serializable
{
	private static final long serialVersionUID = 1L;
	private int atk, def, hp;
	private String name;
	private TYPE type;
	public static enum TYPE {ARMOR, WEAPON, ACCESSORY};
	
	public Item(int atk, int def, int hp, String name, TYPE type) {
		this.atk = atk;
		this.def = def;
		this.hp = hp;
		this.name = name;
		this.type = type;
	}
	
	public int getAtk() {
		return atk;
	}
	public void setAtk(int atk) {
		this.atk = atk;
	}
	public int getDef() {
		return def;
	}
	public void setDef(int def) {
		this.def = def;
	}
	public int getHp() {
		return hp;
	}
	public void setHp(int hp) {
		this.hp = hp;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public TYPE getType() {
		return type;
	}

	public void setType(TYPE type) {
		this.type = type;
	}
	
	public void save() {
		try {
			File f = new File(ItemEditorActivity.items_dir, this.getName());
			Log.i("Digital Hero", "Write: " + f.getAbsolutePath());
			FileOutputStream outfile = new FileOutputStream(f);
			ObjectOutputStream os = new ObjectOutputStream(outfile);
			os.writeObject(this);
			os.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	public static Item load(String name) {
		Item i = null;
		try {
			File f = new File(ItemEditorActivity.items_dir, name);
			Log.i("Digital Hero", "Read: " + f.getAbsolutePath());
			FileInputStream infile;
			infile = new FileInputStream(f);
			ObjectInputStream is = new ObjectInputStream(infile);
			i = (Item) is.readObject();
			is.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return i;
	}
	
	public void delete() {
		File f = new File(ItemEditorActivity.items_dir, this.getName());
		f.delete();
		Log.i("Digital Hero", "Delete: " + f.getAbsolutePath());

	}
	
	public void showEditor() {
		
	}
}
