#include <iostream>

using namespace std;

long Sieve(long num) {
  if (num < 4) {
    return 0;
  }
  if (num % 2 == 0) {
    return 2;
  }
  long iter = 3;
  while (iter * iter <= num) {
    if (num % iter == 0) {
      return iter;
    }
    iter += 2;
  }
  return 0;
}

int main() {
  ios::sync_with_stdio(false);
  cin.tie(0);
  cout.tie(0);
  long number;
  cin >> number;
  long result = 0;
  for (int i = 0; i <= number; ++i) {
    result += Sieve(i);
  }
  cout << result;
}