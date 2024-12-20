#include <iostream>
#include <vector>

using namespace std;

int main() {
  ios::sync_with_stdio(false);
  cin.tie(0);
  cout.tie(0);
  int num;
  int quot;
  cin >> num;
  vector<long> data(num);
  vector<long> pref(num);
  vector<long> suff(num);
  for (int i = 0; i < num; ++i) {
    cin >> data[i];
    if (i > 0) {
      pref[i] = min(pref[i - 1], data[i]);
    } else {
      pref[i] = data[i];
    }
  }
  suff[num - 1] = data[num - 1];
  for (int i = num - 2; i >= 0; --i) {
    suff[i] = min(suff[i + 1], data[i]);
  }
  cin >> quot;
  for (int i = 0; i < quot; ++i) {
    int left;
    int right;
    cin >> left >> right;
    cout << min(pref[left - 1], suff[right - 1]) << '\n';
  }
}