#include <cmath>
#include <iostream>
#include <vector>

using namespace std;
static const int kPrecision = 8;

int main() {
  ios::sync_with_stdio(false);
  cin.tie(0);
  cout.tie(0);
  cout.setf(ios::fixed);
  cout.precision(kPrecision);
  int num;
  int quot;
  cin >> num;
  vector<double> values(num + 1, 0.0);
  for (int i = 1; i <= num; ++i) {
    double x;
    cin >> x;
    values[i] = values[i - 1] + log(x);
  }
  cin >> quot;
  for (int k = 0; k < quot; ++k) {
    int i_1;
    int j_1;
    cin >> i_1 >> j_1;
    j_1++;
    cout << pow(exp(1.0), (values[j_1] - values[i_1]) / (j_1 - i_1)) << '\n';
  }
}