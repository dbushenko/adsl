package {{namespace}}.views;

import android.os.Bundle;
import android.view.View;
import android.app.Activity;
import java.util.Observable;
import java.util.Observer;
import android.widget.*;

import {{namespace}}.R;
import {{namespace}}.controllers.{{name}}Controller;
import {{namespace}}.models.{{name}}Model;

public class {{name}}View extends Activity implements Observer {
    // Members
    {{#ids}}
    private {{type}} _{{id}};
    {{/ids}}

    private {{name}}Controller controller;

    private void initialize() {
	// Initialize
	{{#ids}}
	_{{id}} = ({{type}}) findViewById(R.id.{{id}});
        {{/ids}}


    	View.OnClickListener listener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
		    {{#buttons}}
		    if (v.equals(_{{id}})) {
			controller.{{id}}ActionPerformed({{name}}View.this);
			return;
		    }
		    {{/buttons}}
		}
	    };

	{{#buttons}}
	_{{id}}.setOnClickListener(listener);
	{{/buttons}}
    }

    /** Called when the activity is first created. */
    @Override
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.{{resource}});

	{{name}}Model model = new {{name}}Model();
	model.addObserver(this);

	controller = new {{name}}Controller(model);

	initialize();
	controller.loadData(this);
    }

    public void update(Observable modelObj, Object arg) {
	initialize();
	{{name}}Model model = ({{name}}Model)modelObj;
    }

    // Accessors
    {{#ids}}
    public {{type}} {{id}}() {
	return _{{id}};
    }
    
    {{/ids}}
}

