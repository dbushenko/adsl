package {{namespace}}.controllers;

import {{namespace}}.models.{{name}}Model;
import {{namespace}}.views.{{name}}View;

public class {{name}}Controller {
    private {{name}}Model model;

    public void loadData({{name}}View view) {
	if (model.loadData(view)) {
	    model.updateAll();
	}
    }
    
    public {{name}}Controller({{name}}Model model) {
	this.model = model;
    }

   {{&buttons_code}}
}
