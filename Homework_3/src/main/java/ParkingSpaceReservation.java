import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;


public class ParkingSpaceReservation {

    private final Map<User, ArrayList<Registration>> repo;
    private static final int MAX_DURATION=14;
    private static final int MIN_DURATION=1;

    public ParkingSpaceReservation(Map<User, ArrayList<Registration>> repo){
        this.repo=repo;
    }
    public boolean addReservation(User user, LocalDateTime startDate, LocalDateTime endDate){
        //checks if the startDate is in the past
        if (startDate.isBefore(LocalDateTime.now())) {
            return false;
        }
        //checks if the endDate is after the startDate
        if (startDate.isAfter(endDate)) {
            return false;
        }
        //checks if the duration is valid
        long dayDifference = ChronoUnit.DAYS.between(startDate, endDate);
        if (dayDifference > MAX_DURATION || dayDifference < MIN_DURATION) {
            return false;
        }

        //Checks if the user has active reservation
        Optional<Registration> userRegistrations = getCurrentReservations(user);
        if (userRegistrations.isPresent()) {
            return false;
        }

        repo.putIfAbsent(user, new ArrayList<>(List.of()));
        ArrayList<Registration> currentList=repo.get(user);
        currentList.add(new Registration(startDate, endDate));
        repo.replace(user, currentList);
        return true;
    }
    public boolean deleteReservation(User user){
        //the user should have a registration to remove one
        if(!repo.containsKey(user))
            return false;

        Optional<Registration> currentRegistrations = getCurrentReservations(user);
        if(currentRegistrations.isEmpty())
           return false;

        ArrayList<Registration> currentList=repo.get(user);
        currentList.remove(currentList.size()-1);
        repo.replace(user, currentList);
        return true;
    }
    public boolean extendReservation(User user, int days){
        if(days<=MIN_DURATION || days>=MAX_DURATION)
            return false;
        Optional<Registration> currentRegistrations =getCurrentReservations(user);
        if(currentRegistrations.isEmpty())
            return false;

        Registration lastRegistration=currentRegistrations.get();
        long dayDifference = ChronoUnit.DAYS.between(lastRegistration.getStartDate(), lastRegistration.getEndDate());
        if(dayDifference+days>=MAX_DURATION)
            return false;

        ArrayList<Registration> currentList=repo.get(user);
        LocalDateTime oldDate=currentRegistrations.get().getEndDate();
        currentList.get(currentList.size()-1).setEndDate(oldDate.plusDays(days));
        repo.replace(user, currentList);
        return true;
    }
    public boolean transferToFriend(User giver, User receiver){
        Optional<Registration> giverRegistration =getCurrentReservations(giver);
        if(giverRegistration.isEmpty())
            return false;
        if(giver.equals(receiver))
            return false;
        Optional<Registration> receiverRegistration =getCurrentReservations(receiver);
        if(receiverRegistration.isPresent())
            return false;
        Registration registration=giverRegistration.get();
        //if the registration is an ongoing one
        if(registration.getStartDate().isBefore(LocalDateTime.now())){
            //end the current registration for the giver
            ArrayList<Registration> oldList=repo.get(giver);
            LocalDateTime oldEndDate=registration.getEndDate();
            oldList.set(oldList.size()-1,new Registration(registration.getStartDate(),LocalDateTime.now()));
            repo.replace(giver, oldList);
            //start the reservation for the receiver
            ArrayList<Registration> newList=repo.get(receiver);
            newList.add(new Registration(LocalDateTime.now(), oldEndDate));
            repo.replace(receiver, newList);
        }
        // if the reservation is for the future
        else{
            ArrayList<Registration> oldList=repo.get(giver);
            oldList.remove(oldList.size()-1);
            repo.replace(giver, oldList);

            ArrayList<Registration> newList=repo.get(receiver);
            newList.add(registration);
            repo.replace(receiver, newList);


        }
        return true;
    }

    public Optional<Registration> getCurrentReservations(User user){
        if(repo.get(user)==null)
            return Optional.empty();
        return repo.get(user).stream().filter(reg ->reg.getEndDate().isAfter(LocalDateTime.now())).findFirst();
    }
    public ArrayList<Registration> getUserRegistrations(User user){
        return repo.get(user);
    }
}
