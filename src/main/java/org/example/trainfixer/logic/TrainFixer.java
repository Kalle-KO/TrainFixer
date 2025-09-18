package org.example.trainfixer.logic;

import org.example.trainfixer.datastructures.LinkedList;
import org.example.trainfixer.datastructures.Node;
import org.example.trainfixer.domain.*;
import org.example.trainfixer.model.Train;

import java.util.ArrayList;
import java.util.List;

public class TrainFixer {

    public void fix(Train train) {
        // 1) Første pass: bucket vogne + tælle
        List<Wagon> locos = new ArrayList<>();
        List<Wagon> seats = new ArrayList<>();
        List<Wagon> sleepers = new ArrayList<>();
        List<Wagon> dinings = new ArrayList<>();
        List<Wagon> cargos = new ArrayList<>();

        Node<Wagon> cur = train.wagons().head();
        int n = 0;
        while (cur != null) {
            Wagon w = cur.getValue();
            if (w instanceof Locomotive) locos.add(w);
            else if (w instanceof SeatCar) seats.add(w);
            else if (w instanceof SleeperCar) sleepers.add(w);
            else if (w instanceof DiningCar) dinings.add(w);
            else if (w instanceof CargoCar) cargos.add(w);
            n++;
            cur = cur.getNext();
        }

        // 2) Lokomotiv-krav (<=10: 1 forrest; >10: 2 – forrest + bagerst)
        int neededLocos = (n <= 10) ? 1 : 2;
        if (locos.size() < neededLocos) {
            // tilføj "manglende" lokomotiver
            while (locos.size() < neededLocos) locos.add(new Locomotive());
        } else if (locos.size() > neededLocos) {
            // fjern overskydende
            while (locos.size() > neededLocos) locos.remove(locos.size() - 1);
        } // :contentReference[oaicite:9]{index=9}

        // 3) Spisevogn-krav: Hvis der er siddevogne og ingen spisevogn -> tilføj 1
        if (!seats.isEmpty() && dinings.isEmpty()) {
            dinings.add(new DiningCar());
        } // :contentReference[oaicite:10]{index=10}

        // 4) Rekonstruktion (én ny liste i korrekt rækkefølge)
        // Passagerblok: Seats + Dining + Sleepers (sleepers samlet)
        // Placér Dining så Seats kan nå den uden at krydse Sleepers: sæt Dining foran Sleepers.
        LinkedList<Wagon> rebuilt = new LinkedList<>();

        // forreste lokomotiv (hvis krævet)
        if (neededLocos >= 1) rebuilt.addLast(locos.get(0));

        // passagerblok
        for (Wagon s : seats) rebuilt.addLast(s);
        for (Wagon d : dinings) rebuilt.addLast(d);
        for (Wagon s : sleepers) rebuilt.addLast(s);

        // gods
        for (Wagon c : cargos) rebuilt.addLast(c);

        // bageste lokomotiv (hvis krævet == 2)
        if (neededLocos == 2) rebuilt.addLast(locos.get(1));

        // 5) Erstat togets liste
        train.wagons().replaceWith(rebuilt);
    }

    /**
     * Kompleksitet:
     * - Tid: O(n). Ét pass for at bucket’e (trin 1) + ét pass for at bygge ny liste (trin 4).
     * - Plads: O(n) til midlertidige buckets og den nye liste.
     * - Gennemløb: 2 fulde gennemløb af toget i værste fald.
     */
}
