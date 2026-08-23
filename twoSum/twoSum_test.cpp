#include <iostream>
#include <string>
#include <vector>

using namespace std;

vector<int> twoSum(vector<int>& nums, int target);

struct TestCase {
    string name;
    vector<int> nums;
    int target;
    vector<int> expected;
};

int main() {
    vector<TestCase> testCases = {
        {"普通情况", {2, 7, 11, 15}, 9, {0, 1}},
        {"答案位于数组中间", {3, 2, 4}, 6, {1, 2}},
        {"使用两个相同元素", {3, 3}, 6, {0, 1}},
        {"包含负数", {-3, 4, 3, 90}, 0, {0, 2}},
        {"包含零", {0, 4, 3, 0}, 0, {0, 3}},
        {"无答案", {1, 2, 3}, 10, {}},
    };

    int passed = 0;

    for (const auto& test : testCases) {
        vector<int> nums = test.nums;
        vector<int> actual = twoSum(nums, test.target);

        if (actual == test.expected) {
            cout << "[PASS] " << test.name << endl;
            passed++;
        } else {
            cout << "[FAIL] " << test.name << endl;
            cout << "  expected: [";
            for (size_t i = 0; i < test.expected.size(); i++) {
                if (i > 0) cout << ", ";
                cout << test.expected[i];
            }
            cout << "]" << endl;

            cout << "  actual:   [";
            for (size_t i = 0; i < actual.size(); i++) {
                if (i > 0) cout << ", ";
                cout << actual[i];
            }
            cout << "]" << endl;
        }
    }

    cout << "测试结果：" << passed << "/" << testCases.size() << " 通过" << endl;
    return passed == static_cast<int>(testCases.size()) ? 0 : 1;
}
