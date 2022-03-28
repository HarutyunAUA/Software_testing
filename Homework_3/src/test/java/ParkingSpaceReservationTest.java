import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

public class ParkingSpaceReservationTest {

    private ParkingSpaceReservation ps;
    private ArrayList<User> users;
    private ArrayList<Registration> regs;
    private LocalDateTime now;
    @BeforeMethod
    public void fillData(){
        now=LocalDateTime.now();
        User user1=new User("Gary_Oldman", "Tinker");     // user with no history
        User user2=new User("Til_Schweiger", "Knocking"); //user with only past history
        User user3=new User("Christopher", "Nolan");      //user with present reservation
        User user4=new User("Steven", "Spielberg");       //user with only future reservation
        User user5=new User("Michael", "Scott");          // user with past and future reservations
        users=new ArrayList<>(List.of(user1, user2, user3, user4, user5));
        Registration reg1=new Registration(now.minusDays(5),  now.plusDays(5));
        Registration reg2=new Registration(now.minusYears(2), now.minusYears(2).plusDays(12));
        Registration reg3=new Registration(now.plusYears(2),  now.plusYears(2).plusDays(10));
        Registration reg4=new Registration(now.minusYears(2), now.minusYears(2).plusDays(10));
        Registration reg5=new Registration(now.plusYears(3), now.plusYears(3).plusDays(2));

        regs=new ArrayList<>(List.of(reg1, reg2, reg3, reg4, reg5));
        HashMap<User, ArrayList<Registration>> repo=new HashMap<>();
        repo.put(user1, new ArrayList<>(List.of()));
        repo.put(user2, new ArrayList<>(List.of(reg4)));
        repo.put(user3, new ArrayList<>(List.of(reg1)));
        repo.put(user4, new ArrayList<>(List.of(reg5)));
        repo.put(user5, new ArrayList<>(List.of(reg2, reg3)));
        ps=new ParkingSpaceReservation(repo);
    }
    @Test
    public void testAddReservation() {
        User user3=new User("Cillian", "Murphy");         //user not from the list
        User user6=new User("John", "Travolta");          //user not from the list


        SoftAssert softAssert=new SoftAssert();
        //reservations for the past
        softAssert.assertFalse(ps.addReservation(user6,now.minusDays(5), now.plusDays(5) ), "User cannot register for the past");
        softAssert.assertTrue(ps.addReservation(user6,now.plusMinutes(1), now.plusDays(5) ), "User can register for the present/future");

        //startDate is after the endDate
        softAssert.assertFalse(ps.addReservation(user6,now.plusDays(5), now.minusDays(5)), "Illegal Chronology");

        //Invalid durations
        softAssert.assertFalse(ps.addReservation(user6,now,now.plusHours(5)), "Duration is less than 1 day");
        softAssert.assertFalse(ps.addReservation(user6,now.plusDays(5),now.plusDays(25) ), "Duration is greater than 14 days");

        // user with active reservation for the present
        softAssert.assertFalse(ps.addReservation(users.get(2), now.plusDays(1), now.plusDays(3)), "User already has an active reservation for the present");
        softAssert.assertFalse(ps.addReservation(users.get(2), now.plusDays(1), now.plusDays(12)), "User already has an active reservation for the present");
        softAssert.assertFalse(ps.addReservation(users.get(2), now.plusDays(10),now.plusDays(15)), "User already has an active reservation for the present");
        // user with active reservation for the future
        softAssert.assertFalse(ps.addReservation(users.get(3), now.plusYears(1), now.plusYears(1).plusDays(3)), "User already has an active reservation for the future");
        softAssert.assertFalse(ps.addReservation(users.get(3), now.plusYears(2).minusDays(1), now.plusYears(2).plusDays(3)), "User already has an active reservation for the future");
        softAssert.assertFalse(ps.addReservation(users.get(3), now.plusYears(2).plusDays(2),  now.plusYears(2).plusDays(5)), "User already has an active reservation for the future");
        softAssert.assertFalse(ps.addReservation(users.get(3), now.plusYears(2).plusDays(7),  now.plusYears(2).plusDays(15)), "User already has an active reservation for the future");
        softAssert.assertFalse(ps.addReservation(users.get(3), now.plusYears(2).plusDays(12), now.plusYears(2).plusDays(15)), "User already has an active reservation for the future");

        // user with no reservations
        softAssert.assertTrue(ps.addReservation(users.get(0), now.plusDays(1), now.plusDays(3)), "User with no history");
        softAssert.assertTrue(ps.getUserRegistrations(users.get(0)).size()==1 && ps.getUserRegistrations(users.get(0)).contains(new Registration(now.plusDays(1), now.plusDays(3))),"User's reservation should be saved");
        // user with past reservations
        softAssert.assertTrue(ps.addReservation(users.get(1), now.plusDays(1), now.plusDays(3)), "User with a past history");
        softAssert.assertTrue(ps.getUserRegistrations(users.get(1)).contains(new Registration(now.plusDays(1), now.plusDays(3))),"User's reservation should be saved");

        //user not from the initial list
        softAssert.assertTrue(ps.addReservation(user3, now.plusYears(1), now.plusYears(1).plusDays(3)), "User not from the map");
        softAssert.assertTrue(ps.getUserRegistrations(user3).size()==1 && ps.getUserRegistrations(user3).contains(new Registration(now.plusYears(1),now.plusYears(1).plusDays(3))),"User's reservation should be saved");


        softAssert.assertAll();
    }

