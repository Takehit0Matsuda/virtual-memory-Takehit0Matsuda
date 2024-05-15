class Memory {
    PageTableEntry[] frames = new PageTableEntry[30]; // 0-29
    int nextFrameToReplace = 0;

    Memory() {
        for (int i = 0; i < 30; i++) {
            frames[i] = null;
        }
    }

    void setFrame(int frame, PageTableEntry pte) {
        frames[frame] = pte;
    }

    PageTableEntry getPageFrame(int frame) {
        return frames[frame];
    }

    int getFreePageFrame() {
        for (int i = 0; i < 30; i++) {
            if (frames[i] == null) {
                return i;
            }
        }
        return -1;  //when there is no free page
    }

    int findSwapPage() {
        int frameToReplace = nextFrameToReplace;
        nextFrameToReplace = (nextFrameToReplace + 1) % 30; // make frame index for next time
        return frameToReplace;
    }
}