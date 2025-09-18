package org.example.trainfixer.logic;

import org.example.trainfixer.datastructures.LinkedList;
import org.example.trainfixer.datastructures.Node;
import org.example.trainfixer.domain.CargoCar;
import org.example.trainfixer.domain.DiningCar;
import org.example.trainfixer.domain.Locomotive;
import org.example.trainfixer.domain.SeatCar;
import org.example.trainfixer.domain.SleeperCar;
import org.example.trainfixer.domain.Wagon;
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
        while (cur != null) {
            Wagon w = cur.getValue();
            if (w instanceof Locomotive)       locos.add(w);
            else if (w instanceof SeatCar)     seats.add(w);
            else if (w instanceof SleeperCar)  sleepers.add(w);
            else if (w instanceof DiningCar)   dinings.add(w);
            else if (w instanceof CargoCar)    cargos.add(w);
            cur = cur.getNext();
        }

        // 2) Bestem behov ift. den ENDELIGE plan (inkl. evt. ny dining)
        boolean addDining = !seats.isEmpty() && dinings.isEmpty();
        int nonLoco = seats.size() + sleepers.size() + cargos.size() + dinings.size() + (addDining ? 1 : 0);

        // total = nonLoco + neededLocos; regler: total <= 10 => 1 loko, ellers 2
        int neededLocos = (nonLoco <= 9) ? 1 : 2;

        // Synk antal lokomotiver til præcis neededLocos
        while (locos.size() < neededLocos) locos.add(new Locomotive());
        while (locos.size() > neededLocos) locos.remove(locos.size() - 1);

        // Tilføj manglende dining hvis påkrævet
        if (addDining) dinings.add(new DiningCar());

        // 3) Rekonstruktion i korrekt rækkefølge
        LinkedList<Wagon> rebuilt = new LinkedList<>();

        // forreste lokomotiv (hvis krævet)
        if (neededLocos >= 1) rebuilt.addLast(locos.get(0));

        // passagerblok: seats -> dinings -> sleepers (sleepers ender i én blok)
        for (Wagon s : seats)   rebuilt.addLast(s);
        for (Wagon d : dinings) rebuilt.addLast(d);
        for (Wagon s : sleepers)rebuilt.addLast(s);

        // gods bagest (før evt. bageste lokomotiv)
        for (Wagon c : cargos)  rebuilt.addLast(c);

        // bageste lokomotiv (hvis krævet == 2)
        if (neededLocos == 2) rebuilt.addLast(locos.get(1));

        // 4) Erstat togets liste
        train.wagons().replaceWith(rebuilt);

        // 5) Tæl fulde gennemløb (1 pass for bucketting + 1 samlet pass for opbygning)
        train.setFullPasses(2);
    }
}
