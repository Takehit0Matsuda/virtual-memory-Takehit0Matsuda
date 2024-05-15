class PageTable {

    PageTableEntry[] entries = new PageTableEntry[64]; // 0-63

    PageTable() {
        for (int i = 0; i < 64; i++) {
            entries[i] = new PageTableEntry();
            entries[i].pageIndex = i;
        }
    }

    void setPageTableEntry(int pt, PageTableEntry pte) {
        entries[pt] = pte;
    }

    PageTableEntry getPageTableEntry(int page) {
        return entries[page];
    }
}