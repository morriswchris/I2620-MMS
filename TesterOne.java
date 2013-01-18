public class TesterOne{
  public static void main (String [] args){
    System.out.println("======Testing MMS=========\n\n");
    
    MMS myMMS = new MMS(100);
    myMMS.alloc("pgmA", 50);
    myMMS.alloc("pgmB", 20);
    myMMS.alloc("pgmA", 10);
    myMMS.free("pgmB");
    myMMS.alloc("pgmD", 41);
    myMMS.printFree();
    myMMS.printAlloc();
    
  }
}