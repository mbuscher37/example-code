#include <vector>
#include <iostream>
#include "buscher_NumericalIntegration.h"

using namespace std;

double NumericalIntegration::CompositeTrapezoidal(int n, double a, double b
	, double(*f)(double x))
{
	double h = (b - a) / n;
	vector<double> partition(n + 1);
	partition[0] = a;
	for (int i = 1; i <= n; i++)
	{
		partition[i] = partition[i - 1] + h;
	}

	double sum = 0.0;
	for (int i = 1; i < n; i++)
	{
		sum = sum + f(partition[i]);
	}

	double val = h * (0.5*f(partition[0]) + sum + 0.5*f(partition[n]));
	return val;
}