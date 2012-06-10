package {{namespace}}.models;

import java.util.Observable;
import {{namespace}}.views.{{name}}View;

public class {{name}}Model extends Observable {
    public void updateAll() {
	this.setChanged();
	this.notifyObservers();
    }

    public boolean loadData({{name}}View view) {
	// IMPLEMENT: Load data here.
	return true;
    }
}
