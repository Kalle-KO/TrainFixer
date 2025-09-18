package org.example.trainfixer.logic;

import org.example.trainfixer.domain.*;
import org.example.trainfixer.model.Train;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TrainValidatorTest {

    @Test
    void valid_small_train_loco_front_passenger_before_cargo() {
        Train t = new Train();
        t.add(new Locomotive());
        t.add(new SeatCar());
        t.add(new DiningCar());
        t.add(new SleeperCar());
        t.add(new CargoCar());

        assertTrue(new TrainValidator().isValid(t));
    }

    @Test
    void invalid_dining_blocked_by_sleeper() {
        Train t = new Train();
        t.add(new Locomotive());
        t.add(new SeatCar());
        t.add(new SleeperCar()); // blokker mellem seat og dining
        t.add(new DiningCar());

        assertFalse(new TrainValidator().isValid(t));
    }

    @Test
    void sleepers_must_be_contiguous_if_more_than_one() {
        Train t = new Train();
        t.add(new Locomotive());
        t.add(new SleeperCar());
        t.add(new SeatCar());
        t.add(new SleeperCar()); // split -> invalid
        assertFalse(new TrainValidator().isValid(t));
    }
}
