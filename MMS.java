/**
 * A Class which represents an implementation of the Memory Management System
 * 
 * Please fill out the following imarking information below:
 * 
 * Famly name : Morris
 * Given names : Christoher
 * Student #: 209142027
 * course: Itec 2620
 * Section : M
 **/
public class MMS{
  //method for creating a cell of memory
  private class ListNode{
    public String name;    
    public int start;
    public int size;
    public ListNode link;    
    public boolean allow = false;
    //method for creating a cell of free memory
    public ListNode (int aStart, int aSize){
      start = aStart;
      size = aSize;
    }
    //method for creating a cell of memory for a program
    public ListNode (String aProcess, int aStart, int aSize){
      this(aStart, aSize);
      name = aProcess;
    }
  }
  //Private Variable
  private int bytes;
  private int min;
  private String programName;
  private ListNode freelist, alloclist, temp, smallestFit; 
  
  //Memory Management System
  public MMS(int memorySize){
    alloclist = null;
    freelist = new ListNode(0, memorySize);
  }
  //methods used in running MMS
  //method alloc determines what process is being assigned to memory, and how to assign it
  public void alloc(String processName, int noBytes){
    if(noBytes <1)
      return;
    ListNode current = freelist;
      min = current.size;
      //if there is only one block of memory (meaning a new/defrag memory) no need to find smallest memory block
      if(current.link == null)
        runAlloc(processName, noBytes);
      //else find smallest block of memory that program will fit into
        else{
      while(current.link != null){
        if(min > current.link.size)
          min = current.link.size;
        current = current.link;
      }
      
      current = freelist;
      while(current.size != min)
        current = current.link;
      
      smallestFit = current;
      
      //compares smallest node to bytes required, if node isn't big enough, find next smallest in freelist
      while(smallestFit.size < noBytes && smallestFit.allow == false){
        
        smallestFit.allow = true;
        current = freelist;
        min = 0;
        
        while(current.allow == true && current.link != null)
          current = current.link;
        
        if(current.allow == true)
          current = smallestFit;
        else if(current.allow == false && current.link != null || current.allow == false && current.link == null)
          min = current.size;
        
        while(current.link != null){
          if(current.link.allow == true)
            current = current.link;
          else if(min > current.link.size){
            min = current.link.size;
            current = current.link;
          }
          else 
            current = current.link;
        }
        
        if(min != 0){
          current = freelist;
          while(current.size != min || current.allow == true)
            current = current.link;
          
          smallestFit = current;
        }
      }
      
      //perform smallest fit on program
      if(smallestFit.size < noBytes){
        defrag();
        if(freelist.size < noBytes)
          errorNoMem(processName, noBytes);
        else
        runAlloc(processName, noBytes);
        }
      else
        runAlloc(smallestFit, processName, noBytes);
    }
  }
  //alloc methods
  //run alloc method when program goes to end of list
  private void runAlloc(String name, int size){
    
    ListNode current = alloclist;
    
    if(current == null && size <= freelist.size){
      alloclist = new ListNode(name, freelist.start, size);
      freelist.start = freelist.start + size;
      freelist.size = freelist.size - size;
    }
    else if(size <= freelist.size){
      current = alloclist;
      while(current != null && current.link != null)
        current = current.link;
      current.link = new ListNode(name, freelist.start, size);
      freelist.start = freelist.start + size;
      freelist.size = freelist.size - size;
    }
  }
  
  //run overloaded alloc method when smallest memory node can be found
  private void runAlloc(ListNode smallestNode, String name, int size){
    
    ListNode current = alloclist;
    while(current != null && current.link != null)
      current = current.link;
    int newAddress = 0;
    
    if(current != null){
      newAddress = current.start + current.size;
      current.link = new ListNode(name, newAddress, size);
      smallestNode.size = smallestNode.size - size;
    }
    else{
      alloclist = new ListNode(name, newAddress, size);
      smallestNode.size = smallestNode.size - size;
    }
  }
  //method of defragmenting memory nodes (garbage collection)
  private void defrag(){
    ListNode current = alloclist;
    int startNode = 0;
    
    if(current != null){
      current.start = 0;
      
      while(current.link != null){
        current.link.start = current.start + current.size;
        current = current.link;
      }
      startNode = current.start + current.size;
    }
    
    current = freelist;
    int totalSize = current.size;
    
    while(current.link != null){
      totalSize = totalSize + current.link.size;
      current = current.link;
    }
    
    freelist = new ListNode(startNode, totalSize);
  }
  //error methods
  //displays error when process requires more memory than available
  private void errorNoMem(String aProcess, int aSize){
    System.out.println("**There is insufficient memory for :**\nProcess: \t" + "Size: \t");
    System.out.println("  " + aProcess + "\t" + "  " + aSize + "\t");
  }
    
  //method for freeing memory
  public void free(String aName){
    ListNode current = alloclist;
    boolean exist = false;
    
    while(current != null){
      if(aName.equals(current.name))
        exist = true;
      
      current = current.link;
    }
    
    if(exist){
      current = alloclist;
      while (current != null && aName.equals(current.name)){
        alloclist = alloclist.link;
        current.link = freelist;
        freelist = current;
        current = alloclist;
      }
      while (current != null){
        if(aName.equals(current.name)){
          temp = alloclist;
          
          while(temp.link != current)
            temp = temp.link;
          temp.link = current.link;
          current.link = freelist;
          freelist = current;
          current = temp.link;
        }
        else 
          current = current.link;
      }
    }
  }
  //Method for printing all processes in Memory and where they are located
  
  public void printAlloc(){
    ListNode current = alloclist;
    System.out.println("\nPrograms currently in Memory: ");
    System.out.println("-----------------------------------------------");
    System.out.println("Process: \t" + " Start: \t" + " Size: \t");
    while (current != null){
      
      System.out.println("  "+current.name + "\t" + "  "+current.start + "\t"+ "  "+current.size +"\t");
      current = current.link;
    }
    System.out.println("-----------------------------------------------");
  }
  
  //method for printing current free memory blocks in memory
  public void printFree(){
    ListNode current = freelist;
    System.out.println("\nFree Memory Blocks: ");
    System.out.println("-----------------------------------------------");
    System.out.println("Start: \t" + " Size: \t");
    while(current != null){
      System.out.println("  "+current.start + "\t" + "  "+current.size+ "\t");
      current = current.link;
    }
    System.out.println("-----------------------------------------------");
  }
}