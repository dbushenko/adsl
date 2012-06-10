package {{namespace}}.widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.*;

import {{namespace}}.R;

public class {{name}}Widget extends LinearLayout {
    // Members
    {{#ids}}
    private {{type}} _{{id}};
    {{/ids}}


    public {{name}}Widget(Context context) {
	super(context);
	initComponents();
    }

    public {{name}}Widget(Context context, AttributeSet attrs) {
	super(context, attrs);
	initComponents();
    }

    public void initComponents() {
	String infService = Context.LAYOUT_INFLATER_SERVICE;
	LayoutInflater li = (LayoutInflater) getContext().getSystemService(
									   infService);
	li.inflate(R.layout.{{resource}}, this, true);

	// Initialize
	{{#ids}}
	_{{id}} = ({{type}}) findViewById(R.id.{{id}});
        {{/ids}}

    	View.OnClickListener listener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
		    {{#buttons}}
		    if (v.equals(_{{id}})) {
			{{id}}ActionPerformed();
			return;
		    }
		    {{/buttons}}
		}
	    };

	{{#buttons}}
	_{{id}}.setOnClickListener(listener);
	{{/buttons}}
    }

    {{#buttons}}
    public void {{id}}ActionPerformed() {
	// IMPLEMENT ME!
    }
    
    {{/buttons}}

    {{#ids}}
    public {{type}} {{id}}() {
	return _{{id}};
    }
    
    {{/ids}}

}
