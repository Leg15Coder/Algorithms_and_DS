#include <iostream>

using namespace std;

int main() {
  ios::sync_with_stdio(false);
  cin.tie(0);
  cout.tie(0);
  int ammount;
  cin >> ammount;
  for (int i = 0; i < ammount; ++i) {
    unsigned long num;
    cin >> num;
    while (num % 2 == 0) {
      num /= 2;
      cout << 2 << ' ';
    }
    unsigned long iter = 3;
    while (num > 1) {
      while (num % iter == 0) {
        num /= iter;
        cout << iter << ' ';
      }
      iter += 2;
      if (iter * iter > num) {
        if (num != 1) {
          cout << num;
        }
        break;
      }
    }
    cout << '\n';
  }
}