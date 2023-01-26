package it.ispw.booknook.logic;

import java.util.Iterator;
import java.util.List;
import java.util.Vector;

public abstract class Subject {
    private List<Observer> observers;

    protected Subject() {
        this((Observer) null);
    }

    protected Subject(Observer obs) {
        this(new Vector<>());
        if (obs != null)
            this.observers.add(obs);
    }

    protected Subject(List<Observer> list) {
        this.observers = list;
    }

    public void attach(Observer obs) {
        this.observers.add(obs);
    }

    public void detach(Observer obs) {
        this.observers.remove(obs);
    }

    //notifica tutti gli observers
    protected void notifyObservers() {
        if (observers != null) {
            Iterator<Observer> i = observers.iterator();
            while (i.hasNext()) {
                Observer obs = i.next();
                obs.update();
            }
        }
    }
}
