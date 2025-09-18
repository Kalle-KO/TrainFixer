package org.example.trainfixer.model;

import org.example.trainfixer.datastructures.LinkedList;
import org.example.trainfixer.domain.Wagon;

public class Train {
    private final LinkedList<Wagon> wagons = new LinkedList<>();

    public void add(Wagon w) { wagons.addLast(w); }
    public LinkedList<Wagon> wagons() { return wagons; }
    public int size() { return wagons.size(); }
}
