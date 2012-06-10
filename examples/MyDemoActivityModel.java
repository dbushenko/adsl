package com.adsl.demo.models;

import java.util.Observable;
import com.adsl.demo.views.MyDemoActivityView;

public class MyDemoActivityModel extends Observable {
    public void updateAll() {
	this.setChanged();
	this.notifyObservers();
    }

    public boolean loadData(MyDemoActivityView view) {
	// IMPLEMENT: Load data here.
	return true;
    }
}
