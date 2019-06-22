package building;

import driver.Configuration;
import gui.ElevatorDisplay;

import java.util.ArrayList;

public class Building {


    private static Building instance;
    private ArrayList<Person> allPendingPeople = new ArrayList<>();
    private ArrayList<Person> allPeopleOrdered = new ArrayList<>(); //this is just to keep the people output ordered for the printStats in driver

    // Step 1
    private Building() {
        ElevatorDisplay.getInstance().initialize(Configuration.NUMBER_FLOORS); //this displays the Building
        for (int i = 1; i <= Configuration.NUM_ELEV; i++) {
            ElevatorDisplay.getInstance().addElevator(i, 1); //initial floor of every elevator is always 1
        }
    }

    // Step 2 - Singleton Design, makes sure only one building
    public static Building getInstance() {

        if (instance == null) {
            instance = new Building();
        }
        return instance;

    }

    public void addPendingPerson(Person person) { //adds person to allPendingPeople ArrayList
        allPendingPeople.add(person);
    }


    public void addPersonOrdered(Person person) {
        allPeopleOrdered.add(person);
    }


    public ArrayList<Person> getAllPeopleList() { //gets all person ArrayList
        return allPendingPeople;
    }


    public ArrayList<Person> getAllPeopleOrdered() {
        return allPeopleOrdered;
    }

    public void removePerson(Person p) { //removes a Person from allPendingPeople ArrayList
        allPendingPeople.remove(p);
    }


}
