package removeSubfolders;

import java.util.*;

class Solution {
    /**
     * <b>删除子文件夹<a href="https://leetcode.cn/problems/remove-sub-folders-from-the-filesystem/description/">删除子文件夹</a></b>
     * 算法思路：
     * <ul>
     *     <li>1. 按照路径的层级进行排序，先处理层级少的路径</li>
     *     <li>2. 使用 HashSet 存储已经处理过的路径</li>
     *     <li>3. 对于每一个路径，检查其父路径是否已经存在于 HashSet 中，如果存在，则说明该路径是子文件夹，跳过；否则，将其加入 HashSet 中</li>
     *     <li>4. 最后返回 HashSet 中的所有路径</li>
     * </ul>
     */
    public List<String> removeSubfolders(String[] folder) {
        Set<String> folderSet = new HashSet<>();
        Arrays.sort(folder, Comparator.comparingInt(a -> a.split("/").length));
        for(String f:folder){
            String[] parts=f.split("/");
            StringBuilder sb=new StringBuilder("/");
            boolean isSubfolder=false;
            for(int i=1;i<parts.length;i++){
                sb.append(parts[i]);

                if(folderSet.contains(sb.toString())){
                    isSubfolder=true;
                    break;
                }
                if(i<parts.length-1){
                    sb.append("/");
                }
            }
            if(!isSubfolder){
                folderSet.add(f);
            }
        }
        return new ArrayList<>(folderSet);
    }
}
