import java.util.*;

public class Page {

    public static Scanner scanner = new Scanner(System.in);

    public static int[][] frames;
    public static LinkedList<Integer> page_ref;
    public static int hit_count;
    public static int fault_count;

    // Initialize frame matrix and page references
    public static void initialize() {
        hit_count = fault_count = 0;
        page_ref = new LinkedList<>();

        System.out.print("Enter the number of pages in the reference string: ");
        int page_count = scanner.nextInt();
        for (int i = 0; i < page_count; i++) {
            System.out.print("Enter page number: ");
            int page = scanner.nextInt();
            page_ref.add(page);
        }

        System.out.print("Enter the number of frames: ");
        int frame_count = scanner.nextInt();
        frames = new int[frame_count][page_count];
        for (int i = 0; i < frame_count; i++) {
            Arrays.fill(frames[i], -1);
        }
    }

    // Display the results
    public static void displayResult() {
        for (int[] frame : frames) {
            for (int val : frame) {
                System.out.print(val + " ");
            }
            System.out.println();
        }
        System.out.println("Hits: " + hit_count);
        System.out.println("Faults: " + fault_count);
    }

    // FIFO Page Replacement Algorithm
    public static void fifo() {
        Queue<Integer> fifo_queue = new LinkedList<>();
        int page_index = 0;

        while (!page_ref.isEmpty()) {
            int page = page_ref.remove();
            boolean is_fault = true;
            boolean is_inserted = false;

            for (int i = 0; i < frames.length; i++) {
                if (frames[i][page_index] == page) {
                    hit_count++;
                    is_fault = false;
                    break;
                } else if (frames[i][page_index] == -1) {
                    frames[i][page_index] = page;
                    fault_count++;
                    is_inserted = true;
                    is_fault = true;
                    fifo_queue.add(page);
                    break;
                }
            }

            if (is_fault && !is_inserted) {
                int removed_page = fifo_queue.poll();
                fault_count++;
                fifo_queue.add(page);
                for (int i = 0; i < frames.length; i++) {
                    if (frames[i][page_index] == removed_page) {
                        frames[i][page_index] = page;
                        break;
                    }
                }
            }
            page_index++;
            // Copy current column to the next column if space remains
            if (page_index < frames[0].length) {
                for (int i = 0; i < frames.length; i++) {
                    frames[i][page_index] = frames[i][page_index - 1];
                }
            }
        }
    }

    // LRU Page Replacement Algorithm
    public static void lru() {
        Deque<Integer> recent_pages = new LinkedList<>();
        int page_index = 0;

        while (!page_ref.isEmpty()) {
            int page = page_ref.remove();
            boolean is_fault = true;
            boolean is_inserted = false;

            for (int i = 0; i < frames.length; i++) {
                if (frames[i][page_index] == page) {
                    hit_count++;
                    is_fault = false;
                    recent_pages.remove(page);
                    recent_pages.addLast(page);
                    break;
                } else if (frames[i][page_index] == -1) {
                    frames[i][page_index] = page;
                    fault_count++;
                    is_inserted = true;
                    is_fault = true;
                    recent_pages.addLast(page);
                    break;
                }
            }

            if (is_fault && !is_inserted) {
                int lru_page = recent_pages.removeFirst();
                fault_count++;
                recent_pages.addLast(page);
                for (int i = 0; i < frames.length; i++) {
                    if (frames[i][page_index] == lru_page) {
                        frames[i][page_index] = page;
                        break;
                    }
                }
            }

            // Copy current column to the next column if space remains
            if (++page_index < frames[0].length) {
                for (int i = 0; i < frames.length; i++) {
                    frames[i][page_index] = frames[i][page_index - 1];
                }
            }
        }
    }

    public static void opt() {
        int page_index = 0;

        while (!page_ref.isEmpty()) {
            int page = page_ref.removeFirst();
            boolean is_fault = true;
            boolean is_inserted = false;

            // Check if page is already in a frame
            for (int i = 0; i < frames.length; i++) {
                if (frames[i][page_index] == page) {
                    hit_count++;
                    is_fault = false;
                    break;
                } else if (frames[i][page_index] == -1) {
                    // Insert page if there is an empty frame
                    frames[i][page_index] = page;
                    fault_count++;
                    is_inserted = true;
                    is_fault = true;
                    break;
                }
            }

            if (is_fault && !is_inserted) {
                int farthest_index = -1;
                int replace_index = -1;

                // Loop to find the page that is used farthest in the future
                for (int i = 0; i < frames.length; i++) {
                    int current_page = frames[i][page_index];
                    int next_use_index = -1;

                    // Find the next use of current_page in the remaining page_ref
                    for (int j = 0; j < page_ref.size(); j++) {
                        if (page_ref.get(j) == current_page) {
                            next_use_index = j;
                            break;
                        }
                    }

                    // If current page is not used in the future, select it for replacement
                    if (next_use_index == -1) {
                        replace_index = i;
                        break;
                    }

                    // Otherwise, select the page with the farthest future use
                    if (next_use_index > farthest_index) {
                        farthest_index = next_use_index;
                        replace_index = i;
                    }
                }

                // Replace the page
                frames[replace_index][page_index] = page;
                fault_count++;
            }

            // Copy current column to the next column if space remains
            if (++page_index < frames[0].length) {
                for (int i = 0; i < frames.length; i++) {
                    frames[i][page_index] = frames[i][page_index - 1];
                }
            }
        }
    }

    public static void main(String[] args) {
        initialize();
        System.out.println("********** OPT **********");
        opt();
        displayResult();
    }
}
