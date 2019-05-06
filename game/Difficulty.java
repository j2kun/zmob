package game;

public enum Difficulty {
   EASY ("Easy", 10), 
   NORMAL ("Normal", 20), 
   HARD ("Hard", 30);
   
   private int factor;
   private String name;
   
   private Difficulty(String name, int factor){
      this.factor = factor;
      this.name = name;
   }
   
   public int getFactor(){
      return factor;
   }
   
   public String getName(){
      return name;
   }
}
