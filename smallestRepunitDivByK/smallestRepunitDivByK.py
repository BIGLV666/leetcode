class Solution:
    def smallestRepunitDivByK(self, k: int) -> int:
        if k%2==0 or k%5==0:
            return -1
        num=1
        res=1
        while num%k!=0:
            num=num*10+1
            res+=1
        return res
