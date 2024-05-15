class PageTableEntry {
    boolean valid = false;
    boolean inMemory = false;
    int location = 999;
    int pageIndex;

    int getLocation() {
        return location;
    }

    boolean isInMemory() {
        return inMemory;
    }

    void setValid(boolean isValid) {
        valid = isValid;
    }

    boolean isValid() {
        return valid;
    }

    void kickOut() {
        inMemory = false;
    }

    void swapToMemory(int memFrame) {
        inMemory = true;
        location = memFrame;
    }
}
