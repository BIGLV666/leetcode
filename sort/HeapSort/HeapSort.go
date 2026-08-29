package HeapSort

// HeapSort 堆排序入口
// 思路：
//  1. 先把数组建成"最大堆"——堆顶永远是当前最大值
//  2. 反复把堆顶（最大值）和末尾元素交换，把堆的范围缩小 1
//  3. 每次交换后对新堆顶做"下沉"（heapify），恢复堆性质
//  4. 重复直到堆只剩 1 个元素，数组就有序了
//
// 时间复杂度：O(n log n)  空间复杂度：O(1)（原地排序）
func HeapSort(arr []int) []int {
	n := len(arr)
	if n <= 1 {
		return arr
	}

	// === 第一阶段：建堆（Build Max-Heap）===
	// 从最后一个非叶子节点开始，向前逐个做 heapify
	// 最后一个非叶子节点的下标 = n/2 - 1
	// 原因：下标 n/2 ~ n-1 都是叶子节点，不需要下沉
	for i := n/2 - 1; i >= 0; i-- {
		heapify(arr, n, i)
	}

	// === 第二阶段：逐步提取最大值，缩小堆 ===
	for i := n - 1; i > 0; i-- {
		// 堆顶 arr[0] 是当前最大值，与末尾元素交换
		// 交换后最大值"沉"到了数组末尾，固定不动
		arr[0], arr[i] = arr[i], arr[0]

		// 堆的有效范围缩小到 [0, i-1]
		// 新的堆顶（原来的末尾元素）可能违反堆性质，做一次 heapify 下沉
		heapify(arr, i, 0)
	}

	return arr
}

// heapify 对下标 root 做"下沉"操作，维持最大堆性质
// arr：数组，n：堆的有效大小，root：需要下沉的节点下标
//
// 最大堆性质：父节点 >= 左右子节点
// 下沉过程：
//  1. 找出 root、左子、右子 三者中最大的
//  2. 如果最大的不是 root，则交换 root 与最大子节点
//  3. 递归对被交换的子节点继续下沉（因为它的子树可能又被破坏了）
func heapify(arr []int, n, root int) {
	largest := root     // 假设当前节点是最大的
	left := 2*root + 1  // 左子节点下标（二叉堆用数组表示：左子 = 2i+1）
	right := 2*root + 2 // 右子节点下标（右子 = 2i+2）

	// 如果左子节点存在且比当前最大值大，更新 largest
	if left < n && arr[left] > arr[largest] {
		largest = left
	}

	// 如果右子节点存在且比当前最大值大，更新 largest
	if right < n && arr[right] > arr[largest] {
		largest = right
	}

	// 如果最大值不是 root，说明堆性质被破坏，需要交换并继续下沉
	if largest != root {
		arr[root], arr[largest] = arr[largest], arr[root]
		// 交换后 largest 位置的值变小了，可能继续违反堆性质，递归下沉
		heapify(arr, n, largest)
	}
}
