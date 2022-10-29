public class Customer extends Thread{
    
    int custnr;

    public Customer(){}

    @Override
    public void run(){
        try{
            DMV.max_cap_of_info_desk_line.acquire();
            //DMV.mutex2.acquire();
            DMV.count++;
            //System.out.println("Count incremented, now: " + DMV.count);
            custnr = DMV.count;
            //System.out.println("Custnr: " + custnr);
            DMV.mutex1.acquire();
            enqueue_info_desk(custnr);
            DMV.mutex1.release();
            //DMV.mutex2.release();

            DMV.info_desk.acquire();
            DMV.cust_ready_for_info_number.release();
            DMV.cust_given_info_num.acquire();
            int num = DMV.CURR_INFO_DESK_NUM;
            int curr_cust = DMV.info_desk_queue.remove();
            System.out.println("Customer " + curr_cust + " gets number " + num + ", enters waiting room");
            enqueue_waiting_room(curr_cust);
            DMV.info_desk.release();

            DMV.announcer_call.acquire();
            DMV.mutex2.acquire();
            int cust_moving_to_agent_line = DMV.waiting_room_queue.remove();
            System.out.println("Customer " + cust_moving_to_agent_line + " moves to agent line");
            enqueue_agent_line(cust_moving_to_agent_line);
            DMV.mutex2.release();

            DMV.served[custnr].acquire();
            System.out.println("Customer " + custnr + " is being served by agent " + DMV.agent_customer_relationship[custnr]);
            DMV.ready_for_exam[custnr].release();
            DMV.take_exam[custnr].acquire();
            System.out.println("Customer " + custnr + " completes photo and eye exam for agent " + DMV.agent_customer_relationship[custnr]);
            DMV.exam_done[custnr].release();
            DMV.license_given[custnr].acquire();
            System.out.println("Customer " + custnr + " gets license and departs");
            //DMV.agent_free[DMV.agent_customer_relationship[custnr]].release();
            

        }

        catch(Exception e){
            System.out.println("Exception in Customer.java: " + e + ", custnr = " + custnr);
        }

    }

    public void enqueue_info_desk(int custnr){
        DMV.info_desk_queue.add(custnr);
        System.out.println("Customer " + custnr + " created, enters DMV");
        //System.out.println("Queue: " + DMV.info_desk_queue);
    }

    public void enqueue_waiting_room(int custnr){
        DMV.waiting_room_queue.add(custnr);
        DMV.waiting_room_occupied.release();
    }

    public void enqueue_agent_line(int custnr){
        DMV.agent_queue.add(custnr);
        DMV.agent_queue_occupied.release();
    }

    public int getNum(){
        return custnr;
    }

}