    @Test
    public void testDeleteReservation() {
        User user6=new User("John", "Travolta");          //user not from the list


        SoftAssert softAssert=new SoftAssert();
        //deleting for non-existing user
        softAssert.assertFalse(ps.deleteReservation(user6), "Non-existing user");
        //user with no history
        softAssert.assertFalse(ps.deleteReservation(users.get(0)), "User with no current reservation cannot delete");
        //user with only past history
        softAssert.assertFalse(ps.deleteReservation(users.get(1)), "User with no current reservation cannot delete");
        //user with current reservation
        softAssert.assertTrue(ps.deleteReservation(users.get(2)), "User with current reservation should be able to delete the reservation");
        softAssert.assertTrue(ps.getUserRegistrations(users.get(2)).size()==0, "User with current reservation should be able to delete the reservation");
        //user with only future reservation
        softAssert.assertTrue(ps.deleteReservation(users.get(3)), "User with future reservation should be able to delete the reservation");
        softAssert.assertTrue(ps.getUserRegistrations(users.get(3)).size()==0, "User with future reservation should be able to delete the reservation");
        //user with future and past reservations
        softAssert.assertTrue(ps.deleteReservation(users.get(4)), "User with future and past reservations should be able to delete the reservation");
        softAssert.assertTrue(ps.getUserRegistrations(users.get(4)).size()==1 && !ps.getUserRegistrations(users.get(4)).contains(regs.get(2)), "User with future and past reservations should be able to delete the reservation");

        softAssert.assertAll();
    }

    @Test
    public void testExtendReservation() {
        User user6=new User("John", "Travolta");          //user not from the list


        SoftAssert softAssert=new SoftAssert();
        //Invalid duration
        softAssert.assertFalse(ps.extendReservation(user6, 0), "Days cannot be less than 1");
        softAssert.assertFalse(ps.extendReservation(user6, 15), "Days cannot be greater than 14");

        //extending for non-existing user
        softAssert.assertFalse(ps.extendReservation(user6, 3), "Non-existing user cannot extend");
        //user with no history
        softAssert.assertFalse(ps.extendReservation(users.get(0), 4), "User with no current reservation cannot extend the reservation");
        //user with only past history
        softAssert.assertFalse(ps.extendReservation(users.get(1), 3), "User with no current reservation cannot extend the reservation");
        //user with current reservation but exceeding threshold after extending
        softAssert.assertFalse(ps.extendReservation(users.get(2), 7), "Reservation cannot be for more than 14 days");
        //user with future reservation but exceeding threshold after extending
        softAssert.assertFalse(ps.extendReservation(users.get(3), 13), "Reservation cannot be for more than 14 days");

        //user with current reservation
        Registration oldValueReg0=new Registration(regs.get(0).getStartDate(), regs.get(0).getEndDate());
        softAssert.assertTrue(ps.extendReservation(users.get(2), 2), "User with current reservation should be able to extend the reservation");
        softAssert.assertTrue(ps.getUserRegistrations(users.get(2)).size()==1 && ps.getUserRegistrations(users.get(2)).get(0).equals(new Registration(oldValueReg0.getStartDate(), oldValueReg0.getEndDate().plusDays(2))), "User with current reservation should be able to extend the reservation");

        //user with future reservation
        Registration oldValueReg4=new Registration(regs.get(4).getStartDate(), regs.get(4).getEndDate());
        softAssert.assertTrue(ps.extendReservation(users.get(3), 3), "User with future reservation should be able to extend the reservation");
        softAssert.assertTrue(ps.getUserRegistrations(users.get(3)).size()==1 && ps.getUserRegistrations(users.get(3)).contains(new Registration(oldValueReg4.getStartDate(), oldValueReg4.getEndDate().plusDays(3))), "User with future reservation should be able to extend the reservation");
        softAssert.assertAll();
    }

