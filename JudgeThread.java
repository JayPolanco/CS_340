
public class JudgeThread extends Thread {
   private RacerThread[] racers;
   private int numOfRacers;
   public static long time = System.currentTimeMillis();         
   
   public JudgeThread(){}
   
   public JudgeThread(RacerThread[] racers, int numR) {
      super("Judge");
      this.racers = racers;
      numOfRacers = numR;
   }
   
   public void run(){
      //if the last racer hasn't started the race and if not all are at the river
      //busy wait
      while(racers[numOfRacers-1] == null ||  RacerThread.racersAtRiver != numOfRacers){
         try {
            sleep(20);
         } catch (InterruptedException e) {
            e.printStackTrace();
         }
      } 
      //interrupt the racers during their long sleep
      for(int i = 0; i < racers.length; i++){
            racers[i].interrupt();  
      }

      
   }
   //Calculates the entire time the racer took to finish race
   public void timeOfRace(RacerThread r,long finished){
      msg(r.getName() + " finished the entire race in " + finished + " milliseconds");
   }
   
   //Calculates the time it took between obstacles
   public void timeOfObstacle(RacerThread r,String obstacle, long started,long finished){
      msg(r.getName() + " finished the " + obstacle+ " in " + (finished - started) + " milliseconds");
   }

   public void msg(String m) {
      System.out.println("["+(System.currentTimeMillis()
            -time)+"] "+getName()+": "+m);
   } 
   
   
}
