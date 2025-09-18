package org.example.trainfixer.logic;

import org.example.trainfixer.datastructures.Node;
import org.example.trainfixer.domain.*;
import org.example.trainfixer.model.Train;

import java.util.ArrayList;
import java.util.List;

public class TrainValidator {

    public boolean isValid(Train train) {
        int n = train.size();
        if (n == 0) return false;

        // Lineært scan: gem typer i liste for nemt opslag
        List<Class<? extends Wagon>> types = new ArrayList<>(n);
        Node<Wagon> cur = train.wagons().head();
        while (cur != null) {
            types.add(cur.getValue().getClass());
            cur = cur.getNext();
        }

        // Lokomotiv-regler (<=10: kun forrest; >10: forrest og bagerst – kun der)
        if (n <= 10) {
            if (types.get(0) != Locomotive.class) return false;
            for (int i = 1; i < n; i++) if (types.get(i) == Locomotive.class) return false;
        } else {
            if (types.get(0) != Locomotive.class || types.get(n - 1) != Locomotive.class) return false;
            for (int i = 1; i < n - 1; i++) if (types.get(i) == Locomotive.class) return false;
        } // :contentReference[oaicite:4]{index=4}

        // Passager (Seat/Sleeper/Dining) før Cargo
        boolean seenCargo = false;
        for (Class<?> c : types) {
            if (c == CargoCar.class) seenCargo = true;
            if (seenCargo && (c == SeatCar.class || c == SleeperCar.class || c == DiningCar.class))
                return false;
        } // :contentReference[oaicite:5]{index=5}

        // Sleeper contiguous hvis >1
        int firstSleep = -1, lastSleep = -1, sleepCount = 0;
        for (int i = 0; i < n; i++) {
            if (types.get(i) == SleeperCar.class) {
                sleepCount++;
                if (firstSleep == -1) firstSleep = i;
                lastSleep = i;
            }
        }
        if (sleepCount > 1) {
            for (int i = firstSleep; i <= lastSleep; i++) {
                if (types.get(i) != SleeperCar.class) return false;
            }
        } // :contentReference[oaicite:6]{index=6}

        // Alle SeatCar skal kunne nå DiningCar uden at krydse SleeperCar
        int diningIdx = -1;
        for (int i = 0; i < n; i++) if (types.get(i) == DiningCar.class) { diningIdx = i; break; }
        if (diningIdx != -1) {
            for (int i = 0; i < n; i++) {
                if (types.get(i) == SeatCar.class) {
                    int lo = Math.min(i, diningIdx), hi = Math.max(i, diningIdx);
                    for (int j = lo + 1; j < hi; j++) if (types.get(j) == SleeperCar.class) return false;
                }
            }
        } // :contentReference[oaicite:7]{index=7}

        return true;
    }
}