    @Test
    public void testTransferToFriend() {
        User user6=new User("John", "Travolta");          //user not from the list

        SoftAssert softAssert=new SoftAssert();
        //Giver is not a valid user
        softAssert.assertFalse(ps.transferToFriend(user6, users.get(0)), "The giver does not have an active reservation");
        //Giver does not have a history
        softAssert.assertFalse(ps.transferToFriend(users.get(0), users.get(1)), "The giver does not have an active reservation");
        //Giver has only past reservations
        softAssert.assertFalse(ps.transferToFriend(users.get(1), users.get(1)), "The giver does not have an active reservation");

        //Giver and receiver cannot be the same user
        softAssert.assertFalse(ps.transferToFriend(users.get(2), users.get(2)), "User cannot transfer the reservation to itself");
        //Receiver cannot have an active reservation
        softAssert.assertFalse(ps.transferToFriend(users.get(4), users.get(2)), "User already has an active reservation");
        softAssert.assertFalse(ps.transferToFriend(users.get(2), users.get(4)), "User already has a future reservation");

        //if the transferring reservation is an ongoing one
        Registration oldValue=new Registration(ps.getUserRegistrations(users.get(2)).get(0).getStartDate(),ps.getUserRegistrations(users.get(2)).get(0).getEndDate());
        softAssert.assertTrue(ps.transferToFriend(users.get(2), users.get(0)), "Transferring to the user with no history");
        Registration afterTransfer1= ps.getUserRegistrations(users.get(2)).get(0);
        Registration afterTransfer2= ps.getUserRegistrations(users.get(0)).get(0);
        //check if the new endDate for the giver is within 1 minute range of now
        softAssert.assertTrue(afterTransfer1.getEndDate().isBefore(now.plusMinutes(1)) && afterTransfer1.getEndDate().isAfter(now.minusMinutes(1)), "The current reservation should have the endDate of now for the giver");
        softAssert.assertEquals(afterTransfer1.getStartDate(), oldValue.getStartDate(), "The current reservation should have the same startDate for the giver");
        //check if the startDate for the receiver is within 1 minute range of now
        softAssert.assertTrue(afterTransfer2.getStartDate().isBefore(now.plusMinutes(1)) && afterTransfer2.getStartDate().isAfter(now.minusMinutes(1)), "The current reservation should have the startDate of now for the receiver");
        softAssert.assertEquals(afterTransfer2.getEndDate(), oldValue.getEndDate(), "The current reservation should have the old endDate for the receiver");

        //if the transferring reservation is a future one
        Registration oldValue1=new Registration(ps.getUserRegistrations(users.get(3)).get(0).getStartDate(),ps.getUserRegistrations(users.get(3)).get(0).getEndDate());
        softAssert.assertTrue(ps.transferToFriend(users.get(3), users.get(1)), "Transferring to the user with only past history");
        softAssert.assertTrue(ps.getUserRegistrations(users.get(3)).isEmpty(), "The reservation for the giver should be deleted");
        softAssert.assertTrue(ps.getUserRegistrations(users.get(1)).get(1).equals(oldValue1), "The reservation for the receiver should be added");

        softAssert.assertAll();
    }

    @Test
    public void testGetUserRegistrations() {
        SoftAssert softAssert=new SoftAssert();
        softAssert.assertEquals(ps.getUserRegistrations(users.get(0)), new ArrayList<>(), "User with no history");
        softAssert.assertEquals(ps.getUserRegistrations(users.get(1)), new ArrayList<>(List.of(regs.get(3))), "User with only past history");
        softAssert.assertEquals(ps.getUserRegistrations(users.get(2)), new ArrayList<>(List.of(regs.get(0))), "User with present history");
        softAssert.assertEquals(ps.getUserRegistrations(users.get(3)), new ArrayList<>(List.of(regs.get(4))), "User with future reservation");
        softAssert.assertEquals(ps.getUserRegistrations(users.get(4)), new ArrayList<>(List.of(regs.get(1),regs.get(2))), "User with present and future history");
        softAssert.assertEquals(ps.getUserRegistrations(new User("a","s")), null, "User not from the list");

        softAssert.assertAll();
    }

    @Test
    public void testGetCurrentReservations() {
        SoftAssert softAssert=new SoftAssert();
        softAssert.assertEquals(ps.getCurrentReservations(users.get(0)), Optional.empty(), "User with no history");
        softAssert.assertEquals(ps.getCurrentReservations(users.get(1)), Optional.empty(), "User with only past history");
        softAssert.assertEquals(ps.getCurrentReservations(users.get(2)), Optional.of(regs.get(0)), "User with present history");
        softAssert.assertEquals(ps.getCurrentReservations(users.get(3)), Optional.of(regs.get(4)), "User with future reservation");
        softAssert.assertEquals(ps.getCurrentReservations(users.get(4)), Optional.of(regs.get(2)), "User with past and future history");
        softAssert.assertEquals(ps.getCurrentReservations(new User("a","s")), Optional.empty(), "User not from the list");

        softAssert.assertAll();
    }
}