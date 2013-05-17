package me.awardsunlocked.digihero;

import android.os.Bundle;
import android.app.TabActivity;
import android.content.Intent;
import android.view.Menu;
import android.widget.TabHost;

public class EditorActivity extends TabActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		TabHost tabHost = getTabHost();

		tabHost.addTab(tabHost.newTabSpec("tab1").setIndicator("Heroes")
				.setContent(new Intent(this, HeroEditorActivity.class)));
		
		tabHost.setCurrentTab(0);
		tabHost.addTab(tabHost.newTabSpec("tab2").setIndicator("Items")
				.setContent(new Intent(this, ItemEditorActivity.class)));
		//tabHost.addTab(tabHost.newTabSpec("tab3").setIndicator("Updates")
		//		.setContent(new Intent(this, DownloadList.class)));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.editor, menu);
		return true;
	}

}
