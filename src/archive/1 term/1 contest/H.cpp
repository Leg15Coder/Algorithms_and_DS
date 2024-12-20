#include <iostream>
#define type long long

using namespace std;
static const type kMod = 1e9 + 7;

type Mod(type num) {
  while (num < 0) {
    num += kMod;
  }
  return num % kMod;
}

type Sum(type left, type right) { return Mod(Mod(left) + Mod(right)); }

type Mul(type left, type right) { return Mod(Mod(left) * Mod(right)); }

type Rev(type num) {
  type pow = kMod - 2;
  type res = 1;
  while (pow != 0) {
    if ((pow & 1) != 0) {
      res = Mul(res, num);
    }
    num = Mul(num, num);
    pow >>= 1;
  }
  return res;
}

int main() {
  ios::sync_with_stdio(false);
  cin.tie(0);
  cout.tie(0);
  type num1;
  type num2;
  type num3;
  type num4;
  cin >> num1 >> num2 >> num3 >> num4;
  cout << Mul(Sum(Mul(num1, num4), Mul(num2, num3)), Rev(Mul(num2, num4)));
}