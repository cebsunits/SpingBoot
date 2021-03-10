package com.tao.hai.concurrent;


import java.util.ArrayDeque;
import java.util.Deque;
import java.util.concurrent.*;

public class ThreadPoolDemo {
    public static void main(String[] args) {

        /**1、JDK本身创建
         **/
        /**返回一个可根据实际情况调整线程数量的线程池。线程池的数量不固定，最大为Integer.MAX_VALUE。若所有线程均在工作，又有新的任务提交，则会创建新的线程处理任务。
         * 所有线程在当前任务执行完成后，将返回线程池进行复用。SynchronousQueue（一一配对消费通信）
         * 注意：该情况在最坏情况下，容易出现OOM
         */
        ExecutorService executorService= Executors.newCachedThreadPool();
        /**
         * 只有一个线程的线程池。若多余一个线程被提交到线程池，则任务会被保存在LinkedBlockingQueue缓存中，等待线程空闲，按先入先出的顺序执行队列中的任务
         */
        ExecutorService executorService1=Executors.newSingleThreadExecutor();
        /**
         *创建指定核心线程的线程池，线程池最大数量为Integer.MAX_VALUE。DelayedWorkQueue。优先级队列，与插入顺序无关，与优先级有关。堆实现
         * */
        ExecutorService executorService2= Executors.newScheduledThreadPool(5);
        /**
         *返回一个固定数量的线程池。当有一个新的任务提交时，若线程池中有空闲线程，则立即执行；否则会被放在队列中，等待线程空闲，按先入先出的顺序执行。LinkedBlockingQueue
         * */
        ExecutorService executorService3=Executors.newFixedThreadPool(5);
        /**
         * 返回一个ScheduledExecutorService对象，线程池大小为1。
         * */
        ExecutorService executorService4=Executors.newSingleThreadScheduledExecutor();
        /**
         * 自定义线程池
         * */
        //线程池线程数量
        int corePoolSize=Runtime.getRuntime().availableProcessors()+1;
        //线程池最大线程数量
        int maximumPoolSize=corePoolSize;
        //当线程池数量超过corePoolSize时，多余的空闲线程的存活时间。即超过corePoolSize的空闲线程，在多长时间会被销毁
        long keepAliveTime=30;
        //keepAliveTime的时间单位
        TimeUnit unit=TimeUnit.SECONDS;
        //任务队列，被提交但尚未执行的任务。仅用于存放Runable对象。
        //SynchronousQueue 直接提交的队列：没有容量，每一个插入操作都要等待一个相应的删除操作。不保存任务，总是将任务提交给线程执行，如果没有空间的线程，则尝试创建新的线程。如果线程数量已经达到最大值，额执行拒绝策略。
        //ArrayBlockingQueue 有界队列：ArrayBlockingQueue的构造函数必须带一个容量参数，表示该队列的最大容量。
        //LinkedBlockingQueue 无界队列：除非系统资源耗尽，否则无界的任务队列不存在任务入队失败的情况。
        //PriorityBlockingQueue 优先队列：优先队列是带有执行优先级的队列，通过PriorityBlokingQueue实现。是一种特殊的无界队列。必须继承Comparable接口
        BlockingQueue<Runnable> workQueue=new LinkedBlockingQueue<>();
        workQueue=new ArrayBlockingQueue<>(1000);//ArrayBlockingQueue必须指定大小，指定是否采用公平锁
        workQueue=new PriorityBlockingQueue<>();//一个支持优先级的无界阻塞队列，直到系统资源耗尽
        workQueue=new LinkedTransferQueue<>();//一个由链表结构组成的无界阻塞TransferQueue队列.相对于其他阻塞队列，LinkedTransferQueue多了tryTransfer和transfer方法。
        //LinkedTransferQueue采用一种预占模式。意思就是消费者线程取元素时，如果队列不为空，则直接取走数据，若队列为空，那就生成一个节点（节点元素为null）入队，
        // 然后消费者线程被等待在这个节点上，后面生产者线程入队时发现有一个元素为null的节点，生产者线程就不入队了，直接就将元素填充到该节点，并唤醒该节点等待的线程，被唤醒的消费者线程取走元素，从调用的方法返回。我们称这种节点操作为“匹配”方式。
        //LinkedTransferQueue是ConcurrentLinkedQueue、SynchronousQueue（公平模式下转交元素）、LinkedBlockingQueue（阻塞Queue的基本方法）的超集。
        DelayQueue workQueue1=new DelayQueue<>();//是一个无界的BlockingQueue，用于放置实现了Delayed接口的对象，其中的对象只能在其到期时才能从队列中取走。这种队列是有序的，即队头对象的延迟到期时间最长。注意：不能将null元素放置到这种队列中
        //workQueue.put();把Object对象放入BlockingQueue里面，如果BlockingQueue没有足够空间，则调用此方法的线程被阻塞直到BlockingQueue里面有空闲空间时再继续
        //workQueue.offer();将Object对象加到BlockingQueue里面，如果BlockingQueue有足够空间则返回true，否则返回false（该方法不阻塞当前执行方法的线程）
        //workQueue.poll(); 取走排在首位的对象，若不能立即取出，则可以等time参数规定的时间，取不到返回null
        //workQueue.take();取走排在首位的对象，若BlockingQueue为空，阻断进入等待状态直到BlockingQueue有新的数据被加入
        //workQueue.drainTo();一次性获取所有可用的数据对像，通过该方法，可以提升获取数据的效率，不需要多次分批加锁或释放锁
        //双端队列：Dequeue
        BlockingDeque<Object> deque=new LinkedBlockingDeque<>();
        Deque<Object> deque1=new ConcurrentLinkedDeque<>();//是双向链表结构的无界并发队列.该阻塞队列同时支持FIFO和FILO两种操作方式，即可以从队列的头和尾同时操作(插入/删除)。适合“多生产，多消费”的场景
        deque1=new ArrayDeque<>();//是线程不安全的，在没有外部同步的情况下，不能在多线程环境下使用。ArrayDeque是Deque的实现类，可以作为栈来使用，效率高于Stack；也可以作为队列来使用，效率高于LinkedList。
        //deque.addFirst();插入第一个元素
        //deque.offerFirst();插入第一个元素
        //deque.push();插入第一个元素
        //deque.add();插入最后一个元素
        //deque.addLast();插入最后一个元素
        //deque.offerLast();插入最后一个元素
        //deque.removeFirst();删除第一个元素
        //deque.pollFirst();删除第一个元素
        //deque.pop();删除第一个元素
        //deque.poll();删除第一个元素
        //deque.removeLast();删除最后一个元素
        //deque.pollLast();删除最后一个元素
        //deque.getFirst();只读取不删除第一个元素
        //deque.peekFirst();只读取不删除第一个元素
        //deque.element();只读取不删除第一个元素
        //deque.peek();只读取不删除第一个元素
        //deque.getLast();只读取不删除最后一个元素
        //deque.peekLast();只读取不删除最后一个元素
        //线程工厂，用于创建线程，一般用默认的即可
        ThreadFactory threadFactory;
        //拒绝策略。当任务太多来不及处理，如何拒绝任务。
        //ThreadPoolExecutor.AbortPolicy; 直接抛出异常，阻止系统正常工作。
        //ThreadPoolExecutor.CallerRunsPolicy; 只要线程池未关闭，该策略直接在调用者线程中，运行当前被丢弃的任务。
        //ThreadPoolExecutor.DiscardOldestPolicy;将丢弃最老的一个请求，也就是即将被执行的一个任务，并尝试再次提交当前任务
        //ThreadPoolExecutor.DiscardPolicy;默默丢弃无法处理的任务，不予任何处理
        RejectedExecutionHandler handler;
        ThreadPoolExecutor threadPoolExecutor=new ThreadPoolExecutor(corePoolSize,maximumPoolSize,keepAliveTime,unit,workQueue);

        //信号量，指定多个线程同时访问某一个资源
        Semaphore semaphore=new Semaphore(100,false);
        try {
            semaphore.acquire();//尝试获得准入的许可，如果无法获取，则线程会等待，直到有线程释放一个许可或者当前线程被中断。
            semaphore.acquireUninterruptibly();//同上
            semaphore.tryAcquire();//尝试获得许可，如果成功返回true，否则返回false；不会进行等待，立即返回。
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            semaphore.release();//释放许可
        }
        /**线程局部变量
         * 不提供锁，以空间换时间的手段，为每个线程提供变量的独立副本，以保障线程安全。
         * 不是一种数据共享的解决方案
         * */
        ThreadLocal threadLocal=new ThreadLocal();
        threadLocal.set(new Object());//将此局部变量的当前线程副本中的值设置为指定值
        threadLocal.get();//返回线程局部变量的当前线程副本中的值
        threadLocal.remove();//移除此线程局部变量当前线程的值
    }
}
