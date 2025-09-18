package org.example.trainfixer.logic;

import org.example.trainfixer.domain.*;
import org.example.trainfixer.model.Train;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TrainFixerTest {

    @Test
    void fixer_makes_train_valid_and_positions_correctly() {
        Train t = new Train();
        // bevidst “dårligt” tog: gods midt i, flere locos, ingen dining
        t.add(new CargoCar());
        t.add(new Locomotive());
        t.add(new SeatCar());
        t.add(new SleeperCar());
        t.add(new CargoCar());
        t.add(new Locomotive());

        TrainFixer fixer = new TrainFixer();
        fixer.fix(t);

        assertTrue(new TrainValidator().isValid(t),
                "After fix, train should be valid according to all rules");
    }
}
