#include <iostream>
#include <vector>
#define type unsigned long long

using namespace std;

struct Part {
  type l;
  type r;
  type x;
  type v;
  type a;
};

type GetDist(Part& part, type dist) {
  type left = part.l;
  type right = part.r;
  while (right - left > 1) {
    type middle = left + (right - left) / 2;
    type current = part.x + 2 * (middle - part.l) * part.v +
                   (middle - part.l) * (middle - part.l) * part.a;
    if (current <= dist) {
      left = middle;
    } else {
      right = middle;
    }
  }
  return left;
}

type GetPart(vector<Part>& data, type dist) {
  type left = 0;
  type right = (type)data.size();
  while (right - left > 1) {
    type middle = left + (right - left) / 2;
    if (data[middle].x <= dist) {
      left = middle;
    } else {
      right = middle;
    }
  }
  return GetDist(data[left], dist);
}

int main() {
  ios::sync_with_stdio(false);
  cin.tie(0);
  cout.tie(0);
  type current_speed = 0;
  type current_dist = 0;
  int ammount;
  cin >> ammount;
  vector<Part> data(ammount + 1);
  for (int i = 0; i < ammount; ++i) {
    type left;
    type right;
    type boost;
    cin >> left >> right >> boost;
    type time = right - left;
    data[i] = {left, right, current_dist, current_speed, boost};
    current_dist += 2 * time * current_speed + time * time * boost;
    current_speed += time * boost;
  }
  type left = data[ammount - 1].r;
  data[ammount] = {left, left + 1, current_dist, current_speed, 0};
  int quot;
  cin >> quot;
  for (int i = 0; i < quot; ++i) {
    type dist;
    cin >> dist;
    cout << GetPart(data, 2 * dist) << '\n';
  }
}