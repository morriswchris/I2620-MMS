/* tester to see if list reorders itself
 **/

public class TesterTwo{
  public static void main (String [] args){
    System.out.println("======Testing MMS=========\n\n");
    
    MMS myMMS = new MMS(100);
    myMMS.alloc("prgA", 25);
    myMMS.printAlloc();
     myMMS.printFree();
    myMMS.alloc("prgB", 25);
     myMMS.printAlloc();
     myMMS.printFree();
    myMMS.free("prgA");
     myMMS.printAlloc();
     myMMS.printFree();
    myMMS.alloc("prgC", 60);
    myMMS.printAlloc();
     myMMS.printFree();
    myMMS.alloc("nomem", 110);
     myMMS.printAlloc();
     myMMS.printFree();
     
  }
}