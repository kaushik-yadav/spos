import java.util.*;

class Process
{
    int id;
    int arrival_time;
    int burst_time;
    int priority;
    int completion_time;
    int turnaround_time;
    int waiting_time;
    int remaining_time;
    public Process(int id,int arrival_time,int burst_time,int priority){
    this.id=id;
    this.arrival_time=arrival_time;
    this.burst_time=burst_time;
    this.remaining_time=burst_time;
    this.priority=priority;
    }
}   

public class schedule {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        // Input the number of processes
        System.out.print("Enter number of processes: ");
        int n = sc.nextInt();
        
        Process[] process = new Process[n];
        for (int i = 0; i < n; i++) {
            System.out.println("Enter arrival time, burst time and priority (for Priority Scheduling) for Process " + (i + 1) + ": ");
            int arrivalTime = sc.nextInt();
            int burstTime = sc.nextInt();
            int priority = sc.nextInt();
            process[i] = new Process(i + 1, arrivalTime, burstTime, priority);
        }
        FCFS(process);
        SJF(process);
        Priority(process);
        System.out.print("Enter Quantum number : ");
        int quantum=sc.nextInt();
        RR(process,quantum);
        sc.close();
    }
    public static void FCFS(Process[] process){
        int current_time=0;
        Arrays.sort(process,Comparator.comparingInt(p->p.arrival_time));
        for (Process p:process){
            if (current_time<p.arrival_time){
                current_time=p.arrival_time;
            }   
            current_time+=p.burst_time;
            p.completion_time=current_time;
            p.turnaround_time=p.completion_time-p.arrival_time;
            p.waiting_time=p.turnaround_time-p.burst_time;
        }
        result(process);
    }
    public static void SJF(Process[] process){
        int current_time=0;
        int completed=0;
        Process current=null;
        Arrays.sort(process,Comparator.comparingInt( p-> p.arrival_time));
        PriorityQueue<Process> pq = new PriorityQueue<>(Comparator.comparingInt(p->p.remaining_time));

        while (completed<process.length){
            for(Process p:process){
                if(p.arrival_time<=current_time && p.remaining_time>0 && !pq.contains(p)){
                    pq.add(p);
                }
            if(!pq.isEmpty()){
                // processing it for 1 unit of time
                current=pq.poll();
                current.remaining_time--;
                current_time++;
                
                //process completed if not then add to pq
                if(current.remaining_time==0){
                    completed++;
                    current.completion_time=current_time;
                    current.turnaround_time=current.completion_time-current.arrival_time;
                    current.waiting_time=current.turnaround_time-current.burst_time;
                }
                else{
                    pq.add(current);
                }
            }
            else{
                current_time++;
            }
            }

        }
        result(process);

    }
    public static void Priority(Process[] process){
        int current_time=0;
        int completed=0;
        Process current=null;
        Arrays.sort(process,Comparator.comparingInt( p-> p.arrival_time));
        PriorityQueue<Process> pq = new PriorityQueue<>(Comparator.comparingInt(p->p.priority));

        while (completed<process.length){
            for(Process p:process){
                if(p.arrival_time<=current_time && p.remaining_time>0 && !pq.contains(p)){
                    pq.add(p);
                }
            if(!pq.isEmpty()){
                // if not empty and it is non premptive so process is completed here thus we add whole burst time in current time
                completed++;

                current=pq.poll();
                
                current_time+=current.burst_time;
                current.completion_time=current_time;
                current.turnaround_time=current.completion_time-current.arrival_time;
                current.waiting_time=current.turnaround_time-current.burst_time;
                current.remaining_time=0;
                //we have to clear the queue here as the whole process is completed
                pq.clear();
                }
            else{
                current_time++;
            }
            }

        }
        result(process);

    }
    public static void RR(Process[] process, int quantum) {
        int current_time = 0;
        int completed = 0;
        int i = 0;
        Arrays.sort(process, Comparator.comparingInt(p -> p.arrival_time));
        Queue<Process> q = new LinkedList<>();
    
        while (completed < process.length) {
            // Add all processes that have arrived by current time
            while (i < process.length && process[i].arrival_time <= current_time) {
                q.add(process[i]);
                i++;
            }
    
            if (!q.isEmpty()) {
                // Get the next process in the queue
                Process current = q.poll();
                int exec = Math.min(current.remaining_time, quantum); // Process for 'quantum' or remaining time
                for(int j=0;j<exec;j++){
                    current.remaining_time --;
                    current_time ++;
                    // add any new arrivals while things are -- or ++ by exec
                    while (i < process.length && process[i].arrival_time <= current_time) {
                        q.add(process[i]);
                        i++;
                    }
                }

    
                // After execution, check if the process is completed
                if (current.remaining_time == 0) {
                    completed++;
                    current.completion_time = current_time;
                    current.turnaround_time = current.completion_time - current.arrival_time;
                    current.waiting_time = current.turnaround_time - current.burst_time;
                } else {
                    // Process is not complete, re-add it to the queue
                    q.add(current);
                }
            } else {
                // No processes ready, increment time to the next process arrival
                current_time++;

            }
        }
    
        result(process);
    }
    


    public static void result(Process[] process){
        int total_turnaround_time=0,total_waiting_time=0;
        System.out.println("id"+"\t\t"+"arrival_time"+"\t\t"+"burst_time"+"\t\t"+"priority"+"\t\t"+"completion_time"+"\t\t"+"turnaround_time"+"\t\t"+"waiting_time");
        for (Process p:process){
           System.out.println(p.id+"\t\t"+p.arrival_time+"\t\t"+p.burst_time+"\t\t"+p.priority+"\t\t"+p.completion_time+"\t\t"+p.turnaround_time+"\t\t"+p.waiting_time);
            total_turnaround_time+=p.turnaround_time;
            total_waiting_time+=p.waiting_time;
        }
        System.out.println("Total turnaround : "+(total_turnaround_time)/process.length);
        System.out.println("Total waiting : "+(total_waiting_time)/process.length);

    }
}
