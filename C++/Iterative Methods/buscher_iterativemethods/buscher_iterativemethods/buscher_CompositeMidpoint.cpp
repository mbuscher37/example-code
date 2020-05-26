#include <iostream>
#include "buscher_NumericalIntegration.h"

double NumericalIntegration::CompositeMidpoint(int n, double a, double b
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
	double x_m;
	for (int i = 1; i <= n; i++)
	{
		//find midpoint for [x_{i-1},x_i]
		x_m = (partition[i] + partition[i - 1]) / 2;
		sum = sum + f(x_m) * h;
	}

	double val = sum;
	return val;
}

double NumericalIntegration::CompositeMidpoint(int n, double a, double b
	, double(*f)(double t, double x), double x)
{
	double h = (b - a) / n;
	vector<double> partition(n + 1);
	partition[0] = a;
	for (int i = 1; i <= n; i++)
	{
		partition[i] = partition[i - 1] + h;
	}
	double sum = 0.0;
	double t_m;
	for (int i = 1; i <= n; i++)
	{
		//find midpoint for [x_{i-1},x_i]
		t_m = (partition[i] + partition[i - 1]) / 2;
		sum = sum + f(t_m, x) * h;
	}

	double val = sum;
	return val;
}