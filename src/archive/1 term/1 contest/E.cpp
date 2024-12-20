#include <algorithm>
#include <iostream>
#include <vector>

static const long long kMin = -1;
static const long long kMax = 2e9 + 3;
using namespace std;

bool CheckSegmentsLength(vector<long>& cords, long length, long count) {
  long right = 0;
  size_t cur = 0;
  while (count > 0) {
    right = length + cords[cur];
    while (cords[cur] <= right) {
      cur++;
      if (cur == cords.size()) {
        return true;
      }
    }
    count--;
  }
  return false;
}

int main() {
  ios::sync_with_stdio(false);
  cin.tie(0);
  cout.tie(0);
  int num;
  int quot;
  cin >> num >> quot;
  vector<long> cords(num);
  for (int i = 0; i < num; ++i) {
    cin >> cords[i];
  }
  sort(cords.begin(), cords.end());
  long left = kMin;
  long right = kMax;
  while (right - left > 1) {
    long middle = left + (right - left) / 2;
    bool result = CheckSegmentsLength(cords, middle, quot);
    if (result) {
      right = middle;
    } else {
      left = middle;
    }
  }
  cout << right;
}