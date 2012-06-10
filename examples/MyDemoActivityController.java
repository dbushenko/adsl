package com.adsl.demo.controllers;

import com.adsl.demo.models.MyDemoActivityModel;
import com.adsl.demo.views.MyDemoActivityView;

public class MyDemoActivityController {
    private MyDemoActivityModel model;

    public void loadData(MyDemoActivityView view) {
	if (model.loadData(view)) {
	    model.updateAll();
	}
    }
    
    public MyDemoActivityController(MyDemoActivityModel model) {
	this.model = model;
    }

   
   public void helloWorldBtnActionPerformed(MyDemoActivityView view) {
       Object hello = "Hello";
Object world = "world";
System.out.println(hello + " " + world);

   }

   public void demoBtnActionPerformed(MyDemoActivityView view) {
       startActivity(new android.content.Intent(this, OrderScreen.class));

   }

}
