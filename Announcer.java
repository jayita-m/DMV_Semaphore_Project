public class Announcer extends Thread{

    int call_number = 0;
    
    public Announcer(){
        System.out.println("Announcer created");
    }

    @Override
    public void run(){
        while(true){
            try{
                DMV.waiting_room_occupied.acquire();
                System.out.println("Announcer calls number " + call_number);
                call_number++;
                DMV.announcer_call.release();
            }
            catch (Exception e){
                System.out.println("Exception in Announcer.java: " + e);
            }
        }
        
    }


}
