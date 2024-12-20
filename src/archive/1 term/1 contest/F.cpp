#include <iostream>
#include <vector>

using namespace std;
static const long kMin = 01;
static const long kMax = 1e9 + 1;

bool CheckDistance(vector<long>& data, long dist, int count) {
  int last = 0;
  count--;
  for (int i = 1; i < (int)data.size(); ++i) {
    if (data[i] - data[last] >= dist) {
      last = i;
      count--;
    }
    if (count == 0) {
      return true;
    }
  }
  return count == 0;
}

int main() {
  ios::sync_with_stdio(false);
  cin.tie(0);
  cout.tie(0);
  int ammount;
  int num;
  cin >> ammount >> num;
  vector<long> cords(ammount);
  for (int i = 0; i < ammount; ++i) {
    cin >> cords[i];
  }
  long left = kMin;
  long right = kMax;
  while (right - left > 1) {
    long middle = left + (right - left) / 2;
    if (CheckDistance(cords, middle, num)) {
      left = middle;
    } else {
      right = middle;
    }
  }
  cout << left;
}