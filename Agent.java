public class Agent extends Thread{

    int agent_number;
    
    public Agent(int n){
        this.agent_number = n;
        System.out.println("Agent " + n + " created");
    }

    @Override
    public void run(){
        while(true){
            try{

                DMV.agent_queue_occupied.acquire();
                DMV.mutex3.acquire();
                // int customer_number = DMV.agent_queue.remove();

                DMV.agent_free[agent_number].acquire();
                //System.out.println("---- AGENT " + agent_number + " IS FREE ----");
                // DMV.agent_queue_occupied.acquire();
                // //DMV.mutex3.acquire();
                // int customer_number = DMV.agent_queue.remove();

                int customer_number = DMV.agent_queue.remove();
                System.out.println("Agent " + this.agent_number + " is serving customer " + customer_number);
                //DMV.agent_customer_relationship.add(customer_number, agent_number);
                DMV.agent_customer_relationship[customer_number] = agent_number;
                DMV.served[customer_number].release();

                DMV.ready_for_exam[customer_number].acquire();
                System.out.println("Agent " + this.agent_number + " asks customer " + customer_number 
                + " to take photo and eye exam");
                DMV.take_exam[customer_number].release();
                DMV.exam_done[customer_number].acquire();
                System.out.println("Agent " + agent_number + " gives license to customer " + customer_number);
                DMV.license_given[customer_number].release();
                DMV.agent_free[agent_number].release();
                DMV.mutex3.release();

            }
            catch(Exception e){
                System.out.println("Exception in Agent.java: " + e);
                e.printStackTrace();
            }
        }
    }

}
