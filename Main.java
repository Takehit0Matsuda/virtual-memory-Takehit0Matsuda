import javax.swing.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws FileNotFoundException {
        Memory memory = new Memory(); // instantiate memory with 30 page frames

        // idk how many process will appear so just guess there are 10 process
        // each page table has 64 page table entries
        PageTable[] processTables = new PageTable[10];

        int currentProcess = 0; // used as an index of process table
        int accesses = 0; // stores how many access happened
        int hits = 0; // stores how many hit happened
        int misses = 0; // stores how many miss happened
        int compulsoryMisses = 0; //stores how many compulsory miss happened

        // open file
        JFileChooser fileChooser = new JFileChooser();
        int result = fileChooser.showOpenDialog(null);
        if (result == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();

            // scan text file
            Scanner scanner = new Scanner(file);
            while (scanner.hasNext()) {     // loop from head to tail
                String command = scanner.next();
                if (command.equals("new")) {    // create new page table
                    currentProcess = scanner.nextInt();
                    processTables[currentProcess] = new PageTable();

                } else if (command.equals("switch")) {  // switch which page table to use
                    currentProcess = scanner.nextInt();

                } else if (command.equals("access")) {  // try to access to the memory with pte
                    int address = scanner.nextInt();
                    int pageIndex = address >> 10;   // slide 10 bits right so first 6 bits will remain
                    System.out.println("process" + currentProcess);
                    System.out.println(address +  " : " + pageIndex);

                    // refer process table of current process then see its page table entry with pageIndex
                    PageTableEntry pte = processTables[currentProcess].getPageTableEntry(pageIndex);
                    if (pte.location == 999){
                        System.out.println("pte " + pageIndex +  " [Valid:" + pte.valid + "; Memory:" + pte.inMemory + "; Location: BLANK]");
                    }else{
                        System.out.println("pte " + pageIndex +  " [Valid:" + pte.valid + "; Memory:" + pte.inMemory + "; Location:" + pte.location + "]");
                    }


                    // count up number of time memory was accessed
                    accesses++;

                    if (!pte.isValid()) {   // page fault
                        System.out.println("Page Fault");
                        // count up number of time to access to a page frame that never used
                        compulsoryMisses++;
                        // count up number of time to access to a page not in memory
                        misses++;

                        int pageFrame = memory.getFreePageFrame();   // find a free page frame from memory
                        if (pageFrame == -1) {  // if there is no free page frame in memory
                            System.out.println("No free page frame in memory");
                            pageFrame = memory.findSwapPage(); // find page frame index that stores the oldest page in memory
                            PageTableEntry oldPte = memory.getPageFrame(pageFrame); //get info of pte for the page frame
                            oldPte.kickOut(); // page out
                            System.out.println("the oldest page stored in pageframe:" + pageFrame + "(pte:" + oldPte.pageIndex + ") is kicked out");
                        }
                        pte.swapToMemory(pageFrame); // update pte [Memory -> 1; Location -> pageFrame]
                        memory.setFrame(pageFrame, pte); // page in
                        System.out.println("page stored to pageframe:" + pageFrame + " in memory");
                        pte.setValid(true); //update pte [Valid -> 1]
                        System.out.println("Updated pte " + pageIndex +  " [Valid:" + pte.valid + "; Memory:" + pte.inMemory + "; Location:" + pte.location + "]");


                    } else if (!pte.isInMemory()) { //page not in memory
                        // count up number of time to access to a page not in memory
                        misses++;

                        int pageFrame = memory.getFreePageFrame();
                        if (pageFrame == -1) {
                            System.out.println("No free page frame in memory");
                            pageFrame = memory.findSwapPage();
                            PageTableEntry oldPte = memory.getPageFrame(pageFrame);
                            oldPte.kickOut();   // page out
                            System.out.println("the oldest page stored in pageframe:" + pageFrame + "(pte:" + oldPte.pageIndex + ") is kicked out");
                        }
                        pte.swapToMemory(pageFrame);
                        memory.setFrame(pageFrame, pte);
                        System.out.println("page stored to pageframe:" + pageFrame + " in memory");
                        System.out.println("Updated pte " + pageIndex +  " [Valid:" + pte.valid + "; Memory:" + pte.inMemory + "; Location:" + pte.location + "]");


                    } else {
                        System.out.println("Hit!");
                        // count up number of time to access to a page in memory
                        hits++;
                    }
                }
                System.out.println("");
            }

            // output results
            System.out.println("---------Result----------");
            System.out.println("Accesses: " + accesses);
            System.out.println("Hits: " + hits);
            System.out.println("Misses: " + misses);
            System.out.println("Compulsory Misses: " + compulsoryMisses);
        }
    }
}
