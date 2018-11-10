import java.util.*;

public class RacerThread extends Thread {
   private int racerID;
   private String magicWord;
   public boolean ready = false;
   public static int numOfR, racersAtRiver, racersFinished;
   private JudgeThread judge;
   private Random num = new Random();
   private String[] forest;
   public static Queue<RacerThread> mPriority = new LinkedList<>();
   public static long time = System.currentTimeMillis(); 
   public long startRace, endRace, forestStart, forestEnd, mountainStart, mountainEnd,
               riverStart, riverEnd;
   //Constructors
   public RacerThread(){
      
   }
   public RacerThread(String tName, String[] forest, int nRacer, JudgeThread j,int i){
      super(tName); 
      this.forest = forest;
      numOfR = nRacer;
      judge = j;
      racerID = i;
   }
   
   public void run(){
      //Rest and food break before forest
      msg("is resting before he enters the forest");
      restNFood();
      
      //Racer goes through forest
      forestStart = System.currentTimeMillis()-time;
      msg("Entering forest");
      searchForest(forest);
      msg("Stopped rushing");
      currentThread().setPriority(5);
      forestEnd = System.currentTimeMillis()-time;
      
      //rest before mountain, give other racers time to catch up
      restNFood();
      
      //Racer goes through mountain
      mountainStart = System.currentTimeMillis()-time;
      msg("HAS arrived at the mountain passage");
      mPriority.add(this);
      try {
         enteringMountain();
         mountainEnd = System.currentTimeMillis()-time;
         //another racer has arrived at the river so increment
         racersAtRiver++;
         //rest for a long time before the river
         sleep(2000000);
      } catch (InterruptedException e) {
         msg("had its sleep interrupted");
      }
      
      //if the judge hasn't finished his job of interrupting, busy wait
      //until he interrupts everybody
      while(judge.isAlive()){
         restNFood();
      }
      
      //Racer crosses river
      riverStart = System.currentTimeMillis()-time;
      msg("IS crossing river");
      restNFood(); //crossing of the river simulated by sleeping for a random time
      msg("HAS crossed river");
      riverEnd = System.currentTimeMillis()-time;
      racersFinished++;
      
      //while all the racers haven't finished, busy wait until they all finish
      while(racersFinished != numOfR){
         try {
            sleep(20);
         } catch (InterruptedException e) {
            e.printStackTrace();
         }
      }
      
      //Send racer home
      Race.goHome(racerID);
      msg("went HOME");
      
      endRace = System.currentTimeMillis()-time;
      
      //Judge reports everything about the racers
      judge.timeOfRace(this,endRace);
      judge.timeOfObstacle(this,"forest", forestStart, forestEnd);
      judge.timeOfObstacle(this,"mountain", mountainStart, mountainEnd);
      judge.timeOfObstacle(this,"river", riverStart , riverEnd);
      
   }
  
   private void searchForest(String[] f) {
      //racer tries to rush
      currentThread().setPriority(currentThread().getPriority() +num.nextInt(5));
      msg("is looking for map with magic word");
      
      //creates magic word for racer
      String magicWord = "";
      char[] letters = {'a','b','c','d'};
      for(int j = 0; j < letters.length; j++){
         magicWord += letters[num.nextInt(letters.length)];
      }
      //sees if racer finds magic word
      for(int i = 0; i < f.length; i++){
         if(magicWord.equals(f[i])){
            msg("Found map of magic word");
            return;
         }
      }
      msg("Did not find the Map, Has to yield");
      yield();
      yield();
      
   }
   //The racers are in front of the mountain
   private void enteringMountain() throws InterruptedException {
      //If the last racer has arrived, set ready = true for the first racer
      if(mPriority.size() == numOfR)
         mPriority.remove().ready = true;
      while(ready == false){
         sleep(20);
      }// as long as there are still racers, ready the next racer
      if(!mPriority.isEmpty()){
         mPriority.remove().ready = true;
      }
      msg("has crossed the passage");
      restNFood(); //sleep to let other racers go
      
   }
   //method to rest for a random time
   public void restNFood(){
      try { 
         sleep(num.nextInt((4000-2000)+1) + 2000);
      } catch (InterruptedException e) {
         e.printStackTrace();
      }
   }        
   
   public void msg(String m) {
      System.out.println("["+(System.currentTimeMillis()
            -time)+"] "+getName()+": "+m);
   }
   
}
