import java.util.*;
import java.util.concurrent.Semaphore;

public class DMV {

    public static int CURR_INFO_DESK_NUM = -1;
    public static int count = -1;

    public static Semaphore max_cap_of_info_desk_line = new Semaphore(20, true);
    public static Semaphore max_cap_of_agent_line = new Semaphore(4, true);
    public static Semaphore mutex1 = new Semaphore(1, true);
    public static Semaphore mutex2 = new Semaphore(1, true);
    public static Semaphore mutex3 = new Semaphore(1, true);
    public static Semaphore mutex4 = new Semaphore(1, true);
    public static Semaphore info_desk = new Semaphore(1, true);
    public static Semaphore cust_ready_for_info_number = new Semaphore(0, true);
    public static Semaphore cust_given_info_num = new Semaphore(0, true);
    public static Semaphore waiting_room_occupied = new Semaphore(0, true);
    public static Semaphore announcer_call = new Semaphore(0, true);
    public static Semaphore agent_queue_occupied = new Semaphore(0, true);

    public static Queue<Integer> GLOBAL_CUSTOMER = new LinkedList<>();
    public static Queue<Integer> info_desk_queue = new LinkedList<>(); //Maybe queue of Customer instead of integer?
    public static Queue<Integer> waiting_room_queue = new LinkedList<>();
    public static Queue<Integer> agent_queue = new LinkedList<>();

    //public static List<Integer> agent_customer_relationship = new ArrayList<>();
    public static int[] agent_customer_relationship = new int[20];
    public static Semaphore[] served = new Semaphore[20]; 
    public static Agent[] agent_list = new Agent[2];
    public static Semaphore[] agent_free = new Semaphore[2];
    public static Semaphore[] ready_for_exam = new Semaphore[20];
    public static Semaphore[] take_exam = new Semaphore[20];
    public static Semaphore[] exam_done = new Semaphore[20];
    public static Semaphore[] license_given = new Semaphore[20];

    public static void main(String args[]){

        Arrays.fill(served, new Semaphore(0, true));
        Arrays.fill(agent_free, new Semaphore(1, true));
        Arrays.fill(ready_for_exam, new Semaphore(0, true));
        Arrays.fill(take_exam, new Semaphore(0, true));
        Arrays.fill(exam_done, new Semaphore(0, true));
        Arrays.fill(license_given, new Semaphore(0, true));
        
        //Create 1 Information Desk Thread
        InfoDesk myInfoDesk = new InfoDesk();
        myInfoDesk.setDaemon(true);
        myInfoDesk.start();

        //Create 1 Announcer Thread
        Announcer myAnnouncer = new Announcer();
        myAnnouncer.setDaemon(true);
        myAnnouncer.start();

        //Create 2 Agent Threads
        for(int i=0; i<2; i++){
            agent_list[i] = new Agent(i);
            agent_list[i].setDaemon(true);
            agent_list[i].start();
        }

        //Create 20 Customer Threads
        Customer[] customer_array = new Customer[20];
        for(int i=0; i<20; i++){
            Customer myCust = new Customer();
            customer_array[i] = myCust;
            myCust.start();
        }

        try{
            for(int i=0; i<20; i++){
                customer_array[i].join();
                System.out.println("Customer " + customer_array[i].getNum() + " was joined");
            }
        }
        catch(Exception e){
            System.out.println("Exception in DMV.java: " + e);
        }

        System.out.println("Done");
        
    }


}
