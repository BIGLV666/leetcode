import java.util.Arrays;
import java.util.List;

public class Test {
    private static Solution solution=new Solution();
    private static TestCase[] testCases=new TestCase[]{
        new TestCase(2,3,10,Arrays.asList(2,3,4,5,7,9,10)),
    };
    public static void main(String[] args) {
       for(TestCase testCase:testCases){
           List<Integer>res=solution.powerfulIntegers(testCase.x, testCase.y, testCase.bound);
           System.out.println(res);
           System.out.println(testCase.expect);
           if(!res.equals(testCase.expect)){
               System.out.println("测试用例"+testCase+"失败");
           }
       }
    }
    
}
class TestCase{
    int x;
    int y;
    int bound;
    List<Integer>expect;
    public TestCase(int x,int y,int bound,List<Integer>expect){
        this.x=x;
        this.y=y;
        this.bound=bound;
        this.expect=expect;
    }
}