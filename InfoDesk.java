public class InfoDesk extends Thread{

    int info_number = 0;
    
    public InfoDesk(){
        System.out.println("Information desk created");
    }

    @Override
    public void run(){

        while(true){
            try{
                DMV.cust_ready_for_info_number.acquire();
                DMV.CURR_INFO_DESK_NUM = info_number;
                info_number++;
                //System.out.println("Yuh");
                //System.out.println("Incremented CURR_INFO_DESK_NUM, new value = " + DMV.CURR_INFO_DESK_NUM);
                DMV.cust_given_info_num.release();
            }
            catch(Exception e){
                System.out.println("Exception in InfoDesk.java: " + e);
            }
        }


    }
}
