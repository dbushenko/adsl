package com.adsl.demo.views;

import android.os.Bundle;
import android.view.View;
import android.app.Activity;
import java.util.Observable;
import java.util.Observer;
import android.widget.*;

import com.adsl.demo.R;
import com.adsl.demo.controllers.MyDemoActivityController;
import com.adsl.demo.models.MyDemoActivityModel;

public class MyDemoActivityView extends Activity implements Observer {
    // Members
    private Button _helloWorldBtn;
    private Button _demoBtn;

    private MyDemoActivityController controller;

    private void initialize() {
	// Initialize
	_helloWorldBtn = (Button) findViewById(R.id.helloWorldBtn);
	_demoBtn = (Button) findViewById(R.id.demoBtn);

    	View.OnClickListener listener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
		    if (v.equals(_helloWorldBtn)) {
			controller.helloWorldBtnActionPerformed(MyDemoActivityView.this);
			return;
		    }
		    if (v.equals(_demoBtn)) {
			controller.demoBtnActionPerformed(MyDemoActivityView.this);
			return;
		    }
		}
	    };

	_helloWorldBtn.setOnClickListener(listener);
	_demoBtn.setOnClickListener(listener);
    }

    /** Called when the activity is first created. */
    @Override
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mydemoactivity);

	MyDemoActivityModel model = new MyDemoActivityModel();
	model.addObserver(this);

	controller = new MyDemoActivityController(model);

	initialize();
	controller.loadData(this);
    }

    public void update(Observable modelObj, Object arg) {
	initialize();
	MyDemoActivityModel model = (MyDemoActivityModel)modelObj;
    }

    // Accessors
    public Button helloWorldBtn() {
	return _helloWorldBtn;
    }
    
    public Button demoBtn() {
	return _demoBtn;
    }
    
}

