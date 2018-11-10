import java.util.*;

public class Race {
   //Declaring an array of RacerThreads
   public static RacerThread[] racers;
   public static JudgeThread j;
   public static int nRacer;
   public static String[] forest = new String[350];
   public static Random chr = new Random();
   public static Queue<Thread> mountainPriority = new LinkedList<>();
   
   public static void main(String[] args){
      long time = System.currentTimeMillis(); 
      //Checks if the user put his own number of racers, if not then its 10
      if(args.length == 1){
         if(Integer.parseInt(args[0]) > 0)
            nRacer = Integer.parseInt(args[0]);    
         else   
            throw new IllegalArgumentException("You must enter a proper number of racers");
      }
      else
         nRacer = 10; //by default its 10
     
      //Generate a random forest 
      
      forest = createForest();
      
      //instantiate array of racers
      racers = new RacerThread[nRacer];
      
      //create judge thread
      j = new JudgeThread(racers, nRacer);
      j.start();
      
      //create each racer
      for(int i = 0; i < nRacer; i++){
         racers[i] = new RacerThread("Racer" + (i+1),forest, nRacer, j, i+1);
         racers[i].start();
      }
      
   }
   //Creates a forest of words
   private static String[] createForest() {
      char[] letters = {'a','b','c','d'};
      String Tree = "";
      for(int i = 0; i <350; i++){
         for(int j = 0; j < letters.length; j++){
            Tree += letters[chr.nextInt(letters.length)];
         }
         forest[i] = Tree;
         Tree= "";
      }
      return forest;
   }
   
   //Sends the racer home if its friend has went home already
   public static void goHome(int i){
         //If we are on the last racer just send him home, else wait for friend
         if(i == nRacer){
            return;
         }
         else if(racers[i].isAlive()){
            try {
               racers[i].join();
            } catch (InterruptedException e) {
               e.printStackTrace();
            }
         }
   }
}   
