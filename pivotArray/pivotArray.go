package pivotArray

func pivotArray(nums []int, pivot int) []int {
    res := make([]int, 0, len(nums))
    for _,v := range nums {
        if v<pivot{
            res=append(res,v)
        }
    }
    for _,v := range nums {
        if v==pivot{
            res=append(res,v)
        }
    }
    for _,v := range nums {
        if v>pivot{
            res=append(res,v)
        }
    }
    return res
}