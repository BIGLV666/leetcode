package exclusiveTime;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;

/**
 * <a href="https://leetcode.cn/problems/exclusive-time-of-functions/">636. 函数的独占时间</a>
 *
 * <p>单线程 CPU 依次执行函数调用,日志格式为 {@code 函数id:start|end:时间戳}(时间戳严格递增,
 * 调用成对闭合、可嵌套)。某函数的<strong>独占时间</strong> = 它自己占用 CPU 的时间,
 * 不含被它嵌套调用的子函数的时间;{@code end:t} 的函数在时刻 t 仍在运行(时长按含端点计)。
 * 返回每个函数的独占时间。</p>
 *
 * <p>解法:栈 + 按日志事件切分时间轴。任意时刻栈顶函数独占 CPU;
 * 记 prev = 上一事件的结束点(start:t → t,end:t → t+1):
 * 遇到 start:t 给栈顶结算 [prev, t-1],遇到 end:t 给自己结算 [prev, t]。</p>
 *
 * <p>复杂度:每条日志 O(1) 处理,时间 O(L),空间 O(n)(栈深)。</p>
 */
class Solution {
    public int[] exclusiveTime(int n, List<String> logs) {
        Deque<Node> stack = new ArrayDeque<>(); // 保存所有"已开始未结束"的函数帧
        int[] res = new int[n];
        Node lastNode=null; // 上一条日志事件,用于结算上一段时长
        for(String log : logs){
            Node node=getNode(log);
            if(node.startTime!=-1){ // start 事件
                if(stack.isEmpty()){
                    stack.push(node);
                    lastNode=node;
                }else{
                    Node top=stack.peek();
                    // 栈顶自上一事件起独占运行 [prev, t-1];prev = 上一事件是 start ? 其 t : 其 end+1
                    top.exTime+=node.startTime-(lastNode.startTime==-1?lastNode.endTime+1:lastNode.startTime);
                    stack.push(node);
                    lastNode=node;
                }
            }else { // end 事件:栈顶必然就是该函数
                Node top=stack.pop();
                // end:t 占用 [prev, t],时长 = t - prev + 1
                assert lastNode != null;
                top.exTime+=node.endTime-(lastNode.startTime==-1?lastNode.endTime+1:lastNode.startTime)+1;
                res[top.id]+=top.exTime;
                lastNode=node; // 记住刚处理的 end 事件,下一段基准从 endTime+1 起算
            }
        }
        return res;
    }

    /** 解析一条日志 "id:type:time":start → startTime=time,end → endTime=time,缺席的一侧置 -1 作标记。 */
    private Node getNode(String log) {
        String[] parts = log.split(":");
        int id = Integer.parseInt(parts[0]);
        String type = parts[1];
        int time = Integer.parseInt(parts[2]);
        if ("start".equals(type)) {
            return new Node(id, 0, time,-1);
        } else {
            return new Node(id, 0, -1, time);
        }
    }

}

/** 一条日志事件(不是函数帧的完整生命周期):start 有 startTime、end 有 endTime,另一侧为 -1。 */
class Node{
    int id;        // 函数编号
    int exTime;    // 该帧已累计的独占时间
    int startTime; // start 事件的时间戳;end 事件为 -1
    int endTime;   // end 事件的时间戳;start 事件为 -1
    Node(int id,int exTime,int startTime,int endTime) {
        this.id = id;
        this.exTime = exTime;
        this.startTime = startTime;
        this.endTime = endTime;
    }
}